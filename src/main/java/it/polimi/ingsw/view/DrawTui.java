package it.polimi.ingsw.view;

import it.polimi.ingsw.model.enumerations.Couple;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.T_Type;
import it.polimi.ingsw.structures.LivingRoomView;
import it.polimi.ingsw.structures.ShelfView;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DrawTui {
    static final String colorRESET = "\033[0m";  // Reset Changes
    static final String colorTileG = "\033[1;30;48;5;214m"; //Orange
    static final String colorTileC = "\033[1;30;48;5;10m"; //Green
    static final String colorTileB = "\033[1;30;48;5;230m"; //White
    static final String colorTileP = "\033[1;30;48;5;13m"; //Magenta
    static final String colorTileF = "\033[1;30;48;5;27m"; //Blue
    static final String colorTileT = "\033[1;30;48;5;14m"; //Cyan
    static final String colorTitle = "\033[38;5;11m"; //Yellow
    static final String colorTilePicked = "\033[1;97;48;5;124m"; //Red tile, White text
    static final String tileSquare = "\033[1;51m"; //Rappresenta la grafica di un rettangolino vuoto
    static final String[] boardSide = {"│","┌","└", "─", "┐", "┘", "┬", "┴", "├", "┼", "┤"};
    static final String equal = "="; //Per le CGC
    static final String diff = "≠";  //Per le CGC
    static final String empty = " "; //Spazio separatore
    static final String startLine = "  "; //è la parte di inizio di ogni riga di testo
    static final String dividNum = "#"; //dividLine  mi serve per dividere i parametri dell'altezza e lunghezza di una stringa da fondere con un'altra
    static final Integer sizeSlotTile = 3; //Tile size to be colored è meglio che sia dispari
    private static ArrayList<String> stringCGC = new ArrayList<>();
    private final static PrintStream print = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private final static Scanner scanner = new Scanner(System.in);
    private static String stringPGC;

    public static void printlnString(String string){
        print.println(string);
    }
    public static void printString(String string){
        print.print(string);
    }
    public static void askWhat(String whatString){
        print.print(whatString + "\n#: ");
    }
    /*

    static int getPositionTiles(){
        String tiles;
        int[] positionT;
        do{
            position =  getNextLine();
            position.
        }
        return;
    }

     */
    static String getNextLine() {
        return scanner.nextLine();
    }

    public static String graphicsShelf(ShelfView myShelfView, boolean activEnd, boolean activMerge){
        Couple[][] shelfView = myShelfView.getShelfsMatrixView();
        Iterator<Couple[]> board = Arrays.asList(shelfView).iterator();
        int numTileShelf = shelfView[0].length;
        String startDouble = startLine.repeat(2);
        String endLine = startDouble + activEndLine(activEnd);
        String side = startDouble + boardSide[0].repeat(2) + stringRepeat(boardSide[3].repeat(sizeSlotTile) + boardSide[0].repeat(2), numTileShelf) + endLine; //la string che divide le strinche con le tile
        String roof = stringRepeat(boardSide[3].repeat(2 + sizeSlotTile), numTileShelf) + boardSide[3].repeat(2);
        StringBuilder shelfString = new StringBuilder(startDouble + roof + endLine);
        int lengthShelf = shelfString.length();
        int heigthSHelf = 2;
        shelfString.append(side);
        while(board.hasNext()){
            shelfString.append(startDouble + boardSide[0].repeat(2));
            Couple[] rowShelf = board.next();
            for (Couple couple : rowShelf) {
                shelfString.append(slotTile(couple) + boardSide[0].repeat(2));
            }
            shelfString.append(endLine + side);
            heigthSHelf += 2;
        }
        heigthSHelf += 2;
        shelfString.append(startLine + empty + "/" + roof + "\\" + empty + startLine + activEndLine(activEnd) );
        addEmptySpaceToString(shelfString,startDouble + stringRepeat(empty, sizeSlotTile/2 + 2) + sequenceNumbers(0, numTileShelf, 2 + (sizeSlotTile/2)*2), startDouble + activEndLine(activEnd), lengthShelf);
        if(activMerge) return lengthShelf + dividNum + heigthSHelf + dividNum + shelfString;
        else return shelfString.toString();
    }

    //Ritorna la stringa della tile specifica a seconda della tipologia della Tile letta
    private static String slotTile(Couple tile){
        if(tile.getState().equals(State.INVALID) || tile.getState().equals(State.EMPTY) || tile.getState().equals(State.EMPTY_AND_UNUSABLE) ){
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

    public static void setStringCGC(ArrayList<Integer> idCGC){
        idCGC.forEach((n) -> stringCGC.add(graphicsCGC(n, false, true)));
    }


    public static String graphicsCGC(int numCG, boolean activEnd, boolean activMerge){
        String tileEqual = tileSquare + empty +  equal + empty + colorRESET;
        String tileDiff = tileSquare + empty + diff + empty + colorRESET;
        String whiteSlot = empty.repeat(sizeSlotTile);
        String tileEmpty = tileSquare + whiteSlot + colorRESET;
        int lenMaxString = sizeSlotTile*9; //Dimensione del rettangolo della stringa

        StringBuilder CGC = new StringBuilder();
        String endS = activEndLine(activEnd);
        addEmptySpaceToString(CGC, "CommonGoalCard[" + numCG + "]:", endS, lenMaxString);
        int heightString = 1; //altezza della Linea
        switch (numCG) {
            case 1 -> tileCGColumn(CGC, tileEqual, 2, 2, lenMaxString, endS, false, 2, 0);
            case 2 -> tileCGColumn(CGC, tileDiff, 6, 1, lenMaxString, endS, false, 2, 0);
            case 3 -> tileCGColumn(CGC, tileEqual, 4, 1, lenMaxString, endS, true, 4, 0);
            case 4 -> tileCGColumn(CGC, tileEqual, 2, 1, lenMaxString, endS, true, 6, 0);
            case 5 -> tileCGColumn(CGC, tileEmpty, 6, 1, lenMaxString, endS, false, 3, 3);
            case 6 -> tileCGColumn(CGC, tileDiff, 1, 5, lenMaxString, endS, false, 3, 0);
            case 7 -> tileCGColumn(CGC, tileEmpty, 1, 5, lenMaxString, endS, false, 4, 3);
            case 8 -> {
                String board = stringRepeat(empty + boardSide[3], sizeSlotTile);
                String white = empty.repeat(11);
                for (int i = 0; i < 5; i++) {
                    if (i == 0 || i == 4) addEmptySpaceToString(CGC, startLine + tileEqual + board + empty + tileEqual, endS, lenMaxString);
                    else addEmptySpaceToString(CGC, startLine + "|" + white + "|", endS, lenMaxString);
                    ++heightString;
                }
            }
            case 9 -> {
                String whiteSpaceFull = empty.repeat(lenMaxString - 1);
                String whiteSpace = empty.repeat(2);
                addEmptySpaceToString(CGC, startLine.repeat(2) + tileEqual + whiteSlot + tileEqual, endS, lenMaxString);
                ++heightString;
                for (int i = 0; i < 2; i++) {
                    CGC.append(whiteSpaceFull).append(endS);
                    addEmptySpaceToString(CGC, startLine + tileEqual + whiteSpace + tileEqual + whiteSpace + tileEqual, endS, lenMaxString);
                    heightString += 2;
                }
            }
            case 10 -> {
                for (int i = 0; i < 3; i++) {
                    if (i == 1) addEmptySpaceToString(CGC, startLine + whiteSlot + tileEqual, endS, lenMaxString);
                    else addEmptySpaceToString(CGC, startLine + tileEqual + whiteSlot + tileEqual, endS, lenMaxString);
                    ++heightString;
                }
            }
            case 11 -> {
                String spaceWhite = "";
                for (int i = 0; i < 5; i++) {
                    addEmptySpaceToString(CGC, startLine + spaceWhite + tileEqual, endS, lenMaxString);
                    spaceWhite += whiteSlot;
                    ++heightString;
                }
            }
            case 12 -> {
                for (int i = 1; i < 6; i++) {
                    addEmptySpaceToString(CGC, tileCGRow(tileEmpty, i, false, 0, 0), endS, lenMaxString);
                    ++heightString;
                }
            }
        }
        if(numCG > 7) CGC.insert(0,lenMaxString + dividNum + heightString + dividNum );
        if(!activMerge){
            CGC.delete(0, CGC.indexOf(dividNum) + 1);
            CGC.delete(0, CGC.indexOf(dividNum) + 1);
        }
        return  CGC.toString();
    }

    private static String tileCGRow(String tile, int numTileForRow, boolean dashedEdge, int repet, int maxDif){
        String stringRow = startLine;
        if(dashedEdge) stringRow += "| ";
        for(int i = numTileForRow; i > 0; i--){
            stringRow += tile;
            if(i != 1) stringRow += boardSide[0];
        }
        if(dashedEdge) stringRow += " |";
        if(maxDif > 0) stringRow += "  Max " + maxDif + empty + tileSquare + empty + diff + empty + colorRESET;
        if(repet > 1) stringRow += " X" + repet;
        return stringRow;
    }

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

    //Con questo funzione aggiungo
    private static void addEmptySpaceToString(StringBuilder stringOld, String add, String end, int mod){
        stringOld.append(add);
        stringOld.append(stringRepeat(empty, mod - ( add.replaceAll("\033\\[[;\\d]*m", "").length() % mod) - end.length() ) ).append(end);
    }



    //Mi serve per rimuovere la grafica di una stringa per sapere la sua vera lunghezza
    //Bisogna ricordarsi che la prima stringa deve avere il parametro divisore dividNum, mentre l'altra può anche non averlo settando activEndN
    //Mentre la entrambe devono avere la parte iniziale con i dati relativi alla lunghezza della linea e alla sua altezza
    //topAlign: per allineare in alto se è vero, se no allinea in basso le due stringhe

    public static String mergerString(String stringLeft, String stringRight, boolean activEndN, boolean activMerge, boolean topAlign){
        String merge = "";
        String endLine = activEndLine(activEndN);

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
                merge += stringLeft.substring(startL, posL++) + stringRight.substring(startR, ++posR);
                startR = posR;
                posR = stringRight.indexOf(endLine, startR);
                startL = posL;
                posL = stringLeft.indexOf(dividNum, startL);
                --heightStrL;
                --heightStrR;
                if(heightStrL == 0 || heightStrR == 0) topAlign = false;
            } else if (heightStrL > heightStrR) {
                merge += stringLeft.substring(startL, posL++) + stringRepeat(empty, lenLineR) + endLine;
                startL = posL;
                posL = stringLeft.indexOf(dividNum, startL);
                --heightStrL;
            } else {
                merge += stringRepeat(empty, lenLineL) + stringRight.substring(startR, ++posR);
                startR = posR;
                posR = stringRight.indexOf(endLine, startR);
                --heightStrR;
            }
        }
        if(activMerge) return (lenLineL + lenLineR + 1) + dividNum + height + dividNum +  merge;
        else return merge;
    }

    public static String graphicsLivingRoom(LivingRoomView livingRoomView, boolean activEnd, boolean activMerge){
        Couple[][] livingRoom = livingRoomView.getBoard();
        int numColum = 0;
        int numTileLine = livingRoom[0].length; //è il numero di quante tile ci sono per riga
        String endLine = startLine.repeat(2) + activEndLine(activEnd); //parte finale della linea
        String boardHoriz = stringRepeat(boardSide[3], sizeSlotTile + 2);
        String sideLR = startLine + empty + startLine + boardSide[8] + stringRepeat(boardHoriz + boardSide[9], numTileLine - 1 ) + boardHoriz + boardSide[10] + endLine;   //Parte divisoria della livingRoom
        StringBuilder livingRoomString = new StringBuilder(startLine + empty + startLine + boardSide[1] + stringRepeat(boardHoriz + boardSide[6], numTileLine - 1) + boardHoriz + boardSide[4] + endLine); //Testa della livingRoom
        int lengthLine = livingRoomString.length();
        int heightLR = 1;
        for(int row = 0; row < numTileLine; row++){
            livingRoomString.append(startLine + numColum + startLine + boardSide[0]);
            for (int colum = 0; colum < livingRoom.length; colum++) {
                livingRoomString.append(empty + slotTile(livingRoom[row][colum]) + empty + boardSide[0]);
            }
            livingRoomString.append(endLine);
            if(row != numTileLine - 1) livingRoomString.append(sideLR);
            else livingRoomString.append(startLine + empty + startLine + boardSide[2] + stringRepeat(boardHoriz + boardSide[7],numTileLine - 1) + boardHoriz + boardSide[5] + endLine);
            heightLR += 2;
            ++numColum;
        }
        //Ultima righa che raffigura i numeri della colonna
        addEmptySpaceToString(livingRoomString,startLine + empty + startLine + stringRepeat(empty, sizeSlotTile) + sequenceNumbers(0, numTileLine,3 + (sizeSlotTile/2)*2), endLine,lengthLine);
        ++heightLR;
        if(activMerge) return lengthLine + dividNum + heightLR + dividNum + livingRoomString;
        else return livingRoomString.toString();
    }

    //mi restituisce una sequenza dei numeri da start fino a end (non compreso), separati tutti dallo spazio vuoto, ripetuto un certo numero
    private static String sequenceNumbers(int start, int end, int spaceDivisor){
        String string = "";
        String space = stringRepeat(empty, spaceDivisor );
        for(int i = start; i < end; i++){
            string += i;
            if(i != end - 1) string += space;
        }
        return string;
    }

    //lenMaxColumn: rappresenta il numero di colonne che compongono la personalGoalCard
    //positionTilePCG: rappresenta il numero di tile

    public static String setStringPCG(ArrayList<String> positionTilePCG, int lenMaxColumn, boolean activEndN, boolean activMerge){
        /*PersonalGoalCard:
         ┌───────────────────────┐
         │ ┌───┬───┬───┬───┬───┐ │
         │ │   │   │   │   │   │ │
         │ │   │   │   │   │   │ │
         │ │   │   │   │   │   │ │
         │ │   │   │   │   │   │ │
         │ │   │   │   │   │   │ │
         │ │   │   │   │   │   │ │
         │ └───┴───┴───┴───┴───┘ │
         ├───────────────────────┤
         │ 1 | 2 | 3 | 4 | 5 | 6 │
         │ 1 | 2 | 4 | 6 | 9 |12 │
         └───────────────────────┘
         */
        StringBuilder pcg = new StringBuilder();
        String tileS = "";
        String tileEmty = empty.repeat(sizeSlotTile) + boardSide[0];
        String endLine = startLine + activEndLine(activEndN);
        String board = stringRepeat(boardSide[3], (sizeSlotTile + 1)*lenMaxColumn + 3);

        pcg.append(startLine + boardSide[1] + board + boardSide[4] + endLine);
        int lengthLine = pcg.length();
        pcg.append(startLine + boardSide[0] + empty + boardSide[1] + stringRepeat(boardSide[3].repeat(sizeSlotTile) + boardSide[6], lenMaxColumn - 1) + boardSide[3].repeat(sizeSlotTile) + boardSide[4] + empty + boardSide[0] + endLine);

        int heightPCG = 2;
        int lenStringTile; //Se la lunghezza della stringa non è maggiore di 1 significa che non contiene tile in quella posizione

        for(int i = 0; i < positionTilePCG.size(); i++){
            pcg.append(startLine + boardSide[0] + empty + boardSide[0]);
            tileS = positionTilePCG.get(i);
            lenStringTile = tileS.length();
            int oldPosT = 0;
            if(lenStringTile > 1){
                String[] tilesInRow = tileS.split(":"); //contiene le informazioni sulla posizione di ogni tile presenti nella riga di riferimento
                int positionTile; //Posizione delle di una tile contenuta in una riga

                for(int index = 0; index < tilesInRow.length; index += 2 ) {
                    positionTile = Integer.parseInt(tilesInRow[index]);
                    pcg.append(stringRepeat(tileEmty, positionTile - oldPosT));

                    if (Objects.equals(tilesInRow[index + 1], "G")) {
                        pcg.append(colorTileG + " G " + colorRESET);
                    } else if (Objects.equals(tilesInRow[index + 1],"P")) {
                        pcg.append(colorTileP + " P " + colorRESET);
                    } else if (Objects.equals(tilesInRow[index + 1],"C")) {
                        pcg.append(colorTileC + " C " + colorRESET);
                    } else if (Objects.equals(tilesInRow[index + 1],"F")) {
                        pcg.append(colorTileF + " F " + colorRESET);
                    } else if (Objects.equals(tilesInRow[index + 1],"T"))  {
                        pcg.append(colorTileT + " T " + colorRESET);
                    } else {
                        pcg.append(colorTileB + " B " + colorRESET);
                    }
                    oldPosT = positionTile + 1;
                    ++heightPCG;
                    pcg.append(boardSide[0]);
                }
            }
            pcg.append(stringRepeat(tileEmty, lenMaxColumn - oldPosT) + empty + boardSide[0] + endLine);
        }
        pcg.append(startLine + boardSide[0] + empty + boardSide[2] + stringRepeat(boardSide[3].repeat(sizeSlotTile) + boardSide[7], lenMaxColumn - 1) + boardSide[3].repeat(sizeSlotTile) + boardSide[5] + empty + boardSide[0] + endLine);
        pcg.append(startLine + boardSide[8] + board + boardSide[10] + endLine);
        pcg.append(startLine + boardSide[0] + " 1 | 2 | 3 | 4 | 5 | 6 " +  boardSide[0] + endLine);
        pcg.append(startLine + boardSide[0] + " 1 | 2 | 4 | 6 | 9 |12 " +  boardSide[0] + endLine);
        pcg.append(startLine + boardSide[2] + board + boardSide[5] + endLine);
        return pcg.toString();
    }

    //mette '\n' se vero se no mette dividNum
    private static String activEndLine(boolean activEnd){
        return activEnd ? "\n" : dividNum;
    }
    //ritorna la stringa ripetuta un certo numero di volte
    private static String stringRepeat(String text, int repeatNum){
        return text.repeat(repeatNum);
    }

}
