package it.polimi.ingsw.client;


import it.polimi.ingsw.view.View;

public interface IClientConnection {
     void setName(String name);
     void setViewClient(View view);
     void chooseTiles(String username, String tilesChosen);

     void login(String name);

     void setUserToken(String token);

     void setReceivedResponse(boolean b);

     void numberOfPlayers(String name, String token, int number);

    void chooseColumn(int column);

    void setSynCheckTimer(boolean b);

    void close();

    void setResponseDecoder(ResponseDecoder responseDecoder);

    boolean isConnected();
}
