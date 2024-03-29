package it.polimi.ingsw.client;

import it.polimi.ingsw.model.board.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**CommandParsing class is used to parse the user's input*/

public class CommandParsing {
    /**fileLog is the logger used to keep track of events*/
    private static final Logger fileLog = LogManager.getRootLogger();

    /**list of commands recognized by the game*/
    private static final String QUIT = "quit";
    private static final String TILES = "tiles";
    private static final String HIDESHELF = "hideshelves";
    private static final String HELP = "help";
    private static final String REARRANGE = "order";
    private static final String SHELFSHOW = "showshelves";
    private static final String SHOWCARDS = "showcards";
    private static final String HIDECARDS = "hidecards";
    private static final String HIDECOMMANDS = "hidecommands";
    private static final String SHOWCHAT = "showchat";
    private static final String HIDECHAT = "hidechat";
    private static final String COLUMN = "column";

    /**boolean isPlaying signalling whether it's the player's turn or not*/
    private boolean isPlaying;
    /**boolean gameIsOn signalling whether it's a game is active or not*/
    private boolean gameIsOn;
    /**boolean initializingName used to signal if the client is logging in*/
    private boolean initializingName;
    /**boolean initializing roon used to signal if the client is creating a waiting room or not*/
    private boolean initializingRoom;

    /**int choiceNumber is used to save and pass to the ClientController an input of a single integer (used to choose
     * the column of the shelf and to set the number of players expected in a waiting room)*/
    private Integer choiceNumber;
    /**multipleChoice is used to save and pass an input of multiple strings to the ClientController*/
    private List<String> multipleChoiceNumber = new ArrayList<>();
    /**choice is used to save and pass an input of a single string to the ClientController*/
    private String choice;
    /**master is used to invoke the method of the ClientController
     * @see ClientController*/
    private final ClientController master;


    /**constructor CommandParsing to set the ClientController and set the parameters.
     * @param master - the clientController, used to invoke its methods*/
    public CommandParsing(ClientController master) {
        this.master = master;
        initializingName = true;
        initializingRoom = false;
    }

    /**elaborateInput method first checks whether the command could be a username or a number of players (to
     * create a waiting room). If neither is the case, it parses the user's input by separating the words and
     * saving them in a list of strings, and it saves the first argument into the command parameter.
     * @param command - the input of the client from the view.*/
    public void elaborateInput(String command) {
        if(initializingName) {
            //if asking for name
            if(!checkUsernameFormat(command)){
                return;
            }
            master.askLogin(command);
            if ( master.getUsername() != null) {
                initializingName = false;
                if ( master.isGameOn() ) {
                    initializingRoom = false;
                }
            } else if ( master.getUsername() == null ) {
                initializingRoom = false;
                initializingName = true;
            }
            return;

        } else if(initializingRoom){
            //if choosing tiles
            if(command.length() != 1 || command.charAt(0) < 50 || command.charAt(0) > 52) {
                master.wrongNumber();
                return;
            }
            choiceNumber= Integer.parseInt(command);
            master.numberOfPlayers(choiceNumber);
            initializingRoom = false;
            return;
        }
        //note: \\s is single whitespace command
        String [] splitted = command.split("\\s");
        List<String> args = new ArrayList<>();
        if(splitted.length > 1) {
            command = splitted[0];
            args = new ArrayList<>(Arrays.asList(splitted));
            args.remove(0);
        } else if ( splitted.length == 1 ){
            command = splitted[0];
        }
        executeCom(command, args);
    }
    /**checkUsernameFormat method checks whether the username is valid or not (meaning it's a single word made of
    * less than 20 characters*/
    private boolean checkUsernameFormat(String command) {
        if(command.length() > 20 || command.isBlank()) {
            master.errorFormat();
            master.userLogin();
            return false;
        }
        return true;
    }

    /**executeCom method is a switch case that gets the command parameter and checks whether it's a known command.
     * If it's a known command, it calls the right method and passes it the list of strings.
     * @param command - the command written by the user.
     * @param args - an optional list of strings which contains the rest of the command (e.g. a chat message)*/
    private void executeCom(String command, List<String> args) {
        switch (command) {
            case (TILES) -> {
                if (master.isMyTurn() == 0) {
                    notMyTurn();
                    break;
                }
                parseMultipleInteger(args, "selection");
                executeTileCommand();
            }
            case (REARRANGE) -> {
                if (master.isMyTurn() == 0) {
                    notMyTurn();
                    break;
                } else if (master.isMyTurn() == 1) {
                    master.errorNoSelection("selection");
                    break;
                }
                parseMultipleInteger(args, REARRANGE);
                executeRearrangeCommand();
            }
            case (COLUMN) -> {
                if (master.isMyTurn() == 0) {
                    notMyTurn();
                    break;
                } else if (master.isMyTurn() == 1) {
                    master.errorNoSelection(COLUMN);
                    break;
            }
                parseInteger(args);
                executeColumnCommand();
            }
            case (SHELFSHOW) -> {
                if (!gameIsOn) {
                    master.gameNotStarted();
                    break;
                }
                master.showShelves();
            }
            case(HIDESHELF) -> {
                if (!gameIsOn) {
                    master.gameNotStarted();
                    break;
                }
                master.hideShelves();
            }
            case(SHOWCARDS) -> {
                if (!gameIsOn) {
                    master.gameNotStarted();
                    break;
                }
                master.showCards();
            }
            case (HIDECARDS) -> {
                if (!gameIsOn) {
                    master.gameNotStarted();
                    break;
                }
                master.hideCards();
            }
            case (HELP) -> {
                if (!gameIsOn) {
                    master.gameNotStarted();
                    break;
                }
                master.showCommands();
            }
            case (HIDECOMMANDS) -> {
                if (!gameIsOn) {
                    master.gameNotStarted();
                    break;
                }
                master.hideCommands();
            }
            case (QUIT) -> {
                master.quit();
            }
            case (SHOWCHAT) -> {
                if (!gameIsOn) {
                    master.gameNotStarted();
                    break;
                }
                master.showChat();
            }
            case (HIDECHAT) -> {
                if (!gameIsOn) {
                    master.gameNotStarted();
                    break;
                }
                master.hideChat();
            }
            default -> {
                if(command.startsWith("@")) {
                    if (!gameIsOn) {
                        master.gameNotStarted();
                        break;
                    }
                    choice = command.substring(1);
                    parseChat(args);
                }
                else{
                    master.wrongCommand();
                }
            }
        }
    }

    /**parseChat method merges the strings of arguments into a single string which will be passed to the
     * clientController and then to the server.
     * @param args - list of strings containing the message*/
    private void parseChat(List<String> args) {
        if(args.size() < 1){
            master.errorFormat();
            return;
        }
        StringBuilder message = new StringBuilder();
        for(String s : args){
            message.append(s).append(" ");
        }
        fileLog.info("sending message " + message + " to " + choice);
        master.sendChat(choice, message.toString());
    }

    /**
     * calls ClientController's invalidNotMyTurn method which calls a method for printing an error on the view
     */
    private void notMyTurn() {
        master.invalidNotMyTurn();
    }


    /**method executeColumnCommand checks whether the format is correct (since there are only 5 columns, it
     * would not make sense if the input was less than 0 or more than 4). If so, it sends the column number
     * to the clientController*/
    private void executeColumnCommand() {
        if (choiceNumber == null) {
            return;
        }
        if(choiceNumber > 4 || choiceNumber < 0){
            master.errorFormat();
            return;
        }

        master.chooseColumn(choiceNumber);
        choiceNumber = null;

    }

    /**method executeRearrangeCommand checks whether the format is correct. If so, it sends the re-arranged
     * positions to the clientController*/
    private void executeRearrangeCommand()
    {
        if(!checkOrderFormat(multipleChoiceNumber))
        {
            if ( multipleChoiceNumber.isEmpty() ){
                return;
            } else master.errorFormat();
            multipleChoiceNumber.clear();
            return;
        }
        master.rearrangeTiles(multipleChoiceNumber);
        multipleChoiceNumber.clear();
    }

    /**
     * method checkOrderFormat checks whether the format is correct. If so, it returns true
     * @param order - list of strings containing user's input
     * @return true if the format is correct, otherwise it returns false
     */
    private boolean checkOrderFormat(List<String> order)
    {
        if ( order.isEmpty() ) return false;
        //checking for admissible values
        for (String s : order) {
            if (!s.equals("1") && !s.equals("2") && !s.equals("3")) return false;
        }

        //checking for duplicates
        for(int i=0; i< order.size()-1;i++) {
            for(int j=i+1; j< order.size();j++) {
                if(order.get(i).equals(order.get(j))) return false;
            }
        }
        return true;
    }

    /**method executeTileCommand checks whether the format is correct. If so, it sends the list of coordinates
     * to the clientController*/
    private void executeTileCommand()
    {
        if(multipleChoiceNumber.isEmpty()) return;
        for(String s : multipleChoiceNumber)
        {
            if(!checkTilesFormat(s))
            {
                master.errorFormat();
                multipleChoiceNumber.clear();
                return;
            }
        }
        master.chooseTiles(multipleChoiceNumber);
        multipleChoiceNumber.clear();
    }

    /**
     * method parseInteger parses the string into integer and saves it into the choiceNumber parameter
     * @param args - list of strings containing user's input
     */
    private void parseInteger(List<String> args) {
        if ( args.size() < 1 ) {
            master.errorNoSelection("choose");
            choiceNumber = null;
            return;
        }
        if(args.size()!=1 || args.get(0).length() != 1) {
            args.clear();
            choiceNumber = null;
            master.errorFormat();
            return;
        }
        if(!args.get(0).equals("0") && !args.get(0).equals("1") && !args.get(0).equals("2") && !args.get(0).equals("3") && !args.get(0).equals("4")) {
            choiceNumber = null;
            args.clear();
            master.errorFormat();
            return;
        }
        choiceNumber = Integer.parseInt(args.get(0));


    }

    /**
     * method parseMultipleInteger parses multiple strings into multiple integers and saves them into
     * multipleChoiceNumber parameter
     * @param args - list of strings containing user's input
     * @param command - the first word typed by the user
     */
    private void parseMultipleInteger(List<String> args, String command) {
        if(args.size() < 1){
            multipleChoiceNumber.clear();
            master.errorNoSelection(command);
            return;
        }
        try{
            multipleChoiceNumber = args;
        }catch(NumberFormatException e) {
            fileLog.error(e.getMessage());
        }
    }

    /**setPlaying method is used to set the attribute isPlaying to true when it's the user's turn
     * @param playing - when this int is >0, the user is either choosing the tiles, the column or re-arranging
     * their tiles; when it's 0, it is not the user's turn*/
    public void setPlaying(int playing) {
        isPlaying = playing > 0;
    }

    /**setGameIsOn is used to set the attribute gameIsOn when the game has started
     * @param gameIsOn - boolean signalling whether the game has started or not*/
    public void setGameIsOn(boolean gameIsOn) {
        this.gameIsOn = gameIsOn;
    }

    /**
     * method checkTilesFormat checks whether the format is correct. If so, it returns true
     * @param s - user's input
     * @return true if the format is correct, otherwise it returns false
     */
    public boolean checkTilesFormat(String s)
    {
        //user input should be like this: "0,2" or "3,8 4,5" or "5,4 1,1 6,4"
        //user input can't be null or empty
        if(s == null || s.length() == 0) return false;

        //extracting positions from the input
        Position p;
        String[] pos = s.split(","); //[5] [4]
        if(pos.length != 2) return false;
        try {
            p = new Position(Integer.parseInt(pos[0]),Integer.parseInt(pos[1])); //(5,4)
        }
        catch(NumberFormatException e) {
            return false;
        }

        if(p.getX()<0 || p.getY()<0) return false;

        //at least 1 couple of coordinates, maximum 3
        return multipleChoiceNumber.size() >= 1 && multipleChoiceNumber.size() <= 3;
    }

    /**setFirst method signals whether the user is the first one connecting, and therefore has to create a
     * waiting room
     * @param b - boolean signalling whether the user needs to create a waiting room or not*/
    public void setFirst(boolean b) {
        this.initializingRoom = b;
    }
}

