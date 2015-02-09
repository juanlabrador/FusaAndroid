package edu.ucla.fusa.android.fragmentos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.yalantis.pulltorefresh.library.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ListNoticiasAdapter;
import edu.ucla.fusa.android.adaptadores.ListNoticiasImportanteAdapter;
import edu.ucla.fusa.android.modelo.fundacion.Noticia;
import edu.ucla.fusa.android.modelo.fundacion.NoticiaSlide;
import edu.ucla.fusa.android.modelo.herramientas.ItemListNoticia;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class NoticiasImportantesFragment extends ListFragment implements PullToRefreshView.OnRefreshListener, View.OnClickListener {

    private PullToRefreshView mPull;
    private ListNoticiasImportanteAdapter mListAdapter;
    private ArrayList<ItemListNoticia> mItemsNoticias;
    private JSONParser mJSONParser;
    private static String TAG = "ListadoNoticiasFragment";
    private CircleButton mButtonRetry;
    private TextView mTextLoading;
    private CircularProgressBar mLoading;
    private ItemListNoticia mItemNoticia;
    private List<NoticiaSlide> mNoticias;
    private int contador = 0;
    private LoadingNewsNoticiasTaks mServiceNew;
    private LoadingNoticiasTaks mServiceNoticias;
    private Bundle mCache;

    public static NoticiasImportantesFragment newInstance() {
        NoticiasImportantesFragment fragment = new NoticiasImportantesFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mJSONParser = new JSONParser();
        mNoticias = new ArrayList<>();
        mItemsNoticias = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        super.onCreateView(inflater, container, bundle);
        return inflater.inflate(R.layout.fragment_drawer_list_noticias_importantes, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPull = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh_importantes);
        mPull.setOnRefreshListener(this);

        mLoading = (CircularProgressBar) view.findViewById(R.id.pb_cargando_noticias_importantes);
        mTextLoading = (TextView) view.findViewById(R.id.noticias_vacias_importantes);
        mButtonRetry = (CircleButton) view.findViewById(R.id.button_network_noticias_importantes);
        mButtonRetry.setOnClickListener(this);
        
    }
    
    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "En onPause()");
        mCache = new Bundle();
        mCache.putParcelableArrayList("noticias", mItemsNoticias);
    }


    @Override
    public void onStop() {
        super.onStop();
        mCache = new Bundle();
        mCache.putParcelableArrayList("noticias", mItemsNoticias);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (mCache != null) {
            mItemsNoticias = mCache.getParcelableArrayList("noticias");
            Log.i(TAG, "Noticias restauradas: " + mItemsNoticias.size());
            mListAdapter = new ListNoticiasImportanteAdapter(getActivity(), mItemsNoticias, NoticiasImportantesFragment.this);
            setListAdapter(mListAdapter);
            mTextLoading.setVisibility(View.GONE);
            mLoading.setVisibility(View.GONE);
            mPull.setVisibility(View.VISIBLE);
            Log.i(TAG, "¡Restaurando noticias!");
        } else {
            Log.i(TAG, "¡No tengo datos guardados!");
            mServiceNoticias = new LoadingNoticiasTaks();
            mServiceNoticias.execute();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_network_noticias_importantes:
                mTextLoading.setVisibility(View.GONE);
                mButtonRetry.setVisibility(View.GONE);
                mLoading.setVisibility(View.VISIBLE);
                mPull.setVisibility(View.GONE);
                mServiceNoticias = new LoadingNoticiasTaks();
                mServiceNoticias.execute();
                break;
        }
    }

    @Override
    public void onRefresh() {
        mItemNoticia = (ItemListNoticia) mListAdapter.getItem(0);
        mServiceNew = new LoadingNewsNoticiasTaks();
        mServiceNew.execute(mItemNoticia.getId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mServiceNoticias != null) {
            if (!mServiceNoticias.isCancelled()) {
                mServiceNoticias.cancel(true);
            }
        }
        
        if (mServiceNew != null) {
            if (!mServiceNew.isCancelled()) {
                mServiceNoticias.cancel(true);
            }
        }
        Log.i(TAG, "¡Destruyendo servicios!");
    }

    private class LoadingNoticiasTaks extends AsyncTask<Void, Void, Integer> {

        protected void onPreExecute() {
            super.onPreExecute();
            mItemsNoticias.clear();
            mButtonRetry.setVisibility(View.GONE);
            mPull.setVisibility(View.GONE);
        }

        protected Integer doInBackground(Void[] paramArrayOfVoid) {
            mNoticias = mJSONParser.serviceLoadingNoticiasImportantes();
            if (mNoticias == null) {
                return 0;
            } else if (mNoticias.size() != 0) {
                for (NoticiaSlide noticia : mNoticias) {
                    //Agregamos a la lista de noticias
                    if (noticia.getImagen() != null) {
                        mItemsNoticias.add(new ItemListNoticia(
                                noticia.getId(),
                                noticia.getTitulo(),
                                noticia.getFechapublicacion(),
                                noticia.getImagen(),
                                noticia.getDescripcion(),
                                1));
                    } else {
                        mItemsNoticias.add(new ItemListNoticia(
                                noticia.getId(),
                                noticia.getTitulo(),
                                noticia.getFechapublicacion(),
                                noticia.getImagen(),
                                noticia.getDescripcion(),
                                0));
                    }
                }
                return 100;
            } else {
                return -1;
            }
        }

        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 100:
                    Log.i(TAG, "¡Noticias cargadas!");
                    mListAdapter = new ListNoticiasImportanteAdapter(getActivity(), mItemsNoticias, NoticiasImportantesFragment.this);
                    setListAdapter(mListAdapter);
                    mTextLoading.setVisibility(View.GONE);
                    mLoading.setVisibility(View.GONE);
                    mPull.setVisibility(View.VISIBLE);
                    break;
                case 0:
                    Log.i(TAG, "¡No hay conexion!");
                    mPull.setVisibility(View.GONE);
                    mTextLoading.setText(R.string.mensaje_reintentar);
                    mTextLoading.setVisibility(View.VISIBLE);
                    mButtonRetry.setVisibility(View.VISIBLE);
                    mLoading.setVisibility(View.GONE);
                    break;
                case -1:
                    Log.i(TAG, "¡No hay noticias!");
                    mPull.setVisibility(View.GONE);
                    mTextLoading.setText(R.string.noticias_vacia);
                    mTextLoading.setVisibility(View.VISIBLE);
                    mButtonRetry.setVisibility(View.VISIBLE);
                    mLoading.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private class LoadingNewsNoticiasTaks extends AsyncTask<Long, Void, Integer> {
        
        protected void onPreExecute() {
            super.onPreExecute();
            mNoticias.clear();
            contador++;
        }

        protected Integer doInBackground(Long... params) {
            Log.i(TAG, "Ultimo de la lista " + params[0]);
            mNoticias = mJSONParser.serviceRefreshNoticiasImportantes(params[0]);
            if (mNoticias == null) {
                return 0;
            } else if (mNoticias.size() != 0) {
                Log.i(TAG, "Tamaño de la lista " + mNoticias.size());
                for (NoticiaSlide noticia : mNoticias) {
                    if (noticia.getImagen() != null) {   //Noticia con foto
                        mItemsNoticias.add(0, new ItemListNoticia(
                                noticia.getId(),
                                noticia.getTitulo(),
                                noticia.getFechapublicacion(),
                                noticia.getImagen(),
                                noticia.getDescripcion(),
                                1));
                    } else {   //Noticias sin foto
                        mItemsNoticias.add(0, new ItemListNoticia(
                                noticia.getId(),
                                noticia.getTitulo(),
                                noticia.getFechapublicacion(),
                                noticia.getImagen(),
                                noticia.getDescripcion(),
                                0));
                    }
                }
                return 100;
            } else {
                return -1;
            }
        }

        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mPull.setRefreshing(false);
            switch (result) {
                case 100:
                    Log.i(TAG, "¡Hay nuevas noticias!");
                    mListAdapter = new ListNoticiasImportanteAdapter(getActivity(), mItemsNoticias, NoticiasImportantesFragment.this);
                    setListAdapter(mListAdapter);
                    mListAdapter.notifyDataSetChanged();
                    break;
                case 0:
                    Log.i(TAG, "¡No hay forma de conectarse para traer nuevas noticias!");
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.noticias_error_refrescar));
                    break;
                case -1:
                    Log.i(TAG, "¡Noticias actualizadas!");
                    if (contador > 2) {
                        SnackbarManager.show(
                                Snackbar.with(getActivity())
                                        .type(SnackbarType.MULTI_LINE)
                                        .text(R.string.noticias_actualizadas));
                    }
                    break;
            }
        }
    }
}