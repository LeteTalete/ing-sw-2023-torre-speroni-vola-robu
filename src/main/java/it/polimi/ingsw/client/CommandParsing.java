package it.polimi.ingsw.client;

import it.polimi.ingsw.notifications.ChatMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandParsing {
    private static Logger fileLog = LogManager.getRootLogger();
    private static final String TILES = "tiles";
    private static final String HELP = "help";
    //private static final String CHAT = "@";
    private static final String REARRANGE = "order";
    private static final String SHELFSHOW = "showshelf";
    private static final String COLUMN = "column";
    private static final String BACK = "back";
    //for when it's the player's turn
    private boolean isPlaying;
    private boolean gameIsOn;
    private boolean initializingName;
    private boolean initializingRoom;

    private int choiceNumber;
    private List<String> multipleChoiceNumber;
    private String choice;
    private final ClientController master;

    public CommandParsing(ClientController master) {
        this.master = master;
        initializingName = true;
        initializingRoom = false;
    }

    public void elaborateInput(String command) {
        if(initializingName) {
            //if asking for name
            master.askLogin(choice);
            initializingRoom= true;
            return;
        }
        if(initializingRoom){
            //if choosing tiles
            choiceNumber= Integer.parseInt(command);
            fileLog.debug("choice number is " + choiceNumber);
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
        }
        executeCom(command, args);
    }

    private void executeCom(String command, List<String> args) {
        switch (command) {
            case (TILES) -> {
                if (!isPlaying) {
                    notMyTurn();
                    break;
                }
                //if choosing tiles
                parseMultipleInteger(args);
                executeTileCommand();
            }
            case (BACK) -> {
                if (!isPlaying) {
                    notMyTurn();
                    break;
                }
                //if user wants to go back
                masterGoBack();
            }
            case (REARRANGE) -> {
                if (!isPlaying) {
                    notMyTurn();
                    break;
                }
                //if choosing tiles
                parseMultipleInteger(args);
                executeRearrangeCommand();
            }
            case (COLUMN) -> {
                if (!isPlaying) {
                    notMyTurn();
                    break;
                }
                //if choosing tiles
                parseInteger(args);
                executeColumnCommand();
            }
            case (SHELFSHOW) -> {
                if (!gameIsOn) {
                    master.gameNotStarted();
                    break;
                }
                //if choosing tiles
                executeShelfCommand();
            }
            case (HELP) -> master.printCommands();
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

    private void parseChat(List<String> args) {
        if(args.size() < 1){
            master.errorFormat();
            return;
        }
        StringBuilder message = new StringBuilder();
        for(String s : args){
            message.append(s).append(" ");
        }
        fileLog.info("sending message " + message.toString() + " to " + choice);
        master.sendChat(choice, message.toString());
    }

    private void masterGoBack() {
        /*todo, sends a notification to the server that the client wants to go back and...does something with the model & view, i guess*/
    }

    private void notMyTurn() {
        master.invalidNotMyTurn();
    }


    private void executeShelfCommand() {
    }

    private void executeColumnCommand() {
        if(choiceNumber > 5 || choiceNumber <0 ){
            master.errorFormat();
        }
        else{
            master.chooseColumn(choiceNumber);
        }
    }

    private void parseUsername(List<String> args) {
        //todo this could mean i can put an username with two or more words, but only the first will be parsed
        try {
            choice = args.get(0);
        }catch(NumberFormatException e){
            //ack("ERROR: Wrong parameter");
            //clientController.getViewClient().denyMove();
            //choiceNumber = -1;
            fileLog.error(e.getMessage());
        }
    }

    private void executeRearrangeCommand() {
        for(String s : multipleChoiceNumber){
            if(!s.equals("1") && !s.equals("2") && !s.equals("3")){
                master.errorFormat();
                return;
            }
        }
        master.rearrangeTiles(multipleChoiceNumber);
    }

    private void executeTileCommand() {
        for(String s : multipleChoiceNumber){
            if(!checkTilesFormat(s)){
                master.errorFormat();
                return;
            }
        }
        master.chooseTiles(multipleChoiceNumber);
    }

    private void parseInteger(List<String> args) {
        if(args.size()!=1){
            //ack(">>>>>> ERROR: Insert one parameter");
            //clientController.getViewClient().denyMove();
            //choiceNumber = -1;
            master.errorFormat();
            return;
        }
        try {
            choiceNumber =Integer.parseInt(args.get(0));
        }catch(NumberFormatException e){
            //ack("ERROR: Wrong parameter");
            //clientController.getViewClient().denyMove();
            //choiceNumber = -1;
            fileLog.error(e.getMessage());
        }
    }

    private void parseMultipleInteger(List<String> args) {
        try{
            multipleChoiceNumber = args;
        }catch(NumberFormatException e) {
            fileLog.error(e.getMessage());
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void setGameIsOn(boolean gameIsOn) {
        this.gameIsOn = gameIsOn;
    }
    public boolean checkTilesFormat(String s)
    {
        //todo adapt this to a string of a single coordinate

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
}

