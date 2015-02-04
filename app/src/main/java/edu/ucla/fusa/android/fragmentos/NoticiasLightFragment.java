package edu.ucla.fusa.android.fragmentos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

import java.util.ArrayList;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ListNoticiasAdapter;
import edu.ucla.fusa.android.modelo.fundacion.Noticia;
import edu.ucla.fusa.android.modelo.herramientas.ItemListNoticia;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class NoticiasLightFragment extends ListFragment {

    private Toolbar mToolbar;
    private ListNoticiasAdapter mListAdapter;
    private ArrayList<ItemListNoticia> mItemsNoticias = new ArrayList();
    private ListView mList;
    private View mView;
    private JSONParser mJSONParser;
    private static String TAG = "ListadoNoticiasFragment";
    private CircularProgressBar mLoading;
    private ArrayList<Noticia> mNoticias;
    private DrawerArrowDrawable mDrawerArrow;

    public static NoticiasLightFragment newInstance() {
        NoticiasLightFragment fragment = new NoticiasLightFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        new LoadingNoticiasTaks().execute();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        super.onCreateView(inflater, container, bundle);
        mView = inflater.inflate(R.layout.fragment_list_noticias_light, container, false);
        
        mList = (ListView) mView.findViewById(android.R.id.list);

        mLoading = (CircularProgressBar) mView.findViewById(R.id.pb_cargando);
        
        return mView;
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
                        mItemsNoticias.add(0, new ItemListNoticia(
                                noticia.getId(),
                                noticia.getTitulo(),
                                noticia.getFechapublicacion(),
                                noticia.getImagen(),
                                noticia.getDescripcion(),
                                1));
                    } else {
                        mItemsNoticias.add(0, new ItemListNoticia(
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

        protected void onPostExecute(Integer response) {
            super.onPostExecute(response);
            if (response == 100) {
                mToolbar.setVisibility(View.VISIBLE);
                Log.i(TAG, "¡Noticias cargadas!");
                mListAdapter = new ListNoticiasAdapter(getActivity(), mItemsNoticias, NoticiasLightFragment.this);
                setListAdapter(mListAdapter);
            } else {
                Log.i(TAG, "¡Error al cargar noticias!");
                SnackbarManager.show(
                        Snackbar.with(getActivity())
                                .type(SnackbarType.MULTI_LINE)
                                .text(R.string.mensaje_error_busqueda));
                getFragmentManager().popBackStack();
            }
        }

    }
}