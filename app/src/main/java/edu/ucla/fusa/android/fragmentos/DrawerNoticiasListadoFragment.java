package edu.ucla.fusa.android.fragmentos;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import edu.ucla.fusa.android.DB.NoticiasTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ListNoticiasAdapter;
import edu.ucla.fusa.android.modelo.fundacion.Noticia;
import edu.ucla.fusa.android.modelo.herramientas.ItemListNoticia;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import edu.ucla.fusa.android.modelo.instrumentos.SolicitudPrestamo;

import java.util.ArrayList;

public class DrawerNoticiasListadoFragment extends ListFragment implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener<ListView>, View.OnClickListener {

    private ListNoticiasAdapter adapter;
    private Bundle arguments;
    private View backToTop;
    private int index = -1;
    private ArrayList<ItemListNoticia> items = new ArrayList();
    private PullToRefreshListView list;
    private View view;
    private JSONParser jsonParser = new JSONParser();
    private NoticiasTable db;
    private static String TAG = "DrawerNoticiasListadoFragment";
    private Button reintentar;
    private TextView descripcionConexion;
    private ProgressBar progress;

    public static DrawerNoticiasListadoFragment newInstance() {
        DrawerNoticiasListadoFragment fragment = new DrawerNoticiasListadoFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back_top:
                getListView().smoothScrollToPositionFromTop(0, 0);
                break;
            case R.id.btn_reintentar_conexion_noticias:
                new LoadingNoticiasTaks().execute();
                break;
        }

    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getActivity().getActionBar().setTitle(R.string.contenido_noticia_action_bar_titulo);
        getActivity().getActionBar().setIcon(R.drawable.ic_noticias_blanco);
        backToTop = ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_back_to_top_list_view, null, false);
        backToTop.setOnClickListener(this);
        db = new NoticiasTable(getActivity());
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_drawer_list_noticias, paramViewGroup, false);
        list = ((PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_list));
        progress = (ProgressBar) view.findViewById(R.id.progress_bar_noticias);
        descripcionConexion = (TextView) view.findViewById(R.id.tv_empty_text);
        reintentar = (Button) view.findViewById(R.id.btn_reintentar_conexion_noticias);
        reintentar.setOnClickListener(this);
        list.setOnRefreshListener(this);
        return view;
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
        ItemListNoticia item = (ItemListNoticia) paramAdapterView.getItemAtPosition(paramInt);
        arguments = new Bundle();
        arguments.putString("titulo_noticia", item.getTitulo());
        //arguments.putString("fecha_noticia", item.getFecha());
        //arguments.putInt("imagen_noticia", item.getImagen());
        arguments.putString("descripcion_noticia", item.getDescripcion());
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, DrawerNoticiasDetalleFragment.newInstance(this.arguments))
                .addToBackStack(null)
                .commit();
    }

    public void onPause() {
        super.onPause();
        index = getListView().getFirstVisiblePosition();
    }

    public void onRefresh(PullToRefreshBase<ListView> paramPullToRefreshBase) {
        ItemListNoticia item = (ItemListNoticia) adapter.getItem(0);
        new LoadingNewsNoticiasTaks().execute(String.valueOf(item.getId()));

    }

    public void onResume() {
        super.onResume();
        getListView().setOnItemClickListener(this);
        getActivity().getActionBar().setTitle(R.string.contenido_noticia_action_bar_titulo);
        getActivity().getActionBar().setIcon(R.drawable.ic_noticias_blanco);
        items = db.searchNews();
        if (items.size() != 0) {
            Log.i("CANTIDAD", String.valueOf(items.size()));
            adapter = new ListNoticiasAdapter(getActivity(), items, this);
            adapter.notifyDataSetChanged();
            setListAdapter(adapter);
            //if (index != -1) {
                //getListView().setSelectionFromTop(index, 0);
           // }
        } else {
            Log.i(TAG, "Buscando noticias");
            new LoadingNoticiasTaks().execute();
        }
    }

    public void onSaveInstanceState(Bundle paramBundle) {
        super.onSaveInstanceState(paramBundle);
        paramBundle.putInt("item_position", getListView().getSelectedItemPosition());
    }

    private class LoadingNoticiasTaks extends AsyncTask<Void, Void, Integer> {

        private ArrayList<ItemListNoticia> items = new ArrayList();

        protected void onPreExecute() {
            super.onPreExecute();
            items.clear();
            reintentar.setVisibility(View.GONE);
            getListView().setEmptyView(progress);
        }

        protected Integer doInBackground(Void[] paramArrayOfVoid) {
            SystemClock.sleep(2000);
            Log.i(TAG, "Dentro del background");
            ArrayList<Noticia> noticias = jsonParser.serviceLoadingNoticias();
            if (noticias.size() != 0 && noticias != null) {
                for (Noticia noticia : noticias) {
                    //Agregamos a la lista de noticias
                    items.add(0, new ItemListNoticia(
                            noticia.getId(),
                            noticia.getTitulo(),
                            noticia.getFechapublicacion().getTime(),
                            noticia.getImagen(),
                            noticia.getDescripcion()));
                    //Guardamos en la base de datos
                    db.insertData(noticia.getTitulo(),
                            noticia.getDescripcion(),
                            noticia.getFechapublicacion().getTime(),
                            noticia.getImagen(),
                            noticia.getId());
                }
                return 100;
            } else {
                return 0;
            }
        }

        protected void onPostExecute(Integer response) {
            super.onPostExecute(response);
            if (response == 100) {
                adapter = new ListNoticiasAdapter(getActivity(), items, DrawerNoticiasListadoFragment.this);
                getListView().addFooterView(backToTop);
                setListAdapter(adapter);
                getListView().removeFooterView(backToTop);
                getListView().post(new Runnable() {
                    public void run() {
                        int i = DrawerNoticiasListadoFragment.this.getListView().getLastVisiblePosition();
                        int j = DrawerNoticiasListadoFragment.this.getListAdapter().getCount();
                        if (i + 1 < j)
                            getListView().addFooterView(backToTop);
                    }
                });
            } else {
                Log.i(TAG, "Error al cargar noticias");
                try {
                    progress.setVisibility(View.GONE);
                    getListView().setEmptyView(reintentar);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private class LoadingNewsNoticiasTaks extends AsyncTask<String, Void, Integer> {

        private ArrayList<ItemListNoticia> items = new ArrayList();

        protected void onPreExecute() {
            super.onPreExecute();
            items.clear();
        }

        protected Integer doInBackground(String... params) {
            SystemClock.sleep(2000);
            /** Cargamos los parametros que enviaremos por URL */
            ArrayList<NameValuePair> parametros = new ArrayList<NameValuePair>();
            parametros.add(new BasicNameValuePair("ultimaNoticia", params[0]));
            Log.i(TAG, "Ultimo de la lista " + params[0]);
            ArrayList<Noticia> noticias = jsonParser.serviceRefreshNoticias(parametros);
            Log.i(TAG, "Tamaño de la lista " + noticias.size());
            if (noticias.size() != 0) {
                for (Noticia noticia : noticias) {
                    //Agregamos a la lista de noticias
                    items.add(0, new ItemListNoticia(
                            noticia.getId(),
                            noticia.getTitulo(),
                            noticia.getFechapublicacion().getTime(),
                            noticia.getImagen(),
                            noticia.getDescripcion()));
                    //Guardamos en la base de datos
                    db.insertData(noticia.getTitulo(),
                            noticia.getDescripcion(),
                            noticia.getFechapublicacion().getTime(),
                            noticia.getImagen(),
                            noticia.getId());
                }
                return 100;
            } else {
                return 0;
            }
        }

        protected void onPostExecute(Integer response) {
            super.onPostExecute(response);
            list.onRefreshComplete();
            if (response == 100) {
                adapter = new ListNoticiasAdapter(getActivity(), items, DrawerNoticiasListadoFragment.this);
                getListView().addFooterView(backToTop);
                setListAdapter(adapter);
                adapter.notifyDataSetChanged();
                getListView().removeFooterView(backToTop);
                getListView().post(new Runnable() {
                    public void run() {
                        int i = DrawerNoticiasListadoFragment.this.getListView().getLastVisiblePosition();
                        int j = DrawerNoticiasListadoFragment.this.getListAdapter().getCount();
                        if (i + 1 < j)
                            getListView().addFooterView(backToTop);
                    }
                });
            } else {
                Toast.makeText(getActivity(), R.string.mensaje_busqueda_vacio_noticias, Toast.LENGTH_SHORT).show();
            }
        }

        protected void onProgressUpdate(Void[] paramArrayOfVoid) {
            super.onProgressUpdate(paramArrayOfVoid);
        }
    }

}