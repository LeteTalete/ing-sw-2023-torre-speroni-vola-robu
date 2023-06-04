package it.polimi.ingsw.client;


import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.List;

public interface IClientConnection {
     void setName(String name);
     void setViewClient(View view);
     void chooseTiles(String username, List<String> tilesChosen);

     void login(String name);

     void setUserToken(String token);
     String getToken();

     void setReceivedResponse(boolean b);

     void numberOfPlayers(String name, String token, int number);

    void chooseColumn(int column);

    void setPing(boolean b);
    boolean isSyn();

    void close();

    void setResponseDecoder(ResponseDecoder responseDecoder);

    boolean isConnected();

    void rearrangeTiles(String userToken, List<String> multipleChoiceNumber);

    void passTiles(ArrayList<Position> tilesChosen);

    void sendChat(String username, String toString, String choice);
    void sendPing(String token);

    void setCheckTimer(boolean b);
}
