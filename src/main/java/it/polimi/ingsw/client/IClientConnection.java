package it.polimi.ingsw.client;


import it.polimi.ingsw.view.View;

public interface IClientConnection {
     void setName(String name);
     void setViewClient(View view);
     void chooseTiles(String username, String tilesChosen);

     void login(String name);

     void setUserToken(String token);
}
