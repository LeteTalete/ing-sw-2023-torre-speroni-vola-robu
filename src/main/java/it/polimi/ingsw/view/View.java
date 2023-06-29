package it.polimi.ingsw.view;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.CommandParsing;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.structures.LivingRoomView;
import it.polimi.ingsw.structures.PlayerView;
import it.polimi.ingsw.structures.ShelfView;

import java.util.ArrayList;

public interface View {
    void chooseConnection();
    String getConnectionType();
    void getUsername();
    void displayNotification(String message);
    void askAmountOfPlayers();
    void turnPhase();
    void GameTitle();
    void showShelves();
    void showChat();
    void hideChat();
    void showCommands();
    void hideCommands();
    void showCommonGoalCards();
    void hideCommonGoalCards();
    String getName();
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
    void printCommands();
    void changeTurn(String name);
    void askConnectionServer();
    String getServerIP();;
    void chooseColumn();
    void chooseOrder();
    void nextAction(int num, ArrayList<Position> tiles);
    void showEndResult();
    void addToChatQueue(String message, String receiver);
    void hideShelves();
    void passTilesToView(ArrayList<Position> tiles);
    void passSyn();
    //void askPort();
    String getPort();
}