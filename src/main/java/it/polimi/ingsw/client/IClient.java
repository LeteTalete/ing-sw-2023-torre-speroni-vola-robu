package it.polimi.ingsw.client;


import it.polimi.ingsw.view.View;

public interface IClient {

     void setViewClient(View view);

     String login(String name);

}
