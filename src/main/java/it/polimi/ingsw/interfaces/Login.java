package it.polimi.ingsw.interfaces;


import it.polimi.ingsw.client.ClientHandler;

public interface Login {
    //this returns a boolean because the server will notify if the name chosen already exist and therefore
    //is not valid. if it returns true then the name is valid
    boolean registerUser(String name, ClientHandler clientH);
}
