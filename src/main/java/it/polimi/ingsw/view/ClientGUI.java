package it.polimi.ingsw.view;

import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.network.IListener;
import it.polimi.ingsw.stati.Status;
import it.polimi.ingsw.structures.LivingRoomView;
import it.polimi.ingsw.structures.ShelfView;

import java.rmi.RemoteException;

public class ClientGUI implements View {
    @Override
    public void chooseConnection() {

    }

    @Override
    public String getConnectionType() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public void displayNotification(String message) {

    }

    @Override
    public void GamerStatus(Status current) {

    }

    @Override
    public int askAmountOfPlayers() {
        return 0;
    }

    @Override
    public void startGame() {

    }

    @Override
    public void showShelf(ShelfView myShelf) {

    }

    @Override
    public void showLivingRoom(LivingRoomView livingRoomView) {

    }

    @Override
    public void showPersonalGoalCard() {

    }

    @Override
    public void showSlotTile(Couple tile) {

    }

    @Override
    public IListener getListener() {
        return null;
    }


    @Override
    public void printError(String message) {

    }

}
