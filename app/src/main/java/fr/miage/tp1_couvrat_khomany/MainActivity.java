package fr.miage.tp1_couvrat_khomany;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    // Variable de tag permettant d'ajouter des messages au Log
    private static final String TAG = MainActivity.class.getSimpleName();
    static final int PICK_CONTACT_REQUEST = 1;  // Identifiant de la requête de selection des contacts
    private static final int SPAM_DEFAULT = 1;  //nombre par défaut de sms envoyés
    private static final int SPAM_MAX = 30;
    private ToastMsg toaster;
    private NumberPicker howMany;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toaster = new ToastMsg(getApplicationContext());
        howMany = (NumberPicker) findViewById(R.id.how_many);
        howMany.setValue(SPAM_DEFAULT);
        howMany.setMaxValue(SPAM_MAX);
    }

    // Récupère la liste des numéros de téléphone séparés par une virgule
    protected StringTokenizer getNumbers() {
        EditText text = (EditText) findViewById(R.id.editPhoneNum);
        String numbers = text.getText().toString();
        StringTokenizer st = new StringTokenizer(numbers, ",");

        return st;
    }

    // Récupère le contenu du message
    protected String getMessage() {
        EditText text = (EditText) findViewById(R.id.editMessage);

        return text.getText().toString();
    }

    // Permet d'envoyer un message à un ou plusieurs numéros
    public void sendSMS(View view) throws EmptyFieldException, TooShortNumException {
        String msg = getMessage();
        StringTokenizer num = getNumbers();
        System.out.println("Appuie sur le bouton Envoyer");

        Log.d(TAG, "nombre de tokens --> " + num.countTokens());
        Log.d(TAG, "msg est vide ? --> " + msg.isEmpty());
        Log.d(TAG, "message saisi --> " + msg);

        // Si aucun numéro de téléphone n'a été entré
        if (num.countTokens() == 0) {
            toaster.show("Veuillez entrer un numéro de téléphone");
            System.out.println("Aucun numéro de téléphone renseigné");
        }
        // Si aucun message n'a été renseigné
        else if (msg.isEmpty()) {
            toaster.show("Veuillez entrer un message");

            // S'il y a au moins un numéro et un message
        } else {
            Log.d(TAG, "Message existant");
            System.out.println("Numéro de téléphone et message renseignés");

            //tant qu'il y a un numéro à traiter
            while (num.hasMoreElements()) {

                //récupère le token courant
                String curNum = num.nextToken();
                Log.d(TAG, "WHILE -- Un numéro de téléphone à traiter --> " + curNum);

                try {
                    Log.d(TAG, "num.nextToken().isEmpty() --> " + curNum.isEmpty());
                    Log.d(TAG, "num.nextToken().length() --> " + curNum.length());

                    if (curNum.length() < 4) {
                        Log.d(TAG, "num.nextToken().length() --> " + curNum.length());
                        throw new TooShortNumException(curNum);

                    } else {
                        Log.d(TAG, "num.nextToken() --> " + curNum);
                        Log.d(TAG, "msg --> " + msg);

                        int i;
                        for ( i = 1; i<howMany.getValue(); i++) {

                            SmsManager.getDefault().sendTextMessage(curNum, null, msg, null, null);
                            Log.d(TAG, " SMS envoyé");
                        }
                            toaster.show(i + "Messages envoyés !");
                    }

                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                    e.printStackTrace();
                    toaster.show(e.getMessage());
                }

                ((EditText) findViewById(R.id.editMessage)).getText().clear();
                Log.d(TAG, "Message effacé");
            }
        }
    }


    public void pickContact(View view) {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_CONTACT_REQUEST:
                    Cursor cursor = null;
                    String newNumber = "";
                    String newName = "";

                    try {
                        Uri contactUri = data.getData();
                        String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER
                                , ContactsContract.Contacts.DISPLAY_NAME
                        };
                        cursor = getContentResolver().query(contactUri, projection, null, null, null);

                        int phoneIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        int nameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

                        // let's just get the first number
                        if (cursor.moveToFirst()) {
                            newNumber = cursor.getString(phoneIdx);
                            newName = cursor.getString(nameIdx);
                            Log.v(TAG, "Got number: " + newNumber);
                            Log.v(TAG, "Got name: " + newName);

                        } else {
                            Log.w(TAG, "No results");
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "Failed to get phone data", e);

                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }

                        EditText editNumbers = (EditText) findViewById(R.id.editPhoneNum);

                        if (new StringTokenizer(editNumbers.getText().toString(), ",").hasMoreTokens()) {
                            editNumbers.append("," + newNumber);
                        } else {
                            editNumbers.setText(newNumber);
                        }
                    }

                    break;
            }

        } else {
            Log.w(TAG, "Warning: activity result not ok");
        }
    }
}
