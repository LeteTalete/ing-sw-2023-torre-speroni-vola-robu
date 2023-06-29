package it.polimi.ingsw.view;

import it.polimi.ingsw.model.board.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.structures.LivingRoomView;
import it.polimi.ingsw.structures.PlayerView;
import it.polimi.ingsw.structures.ShelfView;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DrawTui {
    static final String colorERROR = "\033[1;97;48;5;124m"; //colore di un messaggio di errore
    static final String colorRESET = "\033[0m";  // Reset Changes
    static final String colorTileG = "\033[1;30;48;5;214m"; //Orange
    static final String colorTileC = "\033[1;30;48;5;10m"; //Green
    static final String colorTileB = "\033[1;30;48;5;230m"; //White
    static final String colorTileP = "\033[1;30;48;5;13m"; //Magenta
    static final String colorTileF = "\033[1;30;48;5;27m"; //Blue
    static final String colorTileT = "\033[1;30;48;5;14m"; //Cyan
    static final String tileSquare = "\033[1;38;5;88;48;5;244m"; //"\033[1;51m"; Rappresenta la grafica di un rettangolino vuoto
    static final String[] boardSide = {"│","┌","└", "─", "┐", "┘", "┬", "┴", "├", "┼", "┤"};
    static final String equal = "="; //Per le CGC
    static final String diff = "≠";  //Per le CGC
    static final String empty = " "; //Spazio separatore
    static final String startLine = "  "; //è la parte di inizio di ogni riga di testo
    static final String dividNum = "#"; //dividLine  mi serve per dividere i parametri dell'altezza e lunghezza di una stringa da fondere con un'altra
    static final Integer sizeSlotTile = 3; //Tile size to be colored è meglio che sia dispari
    private final static PrintStream print = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private static String stringPGC = "";
    private static final ArrayList<String> stringCGC = new ArrayList<>();
    public static void printlnString(String string){
        print.println(string);
    }
    public static void askWhat(String whatString){
        print.println(whatString);
    }

    /**
     * Method graphicsOrderTiles is an auxiliary method that allows to graphically represent the tiles chosen by the player
     * and help him rearrange them in the order he prefers.
     * @param tiles - list of tiles chosen by the player
     */
    public static void graphicsOrderTiles(ArrayList<Couple> tiles){
        String board = boardSide[3].repeat(sizeSlotTile + 2);
        int numTiles = tiles.size();
        StringBuilder orderTiles = new StringBuilder(startLine + boardSide[1] + stringRepeat(board + boardSide[6], numTiles - 1) + board + boardSide[4] + "\n" + startLine);
        for(Couple tile: tiles){
            orderTiles.append(boardSide[0]).append(empty).append(slotTile(tile)).append(empty);
        }
        orderTiles.append(boardSide[0]).append("\n").append(startLine).append(boardSide[2]).append(stringRepeat(board + boardSide[7], numTiles - 1)).append(board).append(boardSide[5]).append("\n");
        orderTiles.append(startLine).append(empty.repeat(( sizeSlotTile + 4 ) / 2)).append(sequenceNumbers(1, numTiles + 1, ( sizeSlotTile / 2 ) * 2 + 3)).append("\n");
        printlnString(orderTiles.toString());
    }

    /**
     * Method graphicsShelf is an auxiliary method that helps to graphically represent the shelf of the player on the TUI.
     * @param myShelfView - shelf of the player.
     * @param name - name of the player.
     * @param activEnd //TODO : i don't know
     * @param activMerge //TODO : i don't know
     * @return //TODO : i don't know
     */
    public static String graphicsShelf(ShelfView myShelfView, String name, boolean activEnd, boolean activMerge){
        Couple[][] shelfView = myShelfView.getShelfsMatrixView();
        Iterator<Couple[]> board = Arrays.asList(shelfView).iterator();
        int numTileShelf = shelfView[0].length;
        String startDouble = startLine.repeat(2);
        String endLine = startDouble + activeEndLine(activEnd);
        String side = startDouble + boardSide[0].repeat(2) + stringRepeat(boardSide[3].repeat(sizeSlotTile) + boardSide[0].repeat(2), numTileShelf) + endLine; //la string che divide le strinche con le tile
        String roof = stringRepeat(boardSide[3].repeat(2 + sizeSlotTile), numTileShelf) + boardSide[3].repeat(2);
        StringBuilder shelfString = new StringBuilder();
        int lengthShelf = (startDouble + roof + endLine).length();
        int heigthSHelf = 3;
        addEmptySpaceToString(shelfString, startDouble + name, endLine, lengthShelf);
        shelfString.append(startDouble).append(roof).append(endLine);
        shelfString.append(side);
        while(board.hasNext()){
            shelfString.append(startDouble).append(boardSide[0].repeat(2));
            Couple[] rowShelf = board.next();
            for (Couple couple : rowShelf) {
                shelfString.append(slotTile(couple)).append(boardSide[0].repeat(2));
            }
            shelfString.append(endLine).append(side);
            heigthSHelf += 2;
        }
        heigthSHelf += 2;
        shelfString.append(startLine + empty + "/").append(roof).append("\\").append(empty).append(startLine).append(activeEndLine(activEnd));
        addEmptySpaceToString(shelfString,startDouble + stringRepeat(empty, sizeSlotTile/2 + 2) + sequenceNumbers(0, numTileShelf, 2 + (sizeSlotTile/2)*2), startDouble + activeEndLine(activEnd), lengthShelf);
        if(activMerge) return lengthShelf + dividNum + heigthSHelf + dividNum + shelfString;
        else return shelfString.toString();
    }

    /**
     * Method slotTile given a couple (from the board or shelf) returns a colored letter representing the type of the tile.
     * @param tile - couple of the board or shelf.
     * @return - colored letter representing the type of the tile.
     */
    private static String slotTile(Couple tile){
        if(tile.getState().equals(State.INVALID) || tile.getState().equals(State.EMPTY)){
            return stringRepeat(empty, sizeSlotTile);
        } else {
            T_Type typeTile = tile.getTile().getTileType();
            if(typeTile == T_Type.GAMES) {
                return colorTileG + " G " + colorRESET;
            } else if (typeTile == T_Type.CAT) {
                return colorTileC + " C " + colorRESET;
            } else if (typeTile == T_Type.BOOK) {
                return colorTileB + " B " + colorRESET;
            } else if (typeTile == T_Type.PLANT) {
                return colorTileP + " P " + colorRESET;
            } else if (typeTile == T_Type.TROPHY) {
                return colorTileT + " T " + colorRESET;
            } else {
                return colorTileF + " F " + colorRESET;
            }
        }
    }

    /**
     * Method getStringCGC given the number of the CGC it returns the graphic of the CGC.
     * @param numCGC - number of the CGC.
     * @return - graphic of the CGC.
     */
    public static String getStringCGC(int numCGC){
        return stringCGC.get(numCGC);
    }

    /**
     * Method setStringCGC sets the graphic of the CGC given the number of the CGC.
     * @param numCGC - number of the CGC.
     */
    public static void setStringCGC(int numCGC){
        stringCGC.add(graphicsCGC(numCGC, stringCGC.size() + 1));
    }

    /**
     * Method graphicsCGC graphically builds the CGC given the id of the CGC.
     * The numCG is used to know if it is the first or the second CGC.
     * @param idCG - CGC id.
     * @param numCG - CGC number (1 or 2).
     * @return - CGC graphic.
     */
    public static String graphicsCGC(int idCG, int numCG){
        String tileEqual = tileSquare + empty +  equal + empty + colorRESET;
        String tileDiff = tileSquare + empty + diff + empty + colorRESET;
        String whiteSlot = empty.repeat(sizeSlotTile);
        String tileEmpty = tileSquare + whiteSlot + colorRESET;
        int lenMaxString = sizeSlotTile*9;

        StringBuilder CGC = new StringBuilder();
        addEmptySpaceToString(CGC, "CommonGoalCard[" + numCG + "]:", dividNum, lenMaxString);
        int heightString = 1;
        switch (idCG) {
            case 0 -> {
                StringBuilder spaceWhite = new StringBuilder();
                for (int i = 0; i < 5; i++) {
                    addEmptySpaceToString(CGC, startLine + spaceWhite + tileEqual, dividNum, lenMaxString);
                    spaceWhite.append(whiteSlot);
                    ++heightString;
                }
            }
            case 1 -> {
                String board = stringRepeat(empty + boardSide[3], sizeSlotTile);
                String white = empty.repeat(11);
                for (int i = 0; i < 5; i++) {
                    if (i == 0 || i == 4) addEmptySpaceToString(CGC, startLine + tileEqual + board + empty + tileEqual, dividNum, lenMaxString);
                    else addEmptySpaceToString(CGC, startLine + "|" + white + "|", dividNum, lenMaxString);
                    ++heightString;
                }
            }
            case 2 -> tileCGColumn(CGC, tileEqual, 2, 2, lenMaxString, dividNum, false, 2, 0);
            case 3 -> {
                for (int i = 0; i < 3; i++) {
                    if (i == 1) addEmptySpaceToString(CGC, startLine + whiteSlot + tileEqual, dividNum, lenMaxString);
                    else addEmptySpaceToString(CGC, startLine + tileEqual + whiteSlot + tileEqual, dividNum, lenMaxString);
                    ++heightString;
                }
            }
            case 4 -> {
                for (int i = 1; i < 6; i++) {
                    addEmptySpaceToString(CGC, tileCGRow(tileEmpty, i, false, 0, 0), dividNum, lenMaxString);
                    ++heightString;
                }
            }
            case 5 -> tileCGColumn(CGC, tileEqual, 2, 1, lenMaxString, dividNum, true, 6, 0);
            case 6 -> tileCGColumn(CGC, tileEqual, 4, 1, lenMaxString, dividNum, true, 4, 0);
            case 7 -> {
                String whiteSpaceFull = empty.repeat(lenMaxString - 1);
                String whiteSpace = empty.repeat(2);
                addEmptySpaceToString(CGC, startLine.repeat(2) + tileEqual + whiteSlot + tileEqual, dividNum, lenMaxString);
                ++heightString;
                for (int i = 0; i < 2; i++) {
                    CGC.append(whiteSpaceFull).append(dividNum);
                    addEmptySpaceToString(CGC, startLine + tileEqual + whiteSpace + tileEqual + whiteSpace + tileEqual, dividNum, lenMaxString);
                    heightString += 2;
                }
            }
            case 8 -> tileCGColumn(CGC, tileEmpty, 6, 1, lenMaxString, dividNum, false, 3, 3);
            case 9 -> tileCGColumn(CGC, tileEmpty, 1, 5, lenMaxString, dividNum, false, 4, 3);
            case 10 -> tileCGColumn(CGC, tileDiff, 6, 1, lenMaxString, dividNum, false, 2, 0);
            case 11 -> tileCGColumn(CGC, tileDiff, 1, 5, lenMaxString, dividNum, false, 3, 0);
        }
        if( idCG == 0 || idCG == 1 || idCG == 3 || idCG == 4 || idCG == 7) CGC.insert(0,lenMaxString + dividNum + heightString + dividNum );
        return CGC.toString();
    }

    /**
     * Method tileCGRow is an auxiliary method of graphicsCGC //TODO: I don't know
     * @param tile - tile to be printed.
     * @param numTileForRow - //TODO: I don't know
     * @param dashedEdge //TODO: I don't know
     * @param repeat //TODO: I don't know
     * @param maxDif //TODO: I don't know
     * @return //TODO: I don't know
     */
    private static String tileCGRow(String tile, int numTileForRow, boolean dashedEdge, int repeat, int maxDif){
        StringBuilder stringRow = new StringBuilder(startLine);
        if(dashedEdge) stringRow.append("| ");
        for(int i = numTileForRow; i > 0; i--){
            stringRow.append(tile);
            if(i != 1) stringRow.append(boardSide[0]);
        }
        if(dashedEdge) stringRow.append(" |");
        if(maxDif > 0) stringRow.append("  Max ").append(maxDif).append(empty).append(tileSquare).append(empty).append(diff).append(empty).append(colorRESET);
        if(repeat > 1) stringRow.append(" X").append(repeat);
        return stringRow.toString();
    }

    //TODO: I don't know
    //maxLen: Lunghezza della riga per ogni carta
    //end: è la stringa che si dovrà stampare alla fine di ogni riga
    private static void tileCGColumn(StringBuilder cgc, String tile, int numTileForColumn, int numTileForRow, int maxLen, String endLine, boolean dashedEdge, int repet, int maxDif){
        String board = stringRepeat( empty + boardSide[3], numTileForRow*2);
        //aggiungo ad heightString un, in quanto considero anche la parte del tersto che mi dice quale commonGoalCArd sia
        //in caso lo tolgo devo ricordarmi di togliere 1 anche qua!
        int heightString = numTileForColumn + 1;
        int maxDifRow = 0;
        int ripetRow = 0;
        if(dashedEdge){//mette il contorno(Alto) tratteggiato alla carta
            addEmptySpaceToString(cgc,startLine + boardSide[1] + board + empty + boardSide[4], endLine, maxLen);
            heightString += 2;
        }
        //Mi permette di restituire una riga intera
        //maxDifRow: mi permette di salvare sulla stringa la parte del MAX
        //ripetRow: indica che sull'ultima riga della carta bisogna salvarsi il numero di volte che la si vuole salvare
        for(int i = numTileForColumn; i > 0; i--){
            if(i == 2 && maxDif > 0) maxDifRow = maxDif;
            if(i == 1) ripetRow = repet;
            addEmptySpaceToString(cgc, tileCGRow(tile, numTileForRow, dashedEdge, ripetRow, maxDifRow), endLine, maxLen);
            maxDifRow = 0;
        }
        if(dashedEdge){//mette il contorno(Basso) tratteggiato alla carta
            addEmptySpaceToString(cgc, startLine + boardSide[2] + board + empty + boardSide[5], endLine, maxLen);
        }
        if(maxDif > 0 && numTileForColumn < 2){
            addEmptySpaceToString(cgc, startLine + "Max " + maxDif + " " + tileSquare + empty + diff + empty + colorRESET, endLine, maxLen);
            ++heightString;
        }
        cgc.insert(0,maxLen + dividNum + heightString + dividNum);
    }

    /**
     * Method addEmptySpaceToString adds empty spaces to keep the string length constant.
     * @param stringOld - string to be modified.
     * @param add - //TODO: I don't know
     * @param end - //TODO: I don't know
     * @param mod - //TODO: I don't know
     */
    private static void addEmptySpaceToString(StringBuilder stringOld, String add, String end, int mod){
        stringOld.append(add);
        stringOld.append(stringRepeat(empty, mod - ( add.replaceAll("\033\\[[;\\d]*m", "").length() % mod) - end.length() ) ).append(end);
    }


    //TODO: I don't know
    //Mi serve per rimuovere la grafica di una stringa per sapere la sua vera lunghezza
    //Bisogna ricordarsi che la prima stringa deve avere il parametro divisore dividNum, mentre l'altra può anche non averlo settando activEndN
    //Mentre la entrambe devono avere la parte iniziale con i dati relativi alla lunghezza della linea e alla sua altezza
    //topAlign: per allineare in alto se è vero, se no allinea in basso le due stringhe

    public static String mergerString(String stringLeft, String stringRight, boolean activEndN, boolean activMerge, boolean topAlign){
        StringBuilder merge = new StringBuilder();
        String endLine = activeEndLine(activEndN);

        int posL = stringLeft.indexOf(dividNum);
        int posR = stringRight.indexOf(dividNum);

        int lenLineL = Integer.parseInt(stringLeft.substring(0, posL)) - 1; //Il primo numero dappresenta la lunghezza della Linea, la decremento in quanto toglierò il parametro di divisione di questa stringa
        int lenLineR = Integer.parseInt(stringRight.substring(0, posR)) - 1;

        int startL = stringLeft.indexOf(dividNum, ++posL);
        int startR = stringRight.indexOf(dividNum, ++posR);

        int heightStrL = Integer.parseInt(stringLeft.substring(posL, startL++)); //Il secondo è quante linee ci sono
        int heightStrR = Integer.parseInt(stringRight.substring(posR, startR++));

        posL = stringLeft.indexOf(dividNum, startL);
        posR = stringRight.indexOf(endLine, startR);
        int height = Integer.max(heightStrL, heightStrR); //Altezza della nuova stringa che sarà uguale all'altezza massima delle due stringhe
        while(heightStrL != 0 || heightStrR != 0){
            if (topAlign || heightStrL == heightStrR ){
                merge.append(stringLeft, startL, posL++).append(stringRight, startR, ++posR);
                startR = posR;
                posR = stringRight.indexOf(endLine, startR);
                startL = posL;
                posL = stringLeft.indexOf(dividNum, startL);
                --heightStrL;
                --heightStrR;
                if(heightStrL == 0 || heightStrR == 0) topAlign = false;
            } else if (heightStrL > heightStrR) {
                merge.append(stringLeft, startL, posL++).append(stringRepeat(empty, lenLineR)).append(endLine);
                startL = posL;
                posL = stringLeft.indexOf(dividNum, startL);
                --heightStrL;
            } else {
                merge.append(stringRepeat(empty, lenLineL)).append(stringRight, startR, ++posR);
                startR = posR;
                posR = stringRight.indexOf(endLine, startR);
                --heightStrR;
            }
        }
        if(activMerge) return (lenLineL + lenLineR + 1) + dividNum + height + dividNum +  merge;
        else return merge.toString();
    }

    /**
     * Method graphicsLivingRoom graphically builds the living room on the TUI.
     * @param livingRoomView - the living room.
     * @param activeEnd //TODO: I don't know
     * @param activeMerge //TODO: I don't know
     * @return //TODO: I don't know
     */
    public static String graphicsLivingRoom(LivingRoomView livingRoomView, boolean activeEnd, boolean activeMerge){
        Couple[][] livingRoom = livingRoomView.getBoard();
        int numColum = 0;
        int numTileLine = livingRoom[0].length; //è il numero di quante tile ci sono per riga
        String endLine = startLine.repeat(2) + activeEndLine(activeEnd); //parte finale della linea
        String boardHoriz = stringRepeat(boardSide[3], sizeSlotTile + 2);
        String sideLR = startLine + empty + startLine + boardSide[8] + stringRepeat(boardHoriz + boardSide[9], numTileLine - 1 ) + boardHoriz + boardSide[10] + endLine;   //Parte divisoria della livingRoom
        StringBuilder livingRoomString = new StringBuilder(startLine + empty + startLine + boardSide[1] + stringRepeat(boardHoriz + boardSide[6], numTileLine - 1) + boardHoriz + boardSide[4] + endLine); //Testa della livingRoom
        int lengthLine = livingRoomString.length();
        int heightLR = 1;
        for(int row = 0; row < numTileLine; row++){
            livingRoomString.append(startLine).append(numColum).append(startLine).append(boardSide[0]);
            for (int colum = 0; colum < livingRoom.length; colum++) {
                livingRoomString.append(empty).append(slotTile(livingRoom[row][colum])).append(empty).append(boardSide[0]);
            }
            livingRoomString.append(endLine);
            if(row != numTileLine - 1) livingRoomString.append(sideLR);
            else livingRoomString.append(startLine + empty + startLine).append(boardSide[2]).append(stringRepeat(boardHoriz + boardSide[7], numTileLine - 1)).append(boardHoriz).append(boardSide[5]).append(endLine);
            heightLR += 2;
            ++numColum;
        }
        //Ultima righa che raffigura i numeri della colonna
        addEmptySpaceToString(livingRoomString,startLine + empty + startLine + stringRepeat(empty, sizeSlotTile) + sequenceNumbers(0, numTileLine,3 + (sizeSlotTile/2)*2), endLine,lengthLine);
        ++heightLR;
        if(activeMerge) return lengthLine + dividNum + heightLR + dividNum + livingRoomString;
        else return livingRoomString.toString();
    }

    //TODO: I don't know
    //mi restituisce una sequenza dei numeri da start fino a end (non compreso), separati tutti dallo spazio vuoto, ripetuto un certo numero
    private static String sequenceNumbers(int start, int end, int spaceDivisor){
        StringBuilder string = new StringBuilder();
        String space = stringRepeat(empty, spaceDivisor );
        for(int i = start; i < end; i++){
            string.append(i);
            if(i != end - 1) string.append(space);
        }
        return string.toString();
    }

    /**
     * Method setStringPCG graphically builds the personal goal card on the TUI.
     * @param positionTilePCG - the position of the tiles of the personal goal card.
     * @param lenMaxColumn - the number of columns that make up the personal goal card.
     */
    public static void setStringPCG(ArrayList<String> positionTilePCG, int lenMaxColumn){
        String endLine = startLine + "\n";
        StringBuilder pcg = new StringBuilder();
        String tileS;
        String tileEmty = empty.repeat(sizeSlotTile) + boardSide[0];
        String board = stringRepeat(boardSide[3], (sizeSlotTile + 1)*lenMaxColumn + 3);
        int lengthLine = (startLine + boardSide[1] + board + boardSide[4] + endLine).length();
        addEmptySpaceToString(pcg,  startLine + "PersonalGoalCard:", endLine, lengthLine);
        pcg.append(startLine).append(boardSide[1]).append(board).append(boardSide[4]).append(endLine);
        pcg.append(startLine).append(boardSide[0]).append(empty).append(boardSide[1]).append(stringRepeat(boardSide[3].repeat(sizeSlotTile) + boardSide[6], lenMaxColumn - 1)).append(boardSide[3].repeat(sizeSlotTile)).append(boardSide[4]).append(empty).append(boardSide[0]).append(endLine);
        int heightPCG = 8;
        int lenStringTile; //Se la lunghezza della stringa non è maggiore di 1 significa che non contiene tile in quella posizione

        for (String s : positionTilePCG) {
            pcg.append(startLine).append(boardSide[0]).append(empty).append(boardSide[0]);
            tileS = s;
            lenStringTile = tileS.length();
            int oldPosT = 0;
            if (lenStringTile > 1) {
                String[] tilesInRow = tileS.split(":"); //contiene le informazioni sulla posizione di ogni tile presenti nella riga di riferimento
                int positionTile; //Posizione delle di una tile contenuta in una riga

                for (int index = 0; index < tilesInRow.length; index += 2) {
                    positionTile = Integer.parseInt(tilesInRow[index]);
                    pcg.append(stringRepeat(tileEmty, positionTile - oldPosT));
                    if (Objects.equals(tilesInRow[index + 1], "G")) {
                        pcg.append(colorTileG + " G " + colorRESET);
                    } else if (Objects.equals(tilesInRow[index + 1], "P")) {
                        pcg.append(colorTileP + " P " + colorRESET);
                    } else if (Objects.equals(tilesInRow[index + 1], "C")) {
                        pcg.append(colorTileC + " C " + colorRESET);
                    } else if (Objects.equals(tilesInRow[index + 1], "F")) {
                        pcg.append(colorTileF + " F " + colorRESET);
                    } else if (Objects.equals(tilesInRow[index + 1], "T")) {
                        pcg.append(colorTileT + " T " + colorRESET);
                    } else {
                        pcg.append(colorTileB + " B " + colorRESET);
                    }
                    oldPosT = positionTile + 1;
                    ++heightPCG;
                    pcg.append(boardSide[0]);
                }
            }
            pcg.append(stringRepeat(tileEmty, lenMaxColumn - oldPosT)).append(empty).append(boardSide[0]).append(endLine);
        }
        pcg.append(startLine).append(boardSide[0]).append(empty).append(boardSide[2]).append(stringRepeat(boardSide[3].repeat(sizeSlotTile) + boardSide[7], lenMaxColumn - 1)).append(boardSide[3].repeat(sizeSlotTile)).append(boardSide[5]).append(empty).append(boardSide[0]).append(endLine);
        pcg.append(startLine).append(boardSide[8]).append(board).append(boardSide[10]).append(endLine);
        pcg.append(startLine).append(boardSide[0]).append(" 1 | 2 | 3 | 4 | 5 | 6 ").append(boardSide[0]).append(endLine);
        pcg.append(startLine).append(boardSide[0]).append(" 1 | 2 | 4 | 6 | 9 |12 ").append(boardSide[0]).append(endLine);
        pcg.append(startLine).append(boardSide[2]).append(board).append(boardSide[5]).append(endLine);
        stringPGC = lengthLine + dividNum + heightPCG + dividNum + pcg;

    }

    /**
     * Method getStringPCG returns the string of the personal goal card.
     * @return - the string of the personal goal card.
     */
    public static String getStringPCG(){
        return stringPGC;
    }

    /**
     * Method activeEndLine returns "\n" if activeEnd is true, otherwise it returns dividNum.
     * @param activeEnd - //TODO : I don't know
     * @return - "\n" if activeEnd is true, otherwise it returns dividNum.
     */
    private static String activeEndLine(boolean activeEnd){
        return activeEnd ? "\n" : dividNum;
    }

    //TODO: I don't know
    //ritorna la stringa ripetuta un certo numero di volte
    private static String stringRepeat(String text, int repeatNum){
        return text.repeat(repeatNum);
    }

    /** Method printTitle prints the title of the game. */
    public static void printTitle(){
        //TODO: we still need to fix the color of the title
        print.print("""
                          \033[1;38;5;228m#           #                                ######       ####                    ##           ######
                        \033[39;38;5;227m##          ##                               ###    ##    ###   #                  ###         ##     ##
                       \033[38;5;11m###         ###                               ###     ##  ###                       ###        ###
                      \033[38;5;214m####       #####   ####       ###              ###    #   ###             #####      ###        ###      #       #####
                     \033[38;5;208m### ##     ## ###  ## ##      ####               ###      ###    ###     ####   ##    ###      #######   ###    ####   ##
                     \033[38;5;202m###  ##   ##  ###      ##    ## ###        #      ###    ###   ##  ##   ###   ###     ###        ###      ##   ###   ###
                     \033[38;5;9m###   ## ##   ###      ##   ###  ###      ##       ###   ### ##    ##   ######     #  ###        ###      ##   #######    #
                    \033[38;5;197m###     ####    ###    ##   ###    ###      ###     ###   ####     ## #   ###     ##    ###    #  ###      ##    ###     ##
                  \033[38;5;12m####       ##      ####   ######     ###        #######    ###      ####     ########      ######   ###     ####    ########
                                                       \033[38;5;14m###                                                            ###
                                                      \033[38;5;51m###                                                             ###
                                          \033[38;5;85m#          ###                                                              ###
                                           \033[38;5;83m##      ####                                                               ##
                                            \033[38;5;10m########                                                                 #\033[0m
                                
        """);
    }

    /**
     * Method endGameScore prints the scoreboard with the points of the players at the end of the game.
     * If the player has won it will graphically print WINNER, otherwise it will print LOSER.
     * @param namePlayerClient - the username of the player on the client.
     * @param players - the list of player currently playing the game.
     * @return - the textual graphic and the scoreboard.
     */
    public static String endGameScore(String namePlayerClient, ArrayList<PlayerView> players){
        String winner = """
                  ####     ####        ##   #####    ##   #####               #    ####
                  ###       ###   #     # ##    ##    # ##    ##      #####   ## ##    ##
                   ##   #   ###  ###    ##      ###   ##      ###   ###  ###   ###      #
                    ## ### ###    ##    ##      ###   ##      ###  ######      ###
                     ###  ###     ##    ##      ###   ##      ###   ##    ##   ###
                      #    #     ####   ##     ###    ##     ###     ######   ###
                """;
        String loser = """
                   ##                    ####             #    ####
                  ###       #####      ###   #    #####   ## ##    ##
                  ###      ###   ##     ###     ###  ###   ###      #
                  ###     ###     ##     ###   ######      ###
                  ###   #  ###   ##  #    ###   ##    ##   ###
                   #####    ######    #####      ######   ###
                """;
        StringBuilder tabScore = new StringBuilder(startLine + "  Players                            Score\n" + startLine + "#################################  ##########\n");
        int len = players.size();
        String color;
        String title = "";
        for(int i = 0; i < len; i++){
            if(i == 0) color = "\033[1;30;48;5;46m";
            else if(i ==  1) color = "\033[1;30;48;5;214m";
            else if(i == 2) color = "\033[1;30;48;5;208m";
            else color = "\033[1;30;48;5;196m";
            String name = players.get(i).getNickname();
            if(namePlayerClient.equals(name)){
                if(i == 0) title = "\033[1;38;5;46m" + winner;
                else if(i == 1) title = "\033[1;38;5;214m" + loser;
                else if( i == 2) title = "\033[1;38;5;208m" + loser;
                else title = "\033[1;38;5;196m" + loser;
                title += colorRESET;
            }
            String score = Integer.toString(players.get(i).getScore());
            tabScore.append(startLine).append(color).append("  ").append(name).append(" ".repeat(31 - name.length())).append(colorRESET).append("  ").append(color).append("  ").append(score).append(" ".repeat(8 - score.length())).append(colorRESET).append("\n").append(startLine);
            if(i + 1 == len) tabScore.append("#################################  ##########\n");
            else tabScore.append("---------------------------------  ----------\n");
        }
        return title + tabScore;

    }

    //TODO : I don't know
    //il parametro activeDelimit permette di inserire alla fine un carattere separatore in modo che il punteggio di questa carta sia separata dall'altra carta
    //il parametro activeEndN attiva il paramentro di separazione
    public static String graphicsToken(int numToken, boolean activeEndN){
        String endLine = activeEndN ? "\n": startLine.repeat(10) + dividNum;
        String roof = startLine + boardSide[1] + boardSide[3].repeat(5) +  boardSide[4] + endLine;
        return roof.length() + dividNum + 4 + dividNum +  startLine + "Token: " + endLine + roof +
                startLine + boardSide[0] + empty + "[" + numToken + "]" + empty + boardSide[0] + endLine +
                startLine + boardSide[2] + boardSide[3].repeat(5) + boardSide[5] + endLine;
    }



}
