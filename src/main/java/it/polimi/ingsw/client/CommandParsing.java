package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandParsing {
    private static Logger fileLog = LogManager.getRootLogger();
    private static final String TILES = "tiles";
    private static final String HELP = "help";
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
    private List<String> multipleChoiceNumber = new ArrayList<>();
    private String choice;
    private final ClientController master;
    private boolean first;

    public CommandParsing(ClientController master) {
        this.master = master;
        initializingName = true;
        initializingRoom = false;
    }

    public void elaborateInput(String command) {
        if(initializingName) {
            //if asking for name
            master.askLogin(command);
            if ( master.getUsername() != null) {
                initializingName = false;
                if ( master.isGameOn() ) {
                    initializingRoom = false;
                } else {
                    if (first) {
                        initializingRoom = true;
                    } else {
                        initializingRoom = false;
                    }
                }

            } else if ( master.getUsername() == null ) {
                initializingRoom = false;
                initializingName = true;
            }
            return;

        } else if(initializingRoom){
            //if choosing tiles
            if(command.length() != 1 || command.charAt(0) < 50 || command.charAt(0) > 52) return;
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
            //todo check if this is needed
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
                try
                {
                    parseInteger(args);
                }
                catch(NumberFormatException e)
                {
                    master.errorFormat();
                    break;
                }
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

    private void executeRearrangeCommand()
    {
        if(!checkOrderFormat(multipleChoiceNumber))
        {
            master.errorFormat();
            return;
        }

        master.rearrangeTiles(multipleChoiceNumber);
    }

    private boolean checkOrderFormat(List<String> order)
    {
        //checking for admissible values
        for(int i=0; i<order.size();i++)
        {
            if(!order.get(i).equals("1") && !order.get(i).equals("2") && !order.get(i).equals("3")) return false;
        }

        //checking for duplicates
        for(int i=0; i< order.size()-1;i++)
        {
            for(int j=i+1; j< order.size();j++)
            {
                if(order.get(i).equals(order.get(j))) return false;
            }
        }

        return true;
    }

    private void executeTileCommand()
    {
        if(multipleChoiceNumber.size() == 0) return;
        for(String s : multipleChoiceNumber)
        {
            if(!checkTilesFormat(s))
            {
                master.errorFormat();
                return;
            }
        }
        if(multipleChoiceNumber.size() == 1)
        {
            master.setOnlyOneTile(true);
        }
        master.chooseTiles(multipleChoiceNumber);
    }

    private void parseInteger(List<String> args) throws NumberFormatException
    {
        if(args.size()!=1 || args.get(0).length() != 1)
        {
            throw new NumberFormatException();
        }
        choiceNumber =Integer.parseInt(args.get(0));
    }

    private void parseMultipleInteger(List<String> args) {
        if(args.size() < 1){
            master.errorFormat();
            return;
        }
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
        //user input should be like this: "0,2" or "3,8 4,5" or "5,4 1,1 6,4"

        //user input can't be null or empty
        if(s == null || s.length() == 0) return false;

        //extracting positions from the input
        String[] substrings = s.split("\\s+"); //[5,4] [1,1] [6,4]
        List<Position> positions = new ArrayList<Position>();
        Position p;
        for(String sub : substrings)
        {
            String[] pos = sub.split(","); //[5] [4]
            if(pos.length != 2) return false;
            try
            {
                p = new Position(Integer.parseInt(pos[0]),Integer.parseInt(pos[1])); //(5,4)
            }
            catch(NumberFormatException e)
            {
                return false;
            }

            if(p.getX()<0 || p.getY()<0) return false;
            positions.add(p);
        }

        //at least 1 couple of coordinates, maximum 3
        if(positions.size() < 1 || positions.size() > 3) return false;

        return true;
    }

    public void setFirst(boolean b) {
        this.first = b;
    }
}

