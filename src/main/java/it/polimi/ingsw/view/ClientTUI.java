package it.polimi.ingsw.view;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.CommandParsing;
import it.polimi.ingsw.model.board.Couple;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.network.ClientListenerTUI;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.server.StaticStrings;
import it.polimi.ingsw.stati.Status;
import it.polimi.ingsw.structures.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.util.*;


public class ClientTUI implements View{
    private static Logger fileLog = LogManager.getRootLogger();
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
    private LinkedList<String> chatQueue = new LinkedList<>();
    private String username;
    private boolean isStarGame;

    public boolean isChatOpen = true;
    private boolean showCommonGoalCards = true;
    private boolean showCommandsList;

    private boolean showOtherShelves;
    private boolean newChatMessage;
    private ArrayList<Position> tiles;

    //constructor
    public ClientTUI() {
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
    public synchronized void writeText(String text) {
        System.out.println(">> " + text);
    }

    private void setupStdInput() {
        this.frominput = new Scanner(System.in);
    }

    public void setPGCandCGC() {
        DrawTui.setStringPCG(gameView.getPlayersView().stream()
                .filter(p -> p.getNickname().equals(master.getUsername())).findFirst().orElse(null).getPersonalGoalCard().getPositionTilePC(), 5);
        this.gameView.getCommonGoalCards().forEach((idCGC) -> DrawTui.setStringCGC(idCGC.getID()));
    }

    public void displayUpdatedModel(ModelUpdate modelUpdate) {
        //todo check this
        this.gameView = new GameView(modelUpdate);
        //
        if (!this.isStarGame) {
            this.isStarGame = true;
            setPGCandCGC();
        }
        refreshBoard();
    }

    public void refreshBoard(){

        clearConsole();
        clearConsole();

        System.out.println("Type 'help' to see the list of commands." + "\n");
        if (showCommandsList) {
            printCommands();
        }

        PlayerView mine = gameView.getPlayersView().stream()
                .filter(p -> p.getNickname().equals(master.getUsername())).findFirst().orElse(null);
        showBoardPlayer(mine, gameView.getGameBoardView());

        if (showOtherShelves) {
            printOtherShelves();
        }

        if (showCommonGoalCards) {
            showCommonGoalCard(gameView.getCGC1Points(), gameView.getCGC2Points());
            System.out.println("Common goal card 1: " + gameView.getCommonGoalCards().get(0).getDescription() + "\n");
            System.out.println("Common goal card 2: " + gameView.getCommonGoalCards().get(1).getDescription() + "\n");
        }

        if (gameView.getEndGame() == null) {
            System.out.println("EndGame token still available." + "\n");
        } else {
            System.out.println("EndGame token taken by: " + gameView.getEndGame() + "\n");
        }

        if ( isChatOpen ) {
            printChatQueue();
            this.newChatMessage = false;
        } else {
            if (newChatMessage) {
                System.out.println("You have new messages in the chat.");
            }
            System.out.println("Type 'showchat' to open the chat.");
        }

        System.out.println("----------------------------------------");
        turnPhase();
        System.out.println("----------------------------------------");
    }

    private void turnPhase() {
        switch (master.isMyTurn()) {
            case 0 -> {
                displayNotification("It's " + gameView.getCurrentPlayerNickname() + "'s turn");
            }
            case 1 -> {
                displayNotification(StaticStrings.YOUR_TURN);
                askForTiles();
            }
            case 2 -> {
                displayNotification(StaticStrings.YOUR_TURN);
                displayNotification("You can now re-arrange the tiles or choose the column. Here are the commands:");
                chooseOrder(tiles);
                chooseColumn();
            }
        }
    }


    public static void clearConsole() {
        try {
            String operatingSystem = System.getProperty("os.name"); //Check the current operating system

            if (operatingSystem.contains("Windows")) {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();

                startProcess.waitFor();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void chooseConnection() {
        String connection;
        writeText("Please choose connection type:");
        do {
            writeText("Socket [S] or RMI[R]?");
            connection = frominput.nextLine();
            connection = connection.toUpperCase();
            if (connection.equals("R")) {
                connection = "RMI";
            } else if (connection.equals("S")) {
                connection = "SOCKET";
            }
        } while (!connection.equals("RMI") && !connection.equals("SOCKET"));

        connectionType = connection;


    }

    private String nextCommand() {
        command = frominput.nextLine();
        if (master.isConnected() /*and if the game is on but i'm not sure about this bit*/) {
            //master.wake();
        }
        /*if (!master.isGameOn()) {
            printError("The game is not started yet");
            command = ERROR_COMMAND;
        }*/

        return command;
    }

    public void running() {
        fileLog.info("ClientTUI running");
        do {
            command = nextCommand();
            fileLog.debug("Command: " + command);
            if (!command.equals(ERROR_COMMAND)) {
                commandParsing.elaborateInput(command);
            }
        } while (master.isConnected());
        if (master.isGameOn() /*and connection is not lost, idk*/) {
            fileLog.debug("entered an if and is stuck");
            //i don't remember what i was supposed to write here, i'm tired
        } else {
            fileLog.debug("ClientTUI stopped");
            master.close();
        }

    }

    @Override
    public void printCommands() {
        writeText("Here are all the commands you can use while playing:\n" +
                "help: shows all the commands\n" +
                "hidecommands: hides all the commands\n" +
                "tiles [coordinatexcoordinatey coordinatexcoordinatey]: to pick the tile(s) you want to place on your shelf\n" +
                "order [number number number]: to set the order of the tiles you want to place on your shelf\n" +
                "column [number]: to choose the column of the shelf in which you want to place your tiles\n" +
                "showshelves: shows the shelves of all the other players\n" +
                "hideshelves: hides the shelves of the other players\n" +
                "showchat: shows the chat\n" +
                "hidechat: hides the chat\n" +
                "showcards: shows common goal cards and their description\n" +
                "hidecards: hide the common goal cards\n" +
                "@[username] [message]: to send a message to a player\n" +
                "@all [message]: to send a message to all the players\n" +
                "quit: quits the game\n");
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

    public void getUsername() {
        writeText("Insert username: ");
        if (!isRunning) {
            setIsRunning(true);
            fileLog.debug("isRunning set to true");
            running();
        }
    }

    private void setIsRunning(boolean b) {
        this.isRunning = b;
    }

    @Override
    public void displayNotification(String message) {
        fileLog.debug("displayNotification: " + message);
        writeText(message);
    }

    //gamerstatus has a status as an argument
    @Override
    public void GamerStatus(Status current) {

    }

    @Override
    public void askAmountOfPlayers() {
        writeText("Insert number of players (from 2 to 4):");
    }

    public void GameTitle() {
        DrawTui.printTitle();
        DrawTui.printlnString("+++++++++++++++++++++++++++++++++++++++++++[ START GAME ]+++++++++++++++++++++++++++++++++++++++++++");
    }

    @Override
    public void showShelves() {
        this.showOtherShelves = true;
        refreshBoard();
    }

    @Override
    public void showChat() {
        this.isChatOpen = true;
        refreshBoard();
    }

    @Override
    public void hideChat() {
        this.isChatOpen = false;
        refreshBoard();
    }

    @Override
    public void showCommands() {
        this.showCommandsList = true;
        refreshBoard();
    }

    @Override
    public void hideCommands() {
        this.showCommandsList = false;
        refreshBoard();
    }

    @Override
    public void showCommonGoalCards() {
        this.showCommonGoalCards = true;
        refreshBoard();
    }

    @Override
    public void hideCommonGoalCards() {
        this.showCommonGoalCards = false;
        refreshBoard();
    }

    public void printOtherShelves(){
        String shelfAll = "";
        ArrayList<PlayerView> listPlayers = new ArrayList<>();
        listPlayers.addAll(this.gameView.getPlayersView());
        int numShelfNeed = listPlayers.size() - 1; //is the number of players (without considering the participant) who have not yet set their shelf

        for (int i = 0, len = numShelfNeed; i < listPlayers.size() && numShelfNeed > 0; i++) {
            if (!Objects.equals(listPlayers.get(i).getNickname(), master.getUsername())) {
                if (len == 1) {
                    if (shelfAll.isEmpty()) {
                        shelfAll = DrawTui.graphicsShelf(listPlayers.get(i).getShelf(), listPlayers.get(i).getNickname(), true, false);
                        //System.out.println(" SHelf1: \n" + shelfAll);
                    } else {
                        shelfAll = DrawTui.mergerString(shelfAll, DrawTui.graphicsShelf(listPlayers.get(i).getShelf(), listPlayers.get(i).getNickname(), true, true), true, false, false);
                        //System.out.println(" SHelf2: \n" + shelfAll);
                    }
                } else {
                    if (shelfAll.isEmpty())
                        shelfAll = DrawTui.graphicsShelf(listPlayers.get(i).getShelf(), listPlayers.get(i).getNickname(), false, true);
                    else
                        shelfAll = DrawTui.mergerString(shelfAll, DrawTui.graphicsShelf(listPlayers.get(i).getShelf(), listPlayers.get(i).getNickname(), false, true), false, true, false);
                    //System.out.println(" SHelf3: \n" + shelfAll);
                }
                --len;
            }
        }
        DrawTui.printlnString(shelfAll);
    }

    @Override
    public void showLivingRoom(LivingRoomView livingRoomView) {
        DrawTui.printlnString(DrawTui.graphicsLivingRoom(livingRoomView, true, false));
    }

    @Override
    public void showBoardPlayer(PlayerView playerBoardView, LivingRoomView livingRoomView) {
        String livingRoomP = DrawTui.graphicsLivingRoom(livingRoomView, false, true);  //livingRoom of Player
        String shelfP = DrawTui.graphicsShelf(playerBoardView.getShelf(), "Shelf:", false, true);
        String pcg = DrawTui.getStringPCG();
        livingRoomP = DrawTui.mergerString(livingRoomP, shelfP, false, true, false);
        DrawTui.printlnString(DrawTui.mergerString(livingRoomP, pcg, true, false, false));
    }

    public void chooseTiles() {
        DrawTui.askWhat("Choose the tiles: [tiles row,column]");
    }

    @Override
    public void showPersonalGoalCard() {
        //todo?
    }

    public void showCommonGoalCard(int token1, int token2) {
        String CGC1 = DrawTui.mergerString(DrawTui.getStringCGC(0), DrawTui.graphicsToken(token1, false), false, true, true);
        String CGC2 = DrawTui.mergerString(DrawTui.getStringCGC(1), DrawTui.graphicsToken(token2, true), true, true, true);
        DrawTui.printlnString(DrawTui.mergerString(CGC1, CGC2, true, false, true));
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
        writeText(DrawTui.colorERROR + message + DrawTui.colorRESET);
    }

    @Override
    public void setMyTurn(int b) {
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
    }

    @Override
    public void serverSavedUsername(String name, boolean b, String token, boolean first) {
        master.serverSavedUsername(name, b, token, first);
        if(b){
            this.username = name;
        }
        if (first) {
            askAmountOfPlayers();
        }
    }


    public int getMyTurn() {
        return master.isMyTurn();
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
    public void chooseOrder(ArrayList<Position> tilesPosition) {
        ArrayList<Couple> tilesChoose = new ArrayList<>();
        LivingRoomView livingRoomView = this.gameView.getGameBoardView();
        tilesPosition.forEach(position -> tilesChoose.add(livingRoomView.getCouple(position)));
        writeText("Choose order: [order 'first number' 'second number' 'third number']");
        DrawTui.graphicsOrderTiles(tilesChoose);
    }

    @Override
    public void nextAction(int num, ArrayList<Position> tiles) {
        master.nextAction(num, tiles);
    }

    @Override
    public void showEndResult() {
        //todo: maybe it's better to call clearConsole() and then print the end result
        ArrayList<PlayerView> playersSorted = new ArrayList<>();
        playersSorted.addAll(gameView.getPlayersView());
        Collections.sort(playersSorted, new Comparator<PlayerView>() {
            @Override
            public int compare(PlayerView player1, PlayerView player2) {
                return Integer.compare(player2.getScore(), player1.getScore());
            }
        });
        DrawTui.printlnString(DrawTui.endGameScore(master.getUsername(), playersSorted));
    }

    @Override
    public void pingSyn() {
        master.pingSyn();
    }

    @Override
    public void addToChatQueue(String message, String receiver) {
        if (chatQueue.size() == 4) {
            chatQueue.removeFirst();
        }
        if (receiver.equals("all")) {
            chatQueue.add("@you to all:" + message);
        } else {
            chatQueue.add("@you to " + receiver + ": " + message);
        }
    }

    public void printChatQueue() {
        DrawTui.printlnString("CHAT: ");
        chatQueue.stream().forEach(x -> DrawTui.printlnString(x));
    }

    @Override
    public void hideShelves() {
        this.showOtherShelves = false;
        refreshBoard();
    }

    @Override
    public void passTilesToView(ArrayList<Position> tiles) {
        this.tiles = tiles;
    }

    public String getServerIP() {
        return ServerIP;
    }


    public void setServerIP(String serverIP) {
        ServerIP = serverIP;
    }

    public void displayChatNotification(String s) {
        if (chatQueue.size() == 4) {
            chatQueue.removeFirst();
        }
        chatQueue.add(s);
        this.newChatMessage = true;
        refreshBoard();
        //todo
        //writeText(s);
    }

    public String getName() {
        return username;
    }
}

