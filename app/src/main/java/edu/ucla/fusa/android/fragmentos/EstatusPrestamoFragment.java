package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juanlabrador.GroupLayout;

import java.text.SimpleDateFormat;

import edu.ucla.fusa.android.DB.PrestamoTable;
import edu.ucla.fusa.android.DB.SolicitudPrestamoTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.VistasPrincipalesActivity;
import edu.ucla.fusa.android.modelo.instrumentos.Prestamo;
import edu.ucla.fusa.android.modelo.instrumentos.SolicitudPrestamo;

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
    private View mView;
    private GroupLayout mGrupoSolicitud;
    private GroupLayout mGrupoPrestamo;
    private GroupLayout mGrupoEstatus;
    private GroupLayout mGrupoInstrumento;
    private Toolbar mToolbar;
    private SolicitudPrestamoTable mSolicitudPrestamoTable;
    private SolicitudPrestamo mSolicitudPrestamo;
    private PrestamoTable mPrestamoTable;
    private Prestamo mPrestamo;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static VistasPrincipalesActivity mActivity;
    private TextView mRecordatorio;
    
    public static EstatusPrestamoFragment newInstance(VistasPrincipalesActivity activity) {
        EstatusPrestamoFragment fragment = new EstatusPrestamoFragment();
        fragment.setRetainInstance(true);
        mActivity = activity;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_drawer_estatus_prestamo, container, false);
        
        mGrupoSolicitud = (GroupLayout) mView.findViewById(R.id.grupos_datos_solicitud);
        mGrupoSolicitud.addTextLayout(R.string.estatus_prestamo_codigo);
        mGrupoSolicitud.addTextLayout(R.string.estatus_prestamo_periodo);
        
        mGrupoInstrumento = (GroupLayout) mView.findViewById(R.id.grupos_datos_instrumento);
        mGrupoInstrumento.addTextLayout(R.string.estatus_prestamo_tipo_instrumento);
        mGrupoInstrumento.addTextLayout(R.string.estatus_prestamo_serial);
        mGrupoInstrumento.addTextLayout(R.string.estatus_prestamo_marca);
        mGrupoInstrumento.addTextLayout(R.string.estatus_prestamo_modelo);
        
        mGrupoPrestamo = (GroupLayout) mView.findViewById(R.id.grupo_datos_prestamo);
        mGrupoPrestamo.addTextLayout(R.string.estatus_prestamo_fecha_emision);
        mGrupoPrestamo.addTextLayout(R.string.estatus_prestamo_fecha_vencimiento);
        
        mGrupoEstatus = (GroupLayout) mView.findViewById(R.id.grupo_estatus_prestamo);
        mGrupoEstatus.addTextLayout(R.string.estatus_prestamo_estado);
        
        mRecordatorio = (TextView) mView.findViewById(R.id.recordatorio_prestamo);
        mostrarEstatus();
        return mView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.estatus_prestamo_titulo_barra);
        mSolicitudPrestamoTable = new SolicitudPrestamoTable(getActivity());
        mPrestamoTable = new PrestamoTable(getActivity());
    }
    
    private void mostrarEstatus() {
        mSolicitudPrestamo = mSolicitudPrestamoTable.searchSolicitudPrestamo();
        mPrestamo = mPrestamoTable.searchPrestamo();
        
        if (mPrestamo != null) {
            Log.i(TAG, "¡Tengo un prestamo!");
            mGrupoSolicitud.getTextLayoutAt(0).setContent(String.valueOf(mSolicitudPrestamo.getId()));
            mGrupoSolicitud.getTextLayoutAt(1).setContent(mSolicitudPrestamo.getTipoPrestamo().getDescripcion());
            
            mGrupoInstrumento.getTextLayoutAt(0).setContent(mPrestamo.getInstrumento().getTipoInstrumento().getDescripcion());
            mGrupoInstrumento.getTextLayoutAt(1).setContent(mPrestamo.getInstrumento().getSerial());
            mGrupoInstrumento.getTextLayoutAt(2).setContent(mPrestamo.getInstrumento().getModelo().getMarca().getDescripcion());
            mGrupoInstrumento.getTextLayoutAt(3).setContent(mPrestamo.getInstrumento().getModelo().getDescripcion());
            
            mGrupoPrestamo.getTextLayoutAt(0).setContent(mDateFormat.format(mPrestamo.getFechaEmision()));
            mGrupoPrestamo.getTextLayoutAt(1).setContent(mDateFormat.format(mPrestamo.getFechaVencimiento()));
            
            if (mSolicitudPrestamo.getEstatus().equals("finalizado")) {
                mToolbar.inflateMenu(R.menu.action_nuevo);
                mToolbar.setOnMenuItemClickListener(this);
                mGrupoEstatus.getTextLayoutAt(0).setContent(ESTATUS_FINALIZADO);
                mGrupoEstatus.getTextLayoutAt(0).setContentColor(getResources().getColor(R.color.gris_oscuro));
                mRecordatorio.setVisibility(View.VISIBLE);
            } else if (mSolicitudPrestamo.getEstatus().equals("entregado")) {
                mGrupoEstatus.getTextLayoutAt(0).setContent(ESTATUS_ENTREGADO);
                mGrupoEstatus.getTextLayoutAt(0).setContentColor(getResources().getColor(R.color.azul));
            } 
        } else {
            if (mSolicitudPrestamo != null) {
                if (mSolicitudPrestamo.getEstatus().equals("rechazado")) {
                    Log.i(TAG, "¡La solicitud fue rechazada!");
                    mGrupoSolicitud.getTextLayoutAt(0).setContent(String.valueOf(mSolicitudPrestamo.getId()));
                    mGrupoSolicitud.getTextLayoutAt(1).setContent(mSolicitudPrestamo.getTipoPrestamo().getDescripcion());

                    mGrupoInstrumento.getTextLayoutAt(0).setContent(mSolicitudPrestamo.getTipoInstrumento().getDescripcion());
                    mGrupoInstrumento.getTextLayoutAt(1).setContent(R.string.estatus_prestamo_rechazado);
                    mGrupoInstrumento.getTextLayoutAt(2).setContent(R.string.estatus_prestamo_rechazado);
                    mGrupoInstrumento.getTextLayoutAt(3).setContent(R.string.estatus_prestamo_rechazado);

                    mGrupoPrestamo.getTextLayoutAt(0).setContent(mDateFormat.format(mSolicitudPrestamo.getFechaEmision()));
                    mGrupoPrestamo.getTextLayoutAt(1).setContent(mDateFormat.format(mSolicitudPrestamo.getFechaVencimiento()));

                    mToolbar.inflateMenu(R.menu.action_nuevo);
                    mToolbar.setOnMenuItemClickListener(this);
                    mGrupoEstatus.getTextLayoutAt(0).setContent(ESTATUS_RECHAZADO);
                    mGrupoEstatus.getTextLayoutAt(0).setContentColor(getResources().getColor(R.color.rojo));

                } else {
                    Log.i(TAG, "¡La solicitud esta en proceso o aprobada!");
                    mGrupoSolicitud.getTextLayoutAt(0).setContent(String.valueOf(mSolicitudPrestamo.getId()));
                    mGrupoSolicitud.getTextLayoutAt(1).setContent(mSolicitudPrestamo.getTipoPrestamo().getDescripcion());

                    mGrupoInstrumento.getTextLayoutAt(0).setContent(mSolicitudPrestamo.getTipoInstrumento().getDescripcion());
                    mGrupoInstrumento.getTextLayoutAt(1).setContent(R.string.estatus_prestamo_por_asignar);
                    mGrupoInstrumento.getTextLayoutAt(2).setContent(R.string.estatus_prestamo_por_asignar);
                    mGrupoInstrumento.getTextLayoutAt(3).setContent(R.string.estatus_prestamo_por_asignar);

                    mGrupoPrestamo.getTextLayoutAt(0).setContent(mDateFormat.format(mSolicitudPrestamo.getFechaEmision()));
                    mGrupoPrestamo.getTextLayoutAt(1).setContent(mDateFormat.format(mSolicitudPrestamo.getFechaVencimiento()));

                    if (mSolicitudPrestamo.getEstatus().equals("en proceso")) {
                        mGrupoEstatus.getTextLayoutAt(0).setContent(ESTATUS_EN_PROCESO);
                        mGrupoEstatus.getTextLayoutAt(0).setContentColor(getResources().getColor(R.color.gris_oscuro));
                    } else if (mSolicitudPrestamo.getEstatus().equals("aprobado")) {
                        mGrupoEstatus.getTextLayoutAt(0).setContent(ESTATUS_APROBADO);
                        mGrupoEstatus.getTextLayoutAt(0).setContentColor(getResources().getColor(R.color.verde_bienvenido));
                    }
                }
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        mSolicitudPrestamoTable.destroyTable();
        mPrestamoTable.destroyTable();
        mSolicitudPrestamoTable.createTable();
        mPrestamoTable.createTable();
        mActivity.nuevaSolicitudPrestamo();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, SolicitudPrestamoFragment.newInstance(mActivity))
                .addToBackStack(null)
                .commit();
        return true;
    }
}
