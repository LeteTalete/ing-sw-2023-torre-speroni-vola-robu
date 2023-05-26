package it.polimi.ingsw.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandParsing {
    private static Logger fileLog = LogManager.getRootLogger();
    private static final String TILES = "tiles";
    private static final String HELP = "help";
    private static final String CHAT = "@";
    private static final String REARRANGE = "order";
    private static final String SHELFSHOW = "showshelf";
    private static final String COLUMN = "column";
    private static final String NUMBER = "number";
    private static final String USERNAME = "username";
    private static final String BACK = "back";
    //for when it's the player's turn
    private boolean isPlaying;
    private boolean gameIsOn;

    private int choiceNumber;
    private List<String> multipleChoiceNumber;
    private final ClientController master;

    public CommandParsing(ClientController master) {
        this.master = master;
    }

    public void elaborateInput(String command) {
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
            case (USERNAME) ->
                //if choosing username
                    parseUsername(args);
            case (TILES) -> {
                if (!gameIsOn && !isPlaying) {
                    notMyTurn();
                    break;
                }
                //if choosing tiles
                parseMultipleInteger(args);
                executeTileCommand();
            }
            case (BACK) -> {
                if (!gameIsOn && !isPlaying) {
                    notMyTurn();
                    break;
                }
                //if user wants to go back
                masterGoBack();
            }
            case (NUMBER) -> {
                //if choosing tiles
                parseInteger(args);
                master.numberOfPlayers(choiceNumber);
            }
            case (REARRANGE) -> {
                if (!gameIsOn && !isPlaying) {
                    notMyTurn();
                    break;
                }
                //if choosing tiles
                parseMultipleInteger(args);
                executeRearrangeCommand();
            }
            case (COLUMN) -> {
                if (!gameIsOn && !isPlaying) {
                    notMyTurn();
                    break;
                }
                //if choosing tiles
                parseInteger(args);
                executeColumnCommand();
            }
            case (SHELFSHOW) -> {
                if (!gameIsOn) {
                    notMyTurn();
                    break;
                }
                //if choosing tiles
                executeShelfCommand();
            }
            case (CHAT) -> {
                if (!gameIsOn) {
                    notMyTurn();
                    break;
                }
                parseUsername(args);
            }
            case (HELP) -> master.printCommands();
            default -> master.wrongCommand();
        }
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
        if(args.size()!=1){
            master.errorFormat();
            return;
        }
        try {
            String name = (String)args.get(0);
            master.askLogin(name);
        }catch(NumberFormatException e){
            //ack("ERROR: Wrong parameter");
            //clientController.getViewClient().denyMove();
            //choiceNumber = -1;
            fileLog.error(e.getMessage());
        }
    }

    private void executeRearrangeCommand() {
        //todo check formatting of the request and send a notif to the view if formatting is wrong
        master.rearrangeTiles(multipleChoiceNumber);
    }

    private void executeTileCommand() {
        //todo check formatting of the request and send a notif to the view if formatting is wrong
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
}

