package edu.ucla.fusa.android.fragmentos;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.juanlabrador.GroupLayout;

import edu.ucla.fusa.android.R;

public class DatosFundacionFragment extends Fragment implements View.OnClickListener {

    public static String TAG = "DatosFundacion";
    private View mView;
    private GroupLayout mGrupoDireccion;
    private GroupLayout mGrupoMedios;

    public static DatosFundacionFragment newInstance() {
        DatosFundacionFragment fragment = new DatosFundacionFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle arguments) {
        super.onCreateView(inflater, container, arguments);
        mView = inflater.inflate(R.layout.fragment_inicial_contacto_fundacion_datos, container, false);

        mGrupoDireccion = (GroupLayout) mView.findViewById(R.id.direccion_contacto);
        mGrupoDireccion.addTextLayout(R.string.contacto_direccion, R.string.contacto_direccion_fundacion);
        
        mGrupoMedios = (GroupLayout) mView.findViewById(R.id.medios_contacto);
        mGrupoMedios.addOneButtonLayout(R.string.contacto_telefono, R.string.contacto_telefono1, R.drawable.ic_celular);
        mGrupoMedios.addOneButtonLayout(R.string.contacto_telefono, R.string.contacto_telefono2, R.drawable.ic_celular);
        mGrupoMedios.addOneButtonLayout(R.string.contacto_correo, R.string.contacto_correo_fundacion, R.drawable.ic_correo);
        mGrupoMedios.getOneButtonLayoutAt(0).getButton().setOnClickListener(this);
        mGrupoMedios.getOneButtonLayoutAt(1).getButton().setOnClickListener(this);
        mGrupoMedios.getOneButtonLayoutAt(2).getButton().setOnClickListener(this);
        
        return mView;
    }

    public void onClick(View view) {
        if (view == mGrupoMedios.getOneButtonLayoutAt(0).getButton()) {
            try {
                startActivity(new Intent("android.intent.action.CALL",
                        Uri.parse("tel:" + getResources().getString(R.string.contacto_telefono1))));
            } catch (Exception e) {
                Toast.makeText(getActivity(), R.string.error_llamada, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if (view == mGrupoMedios.getOneButtonLayoutAt(1).getButton()) {
            try {
                startActivity(new Intent("android.intent.action.CALL",
                        Uri.parse("tel:" + getResources().getString(R.string.contacto_telefono2))));
            } catch (Exception e) {
                Toast.makeText(getActivity(), R.string.error_llamada, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if (view == mGrupoMedios.getOneButtonLayoutAt(2).getButton()) {
            try {
                Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
                String[] emails = new String[]{getResources().getString(R.string.contacto_correo_fundacion)};
                startActivity(Intent.createChooser(
                        intent.putExtra(Intent.EXTRA_EMAIL, emails)
                                .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contacto_correo)).setType("message/rfc822"),
                        getResources().getString(R.string.mensaje_elegir_cliente_correo)));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(), R.string.error_enviar_correo, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}