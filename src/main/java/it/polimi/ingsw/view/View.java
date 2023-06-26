package it.polimi.ingsw.view;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.CommandParsing;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.stati.Status;
import it.polimi.ingsw.structures.LivingRoomView;
import it.polimi.ingsw.structures.PlayerView;
import it.polimi.ingsw.structures.ShelfView;

import java.util.ArrayList;

public interface View {
    void chooseConnection();
    String getConnectionType();
    void getUsername();
    void displayNotification(String message);
    void GamerStatus(Status current);
    void askAmountOfPlayers();
    //choose tiles from board
    //reorder tiles
    //place tiles on shelf
    void GameTitle();
    void showShelves();
    void showChat();
    void hideChat();
    void showCommands();
    void hideCommands();
    void showCommonGoalCards();
    void hideCommonGoalCards();

    void displayUpdatedModel(ModelUpdate modelUpdate);
    void showLivingRoom(LivingRoomView livingRoomView);
    void showBoardPlayer(PlayerView playerBoardView, LivingRoomView livingRoomView);
    void showPersonalGoalCard();
    void showBoard(LivingRoomView livingRoomView);
    IClientListener getListener();

    void printError(String message);

    void setMyTurn(int b);
    void startRun();

    void setMaster(ClientController clientController, CommandParsing commandParsing);

    void askForTiles();

    void serverSavedUsername(String name, boolean b, String token, boolean first);

    void running();
    void setGameOn(boolean gameOn);

    void printCommands();

    void changeTurn(String name);

    void askServerIP();

    String getServerIP();;

    void chooseColumn();

    void chooseOrder();

    void nextAction(int num, ArrayList<Position> tiles);

    void showEndResult();

    void pingSyn();
    void turnPhase();

    void addToChatQueue(String message, String receiver);

    void hideShelves();


    void passTilesToView(ArrayList<Position> tiles);
}