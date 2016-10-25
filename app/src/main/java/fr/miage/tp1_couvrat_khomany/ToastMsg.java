package fr.miage.tp1_couvrat_khomany;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Anice on 20/10/16.
 */

public class ToastMsg extends Toast {
    public static  int duration = android.widget.Toast.LENGTH_SHORT ;
    public Context ctxt ;
    //constructor
    public ToastMsg(Context context){
        super(context);
        ctxt = context;
    }


    public void show(CharSequence msg){
        android.widget.Toast toast = Toast.makeText(ctxt,msg,duration);

        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }
}
