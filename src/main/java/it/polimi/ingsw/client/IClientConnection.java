package it.polimi.ingsw.client;


import it.polimi.ingsw.view.View;

public interface IClientConnection {

     void setViewClient(View view);

     String login(String name);

}
