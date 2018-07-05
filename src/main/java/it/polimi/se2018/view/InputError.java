package it.polimi.se2018.view;

/**
 * An exception that indicates that the user input was wrong for some reasons. In the message there is the error message.
 */
public class InputError  extends  RuntimeException{
 public InputError(String message){
     super(message);
 }
}
