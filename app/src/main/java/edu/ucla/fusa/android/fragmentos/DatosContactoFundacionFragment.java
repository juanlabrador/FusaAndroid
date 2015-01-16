package edu.ucla.fusa.android.fragmentos;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import edu.ucla.fusa.android.R;

public class DatosContactoFundacionFragment extends Fragment implements View.OnClickListener {

    public static String TAG = "DatosFundacion";
    private ImageView correo;
    private ImageView telefono1;
    private ImageView telefono2;
    private View view;

    public static DatosContactoFundacionFragment newInstance() {
        DatosContactoFundacionFragment fragment = new DatosContactoFundacionFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.btn_telefono_1:
                try {
                    startActivity(new Intent("android.intent.action.CALL",
                            Uri.parse("tel:" + getResources().getString(R.string.texto_contenido_telefono1_contacto))));
                } catch (Exception e) {
                    Toast.makeText(getActivity(), R.string.error_llamada, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
            case R.id.btn_telefono_2:
                try {
                    startActivity(new Intent("android.intent.action.CALL",
                            Uri.parse("tel:" + getResources().getString(R.string.texto_contenido_telefono2_contacto))));
                } catch (Exception e) {
                    Toast.makeText(getActivity(), R.string.error_llamada, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
            case R.id.btn_email:
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
                    String[] emails = new String[] { getResources().getString(R.string.texto_contenido_email_contacto) };
                    startActivity(Intent.createChooser(
                            intent.putExtra(Intent.EXTRA_EMAIL, emails)
                                  .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.titulo_correo_contacto)).setType("message/rfc822"),
                                                getResources().getString(R.string.mensaje_elegir_cliente_correo)));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), R.string.error_enviar_correo, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
        }
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_inicial_contacto_fundacion_datos, paramViewGroup, false);
        telefono1 = ((ImageView) view.findViewById(R.id.btn_telefono_1));
        telefono2 = ((ImageView) view.findViewById(R.id.btn_telefono_2));
        correo = ((ImageView) view.findViewById(R.id.btn_email));
        telefono1.setOnClickListener(this);
        telefono2.setOnClickListener(this);
        correo.setOnClickListener(this);
        return view;
    }
}