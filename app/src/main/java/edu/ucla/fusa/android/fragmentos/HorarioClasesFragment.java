package edu.ucla.fusa.android.fragmentos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import at.markushi.ui.CircleButton;
import edu.ucla.fusa.android.DB.EstudianteTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ListClasesAdapter;
import edu.ucla.fusa.android.modelo.academico.ClaseParticular;
import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

/**
 * Created by juanlabrador on 24/11/14.
 */
public class HorarioClasesFragment extends ListFragment {

    private static String TAG = "HorarioClasesFragment";
    private TextView mTextEmpty;
    private CircularProgressBar mProgress;
    private CircleButton mRetryButton;
    private ArrayList<ClaseParticular> mClases;
    private JSONParser mJSONParser;
    private EstudianteTable mEstudianteTable;
    private Estudiante mEstudiante;
    private ListClasesAdapter mAdapter;
    private LoadingClases mServiceClases;
    private Bundle mCache;

    public static HorarioClasesFragment newInstance () {
        HorarioClasesFragment fragment = new HorarioClasesFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_horario_clases, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgress = (CircularProgressBar) view.findViewById(R.id.pb_cargando_horario_clases);
        mRetryButton = (CircleButton) view.findViewById(R.id.button_network_clases);
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListView().setVisibility(View.GONE);
                mTextEmpty.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);
                mRetryButton.setVisibility(View.GONE);
                mServiceClases = new LoadingClases();
                mServiceClases.execute(mEstudiante.getId());
            }
        });
        mTextEmpty = (TextView) view.findViewById(R.id.horario_clases_vacio);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mJSONParser = new JSONParser();
        mEstudianteTable = new EstudianteTable(getActivity());
        mEstudiante = mEstudianteTable.searchUser();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "En onPause()");
        mCache = new Bundle();
        mCache.putParcelableArrayList("clases", mClases);
    }

    @Override
    public void onStop() {
        super.onStop();
        mCache = new Bundle();
        mCache.putParcelableArrayList("clases", mClases);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (mCache != null) {
            mClases = mCache.getParcelableArrayList("clases");
            mAdapter = new ListClasesAdapter(getActivity(), mClases);
            setListAdapter(mAdapter);
            getListView().setVisibility(View.VISIBLE);
            mTextEmpty.setVisibility(View.GONE);
            mProgress.setVisibility(View.GONE);
            mRetryButton.setVisibility(View.GONE);
            Log.i(TAG, "¡Restaurando noticias!");
        } else {
            Log.i(TAG, "¡No tengo datos guardados!");
            mServiceClases = new LoadingClases();
            mServiceClases.execute(mEstudiante.getId());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mServiceClases != null) {
            if (!mServiceClases.isCancelled()) {
                mServiceClases.cancel(true);
            }
        }
        Log.i(TAG, "¡Destruyendo servicios!");
    }


    private class LoadingClases extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {
            mClases = mJSONParser.serviceClaseEstudiante(integers[0]);
            if (mClases != null) {
                if (mClases.size() != 0) {
                    return 100;
                } else {
                    return -1;
                }
            } else {
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 100:
                    mAdapter = new ListClasesAdapter(getActivity(), mClases);
                    setListAdapter(mAdapter);
                    getListView().setVisibility(View.VISIBLE);
                    mTextEmpty.setVisibility(View.GONE);
                    mProgress.setVisibility(View.GONE);
                    mRetryButton.setVisibility(View.GONE);
                    break;
                case 0:
                    getListView().setVisibility(View.GONE);
                    mTextEmpty.setText(R.string.mensaje_reintentar);
                    mTextEmpty.setVisibility(View.VISIBLE);
                    mProgress.setVisibility(View.GONE);
                    mRetryButton.setVisibility(View.VISIBLE);
                    break;
                case -1:
                    getListView().setVisibility(View.GONE);
                    mTextEmpty.setText(R.string.mis_clases_sin_clase);
                    mTextEmpty.setVisibility(View.VISIBLE);
                    mProgress.setVisibility(View.GONE);
                    mRetryButton.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
