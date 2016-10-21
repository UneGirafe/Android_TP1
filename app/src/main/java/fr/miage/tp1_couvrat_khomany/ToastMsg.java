package fr.miage.tp1_couvrat_khomany;

import android.content.Context;
import android.view.Gravity;

/**
 * Created by Anice on 20/10/16.
 */

public class ToastMsg {
    private Context ctxt ;
    public static  int duration = android.widget.Toast.LENGTH_SHORT ;

    public ToastMsg(Context context){
        ctxt = context;
    }

    public void show(CharSequence message){
        //Show a success toaster
<<<<<<< HEAD
        android.widget.Toast toast = android.widget.Toast.makeText(ctxt, message, duration);
=======
        android.widget.Toast toast = android.widget.Toast.makeText(context, msg, duration);
>>>>>>> 46d5192425111f2d15ad8051b03cce827bce9776
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }
}
