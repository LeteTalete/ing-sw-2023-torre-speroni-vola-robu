package it.polimi.ingsw.view;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.CommandParsing;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.stati.Status;
import it.polimi.ingsw.structures.LivingRoomView;
import it.polimi.ingsw.structures.PlayerView;
import it.polimi.ingsw.structures.ShelfView;

public class ClientGUI implements View {
    private ClientController master;
    private CommandParsing commPars;
    private String ServerIP;

    @Override
    public void chooseConnection() {

    }

    @Override
    public String getConnectionType() {
        return null;
    }

    @Override
    public void getUsername() {
    }

    @Override
    public void displayNotification(String message) {

    }

    @Override
    public void GamerStatus(Status current) {

    }

    @Override
    public void askAmountOfPlayers() {

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
    public void startRun() {

    }

    @Override
    public void setMaster(ClientController clientController, CommandParsing commandParsing) {
        this.master = clientController;
        this.commPars = commandParsing;
    }

    @Override
    public void askForTiles() {

    }

    @Override
    public void serverSavedUsername(String name, boolean b, String token, boolean first) {

    }

    @Override
    public void running() {

    }

    @Override
    public void detangleMessage(Response response) {

    }

    @Override
    public void printCommands() {
        //only for tui
    }

    @Override
    public void changeTurn(String name) {

    }

    @Override
    public void askServerIP() {
        
    }

    @Override
    public String getServerIP() {
        return ServerIP;
    }


    @Override
    public void chooseColumn() {
        //todo
    }

    @Override
    public void chooseOrder() {
        //todo
    }

    @Override
    public void nextAction() {

    }

    @Override
    public void showEndResult() {
        //todo
    }

}
