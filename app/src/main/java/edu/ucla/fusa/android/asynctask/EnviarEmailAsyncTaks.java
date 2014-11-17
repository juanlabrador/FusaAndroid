package edu.ucla.fusa.android.asynctask;

import android.os.AsyncTask;
import android.widget.EditText;
import com.dd.CircularProgressButton;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

public class EnviarEmailAsyncTaks extends AsyncTask<Message, Void, Integer> {

    private CircularProgressButton circularProgressButton;
    private int contador = 0;
    private EditText editText;

    public EnviarEmailAsyncTaks(CircularProgressButton paramCircularProgressButton, EditText paramEditText) {
        this.circularProgressButton = paramCircularProgressButton;
        this.editText = paramEditText;
    }

    protected Integer doInBackground(Message[] message) {
        try {
            Transport.send(message[0]);
            return 100;
        } catch (MessagingException e) {
            e.printStackTrace();
            return -1;
        }
    }

    protected void onPostExecute(Integer paramInteger) {
        this.circularProgressButton.setProgress(paramInteger.intValue());
        if (paramInteger.intValue() == 100) {
            this.editText.setText("");
        }
    }

    protected void onPreExecute() {
        super.onPreExecute();
        while (contador < 100) {
            this.contador = (1 + this.contador);
            this.circularProgressButton.setIndeterminateProgressMode(true);
            this.circularProgressButton.setProgress(this.contador);
        }
    }
}
