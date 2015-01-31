package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juanlabrador.grouplayout.GroupContainer;

import java.util.List;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.academico.Agrupacion;
import edu.ucla.fusa.android.modelo.academico.ClaseParticular;

/**
 * Created by juanlabrador on 24/11/14.
 */
public class HorarioClasesFragment extends Fragment {

    private static String TAG = "HorarioClasesFragment";
    private GroupContainer mGrupoAgrupacion;
    private GroupContainer mGrupoClases;
    private TextView mTextEmpty;
    private Toolbar mToolbar;
    private Agrupacion mAgrupacion;
    private List<ClaseParticular> mClases;
    private int mDia;
    private TextView mCabeceraAgrupacion;
    private TextView mCabeceraClases;
    private boolean hayAgrupacion = false;
    private boolean hayClases = false;

    public HorarioClasesFragment(Agrupacion mAgrupacion, List<ClaseParticular> mClases, int mDia) {
        this.mAgrupacion = mAgrupacion;
        this.mClases = mClases;
        this.mDia = mDia;
        this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_drawer_schedule, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGrupoAgrupacion = (GroupContainer) view.findViewById(R.id.grupo_agrupacion);
        mGrupoClases = (GroupContainer) view.findViewById(R.id.grupo_clase);
        mCabeceraAgrupacion = (TextView) view.findViewById(R.id.cabecera_agrupacion);
        mCabeceraClases = (TextView) view.findViewById(R.id.cabecera_clases);
        mTextEmpty = (TextView) view.findViewById(R.id.sin_clases);
        //prepareView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.mis_clases_action_bar);
    }


    private void prepareView() {
        int horaI;
        int horaF;
        mGrupoClases.clear();
        mGrupoAgrupacion.clear();
        Log.i(TAG, "¡Hay una agrupacion para el dia " + mDia);
        Log.i(TAG, "¡Tamaño del horario " + mAgrupacion.getHorarioArea().size());
        for (int j = 0; j < mAgrupacion.getHorarioArea().size(); j++) {
            Log.i(TAG, " " + mDia + "==" + mAgrupacion.getHorarioArea().get(j).getHorario().getDia().getDia_id());
            if (mDia == mAgrupacion.getHorarioArea().get(j).getHorario().getDia().getDia_id()) {
                mGrupoAgrupacion.addTextLayout(R.string.mis_clases_nombre_agrupacion, mAgrupacion.getDescripcion());
                horaI = Integer.parseInt(mAgrupacion.getHorarioArea().get(j).getHorario().getHoraInicio());
                horaF = Integer.parseInt(mAgrupacion.getHorarioArea().get(j + 1).getHorario().getHoraFin());
                mGrupoAgrupacion.addTextLayout(R.string.mis_clases_horario, horaI + ":00 hasta " + horaF + ":00");
                j++;
                mGrupoAgrupacion.addTextLayout(R.string.mis_clases_instructor, mAgrupacion.getInstructor().getNombre() + " " + mAgrupacion.getInstructor().getApellido());
                hayAgrupacion = true;
            } else {
                Log.i(TAG, "¡No hay una agrupacion para el dia " + mDia);
                mCabeceraAgrupacion.setVisibility(View.GONE);
                mGrupoAgrupacion.setVisibility(View.GONE);
            }
        }
        
        Log.i(TAG, "¡Hay una clase particular para el dia " + mDia);
        for (int k = 0; k < mClases.size(); k++) {
            for (int j = 0; j < mClases.get(k).getHorarioArea().size(); j++) {
                if (mDia == mClases.get(k).getHorarioArea().get(j).getHorario().getDia().getDia_id()) {
                    mGrupoClases.addTextLayout(R.string.mis_clases_catedra, mClases.get(0).getCatedra().getDescripcion());
                    horaI = Integer.parseInt(mClases.get(k).getHorarioArea().get(j).getHorario().getHoraInicio());
                    horaF = Integer.parseInt(mClases.get(k).getHorarioArea().get(j + 1).getHorario().getHoraFin());
                    mGrupoClases.addTextLayout(R.string.mis_clases_horario, horaI + ":00 hasta " + horaF + ":00");
                    j++;
                    mGrupoClases.addTextLayout(R.string.mis_clases_instructor, mClases.get(0).getInstructor().getNombre() + " " + mClases.get(0).getInstructor().getApellido());
                    hayClases = true;
                } else {
                    Log.i(TAG, "¡No hay una clase particular para el dia " + mDia);
                    mCabeceraClases.setVisibility(View.GONE);
                    mGrupoClases.setVisibility(View.GONE);
                }
            }
        }
        

        if (!hayAgrupacion) {
            if (!hayClases) {
                Log.i(TAG, "¡No hay actividad para el dia " + mDia);
                mTextEmpty.setVisibility(View.VISIBLE);
            }
        }
        /*for (int i = 0; i < 6; i++) {
            if (mAgrupacion != null){
                
                for (int j = 0; j < mAgrupacion.getHorarioArea().size(); j++) {
                    if (mDia == mAgrupacion.getHorarioArea().get(j).getHorario().getDia().getDia_id()) {
                        horaI = Integer.parseInt(mAgrupacion.getHorarioArea().get(j).getHorario().getHoraInicio());
                        horaF = Integer.parseInt(mAgrupacion.getHorarioArea().get(j + 1).getHorario().getHoraFin());
                        mGrupoAgrupacion.addTextLayout(R.string.mis_clases_horario, horaI + ":00 hasta " + horaF + ":00");
                        j++;
                    }
                }
                
            } else {
                
            }
            if (mClases != null){
                
                for (int k = 0; k < mClases.size(); k++) {
                    for (int j = 0; j < mClases.get(k).getHorarioArea().size(); j++) {
                        if (mDia == mClases.get(k).getHorarioArea().get(j).getHorario().getDia().getDia_id()) {
                            horaI = Integer.parseInt(mClases.get(k).getHorarioArea().get(j).getHorario().getHoraInicio());
                            horaF = Integer.parseInt(mClases.get(k).getHorarioArea().get(j + 1).getHorario().getHoraFin());
                            mGrupoAgrupacion.addTextLayout(R.string.mis_clases_horario, horaI + ":00 hasta " + horaF + ":00");
                            j++;
                        }
                    }
                }
                mGrupoAgrupacion.addTextLayout(R.string.mis_clases_instructor, mClases.get(0).getInstructor().getNombre() + " " + mClases.get(0).getInstructor().getApellido());
            } else {
                mContenedorClases.setVisibility(View.GONE);
            }
            
            if (mAgrupacion == null && mClases == null) {
                mTextEmpty.setVisibility(View.VISIBLE);
            }
        }*/
    }
}
