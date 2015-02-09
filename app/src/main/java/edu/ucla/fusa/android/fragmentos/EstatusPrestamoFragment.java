package edu.ucla.fusa.android.fragmentos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.juanlabrador.grouplayout.GroupContainer;

import java.text.SimpleDateFormat;

import at.markushi.ui.CircleButton;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.VistasPrincipalesActivity;
import edu.ucla.fusa.android.modelo.herramientas.JSONParser;
import edu.ucla.fusa.android.modelo.instrumentos.Prestamo;
import edu.ucla.fusa.android.modelo.instrumentos.SolicitudPrestamo;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

/**
 * Created by juanlabrador on 22/01/15.
 */
public class EstatusPrestamoFragment extends Fragment implements Toolbar.OnMenuItemClickListener {
    
    private static String ESTATUS_RECHAZADO = "RECHAZADO";
    private static String ESTATUS_APROBADO = "APROBADO";
    private static String ESTATUS_FINALIZADO = "FINALIZADO";
    private static String ESTATUS_EN_PROCESO = "EN PROCESO";
    private static String ESTATUS_ENTREGADO = "ENTREGADO";
    private static String TAG = "EstatusPrestamoFragment";
    private GroupContainer mGrupoSolicitud;
    private GroupContainer mGrupoPrestamo;
    private GroupContainer mGrupoEstatus;
    private GroupContainer mGrupoInstrumento;
    private Toolbar mToolbar;
    private SolicitudPrestamo mSolicitudPrestamo;
    private Prestamo mPrestamo;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private VistasPrincipalesActivity mActivity;
    private TextView mRecordatorio;
    private TextView mEmpty;
    private CircularProgressBar mProgress;
    private CircleButton mRetryButton;
    private JSONParser mJSONParser;
    private ScrollView mContenedor;
    private LoadingSolicitudPrestamo mServiceSolicitud;
    private int mID;
    private Bundle mCache;

    public static EstatusPrestamoFragment newInstance(Bundle arguments) {
        EstatusPrestamoFragment fragment = new EstatusPrestamoFragment();
        fragment.setRetainInstance(true);
        if (arguments != null) {
            fragment.setArguments(arguments);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_drawer_estatus_prestamo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContenedor = (ScrollView) view.findViewById(R.id.contenedor_estatus);
        mGrupoSolicitud = (GroupContainer) view.findViewById(R.id.grupos_datos_solicitud);
        mGrupoSolicitud.addTextLayout(R.string.estatus_prestamo_codigo);
        mGrupoSolicitud.addTextLayout(R.string.estatus_prestamo_periodo);

        mGrupoInstrumento = (GroupContainer) view.findViewById(R.id.grupos_datos_instrumento);
        mGrupoInstrumento.addTextLayout(R.string.estatus_prestamo_tipo_instrumento);
        mGrupoInstrumento.addTextLayout(R.string.estatus_prestamo_serial);
        mGrupoInstrumento.addTextLayout(R.string.estatus_prestamo_marca);
        mGrupoInstrumento.addTextLayout(R.string.estatus_prestamo_modelo);

        mGrupoPrestamo = (GroupContainer) view.findViewById(R.id.grupo_datos_prestamo);
        mGrupoPrestamo.addTextLayout(R.string.estatus_prestamo_fecha_emision);
        mGrupoPrestamo.addTextLayout(R.string.estatus_prestamo_fecha_vencimiento);

        mGrupoEstatus = (GroupContainer) view.findViewById(R.id.grupo_estatus_prestamo);
        mGrupoEstatus.addTextLayout(R.string.estatus_prestamo_estado);

        mRecordatorio = (TextView) view.findViewById(R.id.recordatorio_prestamo);
        
        mProgress = (CircularProgressBar) view.findViewById(R.id.pb_cargando_estatus);
        mEmpty = (TextView) view.findViewById(R.id.estatus_vacio);
        mRetryButton = (CircleButton) view.findViewById(R.id.button_network_estatus);
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContenedor.setVisibility(View.GONE);
                mEmpty.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);
                mRetryButton.setVisibility(View.GONE);
                mServiceSolicitud = new LoadingSolicitudPrestamo();
                mServiceSolicitud.execute(mID);
            }
        });
        
        mID = getArguments().getInt("id_solicitud");
    }

    @Override
    public void onPause() {
        super.onPause();
        mCache = new Bundle();
        mCache.putParcelable("solicitud", mSolicitudPrestamo);
        mCache.putParcelable("prestamo", mPrestamo);
    }

    @Override
    public void onStop() {
        super.onStop();
        mCache = new Bundle();
        mCache.putParcelable("solicitud", mSolicitudPrestamo);
        mCache.putParcelable("prestamo", mPrestamo);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mServiceSolicitud != null) {
            if (!mServiceSolicitud.isCancelled()) {
                mServiceSolicitud.cancel(true);
            }
        }
        
        mToolbar.getMenu().clear();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.estatus_prestamo_titulo_barra);
        mJSONParser = new JSONParser();
        mActivity = (VistasPrincipalesActivity) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        mToolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (mCache != null) {
            mSolicitudPrestamo = mCache.getParcelable("solicitud");
            mPrestamo = mCache.getParcelable("prestamo");
            mostrarEstatus(mSolicitudPrestamo, mPrestamo);
        } else {
            mServiceSolicitud = new LoadingSolicitudPrestamo();
            mServiceSolicitud.execute(mID);
        }
    }

    private void mostrarEstatus(SolicitudPrestamo solicitud, Prestamo prestamo) {
        if (prestamo != null) {
            Log.i(TAG, "¡Tengo un prestamo!");
            mGrupoSolicitud.getTextLayoutAt(0).setContent(String.valueOf(solicitud.getId()));
            mGrupoSolicitud.getTextLayoutAt(1).setContent(solicitud.getTipoPrestamo().getDescripcion());
            
            mGrupoInstrumento.getTextLayoutAt(0).setContent(prestamo.getInstrumento().getTipoInstrumento().getDescripcion());
            mGrupoInstrumento.getTextLayoutAt(1).setContent(prestamo.getInstrumento().getSerial());
            mGrupoInstrumento.getTextLayoutAt(2).setContent(prestamo.getInstrumento().getModelo().getMarca().getDescripcion());
            mGrupoInstrumento.getTextLayoutAt(3).setContent(prestamo.getInstrumento().getModelo().getDescripcion());
            
            mGrupoPrestamo.getTextLayoutAt(0).setContent(mDateFormat.format(prestamo.getFechaEmision()));
            mGrupoPrestamo.getTextLayoutAt(1).setContent(mDateFormat.format(prestamo.getFechaVencimiento()));
            
            if (solicitud.getEstatus().equals("finalizado")) {
                mToolbar.inflateMenu(R.menu.action_nuevo);
                mToolbar.setOnMenuItemClickListener(this);
                mGrupoEstatus.getTextLayoutAt(0).setContent(ESTATUS_FINALIZADO);
                mGrupoEstatus.getTextLayoutAt(0).setContentColor(getResources().getColor(R.color.gris_oscuro));
                mRecordatorio.setVisibility(View.VISIBLE);
            } else if (solicitud.getEstatus().equals("entregado")) {
                mGrupoEstatus.getTextLayoutAt(0).setContent(ESTATUS_ENTREGADO);
                mGrupoEstatus.getTextLayoutAt(0).setContentColor(getResources().getColor(R.color.azul));
            } 
        } else {
            if (solicitud != null) {
                if (solicitud.getEstatus().equals("rechazado")) {
                    Log.i(TAG, "¡La solicitud fue rechazada!");
                    mGrupoSolicitud.getTextLayoutAt(0).setContent(String.valueOf(solicitud.getId()));
                    mGrupoSolicitud.getTextLayoutAt(1).setContent(solicitud.getTipoPrestamo().getDescripcion());

                    mGrupoInstrumento.getTextLayoutAt(0).setContent(solicitud.getTipoInstrumento().getDescripcion());
                    mGrupoInstrumento.getTextLayoutAt(1).setContent(R.string.estatus_prestamo_rechazado);
                    mGrupoInstrumento.getTextLayoutAt(2).setContent(R.string.estatus_prestamo_rechazado);
                    mGrupoInstrumento.getTextLayoutAt(3).setContent(R.string.estatus_prestamo_rechazado);

                    mGrupoPrestamo.getTextLayoutAt(0).setContent(solicitud.getFechaEmision());
                    mGrupoPrestamo.getTextLayoutAt(1).setContent(solicitud.getFechaVencimiento());

                    mToolbar.inflateMenu(R.menu.action_nuevo);
                    mToolbar.setOnMenuItemClickListener(this);
                    mGrupoEstatus.getTextLayoutAt(0).setContent(ESTATUS_RECHAZADO);
                    mGrupoEstatus.getTextLayoutAt(0).setContentColor(getResources().getColor(R.color.rojo));

                } else {
                    Log.i(TAG, "¡La solicitud esta en proceso o aprobada!");
                    mGrupoSolicitud.getTextLayoutAt(0).setContent(String.valueOf(solicitud.getId()));
                    mGrupoSolicitud.getTextLayoutAt(1).setContent(solicitud.getTipoPrestamo().getDescripcion());

                    mGrupoInstrumento.getTextLayoutAt(0).setContent(solicitud.getTipoInstrumento().getDescripcion());
                    mGrupoInstrumento.getTextLayoutAt(1).setContent(R.string.estatus_prestamo_por_asignar);
                    mGrupoInstrumento.getTextLayoutAt(2).setContent(R.string.estatus_prestamo_por_asignar);
                    mGrupoInstrumento.getTextLayoutAt(3).setContent(R.string.estatus_prestamo_por_asignar);

                    mGrupoPrestamo.getTextLayoutAt(0).setContent(solicitud.getFechaEmision());
                    mGrupoPrestamo.getTextLayoutAt(1).setContent(solicitud.getFechaVencimiento());

                    if (solicitud.getEstatus().equals("en proceso")) {
                        mGrupoEstatus.getTextLayoutAt(0).setContent(ESTATUS_EN_PROCESO);
                        mGrupoEstatus.getTextLayoutAt(0).setContentColor(getResources().getColor(R.color.gris_oscuro));
                    } else if (solicitud.getEstatus().equals("aprobado")) {
                        mGrupoEstatus.getTextLayoutAt(0).setContent(ESTATUS_APROBADO);
                        mGrupoEstatus.getTextLayoutAt(0).setContentColor(getResources().getColor(R.color.verde_bienvenido));
                    }
                }
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        mActivity.nuevaSolicitudPrestamo();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, SolicitudPrestamoFragment.newInstance())
                .addToBackStack(null)
                .commit();
        return true;
    }

    public class LoadingSolicitudPrestamo extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {
            mSolicitudPrestamo = mJSONParser.serviceSolicitudPrestamo(integers[0]);
            if (mSolicitudPrestamo != null) {
                if (mSolicitudPrestamo.getId() != -1) {
                    if (mSolicitudPrestamo.getEstatus().equals("entregado") || mSolicitudPrestamo.equals("finalizado")) {
                        mPrestamo = mJSONParser.servicePrestamo(mSolicitudPrestamo.getId());
                        if (mPrestamo != null) {
                            if (mPrestamo.getId() != -1) {
                                mPrestamo = null;
                                return 100;
                            } else {
                                return 100;
                            }
                        } else {
                            return 100;
                        }
                    } else {
                        return 100;
                    }
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
                    mostrarEstatus(mSolicitudPrestamo, mPrestamo);
                    mContenedor.setVisibility(View.VISIBLE);
                    mEmpty.setVisibility(View.GONE);
                    mProgress.setVisibility(View.GONE);
                    mRetryButton.setVisibility(View.GONE);
                    break;
                case 0:
                    Log.i(TAG, "¡Problemas con el servidor!");
                    mContenedor.setVisibility(View.GONE);
                    mEmpty.setVisibility(View.VISIBLE);
                    mProgress.setVisibility(View.GONE);
                    mRetryButton.setVisibility(View.VISIBLE);
                    break;
                case -1:
                    Log.i(TAG, "¡No hay solicitud!");
                    mActivity.actualizarSolicitud();
                    getFragmentManager().popBackStack();
                    break;
            }
        }
    }
}
