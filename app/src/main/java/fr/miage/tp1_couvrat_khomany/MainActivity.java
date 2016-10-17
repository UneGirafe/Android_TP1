package fr.miage.tp1_couvrat_khomany;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

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

        //Test message is not empty
        if (msg.isEmpty()) {
            throw new EmptyFieldException(msg.toString());

        } else {
            while (num.hasMoreElements()) {
                try {
                    if (num.nextToken().isEmpty()) {
                        throw new EmptyFieldException(num.toString());
                    } else if (num.nextToken().length() < 10) {
                        throw new TooShortNumException(num.toString());
                    } else {
                        SmsManager.getDefault().sendTextMessage(num.nextToken(), null, msg, null, null);

                        Context context = getApplicationContext();
                        CharSequence text = "Message envoyé !";
                        int duration = Toast.LENGTH_SHORT;
                        //Show a success toaster
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    }

                } catch (Exception e) {
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
