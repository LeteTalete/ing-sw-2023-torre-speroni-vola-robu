package it.polimi.ingsw.view;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.CommandParsing;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.ClientListenerTUI;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.stati.Status;
import it.polimi.ingsw.structures.*;

import java.rmi.RemoteException;
import java.util.*;


public class ClientTUI implements View{
    private GameView gameView;
    private static final String ERROR_COMMAND = "ERROR";
    private ClientController master;
    private CommandParsing commandParsing;
    private ClientListenerTUI listenerClient;
    static final String colorRESET = "\033[0m";  // Reset Changes
    static final String colorTitle = "\033[38;5;11m"; //Yellow
    private final Integer sizeSlotTile = 3; //Tile size to be colored
    private String connectionType;
    private String command;
    private boolean isRunning;
    private String colorError; //todo please, make this red
    private String ServerIP;


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

    public void displayUpdatedModel(ModelUpdate modelUpdate){
        this.gameView = new GameView(modelUpdate);

        // TODO: this is obviously bad, needs a method that finds the correct view
        if ( (gameView.getPlayersView().size() >= 2 ) && gameView.getPlayersView().get(0).getNickname().equals(master.getUsername()) ){
            showBoardPlayer(gameView.getPlayersView().get(0), gameView.getGameBoardView());
            System.out.println(DrawTui.setStringPCG(gameView.getPlayersView().get(0).getPersonalGoalCard().getPositionTilePC(), 5, true, false));
        } else if( (gameView.getPlayersView().size() >= 2 ) && gameView.getPlayersView().get(1).getNickname().equals(master.getUsername()) ) {
            showBoardPlayer(gameView.getPlayersView().get(1), gameView.getGameBoardView());
            System.out.println(DrawTui.setStringPCG(gameView.getPlayersView().get(1).getPersonalGoalCard().getPositionTilePC(), 5, true, false));
        } else if( (gameView.getPlayersView().size() >= 3 ) && gameView.getPlayersView().get(2).getNickname().equals(master.getUsername()) ) {
            showBoardPlayer(gameView.getPlayersView().get(2), gameView.getGameBoardView());
            System.out.println(DrawTui.setStringPCG(gameView.getPlayersView().get(2).getPersonalGoalCard().getPositionTilePC(), 5, true, false));
        } else if( (gameView.getPlayersView().size() == 4 ) && gameView.getPlayersView().get(3).getNickname().equals(master.getUsername()) ) {
            showBoardPlayer(gameView.getPlayersView().get(3), gameView.getGameBoardView());
            System.out.println(DrawTui.setStringPCG(gameView.getPlayersView().get(3).getPersonalGoalCard().getPositionTilePC(), 5, true, false));
        }
        if ( gameView.getEndGame() == null ) {
            System.out.println("EndGame token still available." + "\n");
        } else {
            System.out.println("EndGame token taken by: " + gameView.getEndGame() + "\n");
        }
        System.out.println( "Common goal card 1: " + gameView.getCommonGoalCards().get(0).getDescription());
        System.out.println( "Points still available: " + gameView.getCommonGoalCards().get(0).getPoints().pop() + "\n");
        System.out.println( "Common goal card 2: " + gameView.getCommonGoalCards().get(1).getDescription());
        System.out.println( "Points still available: " + gameView.getCommonGoalCards().get(1).getPoints().pop() + "\n");

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
        if(master.isConnected() /*and if the game is on but i'm not sure about this bit*/) {
            //master.wake();
        }
        /*if (!master.isGameOn()) {
            printError("The game is not started yet");
            command = ERROR_COMMAND;
        }*/

        return command;
    }
    public void running() {
        writeText("we're on");
        do {
            command = nextCommand();

            if (!command.equals(ERROR_COMMAND)) {
                commandParsing.elaborateInput(command);
            }
        } while (master.isConnected());
        if(master.isGameOn() /*and connection is not lost, idk*/) {
            //i don't remember what i was supposed to write here, i'm tired
        }
        else {
            master.close();
        }

    }

    @Override
    public void detangleMessage(Response response) {
        try {
            master.detangleMessage(response);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void printCommands() {
        /**todo write all the commands: 'help', 'username [username]', 'number [number]',
         * 'tiles [coordinatexcoordinatey coordinatexcoordinatey ecc]', 'order [number number number]',
         * 'column [number]', 'showshelf [username]', '@ [username]' and some others that I can't think of now**/
    }

    @Override
    public void changeTurn(String name) {
        master.isItMyTurn(name);
    }

    @Override
    public void askServerIP() {
        writeText("Insert server IP: ['xxx.xxx.xxx.xxx']");
        ServerIP = frominput.nextLine();
    }

    @Override
    public String getConnectionType() {
        return this.connectionType;
    }

    public void getUsername(){
        writeText("Insert username: [username 'name']");
        if(!isRunning){
            running();
            setIsRunning(true);
        }
    }

    private void setIsRunning(boolean b) {
        this.isRunning = b;
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
        writeText("Insert number of players (from 2 to 4): [number '2/3/4']");
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
    }
    public void chooseTiles(){
        DrawTui.askWhat("Choose the tiles: [tiles rowcolumn(s)]");
    }
    public void rearrangeTiles(){
        writeText("Please, choose an order for your tiles: [order number(s)]");
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
        writeText(message);
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

    //todo cleanup this probably isn't needed
    @Override
    public void askForTiles() {
        chooseTiles();
    }

    @Override
    public void serverSavedUsername(String name, boolean b, String token, boolean first) {
        master.serverSavedUsername(name, b, token);
        if(first){
            askAmountOfPlayers();
        }
    }


    public int getMyTurn() {
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

    @Override
    public void chooseColumn() {
        writeText("Choose column: [column 'number']");
    }

    @Override
    public void chooseOrder() {
        writeText("Choose order: [order 'number(s)']");
    }

    public String getServerIP() {
        return ServerIP;
    }


    public void setServerIP(String serverIP) {
        ServerIP = serverIP;
    }
}
