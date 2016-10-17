package fr.miage.tp1_couvrat_khomany;

/**
 * Created by Gautier on 17/10/2016.
 */

public class EmptyFieldException extends Exception {
    public EmptyFieldException(String message) {
        super(message);
        message += "Le message " + message + "est vide" ;
    }
}