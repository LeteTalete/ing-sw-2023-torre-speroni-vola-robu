package it.polimi.ingsw.view;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.CommandParsing;
import it.polimi.ingsw.network.ClientListenerTUI;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.stati.Status;
import it.polimi.ingsw.structures.*;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;


public class ClientTUI implements View{
    private static final String ERROR_COMMAND = "ERROR";
    private ClientController master;
    private CommandParsing commandParsing;
    private ClientListenerTUI listenerClient;
    static final String colorRESET = "\033[0m";  // Reset Changes
    static final String colorTitle = "\033[38;5;11m"; //Yellow
    private final Integer sizeSlotTile = 3; //Tile size to be colored
    private String connectionType;
    private String command;


    //constructor
    public ClientTUI(){
        setupStdInput();
        try {
            this.listenerClient = new ClientListenerTUI(this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        GameTitle();
    }
    //this one is to read from keyboard input
    private Scanner frominput;
    //this one is for writing
    public synchronized void writeText(String text){
        System.out.println(">> " + text);
    }

    private void setupStdInput(){
        this.frominput = new Scanner(System.in);
    }
    @Override
    public void chooseConnection() {
        String connection;
        writeText("Please choose connection type:");
        do{
            writeText("Socket [S] or RMI[R]?");
            connection = frominput.nextLine();
            connection = connection.toUpperCase();
            if(connection.equals("R")){
                connection = "RMI";
            }else if(connection.equals("S")){
                connection = "SOCKET";
            }
        }while(!connection.equals("RMI") && !connection.equals("SOCKET"));

        connectionType = connection;

    }
    private String nextCommand() {
        command = frominput.nextLine();
        if(master.isGameOn() /*and if we're not disconnected*/) {
            master.wake();
        }
        if (!master.isGameOn()) {
            printError("The game is not started yet");
            command = ERROR_COMMAND;
        }

        return command;
    }
    public void running() {
        writeText("we're on");
        do {
            command = nextCommand();
            if (!command.equals(ERROR_COMMAND)) {
                commandParsing.elaborateInput(command);
            }
        } while (master.isGameOn() /*and connection is not lost*/);
        if(master.isGameOn() /*and connection is not lost, idk*/) {
            //i don't remember what i was supposed to write here, i'm tired
        }
        else {
            master.close();
        }

    }

    @Override
    public String getConnectionType() {
        return this.connectionType;
    }

    public String getUsername(){
        writeText("Insert username");
        return frominput.nextLine();
    }

    @Override
    public void displayNotification(String message) {
        writeText(message);
    }
    //gamerstatus has a status as an argument
    @Override
    public void GamerStatus(Status current) {

    }

    @Override
    public void askAmountOfPlayers() {
        int number = 0;
        while(number==0 || number > 4){
            writeText("Insert number of players (from 2 to 4)");
            number = frominput.nextInt();
            frominput.nextLine();
        }
        master.numberOfPlayers(number);
    }

    public void GameTitle(){
        DrawTui.printlnString(colorTitle + """                       
                               #           #                                ######       ####                   ##        ######
                             ##          ##                               ###    ##     ##   #                  ##      ##     ##
                            ###         ###                                ##     ##   ##                       ##     ##        \s
                           ####       ####    ####       ##                ##    #    ##             #####      ##    ###       #      #####\s
                           ## ##     ## ##   ## ##      ####                ##       ##    ###     ###    ##    ##   #######   ###   ###    ##\s
                           ##  ##   ##  ##       ##    ## ##         #       ##     ##   ##  ##   ##    ###     ##    ###      ##   ##    ###\s
                          ##    ## ##    ##      ##   ##   ##       ##        ##    ## ##    ##   ######     #  ##    ##       ##   ######     #\s
                         ###     ###     ###    ##  ##      ##       ###     ###    ###     ## #   ##       ##  ## #  ##       ##    ##       ##
                       ####      #        ####   ####       ##         ######      ##      ####     ########    ###   ##      ####    ########     \s
                                                            ##                                                        ##  \s
                                                           ##                                                         ##
                                                #         ##                                                         ##
                                                 ##     ###                                                         #
                                                   #####                          \s
                   """ + colorRESET
        );
        DrawTui.printlnString("+++++++++++++++++++++++++++++++++++++++++++[ START GAME ]+++++++++++++++++++++++++++++++++++++++++++");
    }

    @Override
    public void showShelf(ShelfView myShelf){
        DrawTui.graphicsShelf(myShelf, true, false);
    }

    @Override
    public void showLivingRoom(LivingRoomView livingRoomView){
         DrawTui.printlnString(DrawTui.graphicsLivingRoom(livingRoomView, true, false));
    }

    @Override
    public void showBoardPlayer(PlayerView playerBoardView, LivingRoomView livingRoomView){
        String livingRoomP = DrawTui.graphicsLivingRoom(livingRoomView, false, true);  //livingRoom of Player
        String shelfP = DrawTui.graphicsShelf(playerBoardView.getShelf(), true, true);
        DrawTui.printlnString(DrawTui.mergerString(livingRoomP, shelfP, true, false, false));
        chooseTiles();
    }
    public void chooseTiles(){
        DrawTui.askWhat("Choose the tiles [Row, Column]");
    }
    public void rearrangeTiles(){
        writeText("Please, choose an order for your tiles []");
    }

    @Override
    public void showPersonalGoalCard(){

    }

    @Override
    public void showBoard(LivingRoomView livingRoomView) {
        DrawTui.printlnString(DrawTui.graphicsLivingRoom(livingRoomView, true, false));
    }


    @Override
    public IClientListener getListener() {
        return listenerClient;
    }

    @Override
    public void printError(String message) {

    }

    @Override
    public void setMyTurn(boolean b) {
        //the view has a while loop that gets the player's input
        //if this b is false, none of the input can be sent to the server. it is only elaborated when the client asks
        //to see another player's shelf, for example
        this.master.setMyTurn(b);
    }

    @Override
    public void startRun() {
        //playing();
    }



    @Override
    public void setMaster(ClientController clientController, CommandParsing commandParsing) {
        this.master = clientController;
        this.commandParsing = commandParsing;
    }

    @Override
    public void askForTiles() {
        chooseTiles();
        String tilesChosen = frominput.nextLine();
        //commandParsing.elaborateInput(tileScelte);
        if ( checkUserInput(tilesChosen) ) {
            master.chooseTiles(tilesChosen);
        } else {
            System.out.println("Wrong format");
            askForTiles();
        }

    }

    @Override
    public void serverSavedUsername(String name, boolean b, String token, boolean first) {
        master.serverSavedUsername(name, b, token);
        if(first){
            askAmountOfPlayers();
        }
    }


    public boolean getMyTurn() {
        return master.isMyTurn();
    }

    public boolean checkUserInput(String s)
    {
        //user input should be like this: "02" or "38 45" or "54 11 64"
        //from 1 to 3 couples of int separated by a space
        //there cannot be duplicated couples
        //9 is not allowed (index out of bounds)
        //note: ASCII: '0' = 48 ... '9' = 57
        //note: 'space' = 32
        int l = s.length();


        if(l!=2 && l!=5 && l!=8) return false;
        if(l > 5)
        {
            if(s.charAt(5) != 32) return false;
            if(s.charAt(6) < 48 || s.charAt(6) > 56) return false;
            if(s.charAt(7) < 48 || s.charAt(7) > 56) return false;
            if((s.charAt(0) == s.charAt(6) && s.charAt(1) == s.charAt(7))
                    || (s.charAt(3) == s.charAt(6) && s.charAt(4) == s.charAt(7)))  return false;
        }

        if(l > 2)
        {
            if(s.charAt(2) != 32) return false;
            if(s.charAt(3) < 48 || s.charAt(3) > 56) return false;
            if(s.charAt(4) < 48 || s.charAt(4) > 56) return false;
            if(s.charAt(0) == s.charAt(3) && s.charAt(1) == s.charAt(4))  return false;
        }
        if(s.charAt(0) < 48 || s.charAt(0) > 56) return false;
        if(s.charAt(1) < 48 || s.charAt(1) > 56) return false;
        return true;
    }

    //this should be some kind of run that only gets lines and parses them
    public void playing() {
        //needs fixing

    }

    public boolean isGameOn() {
        return master.isGameOn();
    }

    public void setGameOn(boolean gameOn) {
        master.setGameOn(gameOn);

    }

    public void chooseColumn() {
        writeText("Choose column [Number of column]");
        int column = frominput.nextInt();
        if(column > 5 || column <0 ){
            writeText("Wrong format");
            chooseColumn();
        }
        else master.chooseColumn(column);
    }
}
