package fr.miage.tp1_couvrat_khomany;

import android.app.DownloadManager;
import android.content.ContentProvider;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.opengl.ETC1;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.VectorEnabledTintResources;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.NumberPicker;

import com.android.ex.chips.BaseRecipientAdapter;
import com.android.ex.chips.RecipientEditTextView;
import com.android.ex.chips.recipientchip.DrawableRecipientChip;

import java.util.StringTokenizer;


public class MainActivity extends AppCompatActivity {

    // Variable de tag permettant d'ajouter des messages au Log
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PICK_CONTACT_REQUEST = 1;  // Identifiant de la requête de selection des contacts
    private static final int SPAM_DEFAULT = 0;  //nombre par défaut de sms envoyés
    private static final int SPAM_MAX = 30;
    private ToastMsg toaster;
    private NumberPicker howMany;
    private RecipientEditTextView phoneRetv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toaster = new ToastMsg(getApplicationContext());
        howMany = (NumberPicker) findViewById(R.id.how_many);
        howMany.setValue(SPAM_DEFAULT);
        howMany.setMaxValue(SPAM_MAX);
        howMany.setMinValue(SPAM_DEFAULT);

        phoneRetv = (RecipientEditTextView) findViewById(R.id.chipsContact);
        phoneRetv.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        BaseRecipientAdapter adapter = new BaseRecipientAdapter(BaseRecipientAdapter.QUERY_TYPE_PHONE, this);
        adapter.setShowMobileOnly(true);
        phoneRetv.setAdapter(adapter);
        phoneRetv.dismissDropDownOnItemSelected(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DrawableRecipientChip[] chips = phoneRetv.getSortedRecipients();
                for (DrawableRecipientChip chip : chips) {
                    Log.v("DrawableChip", chip.getEntry().getDisplayName() + " " + chip.getEntry().getDestination());
                }
            }
        }, 5000);

        final ImageButton showAll = (ImageButton) findViewById(R.id.show_all);
        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneRetv.showAllContacts();
            }
        });
    }

    // Récupère le contenu du message
    protected String getMessage() {
        EditText text = (EditText) findViewById(R.id.editMessage);

        return text.getText().toString().trim();
    }

    protected StringTokenizer getNumbers(){
        EditText editText = (EditText) findViewById(R.id.chipsContact);
        String st = editText.getText().toString().trim();

        return new StringTokenizer(st,",");
    }

    // Permet d'envoyer un message à un ou plusieurs numéros
    public void sendSMS(View view) throws EmptyFieldException, TooShortNumException {

        String msg = getMessage();
        //DrawableRecipientChip[] chips = phoneRetv.getRecipients();
        //Log.d(TAG,"nombre de chips " + chips.length );
        StringTokenizer contacts = getNumbers();
        Log.d(TAG, " NB TOKENS : " + contacts.countTokens());

        //aucun contact sélectionné
        if (contacts.countTokens() == 0 ){
            toaster.show("Veuillez séléctionner un contact");
        }
        // Si aucun message n'a été renseigné
        else if (msg.isEmpty()) {
            toaster.show("Veuillez entrer un message");
            // S'il y a au moins un numéro et un message
            Log.d(TAG, "msg est vide ? --> " + msg.isEmpty());
            Log.d(TAG, "message saisi --> " + msg);
        } else {
            Log.d(TAG, "Contact et Message existant");
            Log.d(TAG, "msg --> " + msg);

            //pour chaque contact sélectionné
        while (contacts.hasMoreTokens()) {
            String curNum = contacts.nextToken();

            Log.d(TAG,"Numéro de téléphone à traiter " + curNum);

                try {
                    //numéro trop court
                    if (curNum.length() < 4) {
                        Log.d(TAG, "chip.getValue.lenght() --> " + curNum.length());
                        throw new TooShortNumException(curNum);
                    }
                    else {
                        Log.d(TAG, "Numéro courant --> " + curNum);

                        for ( int i = 0 ; i < howMany.getValue(); i++) {
                            SmsManager.getDefault().sendTextMessage(curNum, null, msg, null, null);
                            Log.d(TAG, "SMS envoyé");
                        }
                    }

                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                    e.printStackTrace();
                    toaster.show(e.getMessage());
                }
            }
            toaster.show( "Messages envoyés !");
            ((EditText) findViewById(R.id.editMessage)).getText().clear();
            Log.d(TAG, "Message effacé");
        }
    }

}
