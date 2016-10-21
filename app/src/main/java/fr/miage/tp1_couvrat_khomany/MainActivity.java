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
    private static ToastMsg toaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toaster = new ToastMsg(getApplicationContext());
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

        Log.d(TAG,"nombre de tokens --> " + num.countTokens() );
        Log.d(TAG,"msg est vide ? --> " + msg.isEmpty());
        Log.d(TAG,"message saisi --> " + msg.toString());

        // Si aucun numéro de téléphone n'a été entré
        if(num.countTokens() == 0){

            toaster.show("Veuillez entrer un numéro de téléphone");
            //android.widget.Toast.makeText(getApplicationContext(), "Veuillez entrer un numéro de téléphone", android.widget.Toast.LENGTH_SHORT).show();
        }
        //Test message is empty
        else if (msg.isEmpty()) {

            toaster.show("Veuillez entrer un message");
            //android.widget.Toast.makeText(getApplicationContext(), "Veuillez entrer un message", android.widget.Toast.LENGTH_SHORT).show();
            //throw new EmptyFieldException(getApplicationContext());

        } else {
            Log.d(TAG, "Message existant");

            //tant qu'il y a un numéro à traiter
            while (num.hasMoreElements()) {

                //récupère le token courant
                String curNum = num.nextToken();
                Log.d(TAG, "WHILE -- Un numéro de téléphone à traiter --> " + curNum);

                try {
                    Log.d(TAG,"num.nextToken().isEmpty() --> "+ curNum.isEmpty());
                    Log.d(TAG,"num.nextToken().length() --> "+ curNum.length());

                    if (curNum.isEmpty()) {
                        Log.d(TAG,"Token vide");

                        toaster.show("Veuillez entrer un numéro de téléphone");
                        //android.widget.Toast.makeText(getApplicationContext(), "Veuillez entrer un numéro de téléphone", android.widget.Toast.LENGTH_SHORT).show();
                        throw new EmptyFieldException(getApplicationContext());

                    } else if (curNum.length() < 4) {
                        Log.d(TAG,"num.nextToken().length() --> "+ curNum.length());

                        //toaster.show("Le numéro de téléphone est trop court");
                        //android.widget.Toast.makeText(getApplicationContext(), "Le numéro de téléphone est trop court", android.widget.Toast.LENGTH_SHORT).show();
                        throw new TooShortNumException(curNum.toString());

                    } else {
                        Log.d(TAG,"num.nextToken() --> "+ curNum);
                        Log.d(TAG,"msg --> "+ msg);

                        SmsManager.getDefault().sendTextMessage(curNum, null, msg, null, null);

                        Log.d(TAG, "SMS envoyé");
                        //android.widget.Toast.makeText(getApplicationContext(), "Message envoyé !", android.widget.Toast.LENGTH_SHORT).show();

                        toaster.show("Message envoyé !");
                    }

                } catch (Exception e) {
                    Log.d(TAG, e.toString());

                    toaster.show(e.getMessage());
                    /*Context context = getApplicationContext();
                    CharSequence text = e.getMessage();
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();*/
                }
            }
            ((EditText) findViewById(R.id.editMessage)).getText().clear();
        }
    }
}