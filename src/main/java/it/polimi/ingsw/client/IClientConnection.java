package it.polimi.ingsw.client;


import it.polimi.ingsw.view.View;

import java.util.List;

/**connection interface which is implemented by ClientRMI and ClientSocket*/

public interface IClientConnection {
     void setViewClient(View view);
     void chooseTiles(String username, List<String> tilesChosen);

     void login(String name);

     void setUserToken(String token);

     void setReceivedResponse(boolean b);

     void numberOfPlayers(String name, String token, int number);

    void chooseColumn(int column);

    void close();

    void setResponseDecoder(ResponseDecoder responseDecoder);

    boolean isConnected();
    void setConnected(boolean b);

    void rearrangeTiles(String userToken, List<String> multipleChoiceNumber);

    void sendChat(String username, String toString, String receiver);

    void quit(String token);

    void setSyn(boolean b);

    boolean isSyn();

    void setSynCheckTimer(boolean startTimer);

    void sendAck();
}
