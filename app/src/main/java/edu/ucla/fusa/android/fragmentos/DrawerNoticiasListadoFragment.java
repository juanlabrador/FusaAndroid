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
import android.widget.ListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import edu.ucla.fusa.android.DB.NoticiasTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ListNoticiasAdapter;
import edu.ucla.fusa.android.modelo.eventos.Noticia;
import edu.ucla.fusa.android.modelo.herramientas.ItemListNoticia;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;

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

    public static DrawerNoticiasListadoFragment newInstance() {
        DrawerNoticiasListadoFragment fragment = new DrawerNoticiasListadoFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onClick(View paramView) {
        getListView().smoothScrollToPositionFromTop(0, 0);
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
        new GetDataTask().execute();
    }

    public void onResume() {
        super.onResume();
        getListView().setOnItemClickListener(this);
        getActivity().getActionBar().setTitle(R.string.contenido_noticia_action_bar_titulo);
        getActivity().getActionBar().setIcon(R.drawable.ic_noticias_blanco);
        items = db.searchNews();
        if (items != null) {
            Log.i("CANTIDAD", String.valueOf(items.size()));
            adapter = new ListNoticiasAdapter(getActivity(), items, this);
            setListAdapter(adapter);
            //if (index != -1) {
                //getListView().setSelectionFromTop(index, 0);
           // }
        } else {
            new LoadingNoticiasTaks().execute();
        }
    }

    public void onSaveInstanceState(Bundle paramBundle) {
        super.onSaveInstanceState(paramBundle);
        paramBundle.putInt("item_position", getListView().getSelectedItemPosition());
    }

    private class GetDataTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void[] paramArrayOfVoid) {
            SystemClock.sleep(2000L);
            jsonParser.listadoNoticias();
            return null;
        }

        protected void onPostExecute(Void paramVoid) {
            super.onPostExecute(paramVoid);
            list.onRefreshComplete();
        }
    }

    private class LoadingNoticiasTaks extends AsyncTask<Void, Void, Integer> {

        private ArrayList<ItemListNoticia> items = new ArrayList();

        protected void onPreExecute() {
            super.onPreExecute();
            items.clear();
            getListView().setEmptyView(getView().findViewById(R.id.progress_bar));
        }

        protected Integer doInBackground(Void[] paramArrayOfVoid) {
            SystemClock.sleep(2000L);
            ArrayList<Noticia> noticias = jsonParser.listadoNoticias();
            if (noticias != null) {
                for (Noticia noticia : noticias) {
                    //Agregamos a la lista de noticias
                    items.add(new ItemListNoticia(
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
                getListView().setEmptyView(getView().findViewById(R.id.tv_empty_text));
            }
        }

        protected void onProgressUpdate(Void[] paramArrayOfVoid) {
            super.onProgressUpdate(paramArrayOfVoid);
        }
    }
}