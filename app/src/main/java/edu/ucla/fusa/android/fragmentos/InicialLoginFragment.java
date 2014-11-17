package edu.ucla.fusa.android.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.dd.CircularProgressButton;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.VistasPrincipalesActivity;
import edu.ucla.fusa.android.modelo.FloatingHintEditText;
import edu.ucla.fusa.android.validadores.ValidadorEmails;

public class InicialLoginFragment extends Fragment implements View.OnClickListener, TextWatcher {

    int contador = 0;
    private FloatingHintEditText email;
    private Handler handler = new Handler();
    private CircularProgressButton iniciarSesion;
    private FloatingHintEditText password;
    private TextView restaurarPassword;
    private View view;

    public static InicialLoginFragment newInstance() {
        InicialLoginFragment fragment = new InicialLoginFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public void afterTextChanged(Editable paramEditable) {}

    public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.btn_iniciar_sesion:
                this.email.setEnabled(false);
                this.password.setEnabled(false);
                this.restaurarPassword.setEnabled(false);
                if (ValidadorEmails.validarEmail(this.email.getText().toString()) != true) {
                    Toast.makeText(getActivity(), R.string.mensaje_correo_invalido, Toast.LENGTH_SHORT).show();
                    this.email.setEnabled(true);
                    this.password.setEnabled(true);
                    this.restaurarPassword.setEnabled(true);
                } else if ((this.email.getText().toString().equals("juan@example.com")) && (this.password.getText().toString().equals("1234"))) {
                    new Thread(new Runnable() {
                        public void run() {
                            while (contador < 100) {
                                contador = (1 + contador);
                                handler.post(new Runnable() {
                                    public void run() {
                                        iniciarSesion.setIndeterminateProgressMode(true);
                                        iniciarSesion.setProgress(InicialLoginFragment.this.contador);
                                    }
                                });
                                try {
                                    Thread.sleep(50L);
                                    if (contador == 100) {
                                        Thread.sleep(2000L);
                                        startActivity(new Intent(getActivity(), VistasPrincipalesActivity.class));
                                        getActivity().finish();
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                } else {
                    new Thread(new Runnable() {
                        public void run() {
                            while (contador < 80) {
                                contador = (1 + contador);
                                handler.post(new Runnable() {
                                    public void run() {
                                        iniciarSesion.setIndeterminateProgressMode(true);
                                        iniciarSesion.setProgress(InicialLoginFragment.this.contador);
                                        if (contador == 80) {
                                            email.setEnabled(true);
                                            password.setEnabled(true);
                                            restaurarPassword.setEnabled(true);
                                            iniciarSesion.setProgress(-1);
                                            Toast.makeText(getActivity(), R.string.mensaje_error_iniciar_sesion, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                try {
                                    Thread.sleep(50L);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }
                break;
            case R.id.tv_olvidar_password_iniciar_sesion:
                restaurarPassword.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
                getFragmentManager().beginTransaction()
                        .replace(android.R.id.content, InicialRestaurarPasswordFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_inicial_login, paramViewGroup, false);
        email = ((FloatingHintEditText) view.findViewById(R.id.et_email_iniciar_sesion));
        password = ((FloatingHintEditText) view.findViewById(R.id.et_password_iniciar_sesion));
        iniciarSesion = ((CircularProgressButton) view.findViewById(R.id.btn_iniciar_sesion));
        restaurarPassword = ((TextView) view.findViewById(R.id.tv_olvidar_password_iniciar_sesion));
        restaurarPassword.setOnClickListener(this);
        iniciarSesion.setOnClickListener(this);
        email.addTextChangedListener(this);
        password.addTextChangedListener(this);
        return view;
    }

    public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
        iniciarSesion.setProgress(0);
    }
}
