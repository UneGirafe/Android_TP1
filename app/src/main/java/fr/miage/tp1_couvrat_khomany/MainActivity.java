package fr.miage.tp1_couvrat_khomany;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    // Variable de tag permettant d'ajouter des messages au Log
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected StringTokenizer getNumbers(){
        EditText text = (EditText)findViewById(R.id.editPhoneNum);
        String numbers = text.getText().toString();
        StringTokenizer st = new StringTokenizer(numbers, ",");

        return st;
    }

    protected String getMessage(){
        EditText text = (EditText)findViewById(R.id.editMessage);

        return  text.getText().toString();
    }


    public void sendSMS(View view) throws EmptyFieldException, TooShortNumException {
        String msg = getMessage();
        StringTokenizer num = getNumbers();

        // Si aucun numéro de téléphone n'a été entré
        if(num.countTokens() == 0){
            android.widget.Toast.makeText(getApplicationContext(), "Veuillez entrer un numéro de téléphone", android.widget.Toast.LENGTH_SHORT).show();
        }
        //Test message is empty
        else if (msg.isEmpty()) {
            android.widget.Toast.makeText(getApplicationContext(), "Veuillez entrer un message", android.widget.Toast.LENGTH_SHORT).show();
            throw new EmptyFieldException(getApplicationContext());

        } else {
            android.widget.Toast.makeText(getApplicationContext(), "Message existant", android.widget.Toast.LENGTH_SHORT).show();
            while (num.hasMoreElements()) {
                android.widget.Toast.makeText(getApplicationContext(), "Un numéro de téléphone à traiter", android.widget.Toast.LENGTH_SHORT).show();
                try {
                    if (num.nextToken().isEmpty()) {
                        android.widget.Toast.makeText(getApplicationContext(), "Veuillez entrer un numéro de téléphone", android.widget.Toast.LENGTH_SHORT).show();
                        throw new EmptyFieldException(getApplicationContext());
                    } else if (num.nextToken().length() < 10) {
                        android.widget.Toast.makeText(getApplicationContext(), "Le numéro de téléphone est trop court", android.widget.Toast.LENGTH_SHORT).show();
                        throw new TooShortNumException(num.toString());
                    } else {
                        SmsManager.getDefault().sendTextMessage(num.nextToken(), null, msg, null, null);

                        Log.d(TAG, "SMS envoyé");
                        android.widget.Toast.makeText(getApplicationContext(), "Message envoyé !", android.widget.Toast.LENGTH_SHORT).show();

                        new ToastMsg(getApplicationContext(), "Message envoyé !");
                    }

                } catch (Exception e) {
                    android.widget.Toast.makeText(getApplicationContext(), "Exception prise en compte : " + e.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                    Context context = getApplicationContext();
                    CharSequence text = e.getMessage();
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            }
            ((EditText) findViewById(R.id.editMessage)).getText().clear();
        }
    }
}
