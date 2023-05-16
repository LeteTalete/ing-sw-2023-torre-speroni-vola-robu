package it.polimi.ingsw.view;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.stati.Status;
import it.polimi.ingsw.structures.LivingRoomView;
import it.polimi.ingsw.structures.PlayerView;
import it.polimi.ingsw.structures.ShelfView;

public class ClientGUI implements View {
    private ClientController master;
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
    public void GameTitle() {

    }

    @Override
    public void showShelf(ShelfView myShelf) {

    }

    @Override
    public void showLivingRoom(LivingRoomView livingRoomView) {

    }

    @Override
    public void showBoardPlayer(PlayerView playerBoardView, LivingRoomView livingRoomView) {

    }

    @Override
    public void showPersonalGoalCard() {

    }

    @Override
    public void showBoard(LivingRoomView livingRoomView) {

    }


    @Override
    public IClientListener getListener() {
        return null;
    }


    @Override
    public void printError(String message) {

    }

    @Override
    public void setMyTurn(boolean b) {

    }

    @Override
    public void setMaster(ClientController clientController) {
        this.master = clientController;
    }

}
