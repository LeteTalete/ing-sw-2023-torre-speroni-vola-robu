package it.polimi.ingsw.Exceptions;

public class InvalidChoiceFormatException extends Exception{

    public InvalidChoiceFormatException()
    {
        super("Invalid choice format");
    }

    @Override
    public String toString() {
        return getMessage();
    }


}
