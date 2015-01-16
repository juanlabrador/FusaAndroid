package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.adaptadores.ListInstrumentosAdapter;
import edu.ucla.fusa.android.modelo.herramientas.FloatingHintEditText;
import edu.ucla.fusa.android.modelo.herramientas.ItemListInstrumentos;
import edu.ucla.fusa.android.validadores.ValidadorCedularRif;
import edu.ucla.fusa.android.validadores.ValidadorEmails;

public class DonarInstrumentoFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private LinearLayout barraSuperior;
    private FloatingHintEditText email;
    private Button enviarContratacion;
    private FloatingHintEditText telefono;
    private FloatingHintEditText rifoCedula;
    private FloatingHintEditText nombre;
    private ListView listView;
    private View view;
    private String[] titulos;
    private String[] instrumentos;
    private ArrayList<ItemListInstrumentos> items = new ArrayList();
    private ListInstrumentosAdapter adapter;
    private CheckBox check;

    public static DonarInstrumentoFragment newInstance() {
        DonarInstrumentoFragment fragment = new DonarInstrumentoFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_inicial_donaciones, paramViewGroup, false);
        barraSuperior = ((LinearLayout) view.findViewById(R.id.view_barra_superior_donaciones));
        enviarContratacion = ((Button) view.findViewById(R.id.btn_enviar_solicitud_donaciones));
        enviarContratacion.setOnClickListener(this);
        rifoCedula = ((FloatingHintEditText) view.findViewById(R.id.et_cedula_donaciones));
        nombre = (FloatingHintEditText) view.findViewById(R.id.et_nombre_donaciones);
        telefono = (FloatingHintEditText) view.findViewById(R.id.et_telefono_donaciones);
        email = ((FloatingHintEditText) view.findViewById(R.id.et_email_donaciones));
        listView = (ListView) view.findViewById(R.id.list_view_instrumentos);

        //titulos = getResources().getStringArray(R.array.tipos_instrumentos);

        //instrumentos = getResources().getStringArray(R.array.instrumentos);
        items.clear();

        items.add(new ItemListInstrumentos(instrumentos[0], false));
        items.add(new ItemListInstrumentos(instrumentos[1], false));
        items.add(new ItemListInstrumentos(instrumentos[2], false));
        items.add(new ItemListInstrumentos(instrumentos[3], false));
        items.add(new ItemListInstrumentos(instrumentos[4], false));
        items.add(new ItemListInstrumentos(instrumentos[5], false));

        adapter = new ListInstrumentosAdapter(getActivity(), items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        if (getActivity().getActionBar().isShowing() != false) {
            getActivity().getActionBar().setTitle(R.string.donaciones_titulo_superior);
            getActivity().getActionBar().setIcon(R.drawable.ic_instrumentos);
            barraSuperior.setVisibility(View.GONE);
        } else {
            barraSuperior.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public void onClick(View paramView) {
        if (ValidadorCedularRif.validarCedulaRif(rifoCedula.getText().toString()) != true) {
            Toast.makeText(getActivity(), R.string.mensaje_cedula_rif_invalido, Toast.LENGTH_SHORT).show();
        } else if (ValidadorEmails.validarEmail(email.getText().toString()) != true) {
            Toast.makeText(getActivity(), R.string.mensaje_correo_invalido, Toast.LENGTH_SHORT).show();
        }
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
    }

    public void onResume() {
        super.onResume();
        if (getActivity().getActionBar().isShowing() != false) {
            getActivity().getActionBar().setTitle(R.string.donaciones_titulo_superior);
            getActivity().getActionBar().setIcon(R.drawable.ic_instrumentos_blanco);
            barraSuperior.setVisibility(View.GONE);
        } else {
            barraSuperior.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        check = (CheckBox) view.findViewById(R.id.cb_check_instrumento_donar);

        if (check.isChecked() == false) {
            check.setChecked(true);
        } else {
            check.setChecked(false);
        }
    }

}