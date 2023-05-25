package it.polimi.ingsw.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandParsing {
    private static final String TILES = "tiles";
    private static final String HELP = "help";
    private static final String CHAT = "@";
    private static final String REARRANGE = "order";
    private static final String SHELFSHOW = "showshelf";
    private static final String COLUMN = "column";
    private static final String NUMBER = "number";
    private static final String USERNAME = "username";

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
                //if choosing tiles
                parseMultipleInteger(args);
                executeTileCommand();
            }
            case (NUMBER) -> {
                //if choosing tiles
                parseInteger(args);
                master.numberOfPlayers(choiceNumber);
            }
            case (REARRANGE) -> {
                //if choosing tiles
                parseMultipleInteger(args);
                executeRearrangeCommand();
            }
            case (COLUMN) -> {
                //if choosing tiles
                parseInteger(args);
                executeColumnCommand();
            }
            case (SHELFSHOW) ->
                //if choosing tiles
                    executeShelfCommand();
            case (CHAT) -> parseUsername(args);
            case (HELP) -> master.printCommands();
            default -> master.wrongCommand();
        }

    }


    private void executeShelfCommand() {
    }

    private void executeColumnCommand() {
        master.chooseColumn(choiceNumber);
    }

    private void parseUsername(List<String> args) {
        if(args.size()!=1){
            System.out.println("error in parsing, try again?");
            return;
        }
        try {
            String name = (String)args.get(0);
            master.askLogin(name);
        }catch(NumberFormatException e){
            //ack("ERROR: Wrong parameter");
            //clientController.getViewClient().denyMove();
            //choiceNumber = -1;
            System.out.println("error: exception?");
        }
    }

    private void executeRearrangeCommand() {
        master.rearrangeTiles(multipleChoiceNumber);
    }

    private void executeTileCommand() {
        master.chooseTiles(multipleChoiceNumber);
    }

    private void parseInteger(List<String> args) {
        if(args.size()!=1){
            //ack(">>>>>> ERROR: Insert one parameter");
            //clientController.getViewClient().denyMove();
            //choiceNumber = -1;
            System.out.println("error in parsing?");
            return;
        }
        try {
            choiceNumber =Integer.parseInt(args.get(0));
        }catch(NumberFormatException e){
            //ack("ERROR: Wrong parameter");
            //clientController.getViewClient().denyMove();
            //choiceNumber = -1;
            System.out.println("error: exception?");
        }
    }

    private void parseMultipleInteger(List<String> args) {
        try{
            multipleChoiceNumber = args;
        }catch(NumberFormatException e) {
            System.out.println("error: exception?");
        }
    }

}

