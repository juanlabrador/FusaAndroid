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

/**
 * Created by juanlabrador on 26/10/14.
 *
 * Clase que contiene toda la información referente a fundamusical como datos de contacto
 */
public class InicialViewPagerContactoInformacionDatosFragment extends Fragment implements View.OnClickListener {

    private ImageView telefono1;
    private ImageView telefono2;
    private ImageView correo;
    private View view;

    public static InicialViewPagerContactoInformacionDatosFragment newInstance() {
        InicialViewPagerContactoInformacionDatosFragment activity = new InicialViewPagerContactoInformacionDatosFragment();
        activity.setRetainInstance(true);
        return activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_viewpager_informacion_contacto_datos, container, false);

        telefono1 = (ImageView) view.findViewById(R.id.btnTelefono1);
        telefono2 = (ImageView) view.findViewById(R.id.btnTelefono2);
        correo = (ImageView) view.findViewById(R.id.btnEnviarCorreo);

        telefono1.setOnClickListener(this);
        telefono2.setOnClickListener(this);
        correo.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTelefono1:
                try {
                    /** Realizamos la llamada */
                    startActivity(new Intent(Intent.ACTION_CALL,
                            Uri.parse("tel:" + getResources().getString(R.string.texto_contenido_telefono1_contacto))));
                } catch (Exception e) {
                    Toast.makeText(getActivity(), R.string.error_llamada, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
            case R.id.btnTelefono2:
                try {
                    /** Realizamos la llamada */
                    startActivity(new Intent(Intent.ACTION_CALL,
                            Uri.parse("tel:" + getResources().getString(R.string.texto_contenido_telefono2_contacto))));
                } catch (Exception e) {
                    Toast.makeText(getActivity(), R.string.error_llamada, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
            case R.id.btnEnviarCorreo:
                try {
                    /** Cargamos un cliente de correo instalado, o seleccionamos alguno */
                    startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"))
                            .putExtra(Intent.EXTRA_EMAIL, /** Correo destinatario */
                                    new String[]{getResources().getString(R.string.texto_contenido_email_contacto)})
                                    /** Asunto del mensaje */
                                    .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.titulo_correo_contacto))
                                    .setType("message/rfc822"),
                            getResources().getString(R.string.mensaje_elegir_cliente_correo)));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), R.string.error_enviar_correo, Toast.LENGTH_SHORT).show();
                }
        }
    }
}
