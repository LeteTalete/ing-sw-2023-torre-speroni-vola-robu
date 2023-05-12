package it.polimi.ingsw.view;

import it.polimi.ingsw.network.IListener;
import it.polimi.ingsw.stati.Status;
import it.polimi.ingsw.structures.LivingRoomView;
import it.polimi.ingsw.structures.PlayerView;
import it.polimi.ingsw.structures.ShelfView;

import java.rmi.RemoteException;

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
    IListener getListener();

    void printError(String message);
}
