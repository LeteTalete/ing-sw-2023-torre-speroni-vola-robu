package it.polimi.ingsw.client;

public interface ConnectionHandler {
    //this is to establish a connection
     String setupConnection();

     String Login(String name);

     String howMany(int num, String name);
}
