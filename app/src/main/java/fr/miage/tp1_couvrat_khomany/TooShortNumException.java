package fr.miage.tp1_couvrat_khomany;

/**
 * Created by Gautier on 17/10/2016.
 */

public class TooShortNumException extends Exception {
    public TooShortNumException(String message) {
        super("Le numéro " + message + "est trop court" );
    }
}
