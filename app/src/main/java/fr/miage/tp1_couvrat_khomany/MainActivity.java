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

    // Récupère la liste des numéros de téléphone séparés par une virgule
    protected StringTokenizer getNumbers(){
        EditText text = (EditText)findViewById(R.id.editPhoneNum);
        String numbers = text.getText().toString();
        StringTokenizer st = new StringTokenizer(numbers, ",");

        return st;
    }

    // Récupère le contenu du message
    protected String getMessage(){
        EditText text = (EditText)findViewById(R.id.editMessage);

        return  text.getText().toString();
    }

    /*=========================================
                 ENVOI DU MESSAGE
    ===========================================*/

    // Permet d'envoyer un message à un ou plusieurs numéros
    public void sendSMS(View view) throws EmptyFieldException, TooShortNumException {
        String msg = getMessage();
        StringTokenizer num = getNumbers();
        System.out.println("Appuie sur le bouton Envoyer");

        // Si aucun numéro de téléphone n'a été entré
        if(num.countTokens() == 0){
            System.out.println("Aucun numéro de téléphone renseigné");
            android.widget.Toast.makeText(getApplicationContext(), "Veuillez entrer un numéro de téléphone", android.widget.Toast.LENGTH_SHORT).show();
        }
        // Si aucun message n'a été renseigné
        else if (msg.isEmpty()) {
            System.out.println("Champ message vide");
            android.widget.Toast.makeText(getApplicationContext(), "Veuillez entrer un message", android.widget.Toast.LENGTH_SHORT).show();
            throw new EmptyFieldException(getApplicationContext());

        // S'il y a au moins un numéro et un message
        } else {
            System.out.println("Numéro de téléphone et message renseignés");
            while (num.hasMoreElements()) {

                String number = num.nextToken();
                try {
                    if (number.length() < 4) {
                        android.widget.Toast.makeText(getApplicationContext(), "Le numéro de téléphone est trop court", android.widget.Toast.LENGTH_SHORT).show();
                        throw new TooShortNumException(number.toString());
                    } else {
                        SmsManager.getDefault().sendTextMessage(number, null, msg, null, null);

                        System.out.println("Message envoyé");
                        android.widget.Toast.makeText(getApplicationContext(), "Message envoyé !", android.widget.Toast.LENGTH_SHORT).show();

                        //new ToastMsg(getApplicationContext(), "Message envoyé !");
                    }

                } catch (Exception e) {
                    System.out.println("Exception prise en compte");
                    e.printStackTrace();

                    new ToastMsg(getApplicationContext(), e.getMessage());
                }
            }

            ((EditText) findViewById(R.id.editMessage)).getText().clear();
            System.out.println("Message effacé");
        }
    }
}
