package edu.ucla.fusa.android.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dd.CircularProgressButton;

import edu.ucla.fusa.android.R;
import edu.ucla.fusa.android.asynctask.EnviarEmailAsyncTaks;
import edu.ucla.fusa.android.modelo.herramientas.FloatingHintEditText;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ConfiguracionInformarProblemaFragment extends Fragment implements View.OnClickListener, TextWatcher {
    private static final String password = "teamenergy";
    private static final String username = "sistemafusa@gmail.com";
    private CircularProgressButton enviar;
    private FloatingHintEditText problema;
    private View view;

    private Message createMessage(String paramString1, String paramString2, String paramString3, Session paramSession) throws MessagingException, UnsupportedEncodingException {
        MimeMessage localMimeMessage = new MimeMessage(paramSession);
        localMimeMessage.setFrom(new InternetAddress(paramString1, "Anonimo"));
        localMimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(paramString1, paramString1));
        localMimeMessage.setSubject(paramString2);
        localMimeMessage.setText(paramString3);
        return localMimeMessage;
    }

    private Session createSessionObject() {
        Properties localProperties = new Properties();
        localProperties.put("mail.smtp.auth", "true");
        localProperties.put("mail.smtp.starttls.enable", "true");
        localProperties.put("mail.smtp.host", "smtp.gmail.com");
        localProperties.put("mail.smtp.port", "587");
        return Session.getInstance(localProperties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public static ConfiguracionInformarProblemaFragment newInstance() {
        ConfiguracionInformarProblemaFragment fragment = new ConfiguracionInformarProblemaFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    private void sendProblema(String paramString1, String paramString2, String paramString3) {
        Session localSession = createSessionObject();
        try {
            Message localMessage = createMessage(paramString1, paramString2, paramString3, localSession);
            new EnviarEmailAsyncTaks(enviar, problema).execute(new Message[] { localMessage });
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void afterTextChanged(Editable paramEditable) {}

    public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}

    public void onClick(View paramView) {
        sendProblema("sistemafusa+problemas@gmail.com", "Información de un problema", this.problema.getText().toString());
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getActivity().getActionBar().setIcon(R.drawable.ic_informacion_problema_white);
        getActivity().getActionBar().setTitle(R.string.configuracion_ayuda_problema);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
        view = paramLayoutInflater.inflate(R.layout.fragment_configuraciones_informar_problema, paramViewGroup, false);
        enviar = ((CircularProgressButton) view.findViewById(R.id.btn_informar_problema));
        enviar.setOnClickListener(this);
        problema = ((FloatingHintEditText) view.findViewById(R.id.et_informar_problema));
        problema.addTextChangedListener(this);
        return view;
    }

    public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
        enviar.setProgress(0);
    }
}
