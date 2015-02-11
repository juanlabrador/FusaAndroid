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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.yalantis.pulltorefresh.library.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;
import edu.ucla.fusa.android.DB.EstudianteTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ListNoticiasImportanteAdapter;
import edu.ucla.fusa.android.adaptadores.ListNotificacionesAdapter;
import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.fundacion.NoticiaSlide;
import edu.ucla.fusa.android.modelo.herramientas.ItemListNoticia;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import edu.ucla.fusa.android.modelo.instrumentos.Notificacion;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class NotificacionesFragment extends ListFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListNotificacionesAdapter mListAdapter;
    private ArrayList<Notificacion> mNotificaciones;
    private JSONParser mJSONParser;
    private static String TAG = "NotificacionesFragment";
    private CircleButton mButtonRetry;
    private TextView mTextLoading;
    private CircularProgressBar mLoading;
    private Notificacion mNotificacion;
    private LoadingNotificaciones mServiceNotificaciones;
    private UpdateNotificacion mServiceUpdate;
    private Bundle mCache;
    private Toolbar mToolbar;
    private Estudiante mEstudiante;
    private EstudianteTable mEstudianteTable;

    public static NotificacionesFragment newInstance() {
        NotificacionesFragment fragment = new NotificacionesFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mJSONParser = new JSONParser();
        mEstudianteTable = new EstudianteTable(getActivity());
        mEstudiante = mEstudianteTable.searchUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        super.onCreateView(inflater, container, bundle);
        return inflater.inflate(R.layout.fragment_drawer_notificaciones, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.notificaciones_barra_titulo);
        mLoading = (CircularProgressBar) view.findViewById(R.id.pb_cargando_notificaciones);
        mTextLoading = (TextView) view.findViewById(R.id.notificaciones_vacias);
        mButtonRetry = (CircleButton) view.findViewById(R.id.button_network_notificaciones);
        mButtonRetry.setOnClickListener(this);
        getListView().setOnItemClickListener(this);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "En onPause()");
        mCache = new Bundle();
        mCache.putParcelableArrayList("notificaciones", mNotificaciones);
    }


    @Override
    public void onStop() {
        super.onStop();
        mCache = new Bundle();
        mCache.putParcelableArrayList("notificaciones", mNotificaciones);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (mCache != null) {
            mNotificaciones = mCache.getParcelableArrayList("notificaciones");
            mListAdapter = new ListNotificacionesAdapter(getActivity(), mNotificaciones);
            setListAdapter(mListAdapter);
            mTextLoading.setVisibility(View.GONE);
            mLoading.setVisibility(View.GONE);
            getListView().setVisibility(View.VISIBLE);
            Log.i(TAG, "¡Restaurando noticias!");
        } else {
            Log.i(TAG, "¡No tengo datos guardados!");
            mServiceNotificaciones = new LoadingNotificaciones();
            mServiceNotificaciones.execute();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_network_notificaciones:
                mTextLoading.setVisibility(View.GONE);
                mButtonRetry.setVisibility(View.GONE);
                mLoading.setVisibility(View.VISIBLE);
                getListView().setVisibility(View.GONE);
                mServiceNotificaciones = new LoadingNotificaciones();
                mServiceNotificaciones.execute();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mServiceNotificaciones != null) {
            if (!mServiceNotificaciones.isCancelled()) {
                mServiceNotificaciones.cancel(true);
            }
        }
        
        if (mServiceUpdate != null) {
            if (!mServiceUpdate.isCancelled()) {
                mServiceUpdate.cancel(true);
            }
        }
        Log.i(TAG, "¡Destruyendo servicios!");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i(TAG, "¡Haciendo click en un item!");
        mNotificacion = mNotificaciones.get(i);
        if (!mNotificacion.isMensajeLeido()) {
            ((RelativeLayout) view).findViewById(R.id.contenedor_notificacion).setBackgroundColor(getResources().getColor(android.R.color.white));
            mNotificacion.setMensajeLeido(true);
            mNotificacion.setEstudiante(mEstudiante);
            mServiceUpdate = new UpdateNotificacion();

            mServiceUpdate.execute(mNotificacion);
        }
    }

    private class LoadingNotificaciones extends AsyncTask<Void, Void, Integer> {

        protected void onPreExecute() {
            super.onPreExecute();
            mButtonRetry.setVisibility(View.GONE);
            getListView().setVisibility(View.GONE);
        }

        protected Integer doInBackground(Void[] paramArrayOfVoid) {
            mNotificaciones = mJSONParser.serviceLoadingNotificaciones(mEstudiante.getId());
            if (mNotificaciones == null) {
                return 0;
            } else if (mNotificaciones.size() != 0) {
                return 100;
            } else {
                return -1;
            }
        }

        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 100:
                    Log.i(TAG, "¡Notificaciones cargadas!");
                    mListAdapter = new ListNotificacionesAdapter(getActivity(), mNotificaciones);
                    setListAdapter(mListAdapter);
                    mTextLoading.setVisibility(View.GONE);
                    mLoading.setVisibility(View.GONE);
                    getListView().setVisibility(View.VISIBLE);
                    break;
                case 0:
                    Log.i(TAG, "¡No hay conexion!");
                    getListView().setVisibility(View.GONE);
                    mTextLoading.setText(R.string.mensaje_reintentar);
                    mTextLoading.setVisibility(View.VISIBLE);
                    mButtonRetry.setVisibility(View.VISIBLE);
                    mLoading.setVisibility(View.GONE);
                    break;
                case -1:
                    Log.i(TAG, "¡No hay notificaciones!");
                    getListView().setVisibility(View.GONE);
                    mTextLoading.setText(R.string.notificaciones_vacia);
                    mTextLoading.setVisibility(View.VISIBLE);
                    mButtonRetry.setVisibility(View.VISIBLE);
                    mLoading.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private class UpdateNotificacion extends AsyncTask<Notificacion, Void, Integer> {

        protected Integer doInBackground(Notificacion... params) {
            return mJSONParser.serviceNotificacionLeido(params[0]);
        }

        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 100:
                    Log.i(TAG, "¡Notificacion leida!");
                    break;
                case 0:
                    Log.i(TAG, "¡No hay forma de conectarse al servidor!");
                    break;
                case -1:
                    Log.i(TAG, "¡Notificacion no actualizada!");
                    break;
            }
        }
    }
}