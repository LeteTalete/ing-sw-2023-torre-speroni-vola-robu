package it.polimi.ingsw.view;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.stati.Status;
import it.polimi.ingsw.structures.LivingRoomView;
import it.polimi.ingsw.structures.PlayerView;
import it.polimi.ingsw.structures.ShelfView;

public interface View {
    void chooseConnection();
    String getConnectionType();
    String getUsername();
    void displayNotification(String message);
    void GamerStatus(Status current);
    int askAmountOfPlayers();
    //choose tiles from board
    //reorder tiles
    //place tiles on shelf

    void GameTitle();
    void showShelf(ShelfView myShelf);
    void showLivingRoom(LivingRoomView livingRoomView);

    void showBoardPlayer(PlayerView playerBoardView, LivingRoomView livingRoomView);

    void showPersonalGoalCard();
    void showBoard(LivingRoomView livingRoomView);
    IClientListener getListener();

    void printError(String message);

    void setMyTurn(boolean b);
    void startRun();

    void setMaster(ClientController clientController);

    void askForTiles();
}
