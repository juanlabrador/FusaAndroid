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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ikimuhendis.ldrawer.DrawerArrowDrawable;

import java.util.ArrayList;

import at.markushi.ui.CircleButton;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ListNoticiasAdapter;
import edu.ucla.fusa.android.modelo.fundacion.Noticia;
import edu.ucla.fusa.android.modelo.herramientas.ItemListNoticia;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class NoticiasLightFragment extends ListFragment implements View.OnClickListener {

    private Toolbar mToolbar;
    private ListNoticiasAdapter mListAdapter;
    private ArrayList<ItemListNoticia> mItemsNoticias = new ArrayList();
    private JSONParser mJSONParser;
    private static String TAG = "ListadoNoticiasFragment";
    private CircleButton mButtonRetry;
    private TextView mTextLoading;
    private CircularProgressBar mLoading;
    private ArrayList<Noticia> mNoticias;
    private DrawerArrowDrawable mDrawerArrow;
    private Bundle mCache;
    private LoadingNoticiasTaks mServiceNoticias;
    private LinearLayout mContenedor;

    public static NoticiasLightFragment newInstance() {
        NoticiasLightFragment fragment = new NoticiasLightFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContenedor = (LinearLayout) view.findViewById(R.id.contenedor_noticias_light);
        mDrawerArrow = new DrawerArrowDrawable(getActivity()) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };

        mDrawerArrow.setProgress(1f);

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.noticias_titulo_barra);
        mToolbar.setNavigationIcon(mDrawerArrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        mToolbar.setVisibility(View.GONE);
        mJSONParser = new JSONParser();
        mNoticias = new ArrayList<>();

        mLoading = (CircularProgressBar) view.findViewById(R.id.pb_cargando_noticias_light);
        
        mTextLoading = (TextView) view.findViewById(R.id.noticias_light_vacias);
        mButtonRetry = (CircleButton) view.findViewById(R.id.button_network_noticias_light);
        mButtonRetry.setOnClickListener(this);
        new LoadingNoticiasTaks().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        super.onCreateView(inflater, container, bundle);
        return inflater.inflate(R.layout.fragment_list_noticias_light, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_network_noticias_light:
                mTextLoading.setVisibility(View.GONE);
                mButtonRetry.setVisibility(View.GONE);
                mLoading.setVisibility(View.VISIBLE);
                mContenedor.setVisibility(View.GONE);
                mServiceNoticias = new LoadingNoticiasTaks();
                mServiceNoticias.execute();
                break;
        }
    }
    
    

    public void onPause() {
        super.onPause();
        Log.i(TAG, "En onPause()");
        mCache = new Bundle();
        mCache.putParcelableArrayList("noticias", mItemsNoticias);
    }

    public void onResume() {
        super.onResume();
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.noticias_titulo_barra);
        mToolbar.getMenu().clear();
        mToolbar.setVisibility(View.VISIBLE);
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
            mListAdapter = new ListNoticiasAdapter(getActivity(), mItemsNoticias, NoticiasLightFragment.this);
            setListAdapter(mListAdapter);
            mTextLoading.setVisibility(View.GONE);
            mLoading.setVisibility(View.GONE);
            mContenedor.setVisibility(View.VISIBLE);
            Log.i(TAG, "¡Restaurando noticias!");
        } else {
            Log.i(TAG, "¡No tengo datos guardados!");
            mServiceNoticias = new LoadingNoticiasTaks();
            mServiceNoticias.execute();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mServiceNoticias != null) {
            if (!mServiceNoticias.isCancelled()) {
                mServiceNoticias.cancel(true);
            }
        }
    }


    private class LoadingNoticiasTaks extends AsyncTask<Void, Void, Integer> {

        protected void onPreExecute() {
            super.onPreExecute();
            mItemsNoticias.clear();
            getListView().setEmptyView(mLoading);
        }

        protected Integer doInBackground(Void[] paramArrayOfVoid) {
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
                Log.i(TAG, "¡Traje noticias del servidor!");
                return 100;
            } else {
                Log.i(TAG, "¡No pude traer noticias!");
                return 0;
            }
        }

        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 100:
                    Log.i(TAG, "¡Noticias cargadas!");
                    mListAdapter = new ListNoticiasAdapter(getActivity(), mItemsNoticias, NoticiasLightFragment.this);
                    setListAdapter(mListAdapter);
                    mTextLoading.setVisibility(View.GONE);
                    mLoading.setVisibility(View.GONE);
                    mContenedor.setVisibility(View.VISIBLE);
                    break;
                case 0:
                    Log.i(TAG, "¡No hay conexion!");
                    mContenedor.setVisibility(View.GONE);
                    mTextLoading.setText(R.string.mensaje_error_servidor);
                    mTextLoading.setVisibility(View.VISIBLE);
                    mButtonRetry.setVisibility(View.VISIBLE);
                    mLoading.setVisibility(View.GONE);
                    break;
                case -1:
                    Log.i(TAG, "¡No hay noticias!");
                    mContenedor.setVisibility(View.GONE);
                    mTextLoading.setText(R.string.noticias_vacia);
                    mTextLoading.setVisibility(View.VISIBLE);
                    mButtonRetry.setVisibility(View.VISIBLE);
                    mLoading.setVisibility(View.GONE);
                    break;
            }
        }
    }
}