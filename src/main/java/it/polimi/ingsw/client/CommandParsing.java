package it.polimi.ingsw.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandParsing {
    private int choiceNumber;
    private ClientController master;
    public void elaborateInput(String command) {
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
        //if choosing tiles
        parseInteger(args);
        executeTileCommand();
    }

    private void executeTileCommand() {
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
}

