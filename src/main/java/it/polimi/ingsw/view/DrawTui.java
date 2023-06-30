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
    static final String colorERROR = "\033[1;97;48;5;124m"; //color of an error message
    static final String colorRESET = "\033[0m";  //Reset Changes
    static final String colorTileG = "\033[1;30;48;5;214m"; //Orange
    static final String colorTileC = "\033[1;30;48;5;10m"; //Green
    static final String colorTileB = "\033[1;30;48;5;230m"; //White
    static final String colorTileP = "\033[1;30;48;5;13m"; //Magenta
    static final String colorTileF = "\033[1;30;48;5;27m"; //Blue
    static final String colorTileT = "\033[1;30;48;5;14m"; //Cyan
    static final String tileSquare = "\033[1;38;5;88;48;5;244m"; //It represents the graphic of an empty rectangle
    static final String[] boardSide = {"│","┌","└", "─", "┐", "┘", "┬", "┴", "├", "┼", "┤"};
    static final String equal = "=";
    static final String diff = "≠";
    static final String empty = " ";
    static final String startLine = "  "; //beginning part of each line of text
    static final String dividNum = "#"; //Used to split the height and length parameters of one string to be merged with another
    static final Integer sizeSlotTile = 3; //Tile size to be colored, it had better be odd
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
     * Method graphicsOrderTiles is an auxiliary method that allows to graphically represent the tiles chosen by the player.
     * and help him rearrange them in the order he prefers.
     * @param tiles - list of tiles chosen by the player.
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
     * @param activEnd - true you want to have at the end of each line the character "\n".
     * @param activMerge - true if you want to merge the String with another one.
     * @return - string of the player's Shelf graphic, so that if you want you can merge it with other strings.
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
     * Method tileCGRow is an auxiliary method of graphicsCGC, manages the graphics of the CGC columns.
     * @param tile - tile to be printed.
     * @param numTileForRow - number of Tile per row.
     * @param dashedEdge - if you want the graphic to have a dashed border around it.
     * @param repeat - represents the number of repetitions of the CGC that you want to show.
     * @param maxDif - represents the maximum number of different Tiles per row that CGC wants to show.
     * @return - String graphics of CGC by line.
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

    /**
     * tileCGColumn method is an auxiliary method of graphicsCGC, for CGC graphics.
     * @param cgc - parent string into which column graphics will be merged.
     * @param tile - tile to be joined.
     * @param numTileForColumn - number of Tiles per column.
     * @param numTileForRow - number of Tile per row.
     * @param maxLen - length of line for each CGC.
     * @param endLine - string to be printed at the end of each line.
     * @param dashedEdge - if you want the graphic to have a dashed border around it.
     * @param repeat - number of columns to repeat.
     * @param maxDif - represents the maximum number of different Tiles per row that CGC wants to show.
     */
    private static void tileCGColumn(StringBuilder cgc, String tile, int numTileForColumn, int numTileForRow, int maxLen, String endLine, boolean dashedEdge, int repeat, int maxDif){
        String board = stringRepeat( empty + boardSide[3], numTileForRow*2);
        int heightString = numTileForColumn + 1;
        int maxDifRow = 0;
        int ripetRow = 0;
        if(dashedEdge){
            addEmptySpaceToString(cgc,startLine + boardSide[1] + board + empty + boardSide[4], endLine, maxLen);
            heightString += 2;
        }
        for(int i = numTileForColumn; i > 0; i--){
            if(i == 2 && maxDif > 0) maxDifRow = maxDif;
            if(i == 1) ripetRow = repeat;
            addEmptySpaceToString(cgc, tileCGRow(tile, numTileForRow, dashedEdge, ripetRow, maxDifRow), endLine, maxLen);
            maxDifRow = 0;
        }
        if(dashedEdge){
            addEmptySpaceToString(cgc, startLine + boardSide[2] + board + empty + boardSide[5], endLine, maxLen);
        }
        if(maxDif > 0 && numTileForColumn < 2){
            addEmptySpaceToString(cgc, startLine + "Max " + maxDif + " " + tileSquare + empty + diff + empty + colorRESET, endLine, maxLen);
            ++heightString;
        }
        cgc.insert(0,maxLen + dividNum + heightString + dividNum);
    }

    /**
     * addEmptySpaceToString method adds the new line to the parent string, in case the string to be added does not reach
     * the maximum length of each parent line whitespace is added so that it gets there.
     * @param stringOld - string to be modified.
     * @param add - new string to be added to the old one.
     * @param end - string that you want added to the end.
     * @param mod - number of the length of the parent string that you want not to be exceeded.
     */
    private static void addEmptySpaceToString(StringBuilder stringOld, String add, String end, int mod){
        stringOld.append(add);
        stringOld.append(stringRepeat(empty, mod - ( add.replaceAll("\033\\[[;\\d]*m", "").length() % mod) - end.length() ) ).append(end);
    }

    /**
     * mergerString method returns merging two strings so that it returns a single block of strings, with the string content
     * of the first string on the left while that of the second string on the right of the new string.
     * @param stringLeft - String of which you want its lines to be to the left on the final String.
     * @param stringRight - String of which you want its lines to be to the right on the final String.
     * @param activeEndN - true you want to have at the end of each line the character "\n".
     * @param activeMerge - activates the merge parameter on the final string, so that you can merge the resulting String with another.
     * @param topAlign - aligns the merging of two Strings up or down.
     * @return - the joining of lines between two Strings.
     */

    public static String mergerString(String stringLeft, String stringRight, boolean activeEndN, boolean activeMerge, boolean topAlign){
        StringBuilder merge = new StringBuilder();
        String endLine = activeEndLine(activeEndN);

        int posL = stringLeft.indexOf(dividNum);
        int posR = stringRight.indexOf(dividNum);

        int lenLineL = Integer.parseInt(stringLeft.substring(0, posL)) - 1;
        int lenLineR = Integer.parseInt(stringRight.substring(0, posR)) - 1;

        int startL = stringLeft.indexOf(dividNum, ++posL);
        int startR = stringRight.indexOf(dividNum, ++posR);

        int heightStrL = Integer.parseInt(stringLeft.substring(posL, startL++));
        int heightStrR = Integer.parseInt(stringRight.substring(posR, startR++));

        posL = stringLeft.indexOf(dividNum, startL);
        posR = stringRight.indexOf(endLine, startR);
        int height = Integer.max(heightStrL, heightStrR);
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
        if(activeMerge) return (lenLineL + lenLineR + 1) + dividNum + height + dividNum +  merge;
        else return merge.toString();
    }

    /**
     * Method graphicsLivingRoom graphically builds the living room on the TUI.
     * @param livingRoomView - the living room.
     * @param activeEnd - true you want to have at the end of each line the character "\n".
     * @param activeMerge - true if you want to merge the String with another one.
     * @return - the LivingRoom graphic so that you may or may not merge the lines with those of another String.
     */
    public static String graphicsLivingRoom(LivingRoomView livingRoomView, boolean activeEnd, boolean activeMerge){
        Couple[][] livingRoom = livingRoomView.getBoard();
        int numColum = 0;
        int numTileLine = livingRoom[0].length;
        String endLine = startLine.repeat(2) + activeEndLine(activeEnd);
        String boardHoriz = stringRepeat(boardSide[3], sizeSlotTile + 2);
        String sideLR = startLine + empty + startLine + boardSide[8] + stringRepeat(boardHoriz + boardSide[9], numTileLine - 1 ) + boardHoriz + boardSide[10] + endLine;
        StringBuilder livingRoomString = new StringBuilder(startLine + empty + startLine + boardSide[1] + stringRepeat(boardHoriz + boardSide[6], numTileLine - 1) + boardHoriz + boardSide[4] + endLine);
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
        addEmptySpaceToString(livingRoomString,startLine + empty + startLine + stringRepeat(empty, sizeSlotTile) + sequenceNumbers(0, numTileLine,3 + (sizeSlotTile/2)*2), endLine,lengthLine);
        ++heightLR;
        if(activeMerge) return lengthLine + dividNum + heightLR + dividNum + livingRoomString;
        else return livingRoomString.toString();
    }

    /**
     * sequenceNumbers method returns a string of a sequence of increasing numbers with desired distance space
     * @param start - start number of the sequence to be returned.
     * @param end - end number of the sequence to be returned.
     * @param spaceDivisor - distance space between numbers.
     * @return - String of numbers from start to end with space between each number of spaceDivisor
     */
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
        String tileEmpty = empty.repeat(sizeSlotTile) + boardSide[0];
        String board = stringRepeat(boardSide[3], (sizeSlotTile + 1)*lenMaxColumn + 3);
        int lengthLine = (startLine + boardSide[1] + board + boardSide[4] + endLine).length();
        addEmptySpaceToString(pcg,  startLine + "PersonalGoalCard:", endLine, lengthLine);
        pcg.append(startLine).append(boardSide[1]).append(board).append(boardSide[4]).append(endLine);
        pcg.append(startLine).append(boardSide[0]).append(empty).append(boardSide[1]).append(stringRepeat(boardSide[3].repeat(sizeSlotTile) + boardSide[6], lenMaxColumn - 1)).append(boardSide[3].repeat(sizeSlotTile)).append(boardSide[4]).append(empty).append(boardSide[0]).append(endLine);
        int heightPCG = 8;
        int lenStringTile;

        for (String s : positionTilePCG) {
            pcg.append(startLine).append(boardSide[0]).append(empty).append(boardSide[0]);
            tileS = s;
            lenStringTile = tileS.length();
            int oldPosT = 0;
            if (lenStringTile > 1) {
                String[] tilesInRow = tileS.split(":");
                int positionTile;

                for (int index = 0; index < tilesInRow.length; index += 2) {
                    positionTile = Integer.parseInt(tilesInRow[index]);
                    pcg.append(stringRepeat(tileEmpty, positionTile - oldPosT));
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
            pcg.append(stringRepeat(tileEmpty, lenMaxColumn - oldPosT)).append(empty).append(boardSide[0]).append(endLine);
        }
        pcg.append(startLine).append(boardSide[0]).append(empty).append(boardSide[2]).append(stringRepeat(boardSide[3].repeat(sizeSlotTile) + boardSide[7], lenMaxColumn - 1)).append(boardSide[3].repeat(sizeSlotTile)).append(boardSide[5]).append(empty).append(boardSide[0]).append(endLine);
        pcg.append(startLine).append(boardSide[8]).append(board).append(boardSide[10]).append(endLine);
        pcg.append(startLine).append(boardSide[0]).append(" 1 | 2 | 3 | 4 | 5 | 6 ").append(boardSide[0]).append(endLine);
        pcg.append(startLine).append(boardSide[0]).append(" 1 | 2 | 4 | 6 | 9 |12 ").append(boardSide[0]).append(endLine);
        pcg.append(startLine).append(boardSide[2]).append(board).append(boardSide[5]).append(endLine);
        stringPGC = lengthLine + dividNum + heightPCG + dividNum + pcg;

    }

    /**
     * getStringPCG method returns the string of the personal goal card.
     * @return - the string of the personal goal card.
     */
    public static String getStringPCG(){
        return stringPGC;
    }

    /**
     * activeEndLine method returns "\n" if activeEnd is true, otherwise it returns dividNum.
     * @param activeEnd - activate "\n" or not
     * @return - "\n" if activeEnd is true, otherwise it returns dividNum.
     */
    private static String activeEndLine(boolean activeEnd){
        return activeEnd ? "\n" : dividNum;
    }

    /**
     * stringRepeat method returns a String of repeated text depending on the number of repeatNum.
     * @param text - String of characters that you want to have repeated.
     * @param repeatNum - number of times you want to have the string repeated
     * @return - String text repeated repeatNum times.
     */
    private static String stringRepeat(String text, int repeatNum){
        return text.repeat(repeatNum);
    }

    /** Method printTitle prints the title of the game. */
    public static void printTitle(){
        String title =
                "\033[1;38;5;228m                  #           #                                ######       ####                    ##           ######\n" +
                "\033[39;38;5;227m                ##          ##                               ###    ##    ###   #                  ###         ##     ##\n" +
                "\033[38;5;11m               ###         ###                               ###     ##  ###                       ###        ###\n" +
                "\033[38;5;214m              ####       #####   ####       ###              ###    #   ###             #####      ###        ###      #       #####\n" +
                "\033[38;5;208m             ### ##     ## ###  ## ##      ####               ###      ###    ###     ####   ##    ###      #######   ###    ####   ##\n" +
                "\033[38;5;202m             ###  ##   ##  ###      ##    ## ###        #      ###    ###   ##  ##   ###   ###     ###        ###      ##   ###   ###\n"    +
                "\033[38;5;9m             ###   ## ##   ###      ##   ###  ###      ##       ###   ### ##    ##   ######     #  ###        ###      ##   #######    #\n" +
                "\033[38;5;197m            ###     ####    ###    ##   ###    ###      ###     ###   ####     ## #   ###     ##    ###    #  ###      ##    ###     ##\n" +
                "\033[38;5;12m          ####       ##      ####   ######     ###        #######    ###      ####     ########      ######   ###     ####    ########\n" +
                "\033[38;5;14m                                               ###                                                            ###\n" +
                "\033[38;5;51m                                              ###                                                             ###\n" +
                "\033[38;5;85m                                  #          ###                                                              ###\n" +
                "\033[38;5;83m                                   ##      ####                                                               ##\n" +
                "\033[38;5;10m                                    ########                                                                 #\n\n" + colorRESET;

        print.print(title);
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

    /**
     * graphicsToken method returns the CGC Token graphics string so that the resulting String can be merged with other Strings.
     * @param numToken - number of token CGC.
     * @param activeEndN - put to true if you do not want to merge the lines of the String with those of another string, otherwise false.
     * @return - the token graphic of the CommonGoalCard with the associated score.
     */
    public static String graphicsToken(int numToken, boolean activeEndN){
        String endLine = activeEndN ? "\n": startLine.repeat(10) + dividNum;
        String roof = startLine + boardSide[1] + boardSide[3].repeat(5) +  boardSide[4] + endLine;
        return roof.length() + dividNum + 4 + dividNum +  startLine + "Token: " + endLine + roof +
                startLine + boardSide[0] + empty + "[" + numToken + "]" + empty + boardSide[0] + endLine +
                startLine + boardSide[2] + boardSide[3].repeat(5) + boardSide[5] + endLine;
    }



}
