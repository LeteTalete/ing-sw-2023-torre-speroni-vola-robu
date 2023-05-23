package it.polimi.ingsw.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandParsing {
    private static final String TILES = "tiles";
    private static final String CHAT = "@";
    private static final String REARRANGE = "order";
    private static final String SHELFSHOW = "showshelf";
    private static final String COLUMN = "column";

    private int choiceNumber;
    private ClientController master;
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
        switch(command){
            case(TILES):
                //if choosing tiles
                parseInteger(args);
                executeTileCommand();
                break;
                
            case(REARRANGE):
                //if choosing tiles
                parseInteger(args);
                executeRearrangeCommand();
                break;
                
            case(COLUMN):
                //if choosing tiles
                parseInteger(args);
                executeColumnCommand();
                break;
                
            case(SHELFSHOW):
                //if choosing tiles
                executeShelfCommand();
                break;

            case(CHAT):
                parseUsername(args);
                break;
            }
        
    }

    private void executeShelfCommand() {
    }

    private void executeColumnCommand() {
        
    }

    private void parseUsername(List<String> args) {
        
    }

    private void executeRearrangeCommand() {
        
    }

    private void executeTileCommand() {
    }

    private void parseInteger(List<String> args) {
        //this means we need one tile at a time, i guess
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

}

