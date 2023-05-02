package it.polimi.ingsw.view;

import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.network.IListener;
import it.polimi.ingsw.structures.LivingRoomView;
import it.polimi.ingsw.structures.ShelfView;

import java.rmi.RemoteException;

public interface View {
    void chooseConnection();
    String getConnectionType();
    String getUsername();
    void displayNotification(String message);
    int askAmountOfPlayers();
    //choose tiles from board
    //reorder tiles
    //place tiles on shelf

    void startGame();
    void showShelf(ShelfView myShelf);
    void showLivingRoom(LivingRoomView livingRoomView);
    void showPersonalGoalCard();
    void showSlotTile(Couple tile);

    IListener getListener();

    void printError(String message);
}
