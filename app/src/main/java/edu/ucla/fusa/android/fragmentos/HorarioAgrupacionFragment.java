package edu.ucla.fusa.android.fragmentos;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.juanlabrador.grouplayout.GroupContainer;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

import java.util.List;

import at.markushi.ui.CircleButton;
import edu.ucla.fusa.android.DB.EstudianteTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.EvaluacionAgrupacionAdapter;
import edu.ucla.fusa.android.adaptadores.HorarioAgrupacionAdapter;
import edu.ucla.fusa.android.modelo.academico.Agrupacion;
import edu.ucla.fusa.android.modelo.academico.Estudiante;
import edu.ucla.fusa.android.modelo.academico.EvaluacionPorAgrupacion;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by juanlabrador on 24/11/14.
 */
public class HorarioAgrupacionFragment extends Fragment implements View.OnClickListener {

    private static String TAG = "HorarioAgrupacionFragment";
    private ListView mHorarioAgrupaciones;
    private Agrupacion mAgrupacion;
    private CircularProgressBar mProgress;
    private TextView mEmpty;
    private LinearLayout mContenedorHorario;
    private TextView mNombreAgrupacion;
    private TextView mTipoAgrupacion;
    private GroupContainer mDatosInstructor;
    private GroupContainer mVerNotas;
    private HorarioAgrupacionAdapter mHorarioAdapter;
    private Toolbar mToolbar;
    private CircleButton mButtonNetwork;
    
    // Evaluaciones
    private LinearLayout mContenedorEvaluaciones;
    private ListView mListaEvaluaciones;
    private MaterialDialog mCustomEvaluacion;
    private View mBarra1;
    private View mBarra2;
    private View mBarra3;
    private View mBarra4;
    private View mBarra5;
    private TextView mProgreso;
    private List<EvaluacionPorAgrupacion> mEvaluaciones;
    private JSONParser mJSONParser;
    private EstudianteTable mEstudianteTable;
    private Estudiante mEstudiante;
    private EvaluacionAgrupacionAdapter mEvaluacionAdapter;
    private CircularProgressBar mProgressDialog;
    private View mViewDialog;
    private int mTotalObjetivos;
    private int mObjetivosAprobados;
    private LoadingHorario mServiceHorario;
    private Bundle mCache;

    public static HorarioAgrupacionFragment newInstance(){
        HorarioAgrupacionFragment fragment = new HorarioAgrupacionFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_horario_agrupacion, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mContenedorHorario = (LinearLayout) view.findViewById(R.id.contenedor_horario_agrupacion);
        mHorarioAgrupaciones = (ListView) view.findViewById(R.id.lista_horario_agrupacion);
        mProgress = (CircularProgressBar) view.findViewById(R.id.pb_cargando_horario_agrupacion);
        mNombreAgrupacion = (TextView) view.findViewById(R.id.nombre_agrupacion);
        mDatosInstructor = (GroupContainer) view.findViewById(R.id.grupo_instructor);
        mEmpty = (TextView) view.findViewById(R.id.horario_agrupacion_vacio);
        mTipoAgrupacion = (TextView) view.findViewById(R.id.tipo_agrupacion);
        mVerNotas = (GroupContainer) view.findViewById(R.id.ver_notas);
        mVerNotas.addSimpleMultiTextLayout(R.string.agrupacion_horario_notas);
        mVerNotas.getSimpleMultiTextLayoutAt(0).setOnClickListener(this);
        mButtonNetwork = (CircleButton) view.findViewById(R.id.button_network);
        mButtonNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress.setVisibility(View.VISIBLE);
                mButtonNetwork.setVisibility(View.GONE);
                mEmpty.setVisibility(View.GONE);
                mServiceHorario = new LoadingHorario();
                mServiceHorario.execute(mEstudiante.getId());
            }
        });
        mServiceHorario = new LoadingHorario();
        mServiceHorario.execute(mEstudiante.getId());
    }

    @Override
    public void onResume() {
        super.onResume();
        mToolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mJSONParser = new JSONParser();
        mEstudianteTable = new EstudianteTable(getActivity());
        mEstudiante = mEstudianteTable.searchUser();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCache = new Bundle();
        mCache.putParcelable("agrupacion", mAgrupacion);
    }

    @Override
    public void onStop() {
        super.onStop();
        mCache = new Bundle();
        mCache.putParcelable("agrupacion", mAgrupacion);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mServiceHorario != null) {
            if (!mServiceHorario.isCancelled()) {
                mServiceHorario.cancel(true);
            }
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (mCache != null) {
            mAgrupacion = mCache.getParcelable("agrupacion");
            armarAgrupacion(mAgrupacion);
            mContenedorHorario.setVisibility(View.VISIBLE);
            mProgress.setVisibility(View.GONE);
            mEmpty.setVisibility(View.GONE);
        } else {
            mServiceHorario = new LoadingHorario();
            mServiceHorario.execute(mEstudiante.getId());
        }
    }

    private class LoadingHorario extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {
            mAgrupacion = mJSONParser.serviceAgrupacionEstudiante(integers[0]);
            if (mAgrupacion != null) {
                if (mAgrupacion.getId() != -1) {
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
                    Gson gson = new Gson();
                    String json = gson.toJson(mAgrupacion);
                    Log.i(TAG, json);
                    armarAgrupacion(mAgrupacion);
                    mContenedorHorario.setVisibility(View.VISIBLE);
                    mProgress.setVisibility(View.GONE);
                    mEmpty.setVisibility(View.GONE);
                    break;
                case 0:
                    mProgress.setVisibility(View.GONE);
                    mEmpty.setText(R.string.mensaje_reintentar);
                    mButtonNetwork.setVisibility(View.VISIBLE);
                    mEmpty.setVisibility(View.VISIBLE);
                    break;
                case -1:
                    mProgress.setVisibility(View.GONE);
                    mEmpty.setText(R.string.agrupacion_horario_vacio);
                    mEmpty.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (mDatosInstructor != null) {
            if (view == mDatosInstructor.getSimpleTwoButtonLayoutAt(1).getOneButton()) {
                try {
                    startActivity(new Intent("android.intent.action.CALL",
                            Uri.parse("tel:" + mDatosInstructor.getSimpleTwoButtonLayoutAt(1).getContent())));
                } catch (Exception e) {
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .text(R.string.error_llamada));
                    e.printStackTrace();
                }
            } else if (view == mDatosInstructor.getSimpleTwoButtonLayoutAt(1).getTwoButton()) {
                try {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setData(Uri.parse("smsto:"));
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address", mDatosInstructor.getSimpleTwoButtonLayoutAt(1).getContent());
                    smsIntent.putExtra("sms_body", "");
                    startActivity(smsIntent);
                } catch (Exception e) {
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.error_enviar_sms));
                    e.printStackTrace();
                }
            } else if (view == mDatosInstructor.getSimpleOneButtonLayoutAt(2).getButton()) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
                    String[] emails = new String[]{mDatosInstructor.getSimpleOneButtonLayoutAt(1).getContent()};
                    startActivity(Intent.createChooser(
                            intent.putExtra(Intent.EXTRA_EMAIL, emails)
                                    .putExtra(Intent.EXTRA_SUBJECT, "").setType("message/rfc822"),
                            getResources().getString(R.string.mensaje_elegir_cliente_correo)));
                } catch (ActivityNotFoundException e) {
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.error_enviar_correo));
                    e.printStackTrace();
                }
            } else if (view == mVerNotas.getSimpleMultiTextLayoutAt(0)) {
                armarListaEvaluaciones();
            } 
        }
    }
    
    private void armarAgrupacion(Agrupacion agrupacion) {
        mDatosInstructor.clear();
        mNombreAgrupacion.setText(agrupacion.getDescripcion().toUpperCase());
        mTipoAgrupacion.setText(agrupacion.getTipoAgrupacion().getDescripcion().toUpperCase());
        mDatosInstructor.addSimpleMultiTextLayout("Instructor: " + agrupacion.getInstructor().getNombre().toUpperCase() + " " + agrupacion.getInstructor().getApellido().toUpperCase());
        mDatosInstructor.addSimpleTwoButtonLayout(agrupacion.getInstructor().getTelefonoMovil(), R.drawable.ic_llamada, R.drawable.ic_sms);
        mDatosInstructor.addSimpleOneButtonLayout(agrupacion.getInstructor().getCorreo(), R.drawable.ic_correo);
        mDatosInstructor.getSimpleTwoButtonLayoutAt(1).getOneButton().setOnClickListener(HorarioAgrupacionFragment.this);
        mDatosInstructor.getSimpleTwoButtonLayoutAt(1).getTwoButton().setOnClickListener(HorarioAgrupacionFragment.this);
        mDatosInstructor.getSimpleOneButtonLayoutAt(2).getButton().setOnClickListener(HorarioAgrupacionFragment.this);
        for (int i = 0; i < agrupacion.getHorarioArea().size(); i++) {
            Log.i(TAG, agrupacion.getHorarioArea().get(i).getHorario().getDia().getDescripcion());
        }
        mHorarioAdapter = new HorarioAgrupacionAdapter(getActivity(), agrupacion.getHorarioArea());
        mHorarioAgrupaciones.setAdapter(mHorarioAdapter);
        
    }
    
    private void armarListaEvaluaciones() {
        mCustomEvaluacion = new MaterialDialog(getActivity());
        mViewDialog = LayoutInflater.from(getActivity()).inflate(R.layout.custom_evaluacion_agrupacion, null);

        mListaEvaluaciones = (ListView) mViewDialog.findViewById(R.id.lista_evaluaciones_agrupacion);
        mProgressDialog = (CircularProgressBar) mViewDialog.findViewById(R.id.pb_cargando_evaluacion_agrupacion);
        mContenedorEvaluaciones = (LinearLayout) mViewDialog.findViewById(R.id.contenedor_evaluaciones_agrupacion);
        mBarra1 = mViewDialog.findViewById(R.id.barra_nivel_1);
        mBarra2 = mViewDialog.findViewById(R.id.barra_nivel_2);
        mBarra3 = mViewDialog.findViewById(R.id.barra_nivel_3);
        mBarra4 = mViewDialog.findViewById(R.id.barra_nivel_4);
        mBarra5 = mViewDialog.findViewById(R.id.barra_nivel_5);
        mProgreso = (TextView) mViewDialog.findViewById(R.id.tv_status_progreso);
        new LoadingEvaluacion().execute(mEstudiante.getId());

        
        mCustomEvaluacion.setCanceledOnTouchOutside(true);
        mCustomEvaluacion.setBackgroundResource(R.color.gris_fondo);
        mCustomEvaluacion.setView(mViewDialog);
        mCustomEvaluacion.show();
        
    }
    
    private class LoadingEvaluacion extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mTotalObjetivos = 0;
            mObjetivosAprobados = 0;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            mEvaluaciones = mJSONParser.serviceEvaluacionPorAgrupacion(integers[0]);
            if (mEvaluaciones != null) {
                if (mEvaluaciones.size() != 0) {
                    return 100;
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case 100:
                    Log.i(TAG, "¡Tenemos evaluaciones!");
                    mTotalObjetivos = mEvaluaciones.size();
                    for (int i = 0; i < mTotalObjetivos; i++) {
                        if (mEvaluaciones.get(i).getEstadoCumplimiento().getDescripcion().equals("alcanzado")) {
                            mObjetivosAprobados++;
                        }
                    }
                    marcarProgreso(mTotalObjetivos, mObjetivosAprobados);
                    mEvaluacionAdapter = new EvaluacionAgrupacionAdapter(getActivity(), mEvaluaciones);
                    mListaEvaluaciones.setAdapter(mEvaluacionAdapter);
                    mProgressDialog.setVisibility(View.GONE);
                    mContenedorEvaluaciones.setVisibility(View.VISIBLE);
                    break;
                case -1:
                    Log.i(TAG, "¡No tenemos evaluaciones!");
                    mCustomEvaluacion.dismiss();
                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .type(SnackbarType.MULTI_LINE)
                                    .text(R.string.error_evaluaciones));
                    break;
            }
        }
    }
    
    private void marcarProgreso(int total, int aprobados) {
        if (total != 0) {
            int porcentaje = aprobados / total * 100;
            Log.i(TAG, "Porcentaje: " + porcentaje);
            if (0 == porcentaje) {
                mProgreso.setText("");
                mBarra1.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                mBarra2.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                mBarra3.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                mBarra4.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                mBarra5.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            } else if (0 < porcentaje && porcentaje < 20) {
                mProgreso.setText(R.string.progreso_nivel_1);
                mProgreso.setTextColor(getResources().getColor(R.color.nivel_1));
                mBarra1.setBackgroundColor(getResources().getColor(R.color.nivel_1));
                mBarra2.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                mBarra3.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                mBarra4.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                mBarra5.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            } else if (20 <= porcentaje && porcentaje < 40) {
                mProgreso.setText(R.string.progreso_nivel_2);
                mProgreso.setTextColor(getResources().getColor(R.color.nivel_2));
                mBarra1.setBackgroundColor(getResources().getColor(R.color.nivel_2));
                mBarra2.setBackgroundColor(getResources().getColor(R.color.nivel_2));
                mBarra3.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                mBarra4.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                mBarra5.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            } else if (40 <= porcentaje && porcentaje < 60) {
                mProgreso.setText(R.string.progreso_nivel_3);
                mProgreso.setTextColor(getResources().getColor(R.color.nivel_3));
                mBarra1.setBackgroundColor(getResources().getColor(R.color.nivel_3));
                mBarra2.setBackgroundColor(getResources().getColor(R.color.nivel_3));
                mBarra3.setBackgroundColor(getResources().getColor(R.color.nivel_3));
                mBarra4.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                mBarra5.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            } else if (60 <= porcentaje && porcentaje < 80) {
                mProgreso.setText(R.string.progreso_nivel_4);
                mProgreso.setTextColor(getResources().getColor(R.color.nivel_4));
                mBarra1.setBackgroundColor(getResources().getColor(R.color.nivel_4));
                mBarra2.setBackgroundColor(getResources().getColor(R.color.nivel_4));
                mBarra3.setBackgroundColor(getResources().getColor(R.color.nivel_4));
                mBarra4.setBackgroundColor(getResources().getColor(R.color.nivel_4));
                mBarra5.setBackgroundColor(getResources().getColor(R.color.nivel_4));
            } else if (80 <= porcentaje && porcentaje <= 100) {
                mProgreso.setText(R.string.progreso_nivel_5);
                mProgreso.setTextColor(getResources().getColor(R.color.nivel_5));
                mBarra1.setBackgroundColor(getResources().getColor(R.color.nivel_5));
                mBarra2.setBackgroundColor(getResources().getColor(R.color.nivel_5));
                mBarra3.setBackgroundColor(getResources().getColor(R.color.nivel_5));
                mBarra4.setBackgroundColor(getResources().getColor(R.color.nivel_5));
                mBarra5.setBackgroundColor(getResources().getColor(R.color.nivel_5));
            }
        }
    }
    
}
