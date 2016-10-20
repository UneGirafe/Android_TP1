package fr.miage.tp1_couvrat_khomany;

import android.content.Context;

/**
 * Created by Gautier on 17/10/2016.
 */

public class EmptyFieldException extends Exception {
    public EmptyFieldException(Context ctxt) {
        super();

        android.widget.Toast.makeText(ctxt, "EmptyFieldException !", android.widget.Toast.LENGTH_SHORT).show();
    }
}