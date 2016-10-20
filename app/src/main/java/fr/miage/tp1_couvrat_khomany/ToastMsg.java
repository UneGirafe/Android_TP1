package fr.miage.tp1_couvrat_khomany;

import android.content.Context;
import android.view.Gravity;

/**
 * Created by Anice on 20/10/16.
 */

public class ToastMsg {
    public ToastMsg(Context context, CharSequence msg){

        int duration = android.widget.Toast.LENGTH_SHORT;
        //Show a success toaster
        android.widget.Toast toast = android.widget.Toast.makeText(context, "test", duration);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }
}
