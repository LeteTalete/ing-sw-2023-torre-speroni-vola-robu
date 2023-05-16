package it.polimi.ingsw.client;


import it.polimi.ingsw.view.View;

public interface IClientConnection {
     void setName(String name);
     void setViewClient(View view);

     String login(String name);

}
