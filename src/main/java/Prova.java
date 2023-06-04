
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.board.LivingRoom;
import it.polimi.ingsw.model.board.Shelf;
import it.polimi.ingsw.model.cards.PersonalGoalCard;
import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.model.enumerations.Tile;
import it.polimi.ingsw.view.ClientGUI;
import it.polimi.ingsw.view.DrawTui;

import java.util.ArrayList;
import java.util.SimpleTimeZone;
import java.util.concurrent.ThreadLocalRandom;

public class Prova {

    public static void showCG(int numCG){
        String tileCG = "\033[1;51m";
        String RESET = "\033[0m";
        String tileEqual = tileCG + " = " + RESET;
        String tileDiff = tileCG + " ≠ " + RESET;
        String empty = " ";
        String white = "   ";
        String tileWhite = tileCG + white + RESET;

        printString("\nCarta Comune[" + numCG + "]:\n");
        switch (numCG){
            case 1:
                printTileCGColumn(tileEqual, 2, 2, false, 2, 0);
                break;
            case 2:
                printTileCGColumn(tileDiff, 6, 1, false, 2 ,0);
                break;
            case 3:
                printTileCGColumn(tileEqual, 4, 1, true, 4,0);
                break;
            case 4:
                printTileCGColumn(tileEqual, 2, 1, true, 6,0);
                break;
            case 5:
                printTileCGColumn(tileWhite, 6, 1, false,3,3);
                break;
            case 6:
                printTileCGColumn(tileDiff, 1, 5, false,3,0);
                break;
            case 7:
                printTileCGColumn(tileWhite, 1, 5, false,4,3);
                break;
            case 8:
                String boardTop = returnStringRepeat(" ─", 3);
                String boardSide = returnStringRepeat("\t" + "|"+ empty.repeat(11) + "|" + "\n", 3);
                printString("\t" + tileEqual + boardTop + " " + tileEqual + "\n");
                printString(boardSide);
                printString("\t" + tileEqual + boardTop + " " + tileEqual + "\n");
                break;
            case 9:
                printString("\t  " + tileEqual + "   " + tileEqual + "\n\n");
                printString("\t" + tileEqual + "  " + tileEqual + "  " + tileEqual + "\n\n", 2);
                break;
            case 10:
                printString("\t" + tileEqual + white + tileEqual + "\n");
                printString("\t" + white + tileEqual + white + "\n");
                printString("\t" + tileEqual + white + tileEqual + "\n");
                break;
            case 11:
                String spaceWhite = "";
                for(int i = 0; i < 5; i ++){
                    printString("\t" + spaceWhite + tileEqual + "\n");
                    spaceWhite += white;
                }
                break;
            case 12:
                for(int i = 1; i < 6; i++){
                    printTileCGRow(tileWhite, i, false, 0,0);
                }
                break;
            default: printString("\nErrore");

        }


    }

    public static void printTileCGRow(String tile, int numTileForRow, boolean dashedEdge, int repet, int maxDif){
        String delimitRow = "│";
        String tileMax = "\033[1;51m ≠ \033[0m";
        printString("\t");
        if(dashedEdge) printString("| ");
        for(int i = numTileForRow; i > 0; i--){
            printString(tile);
            if(i != 1) printString(delimitRow);
        }
        if(dashedEdge) printString(" |");
        if(maxDif > 0) printString("\t" + "Max " + maxDif + " " + tileMax);
        if(repet > 1) printString(" X" + repet);
        printString("\n");
    }
    public static void printTileCGColumn(String tile, int numTileForColumn, int numTileForRow,  boolean dashedEdge,  int repet, int maxDif){
        String board = returnStringRepeat(" ─", numTileForRow*2);
        String boardUp = "┌" + board + " ┐";
        String boardDown = "└" + board + " ┘";
        String tileMax = "\033[1;51m ≠ \033[0m";
        int maxDifRow = 0;
        int ripetRow = 0;
        boolean maxDifCol =  maxDif > 0 && numTileForColumn < 2;
        if( dashedEdge ) printString( "\t" + boardUp + "\n");
        for(int i = numTileForColumn; i > 0; i--){
            if(i == 2 && maxDif > 0) maxDifRow = maxDif;
            if(i == 1) ripetRow = repet;
            printTileCGRow(tile, numTileForRow, dashedEdge, ripetRow, maxDifRow);
            maxDifRow = 0;
        }
        if( dashedEdge ) printString("\t" + boardDown);
        if(maxDifCol) printString("\n\t" + "Max " + maxDif + " " + tileMax);
        printString("\n");
    }


    private static String returnStringRepeat(String text, int repeatNum){
        return text.repeat(repeatNum);
    }

    public static void printString(String text){
        System.out.print(text);
    }
    private static void printString(String text, int repeatNum){
        String textRepeat;
        textRepeat = text.repeat(repeatNum);
        System.out.print(textRepeat);
    }

    private static void printScorring(int score){
        printString("Score:\n" +
                "●  ◊  ●\n" +
                "◊  "+ score + "  ◊\n" +
                "●  ◊  ●\n");
    }

    private static void printShelf(){
        System.out.print("""
                ───────────────────────────
                ││───││───││───││───││───││
                ││   ││   ││   ││   ││   ││
                ││───││───││───││───││───││
                ││   ││   ││   ││   ││   ││
                ││───││───││───││───││───││
                ││   ││   ││   ││   ││   ││
                ││───││───││───││───││───││
                ││   ││   ││   ││   ││   ││
                ││───││───││───││───││───││
                ││   ││   ││   ││   ││   ││
                ││───││───││───││───││───││
                ││   ││   ││   ││   ││   ││
                ││───││───││───││───││───││
               /───────────────────────────\\
                   0    1    2    3    4
                """);
    }

    private static void printPersonalGoalCard(){
        String tile = "\033[1;51m   \033[0m";
        System.out.print("""
                
               ┌─────────────────────┐\n"""+
                "│ "+ tile + "│" + tile + "│" + tile +"│" + tile + "│" + tile + " │\n" +
                "│ "+ tile + "│" + tile + "│" + tile +"│" + tile + "│" + tile + " │\n" +
                "│ "+ tile + "│" + tile + "│" + tile +"│" + tile + "│" + tile + " │\n" +
                "│ "+ tile + "│" + tile + "│" + tile +"│" + tile + "│" + tile + " │\n" +
                "│ "+ tile + "│" + tile + "│" + tile +"│" + tile + "│" + tile + " │\n" +
                "│ "+ tile + "│" + tile + "│" + tile +"│" + tile + "│" + tile + " │\n" +
                "│─────────────────────│\n" +
                "│1 | 2 | 3 | 4 | 5 | 6│\n" +
                "│1 | 2 | 4 | 6 | 9 |12│\n" +
                "└─────────────────────┘\n" );
    }

    private static void endGame(){
        System.out.print("""
                
                ++++++++++++++[ END GAME ]++++++++++++
                
                          ######
                        ###    ##
                       ###     ##   
                        ##     #        #####     #####      #       ####     #####
                         ###          ###       ###    ##    ##   ###      ###    ##
                   #      ###       ###       ###       ##    # ###       ##    ### 
                  ##       ###     ###        ###       ##    ###         ######     #
                   ###    ####      ##         ###    ##     ###           ##       ## 
                    #######          #####       #####       ###            ########        
                    
                ++++++++++++++++++++++++++++++++++++++
                | ordine | NomeGiocatore | punteggio | 
                ++++++++++++++++++++++++++++++++++++++
                |   1    | Vernizzi      |   43      |
                |   2    | Luca          |   21      |
                |   3    | Fione         |   10      | 
                |   4    | Marco         |   8       |
                ++++++++++++++++++++++++++++++++++++++
                
              """);
    }

    private static void printLivingRoom(){
        System.out.print("""
                
                        ┌─────┬─────┬─────┬─────┬─────┬─────┬─────┬─────┬─────┐
                     0  │     │     │     │     │     │     │     │     │     │
                        ├─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┤
                     1  │     │     │     │     │     │     │     │     │     │
                        ├─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┤
                     2  │     │     │     │     │     │     │     │     │     │
                        ├─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┤
                     3  │     │     │     │     │     │     │     │     │     │
                        ├─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┤
                     4  │     │     │     │     │     │     │     │     │     │
                        ├─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┤
                     5  │     │     │     │     │     │     │     │     │     │
                        ├─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┤
                     6  │     │     │     │     │     │     │     │     │     │
                        ├─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┤
                     7  │     │     │     │     │     │     │     │     │     │
                        ├─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┼─────┤               
                     8  │     │     │     │     │     │     │     │     │     │
                        └─────┴─────┴─────┴─────┴─────┴─────┴─────┴─────┴─────┘
                           0     1     2     3     4     5     6     7     8
                """);
    }

    //La variabile: activSplit mi tiene attivo il parametro di  separazione in modo che io possa fare un altro merge con un altra stringa
    private static String mergerString(String stringLeft, String stringRight, boolean activEndN, boolean activMerge){
        String merge = "";
        String endLine = activEndN ? "\n" : "#";
        int posL = stringLeft.indexOf("#");
        int posR = stringRight.indexOf("#");

        int lenLineL = Integer.parseInt(stringLeft.substring(0, posL)) - 1; //Il primo numero dappresenta la lunghezza della Linea, la decremento in quanto toglierò il parametro di divisione di questa stringa
        int lenLineR = Integer.parseInt(stringRight.substring(0, posR)) - 1;

        int startL = stringLeft.indexOf("#", ++posL);
        int startR = stringRight.indexOf("#", ++posR);

        int heightStrL = Integer.parseInt(stringLeft.substring(posL, startL++)); //Il secondo è quante linee ci sono
        int heightStrR = Integer.parseInt(stringRight.substring(posR, startR++));

        posL = stringLeft.indexOf("#", startL);
        posR = stringRight.indexOf(endLine, startR);

        int height = Integer.max(heightStrL, heightStrR);
        while( posL != -1 || posR != -1 ){
            if (heightStrL == heightStrR) {
                merge += stringLeft.substring(startL, posL++) + stringRight.substring(startR, ++posR);
                startR = posR;
                posR = stringRight.indexOf(endLine, startR);
                startL = posL;
                posL = stringLeft.indexOf("#", startL);
            } else if (heightStrL > heightStrR) {
                merge += stringLeft.substring(startL, posL++) + returnStringRepeat(" ", lenLineR) + endLine;
                startL = posL;
                posL = stringLeft.indexOf("#", startL);
                --heightStrL;
            } else {
                merge += returnStringRepeat(" ", lenLineL) + stringRight.substring(startR, ++posR);
                startR = posR;
                posR = stringRight.indexOf(endLine, startR);
                --heightStrR;
            }
        }
        if(activMerge) return (lenLineL + lenLineR + 1) + "#" + height + "#" +  merge;
        else return merge;
    }



    public static void add(StringBuilder old, String add){
        old.append(add).append("\tAGGIUNTO");
    }

    public static Shelf setShelf(){
        ThreadLocalRandom numRandom = ThreadLocalRandom.current();
        int positionRow; // Position of the tile (row) in which to search it.
        Shelf myShelf = new Shelf();
        Deck deck = new Deck();
        int numR;
        ArrayList<Integer> numRandomRow = new ArrayList<>(); // It will contain the number of boxes in a shelf column that should be left empty, for each row

        //To create the shelf, randomly, and so that it is not necessarily completely filled and print it on terminal
        for (int row = 0; row < myShelf.ROWS; row++) {
            for (int col = 0; col < myShelf.COLUMNS; col++) {
                if (row == 0) {
                    numRandomRow.add(numRandom.nextInt(0, 3));
                }
                numR = numRandomRow.get(col);
                if(numR == 0){
                    Couple couple = new Couple(deck.draw());
                    myShelf.setCoordinate(row, col, couple);
                }
                else {
                    numRandomRow.set(col, numR - 1);
                }
            }
        }
        return myShelf;
    }

    public static LivingRoom setLivingRoom(int numG){
        LivingRoom myLivingRoom = new LivingRoom(numG);
        return myLivingRoom;
    }


    public static void ClearConsole() {
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





    public static void main(String arg[]) {
        /*it.polimi.ingsw.view.DrawTui di = new it.polimi.ingsw.view.DrawTui(11, true, true);
        String nuova = ci.mergerString(ci.getStringCGC(), di.getStringCGC(), true, false, true);
        System.out.println(ci.getStringCGC());
        System.out.println(di.getStringCGC());
        System.out.print(nuova);*/
        /*Shelf my1 = setShelf();
        Shelf my3 = setShelf();
        String shelfS1 = ci.shelfString(my1, false, true);
        String shelfS3 = ci.shelfString(my3, true, true);
        String shelfT = ci.mergerString(shelfS1, shelfS3, true, false, true);
        System.out.print(shelfT);*/
/*
        new ClientTUI().startGame();
        LivingRoom myLR = setLivingRoom(2);
        String livingRoomS = ci.livingRoomString(myLR, false, true);
        Shelf my1 = setShelf();
        String shelf = ci.shelfString(my1, true, true);
        //System.out.println(livingRoomS);
        //System.out.println(shelf);
        String merg = ci.mergerString(livingRoomS, shelf, true, false,false);
        System.out.print(merg);

    -------------------------------------------------------------
        PersonalGoalCard myPGC = new PersonalGoalCard(6);
        System.out.print(DrawTui.setStringPCG(myPGC.getPositionTilePC(), 5, true, true));
        ArrayList<Couple> tessere = new ArrayList<>();
        Couple tessera1 = new Couple(new Tile(T_Type.TROPHY, 1));
        Couple tessera2 = new Couple(new Tile(T_Type.CAT, 1));
        //Couple tessera3 = new Couple(new Tile(T_Type.GAMES, 1));

        tessere.add(tessera1);
        tessere.add(tessera2);
        //tessere.add(tessera3);
        System.out.print(DrawTui.graphicsOrderTiles(tessere));

        String titolo = """
                                
                                
                          \033[38;5;228m#           #                                ######       ####                   ##        ######\033[0m
                        \033[38;5;227m##          ##                               ###    ##     ##   #                  ##      ##     ##\033[0m
                       \033[38;5;226m###         ###                                ##     ##   ##                       ##     ##        \033[0m
                      \033[38;5;220m####       ####    ####       ##                ##    #    ##             #####      ##    ###       #      #####\033[0m
                      \033[38;5;220m## ##     ## ##   ## ##      ####                ##       ##    ###     ###    ##    ##   #######   ###   ###    ##\033[0m
                      \033[38;5;11m##  ##   ##  ##       ##    ## ##         #       ##     ##   ##  ##   ##    ###     ##    ###      ##   ##    ###\033[0m
                     \033[38;5;214m##    ## ##    ##      ##   ##   ##       ##        ##    ## ##    ##   ######     #  ##    ##       ##   ######     #\033[0m
                    \033[38;5;172m###     ###     ###    ##  ##      ##       ###     ###    ###     ## #   ##       ##  ## #  ##       ##    ##       ##\033[0m
                  \033[38;5;208m####      #        ####   ####       ##         ######      ##      ####     ########    ###   ##      ####    ########     \033[0m
                                                       \033[38;5;202m##                                                        ##  \033[0m
                                                      \033[38;5;166m##                                                         ##\033[0m
                                           \033[38;5;196m#         ##                                                         ##\033[0m
                                            \033[38;5;160m##     ###                                                         #\033[0m
                                              \033[38;5;124m#####\033[0m
          
                """;

        String titolo2 = """
                          \033[38;5;228m#           #                                ######       ####                    ##           ######\033[0m                  \s
                        \033[38;5;227m##          ##                               ###    ##    ###   #                  ###         ##     ##\033[0m                   \s
                       \033[38;5;11m###         ###                               ###     ##  ###                       ###        ###\033[0m                           \s
                      \033[38;5;214m####       #####   ####       ###              ###    #   ###             #####      ###        ###      #       #####\033[0m         \s
                     \033[38;5;208m### ##     ## ###  ## ##      ####               ###      ###    ###     ####   ##    ###      #######   ###    ####   ##\033[0m       \s
                     \033[38;5;202m###  ##   ##  ###      ##    ## ###        #      ###    ###   ##  ##   ###   ###     ###        ###      ##   ###   ###\033[0m          \s
                     \033[38;5;9m###   ## ##   ###      ##   ###  ###      ##       ###   ### ##    ##   ######     #  ###        ###      ##   #######    #\033[0m      \s
                    \033[38;5;197m###     ####    ###    ##   ###    ###      ###     ###   ####     ## #   ###     ##    ###    #  ###      ##    ###     ##\033[0m       \s
                  \033[38;5;12m####       ##      ####   ######     ###        #######    ###      ####     ########      ######   ###     ####    ########\033[0m     \s
                                                       \033[38;5;14m###                                                            ###\033[0m                           \s
                                                      \033[38;5;51m###                                                             ###\033[0m                       \s
                                          \033[38;5;85m#          ###                                                              ###\033[0m                          \s
                                           \033[38;5;83m##      ####                                                               ##\033[0m                            \s
                                            \033[38;5;10m########                                                                 #\033[0m                          \s
                                
                                
                                
                """;

        System.out.print(titolo);
        System.out.print(titolo2);


 */

        ClientGUI clientGUI = new ClientGUI();
        clientGUI.chooseConnection();
    }

}
