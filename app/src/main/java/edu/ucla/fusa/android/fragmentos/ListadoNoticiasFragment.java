package edu.ucla.fusa.android.fragmentos;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

public class ListadoNoticiasFragment extends ListFragment implements PullToRefreshBase.OnRefreshListener<ListView>, View.OnClickListener {

    private Toolbar mToolbar;
    private ListNoticiasAdapter mListAdapter;
    private View mBackToTop;
    private int mIndex = -1;
    private ArrayList<ItemListNoticia> mItemsNoticias = new ArrayList();
    private PullToRefreshListView mList;
    private View mView;
    private JSONParser mJSONParser;
    private NoticiasTable mNoticiasTable;
    private static String TAG = "ListadoNoticiasFragment";
    private Button mButtonRetry;
    private TextView mTextLoading;
    private ProgressBar mLoading;
    private ItemListNoticia mItemNoticia;
    private ArrayList<Noticia> mNoticias;

    public static ListadoNoticiasFragment newInstance() {
        ListadoNoticiasFragment fragment = new ListadoNoticiasFragment();
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

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        //mToolbar.setTitle(R.string.noticias_titulo_barra);
        mBackToTop = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_back_to_top_list_view, null, false);
        mBackToTop.setOnClickListener(this);
        mNoticiasTable = new NoticiasTable(getActivity());
        mJSONParser = new JSONParser();
        mNoticias = new ArrayList<Noticia>();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        super.onCreateView(inflater, container, bundle);
        mView = inflater.inflate(R.layout.fragment_drawer_list_noticias, container, false);
        
        mList = (PullToRefreshListView) mView.findViewById(R.id.pull_to_refresh_list);
        mLoading = (ProgressBar) mView.findViewById(R.id.progress_bar_noticias);
        mTextLoading = (TextView) mView.findViewById(R.id.tv_empty_text);
        mButtonRetry = (Button) mView.findViewById(R.id.btn_reintentar_conexion_noticias);
        mButtonRetry.setOnClickListener(this);
        mList.setOnRefreshListener(this);
        return mView;
    }


    public void onPause() {
        super.onPause();
        mIndex = getListView().getLastVisiblePosition();
    }

    public void onRefresh(PullToRefreshBase<ListView> paramPullToRefreshBase) {
         mItemNoticia = (ItemListNoticia) mListAdapter.getItem(0);
        new LoadingNewsNoticiasTaks().execute(String.valueOf(mItemNoticia.getId()));

    }

    public void onResume() {
        super.onResume();
        //mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        //mToolbar.setTitle(R.string.noticias_titulo_barra);
        mItemsNoticias = mNoticiasTable.searchNews();
        if (mItemsNoticias.size() != 0) {
            Log.i(TAG, "Cantidad de noticias: " + mItemsNoticias.size());
            mListAdapter = new ListNoticiasAdapter(getActivity(), mItemsNoticias, this);
            mListAdapter.notifyDataSetChanged();
            setListAdapter(mListAdapter);
            mItemNoticia = (ItemListNoticia) mListAdapter.getItem(0);
            new LoadingNewsNoticiasTaks().execute(String.valueOf(mItemNoticia.getId()));
            //if (index != -1) {
                //getListView().setSelectionFromTop(index, 0);
           // }
        } else {
            Log.i(TAG, "Buscando noticias");
            new LoadingNoticiasTaks().execute();
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        //bundle.putInt("item_position", getListView().getSelectedItemPosition());
    }

    private class LoadingNoticiasTaks extends AsyncTask<Void, Void, Integer> {

        protected void onPreExecute() {
            super.onPreExecute();
            mItemsNoticias.clear();
            mButtonRetry.setVisibility(View.GONE);
            getListView().setEmptyView(mLoading);
        }

        protected Integer doInBackground(Void[] paramArrayOfVoid) {
            SystemClock.sleep(2000);
            Log.i(TAG, "¡Buscando noticias!");
            mNoticias = mJSONParser.serviceLoadingNoticias();
            if (mNoticias == null) {
                return 0;
            } else if (mNoticias.size() != 0) {
                for (Noticia noticia : mNoticias) {
                    //Agregamos a la lista de noticias
                    mItemsNoticias.add(new ItemListNoticia(
                            noticia.getId(),
                            noticia.getTitulo(),
                            noticia.getFechapublicacion().getTime(),
                            noticia.getImagen(),
                            noticia.getDescripcion()));
                    //Guardamos en la base de datos
                    mNoticiasTable.insertData(noticia.getTitulo(),
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
                Log.i(TAG, "¡Noticias cargadas!");
                mListAdapter = new ListNoticiasAdapter(getActivity(), mItemsNoticias, ListadoNoticiasFragment.this);
                getListView().addFooterView(mBackToTop);
                setListAdapter(mListAdapter);
                getListView().removeFooterView(mBackToTop);
                getListView().post(new Runnable() {
                    public void run() {
                        int i = getListView().getLastVisiblePosition();
                        int j = getListAdapter().getCount();
                        if (i + 1 < j)
                            getListView().addFooterView(mBackToTop);
                    }
                });
            } else {
                Log.i(TAG, "¡Error al cargar noticias!");
                try {
                    mLoading.setVisibility(View.GONE);
                    getListView().setEmptyView(mButtonRetry);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private class LoadingNewsNoticiasTaks extends AsyncTask<String, Void, Integer> {

        protected void onPreExecute() {
            super.onPreExecute();
            mNoticias.clear();
        }

        protected Integer doInBackground(String... params) {
            SystemClock.sleep(2000);
            // Cargamos los parametros que enviaremos por URL 
            ArrayList<NameValuePair> parametros = new ArrayList<NameValuePair>();
            parametros.add(new BasicNameValuePair("ultimaNoticia", params[0]));
            Log.i(TAG, "Ultimo de la lista " + params[0]);
            mNoticias = mJSONParser.serviceRefreshNoticias(parametros);
            if (mNoticias == null) {
                return 0;
            } else if (mNoticias.size() != 0) {
                Log.i(TAG, "Tamaño de la lista " + mNoticias.size());
                for (Noticia noticia : mNoticias) {
                    //Agregamos a la lista de noticias
                    mItemsNoticias.add(new ItemListNoticia(
                            noticia.getId(),
                            noticia.getTitulo(),
                            noticia.getFechapublicacion().getTime(),
                            noticia.getImagen(),
                            noticia.getDescripcion()));
                    //Guardamos en la base de datos
                    mNoticiasTable.insertData(
                            noticia.getTitulo(),
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

        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mList.onRefreshComplete();
            switch (result) {
                case 100:
                    mListAdapter = new ListNoticiasAdapter(getActivity(), mItemsNoticias, ListadoNoticiasFragment.this);
                    getListView().addFooterView(mBackToTop);
                    //setListAdapter(mListAdapter);
                    mListAdapter.notifyDataSetChanged();
                    getListView().removeFooterView(mBackToTop);
                    getListView().post(new Runnable() {
                        public void run() {
                            int i = getListView().getLastVisiblePosition();
                            int j = getListAdapter().getCount();
                            if (i + 1 < j)
                                getListView().addFooterView(mBackToTop);
                        }
                    });
                    break;
                case 0:
                    try {
                        Log.i(TAG, "¡Noticias actualizadas!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}