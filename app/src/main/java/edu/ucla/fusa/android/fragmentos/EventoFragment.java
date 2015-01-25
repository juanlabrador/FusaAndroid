package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joanzapata.pdfview.PDFView;
import com.juanlabrador.GroupLayout;

import java.text.SimpleDateFormat;

import edu.ucla.fusa.android.DB.EventoTable;
import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.modelo.evento.Evento;

/**
 * Created by juanlabrador on 24/01/15.
 */
public class EventoFragment extends Fragment {
    
    private static String TAG = "EventoFragment";
    private GroupLayout mGrupoEvento;
    private GroupLayout mGrupoLugar;
    private PDFView mRepertorio;
    private View mView;
    private EventoTable mEventoTable;
    private Evento mEvento;
    private static int mId;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat mTimeFormat = new SimpleDateFormat("HH:mm aa");
    
    public static EventoFragment newInstance(int id) {
        EventoFragment fragment = new EventoFragment();
        fragment.setRetainInstance(true);
        mId = id;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_evento, container, false);
        
        mGrupoEvento = (GroupLayout) mView.findViewById(R.id.grupo_datos_evento);
        mGrupoEvento.addTextLayout(R.string.evento_nombre);
        mGrupoEvento.addTextLayout(R.string.evento_fecha);
        mGrupoEvento.addTextLayout(R.string.evento_hora);
        
        mGrupoLugar = (GroupLayout) mView.findViewById(R.id.grupo_lugar_evento);
        mGrupoLugar.addTextLayout(R.string.evento_sitio);
        mGrupoLugar.addTextLayout(R.string.evento_direccion);
        
        mRepertorio = (PDFView) mView.findViewById(R.id.repertorio_pdf);

        cargarEvento();
        return mView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEventoTable = new EventoTable(getActivity());
        
    }

    private void cargarEvento() {
        mEvento = mEventoTable.searchEvento(mId);
        if (mEvento != null) {
            mGrupoEvento.getTextLayoutAt(0).setContent(mEvento.getNombre());
            mGrupoEvento.getTextLayoutAt(1).setContent(mDateFormat.format(mEvento.getFecha()));
            
            if (mEvento.getHora() != null) {
                mGrupoEvento.getTextLayoutAt(2).setContent(mTimeFormat.format(mEvento.getHora()));
            } else {
                mGrupoEvento.getTextLayoutAt(2).setContent(R.string.evento_sin_hora);
            }
            
            mGrupoLugar.getTextLayoutAt(0).setContent(mEvento.getLugar().getDescripcion());
            mGrupoLugar.getTextLayoutAt(1).setContent(mEvento.getLugar().getDireccion());
        }
        
    }
}
