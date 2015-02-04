package edu.ucla.fusa.android.fragmentos;

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

import com.yalantis.pulltorefresh.library.PullToRefreshView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import edu.ucla.fusa.android.DB.NoticiasTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ListNoticiasAdapter;
import edu.ucla.fusa.android.modelo.fundacion.Noticia;
import edu.ucla.fusa.android.modelo.herramientas.ItemListNoticia;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;

import java.util.ArrayList;

public class NoticiasFragment extends ListFragment implements PullToRefreshView.OnRefreshListener, View.OnClickListener {

    private PullToRefreshView mPull;
    private Toolbar mToolbar;
    private ListNoticiasAdapter mListAdapter;
    private int mIndex = -1;
    private ArrayList<ItemListNoticia> mItemsNoticias = new ArrayList();
    private ListView mList;
    private View mView;
    private JSONParser mJSONParser;
    private NoticiasTable mNoticiasTable;
    private static String TAG = "ListadoNoticiasFragment";
    private Button mButtonRetry;
    private TextView mTextLoading;
    private ProgressBar mLoading;
    private ItemListNoticia mItemNoticia;
    private ArrayList<Noticia> mNoticias;

    public static NoticiasFragment newInstance() {
        NoticiasFragment fragment = new NoticiasFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reintentar_conexion_noticias:
                new LoadingNoticiasTaks().execute();
                break;
        }

    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.noticias_titulo_barra);
        mToolbar.getMenu().clear();
        mToolbar.setVisibility(View.VISIBLE);
        mNoticiasTable = new NoticiasTable(getActivity());
        mJSONParser = new JSONParser();
        mNoticias = new ArrayList<>();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        super.onCreateView(inflater, container, bundle);
        mView = inflater.inflate(R.layout.fragment_drawer_list_noticias, container, false);
        
        mList = (ListView) mView.findViewById(android.R.id.list);

        mPull = (PullToRefreshView) mView.findViewById(R.id.pull_to_refresh);
        mPull.setOnRefreshListener(this);

        mLoading = (ProgressBar) mView.findViewById(R.id.progress_bar_noticias);
        mTextLoading = (TextView) mView.findViewById(R.id.tv_empty_text);
        mButtonRetry = (Button) mView.findViewById(R.id.btn_reintentar_conexion_noticias);
        mButtonRetry.setOnClickListener(this);
        
        return mView;
    }


    public void onPause() {
        super.onPause();
        mIndex = getListView().getLastVisiblePosition();
    }

    public void onResume() {
        super.onResume();
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.noticias_titulo_barra);
        mToolbar.getMenu().clear();
        mToolbar.setVisibility(View.VISIBLE);
        mItemsNoticias = mNoticiasTable.searchNews();
        if (mItemsNoticias.size() != 0) {
            Log.i(TAG, "Cantidad de noticias: " + mItemsNoticias.size());
            mListAdapter = new ListNoticiasAdapter(getActivity(), mItemsNoticias, this);
            mListAdapter.notifyDataSetChanged();
            setListAdapter(mListAdapter);
            mItemNoticia = (ItemListNoticia) mListAdapter.getItem(0);
            mNoticiasTable.borrarViejasNoticias();
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

    @Override
    public void onRefresh() {
        mPull.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPull.setRefreshing(false);
                mItemNoticia = (ItemListNoticia) mListAdapter.getItem(0);
                new LoadingNewsNoticiasTaks().execute(String.valueOf(mItemNoticia.getId()));
            }
        }, 3000);
        
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
            mItemsNoticias = mNoticiasTable.searchNews();
            if (mItemsNoticias == null) {
                Log.i(TAG, "¡No tengo noticias en la base de datos!");
                mNoticias = mJSONParser.serviceLoadingNoticias();
                if (mNoticias == null) {
                    return 0;
                } else if (mNoticias.size() != 0) {
                    for (Noticia noticia : mNoticias) {
                        //Agregamos a la lista de noticias
                        if (noticia.getImagen() != null) {
                            mItemsNoticias.add(new ItemListNoticia(
                                    noticia.getId(),
                                    noticia.getTitulo(),
                                    noticia.getFechapublicacion(),
                                    noticia.getImagen(),
                                    noticia.getDescripcion(),
                                    1));
                            //Guardamos en la base de datos
                            mNoticiasTable.insertData(noticia.getTitulo(),
                                    noticia.getDescripcion(),
                                    noticia.getFechapublicacion(),
                                    noticia.getImagen(),
                                    noticia.getId(),
                                    1);
                        } else {
                            mItemsNoticias.add(new ItemListNoticia(
                                    noticia.getId(),
                                    noticia.getTitulo(),
                                    noticia.getFechapublicacion(),
                                    noticia.getImagen(),
                                    noticia.getDescripcion(),
                                    0));
                            //Guardamos en la base de datos
                            mNoticiasTable.insertData(noticia.getTitulo(),
                                    noticia.getDescripcion(),
                                    noticia.getFechapublicacion(),
                                    noticia.getImagen(),
                                    noticia.getId(),
                                    0);
                        }
                    }
                    Log.i(TAG, "¡Traje noticias del servidor!");
                    return 100;
                } else {
                    Log.i(TAG, "¡No pude traer noticias!");
                    return 0;
                }
            } else {

                Log.i(TAG, "¡Tengo noticias en la base de datos!");
                return 100;
            }
        }

        protected void onPostExecute(Integer response) {
            super.onPostExecute(response);
            if (response == 100) {
                Log.i(TAG, "¡Noticias cargadas!");
                mListAdapter = new ListNoticiasAdapter(getActivity(), mItemsNoticias, NoticiasFragment.this);
                //getListView().addFooterView(mBackToTop);
                setListAdapter(mListAdapter);
                //getListView().removeFooterView(mBackToTop);
                /*getListView().post(new Runnable() {
                    public void run() {
                        int i = getListView().getLastVisiblePosition();
                        int j = getListAdapter().getCount();
                        if (i + 1 < j)
                            getListView().addFooterView(mBackToTop);
                    }
                });*/
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
                    if (noticia.getImagen() != null) {   //Noticia con foto
                        mItemsNoticias.add(0, new ItemListNoticia(
                                noticia.getId(),
                                noticia.getTitulo(),
                                noticia.getFechapublicacion(),
                                noticia.getImagen(),
                                noticia.getDescripcion(),
                                1));
                        //Guardamos en la base de datos
                        mNoticiasTable.insertData(noticia.getTitulo(),
                                noticia.getDescripcion(),
                                noticia.getFechapublicacion(),
                                noticia.getImagen(),
                                noticia.getId(),
                                1);
                    } else {   //Noticias sin foto
                        mItemsNoticias.add(0, new ItemListNoticia(
                                noticia.getId(),
                                noticia.getTitulo(),
                                noticia.getFechapublicacion(),
                                noticia.getImagen(),
                                noticia.getDescripcion(),
                                0));
                        //Guardamos en la base de datos
                        mNoticiasTable.insertData(noticia.getTitulo(),
                                noticia.getDescripcion(),
                                noticia.getFechapublicacion(),
                                noticia.getImagen(),
                                noticia.getId(),
                                0);
                    }
                }
                return 100;
            } else {
                return 0;
            }
        }

        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 100:
                    Log.i(TAG, "¡Hay nuevas noticias!");
                    mListAdapter = new ListNoticiasAdapter(getActivity(), mItemsNoticias, NoticiasFragment.this);
                    //getListView().addFooterView(mBackToTop);
                    setListAdapter(mListAdapter);
                    mListAdapter.notifyDataSetChanged();
                    //getListView().removeFooterView(mBackToTop);
                    /*getListView().post(new Runnable() {
                        public void run() {
                            int i = getListView().getLastVisiblePosition();
                            int j = getListAdapter().getCount();
                            if (i + 1 < j)
                                getListView().addFooterView(mBackToTop);
                        }
                    });*/
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