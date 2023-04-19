package it.polimi.ingsw.view;

import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.structures.LivingRoomView;
import it.polimi.ingsw.structures.ShelfView;
import java.util.*;


public class ClientTUI implements View{

    static final String colorRESET = "\033[0m";  // Reset Changes
    static final String colorTileG = "\033[1;51;30;48;5;214m"; //Orange
    static final String colorTileC = "\033[1;51;30;48;5;10m"; //Green
    static final String colorTileB = "\033[1;51;30;48;5;230m"; //White
    static final String colorTileP = "\033[1;51;30;48;5;13m"; //Magenta
    static final String colorTileF = "\033[1;51;30;48;5;27m"; //Blue
    static final String colorTileT = "\033[1;51;30;48;5;14m"; //Cyan
    static final String colorTitle = "\033[38;5;11m"; //Yellow
    private final Integer sizeSlotTile = 3; //Tile size to be colored
    private String connectionType;


    //constructor
    public ClientTUI(){
        setupStdInput();
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

    @Override
    public String getConnectionType() {
        return this.connectionType;
    }

    public String getUsername(){
        writeText("Insert username");
        return frominput.nextLine();
    }

    @Override
    public void displayNotification(String message) {
        writeText(message);
    }

    @Override
    public int askAmountOfPlayers() {
        writeText("Insert number of players (from 2 to 4)");
        return frominput.nextInt();
    }

    public void startGame(){
        System.out.println(colorTitle + """                       
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
                   """ + colorRESET);
        printString("\t\t++++++++++++++++++++++++++++[ START GAME ]++++++++++++++++++++++++++++\n");

    }

    private void printString(String text, int repeatNum){
        String textRepeat;
        textRepeat = text.repeat(repeatNum);
        System.out.print(textRepeat);
    }
    private void printString(String text){
        System.out.print(text);
    }
    private String returnStringRepeat(String text, int repeatNum){
        String textRepeat;
        textRepeat = text.repeat(repeatNum);
        return textRepeat;
    }

    /*
    Schema Shelf:
        ___________________________
        ||---||---||---||---||---||
        ||   ||   ||   ||   ||   ||
        ||---||---||---||---||---||
        ||   ||   ||   ||   ||   ||
        ||---||---||---||---||---||
        ||   ||   ||   ||   ||   ||
        ||---||---||---||---||---||
        ||   ||   ||   ||   ||   ||
        ||---||---||---||---||---||
        ||   ||   ||   ||   ||   ||
        ||---||---||---||---||---||
        ||   ||   ||   ||   ||   ||
        ||---||---||---||---||---||
       /___________________________\
           0    1    2    3    4
     */

    @Override
    public void showShelf(ShelfView myShelf){
        Couple[][] shelfView = myShelf.getShelfsMatrixView();
        int lengthShelf = shelfView[0].length;
        String roofShelf = returnStringRepeat("_", lengthShelf*(2 + sizeSlotTile) + 2);
        String boardShelf = "||";
        String dividerShelf = returnStringRepeat(boardShelf + returnStringRepeat("-", sizeSlotTile), lengthShelf) + boardShelf;
        Iterator<Couple[]> board = Arrays.asList(shelfView).iterator();

        printString("\t" + roofShelf + "\n\t");
        printString(dividerShelf + "\n\t");
        while(board.hasNext()){
            Couple[] rowShelf = board.next();
            for (Couple couple : rowShelf) {
                printString(boardShelf);
                showSlotTile(couple);
            }
            printString(boardShelf + "\n\t");
            printString(dividerShelf + "\n\t");
        }
        printString("\r   /" + roofShelf + "\\ \n\n");
    }

    /*
    Schema Living Room:
         ----- ----- ----- ----- ----- ----- ----- ----- -----
     0  |     |     |     |     |     |     |     |     |     |
         ----- ----- ----- ----- ----- ----- ----- ----- -----
     1  |     |     |     |     |     |     |     |     |     |
         ----- ----- ----- ----- ----- ----- ----- ----- -----
     2  |     |     |     |     |     |     |     |     |     |
         ----- ----- ----- ----- ----- ----- ----- ----- -----
     3  |     |     |     |     |     |     |     |     |     |
         ----- ----- ----- ----- ----- ----- ----- ----- -----
     4  |     |     |     |     |     |     |     |     |     |
         ----- ----- ----- ----- ----- ----- ----- ----- -----
     5  |     |     |     |     |     |     |     |     |     |
         ----- ----- ----- ----- ----- ----- ----- ----- -----
     6  |     |     |     |     |     |     |     |     |     |
         ----- ----- ----- ----- ----- ----- ----- ----- -----
     7  |     |     |     |     |     |     |     |     |     |
         ----- ----- ----- ----- ----- ----- ----- ----- -----
     8  |     |     |     |     |     |     |     |     |     |
         ----- ----- ----- ----- ----- ----- ----- ----- -----
           0     1     2     3     4     5     6     7     8
     */
    @Override
    public void showLivingRoom(LivingRoomView livingRoomView){
        Couple[][] livingRoom = livingRoomView.getBoard();
        Iterator<Couple[]> board = Arrays.asList(livingRoom).iterator();
        int numColum = 0;
        int numRow = livingRoom[0].length;
        String roofLivingRoom = returnStringRepeat(" -----", numRow);
        String limitLivingRoom = "|";
        String whiteSpace = " ";
        printString("\t" + roofLivingRoom + "\n");
        while(board.hasNext()){
            printString(" " + numColum + "  ");
            Couple[] rowLivingRoom = board.next();
            for (Couple couple : rowLivingRoom) {
                printString(limitLivingRoom + whiteSpace);
                showSlotTile(couple);
                printString(whiteSpace + limitLivingRoom);
            }
            printString("\n\t " + roofLivingRoom);
            ++numColum;
        }
        printString("\n\n");
    }
    /*
    Schema PersonalGoal:
        ___________________________
        ||---||---||---||---||---||
        ||   ||   ||   ||   ||   ||
        ||---||---||---||---||---||
        ||   ||   ||   ||   ||   ||
        ||---||---||---||---||---||
        ||   ||   ||   ||   ||   ||
        ||---||---||---||---||---||
        ||   ||   ||   ||   ||   ||
        ||---||---||---||---||---||
        ||   ||   ||   ||   ||   ||
        ||---||---||---||---||---||
        ||   ||   ||   ||   ||   ||
        ||---||---||---||---||---||
        ---------------------------
     */
    @Override
    public void showPersonalGoalCard(){

    }
    @Override
    public void showSlotTile(Couple tile){
        if(tile.getState() != State.EMPTY){
            T_Type typeTile = tile.getTile().getTileType();
            if(typeTile == T_Type.GAMES) {
                printString(colorTileG + " G " + colorRESET );
            } else if (typeTile == T_Type.CAT) {
                printString(colorTileC + " C " + colorRESET );
            } else if (typeTile == T_Type.BOOK) {
                printString(colorTileB + " B " + colorRESET );
            } else if (typeTile == T_Type.PLANT) {
                printString(colorTileP + " P " + colorRESET );
            } else if (typeTile == T_Type.TROPHY) {
                printString(colorTileT + " T " + colorRESET );
            } else if (typeTile == T_Type.FRAME) {
                printString(colorTileF + " F " + colorRESET );
            }
        } else {
            printString(" ", sizeSlotTile);
        }
    }
    private void printColorTile(String typeTile){
        if(typeTile == "G") {
            printString(colorTileG + " G " + colorRESET );
        } else if (typeTile == "C") {
            printString(colorTileC + " C " + colorRESET );
        } else if (typeTile == "B") {
            printString(colorTileB + " B " + colorRESET );
        } else if (typeTile == "P") {
            printString(colorTileP + " P " + colorRESET );
        } else if (typeTile == "T") {
            printString(colorTileT + " T " + colorRESET );
        } else if (typeTile == "F") {
            printString(colorTileF + " F " + colorRESET );
        } else {
            printString(" ", sizeSlotTile);
        }
    }
}
