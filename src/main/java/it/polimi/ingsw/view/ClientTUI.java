package it.polimi.ingsw.view;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.CommandParsing;
import it.polimi.ingsw.model.board.Couple;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.network.ClientListenerTUI;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.server.StaticStrings;
import it.polimi.ingsw.structures.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.util.*;

/**class ClientTUI for the textual interface of the game*/

public class ClientTUI implements View{
    /**logger to keep track of events, such as errors and info about parameters*/
    private static final Logger fileLog = LogManager.getRootLogger();
    private GameView gameView;
    private ClientController master;
    private CommandParsing commandParsing;
    /**command String is used to save the textual input from the user*/
    private String command;
    private final ClientListenerTUI listenerClient;
    private String connectionType;
    /**isRunning boolean signals whether the TUI is accepting input from the user*/
    private boolean isRunning;
    private String port;
    private String ServerIP;
    /**chatQueue attribute is used to keep track of the chat's messages*/
    private final LinkedList<String> chatQueue = new LinkedList<>();
    private String username;
    private boolean isGameOn = false;
    /**the following booleans are implemented to keep track of the single user's choices about what to see on the
     * screen, so that each update shows what the user wants*/
    /**isChatOpen boolean is used to know whether to show the chat or not in the TUI*/
    public boolean isChatOpen = true;
    /**showCommonGoalCards boolean is used to know whether to show the CommonGoalCards or not in the TUI*/
    private boolean showCommonGoalCards = true;
    /**showCommandsList boolean is used to know whether to show the commands or not in the TUI*/
    private boolean showCommandsList;
    /**showOtherShelves boolean is used to know whether to show the other players'
     * shelves or not in the TUI*/
    private boolean showOtherShelves;
    /**newChatMessage boolean is used to signal if there is an unread message when the chat is hidden*/
    private boolean newChatMessage;
    /**tiles attribute is used to keep track of the player's re-arranged choice*/
    private ArrayList<Position> tiles;
    /**Scanner fromInput to scan the user's textual input*/
    private Scanner fromInput;


    public ClientTUI() {
        setupStdInput();
        try {
            this.listenerClient = new ClientListenerTUI(this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        GameTitle();
    }

    /** Method setupStdInput is used to set fromInput as the new Scanner for the standard input. */
    private void setupStdInput() {
        this.fromInput = new Scanner(System.in);
    }

    /**
     * Method writeText is used to print a text on the console.
     * @param text - text to be printed.
     */
    public synchronized void writeText(String text) {
        System.out.println(">> " + text);
    }

    /**
     * Method displayUpdatedModel sets the updated game view with the new model and calls the method refreshBoard to
     * print the updated game view. Only on the first time the method is called it will set the elements PGC and CGC
     * inside class DrawTui for future use.
     * @param modelUpdate - updated model.
     */
    @Override
    public void displayUpdatedModel(ModelUpdate modelUpdate) {
        this.gameView = new GameView(modelUpdate);
        //
        if (!this.isGameOn) {
            this.isGameOn = true;
            DrawTui.setStringPCG(gameView.getPlayersView().stream()
                    .filter(p -> p.getNickname().equals(master.getUsername())).findFirst().orElse(null).getPersonalGoalCard().getPositionTilePC(), 5);
            this.gameView.getCommonGoalCards().forEach((idCGC) -> DrawTui.setStringCGC(idCGC.getID()));
        }
        refreshBoard();
    }

    /**
     * Method refreshBoard clears the console and prints the updated game view.
     * Based on the boolean values showCommandsList, showOtherShelves, showCommonGoalCards and isChatOpen, it will
     * remember if the user wants to see the commands list, the other players' shelves, the common goal cards and the
     * chat. If the user has new messages in the chat when it is closed, it will print a message to notify him.
     * When those booleans are set to true, the corresponding elements be printed every time the method refreshBoard is called.
     * If they are set to false, they will not be printed.
     */
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
                System.out.println("\033[1;97;48;5;124m" + " You have new messages in the chat. " + "\033[0m");
            }
            System.out.println("Type 'showchat' to open the chat.");
        }

        System.out.println("----------------------------------------");
        turnPhase();
        System.out.println("----------------------------------------");
        System.out.print("#>: ");
    }

    /**
     * Method turnPhase is used to print the current turn phase.
     * If it's the player's turn, it will guide him through the commands he can use.
     * If it's not the player's turn, it will display a message saying whose turn it is.
     */
    @Override
    public void turnPhase() {
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
                writeText("Choose order: [order 'first number' 'second number' 'third number']. Ex: order 3 1 2");
                chooseColumn();
                chooseOrder();
                displayNotification("To choose different tiles, type the command: [tiles row,column] again. ");
            }
        }
    }

    /**
     * Method clearConsole is used to clear the console, it works by checking the current operating system and then
     * executing the specific command to clear the console.
     */
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

    /**
     * Method chooseConnection is used to ask the user to choose which connection type he wants to use.
     * It will keep asking until the user types a valid input then it will set the connectionType variable.
     */
    @Override
    public void chooseConnection() {
        String connection;
        writeText("Please choose connection type:");
        do {
            writeText("Socket [S] or RMI[R]?");
            connection = fromInput.nextLine();
            connection = connection.toUpperCase();
            if (connection.equals("R")) {
                connection = "RMI";
            } else if (connection.equals("S")) {
                connection = "SOCKET";
            }
        } while (!connection.equals("RMI") && !connection.equals("SOCKET"));

        connectionType = connection;
        askConnectionServer();
    }

    /**
     * Method nextCommand is used to read the user input.
     * @return - the command typed by the user.
     */
    private String nextCommand() {
        command = fromInput.nextLine();
        return command;
    }

    /** Method running is used to keep the TUI running until the user disconnects or the game ends. */
    public void running() {
        fileLog.info("ClientTUI running");
        do {
            command = nextCommand();
            fileLog.debug("Command: " + command);
            commandParsing.elaborateInput(command);
        } while (master.isConnected());

        if (master.isGameOn() && !master.isConnected()) {
            displayNotification("Connection error. Please try again later.");
        } else {
            fileLog.debug("ClientTUI stopped");
            master.close();
        }

    }

    /** Method printCommands is used to print all the commands available to the user. */
    public void printCommands() {
        writeText("Here are all the commands you can use while playing:\n" +
                "help : shows all the commands\n" +
                "hidecommands : hides all the commands\n" +
                "tiles row,col row,col row,col : to pick one to three tiles you want to place on your shelf. Ex: tiles 3,7 4,7\n" +
                "order number number number : to rearrange the order of the tiles you want to place on your shelf. Ex: order 3 1 2\n" +
                "column number : to choose the column of the shelf in which you want to place your tiles. Ex: column 3\n" +
                "showshelves: shows the shelves of all the other players\n" +
                "hideshelves: hides the shelves of the other players\n" +
                "showchat : shows the chat\n" +
                "hidechat : hides the chat\n" +
                "showcards : shows common goal cards and their description\n" +
                "hidecards : hides the common goal cards\n" +
                "@username message : to send a message to the player who has that username\n" +
                "@all message : to send a message to all the players\n" +
                "quit : disconnects from the game\n");
    }

    /**
     * Method changeTurn updates the client state of the current turn based on the name of current player.
     * @param name - the name of the player whose turn it is.
     */
    @Override
    public void changeTurn(String name) {
        master.isItMyTurn(name);
    }

    /** Method askServerIP is used to ask the user to insert the server IP end the server port. */
    @Override
    public void askConnectionServer() {
        writeText("Insert server IP: ['xxx.xxx.xxx.xxx']");
        ServerIP = fromInput.nextLine();
        writeText("Insert server port: ['xxxx']");
        port = fromInput.nextLine();
        master.setupConnection();
    }

    /**
     * Method getConnectionType is used to get the connection type chosen by the user.
     * @return - the connection type chosen by the user.
     */
    @Override
    public String getConnectionType() {
        return this.connectionType;
    }

    /** Method getUserName asks the user to insert his username and if the TUI is not running, it will start it. */
    public void getUsername() {
        writeText("Insert username: ");
        if (!isRunning) {
            setIsRunning(true);
            fileLog.debug("isRunning set to true");
            running();
        }
    }

    /**
     * Method setIsRunning is used to set the isRunning variable.
     * @param b - true if the TUI is running, false otherwise.
     */
    private void setIsRunning(boolean b) {
        this.isRunning = b;
    }

    /**
     * Method displayNotification is used to display a message to the user.
     * @param message - the message to be displayed.
     */
    @Override
    public void displayNotification(String message) {
        fileLog.debug("displayNotification: " + message);
        writeText(message);
    }

    /** Method askAmountOfPlayers is used to ask the user to insert the number of players. */
    @Override
    public void askAmountOfPlayers() {
        writeText("Insert number of players (from 2 to 4):");
    }

    /** Method GameTitle is used to print the title of the game. */
    public void GameTitle() {
        DrawTui.printTitle();
        DrawTui.printlnString("+++++++++++++++++++++++++++++++++++++++++++[ START GAME ]+++++++++++++++++++++++++++++++++++++++++++");
    }

    /**
     * Method showShelves is used to set the showOtherShelves variable to true and refresh the board.
     * It will show the shelves of all the other players.
     */
    @Override
    public void showShelves() {
        this.showOtherShelves = true;
        refreshBoard();
    }

    /**
     * Method hideShelves is used to set the showOtherShelves variable to false and refresh the board.
     * It will hide the shelves of all the other players.
     */
    @Override
    public void hideShelves() {
        this.showOtherShelves = false;
        refreshBoard();
    }

    /**
     * Method showChat is used to set the isChatOpen variable to true and refresh the board.
     * It will show the chat.
     */
    @Override
    public void showChat() {
        this.isChatOpen = true;
        refreshBoard();
    }

    /**
     * Method hideChat is used to set the isChatOpen variable to false and refresh the board.
     * It will hide the chat.
     */
    @Override
    public void hideChat() {
        this.isChatOpen = false;
        refreshBoard();
    }

    /**
     * Method showCommands is used to set the showCommandsList variable to true and refresh the board.
     * It will show the commands list.
     */
    @Override
    public void showCommands() {
        this.showCommandsList = true;
        refreshBoard();
    }

    /**
     * Method hideCommands is used to set the showCommandsList variable to false and refresh the board.
     * It will hide the commands list.
     */
    @Override
    public void hideCommands() {
        this.showCommandsList = false;
        refreshBoard();
    }

    /**
     * Method showCommonGoalCards is used to set the showCommonGoalCards variable to true and refresh the board.
     * It will show the common goal cards.
     */
    @Override
    public void showCommonGoalCards() {
        this.showCommonGoalCards = true;
        refreshBoard();
    }

    /**
     * Method hideCommonGoalCards is used to set the showCommonGoalCards variable to false and refresh the board.
     * It will hide the common goal cards.
     */
    @Override
    public void hideCommonGoalCards() {
        this.showCommonGoalCards = false;
        refreshBoard();
    }

    /** Method printOtherShelves is used to print the shelves of all the other players. */
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

    /**
     * Method showBoardPlayer is used to print the board. It will print the living room, the shelf of the player
     * and his personal goal card adjacent to the living room.
     * @param playerView - the player view.
     * @param livingRoomView - the living room view.
     */
    @Override
    public void showBoardPlayer(PlayerView playerView, LivingRoomView livingRoomView) {
        String livingRoomP = DrawTui.graphicsLivingRoom(livingRoomView, false, true);  //livingRoom of Player
        String shelfP = DrawTui.graphicsShelf(playerView.getShelf(), "Shelf:", false, true);
        String pcg = DrawTui.getStringPCG();
        livingRoomP = DrawTui.mergerString(livingRoomP, shelfP, false, true, false);
        DrawTui.printlnString(DrawTui.mergerString(livingRoomP, pcg, true, false, false));
    }

    /**
     * Method showCommonGoalCard is used to print the common goal cards.
     * @param token1 - the points still available for the common goal card 1.
     * @param token2 - the points still available for the common goal card 2.
     */
    public void showCommonGoalCard(int token1, int token2) {
        String CGC1 = DrawTui.mergerString(DrawTui.getStringCGC(0), DrawTui.graphicsToken(token1, false), false, true, true);
        String CGC2 = DrawTui.mergerString(DrawTui.getStringCGC(1), DrawTui.graphicsToken(token2, true), true, true, true);
        DrawTui.printlnString(DrawTui.mergerString(CGC1, CGC2, true, false, true));
    }

    /**
     * Method getListener is used to get the listener of the client.
     * @return - the listener of the client.
     */
    @Override
    public IClientListener getListener() {
        return listenerClient;
    }

    /**
     * Method printError is used to print an error message with a red background color.
     * @param message - the error message.
     */
    @Override
    public void printError(String message) {
        writeText(DrawTui.colorERROR + " " + message + " " + DrawTui.colorRESET);
    }

    /**
     * Method setMyTurn is used to set the myTurn variable in the client controller.
     * @param b - > 0 if it is the turn of the player, 0 otherwise.
     */
    @Override
    public void setMyTurn(int b) {
        this.master.setMyTurn(b);
    }


    /**
     * Method setMaster is used to set the master of the client and the command parsing.
     * @param clientController - the client controller.
     * @param commandParsing - the command parsing.
     */
    @Override
    public void setMaster(ClientController clientController, CommandParsing commandParsing) {
        this.master = clientController;
        this.commandParsing = commandParsing;
    }

    /** Method askForTiles is used to ask the player to choose the tiles. */
    public void askForTiles() {
        DrawTui.askWhat("Choose the tiles: [tiles row,column]. Ex: tiles x,y x,y x,y");
    }

    /**
     * Method serverSavedUsername is used to save the username of the player.
     * If the boolean b is true, the username is saved, otherwise the username is not saved.
     * If the boolean first is true, it means that it's the first player to connect to the server and login successfully.
     * He will be asked to choose the number of players.
     * @param name - the username chosen by the player.
     * @param b - true if the login is successful, false otherwise.
     * @param token - the token of the player.
     * @param first - true if it's the first player to connect to the server and login successfully, false otherwise.
     */
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

    /**
     * Method setGameOn is used to set the gameOn variable in the client controller and in the command parsing.
     * @param gameOn - true if the game is on, false otherwise.
     */
    public void setGameOn(boolean gameOn) {
        master.setGameOn(gameOn);
        commandParsing.setGameIsOn(gameOn);
    }

    /** Method chooseColumn is used to ask the player to choose the column. */
    @Override
    public void chooseColumn() {
        writeText("Choose column: [column 'number']. Ex: column 3");
    }

    /** Method chooseOrder will print a small representation of the tiles to help the player to choose the order. */
    @Override
    public void chooseOrder() {
        ArrayList<Couple> tilesChoose = new ArrayList<>();
        LivingRoomView livingRoomView = this.gameView.getGameBoardView();
        tiles.forEach(position -> tilesChoose.add(livingRoomView.getCouple(position)));
        DrawTui.graphicsOrderTiles(tilesChoose);
    }

    /**
     * Method nextAction changes the current turn phase and passes the tiles chosen by the player to the client controller.
     * @param num - the number of the turn phase.
     * @param tiles - the tiles chosen by the player.
     */
    @Override
    public void nextAction(int num, ArrayList<Position> tiles) {
        master.nextAction(num, tiles);
    }

    /**
     * Method showEndResult is used to show the end result of the game.
     * It will clear the console and print the score of each player.
     */
    @Override
    public void showEndResult() {
        clearConsole();
        clearConsole();
        DrawTui.printlnString(DrawTui.endGameScore(master.getUsername(), gameView.getScoreboard()));
        writeText("The game is over. Write 'quit' or close the window to disconnect.");
    }



    /** Method printChatQueue is used to print the chat queue. */
    public void printChatQueue() {
        DrawTui.printlnString("CHAT: ");
        chatQueue.stream().forEach(x -> DrawTui.printlnString(x));
    }

    /**
     * Method passTilesToView is used to pass the tiles chosen by the player to clientTUI.
     * @param tiles - the tiles chosen by the player.
     */
    @Override
    public void passTilesToView(ArrayList<Position> tiles) {
        this.tiles = tiles;
    }

    /**
     * method passSyn is used to pass the ping received from the listener to the client controller.
     * */
    @Override
    public void passSyn() {
        master.onSyn();
    }

    /**
     * Method getPort is used to get the server port.
     * @return - the server port.
     */
    @Override
    public String getPort() {
        return port;
    }

    /**
     * Method getServerIP is used to get the server IP.
     * @return - the server IP.
     */
    public String getServerIP() {
        return ServerIP;
    }

    /**
     * Method displayChatNotification is used to display a new chat message.
     * @param s - the message to display.
     */
    public void displayChatNotification(String s) {
        if (chatQueue.size() == 4) {
            chatQueue.removeFirst();
        }
        chatQueue.add(s);
        this.newChatMessage = true;
        refreshBoard();
    }

    /**
     * Method getName is used to get the username of the player.
     * @return - the username of the player.
     */
    public String getName() {
        return username;
    }
}

