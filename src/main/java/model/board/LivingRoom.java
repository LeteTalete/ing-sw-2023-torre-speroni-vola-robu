package model.board;

import model.Deck;
import model.enumerations.Couple;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LivingRoom{
    private Couple[][] board;

    public Couple getCouple(int x,int y) {

        return null;
    }

    public void setCouple(int x, int y) {

    }

    // The constructor reads line by line a .txt file containing a 9x9 matrix that represents the board
    // Completely random matrix example:
    //
    //       0:3,1:0,1:0,0:4,1:0,0:0,0:4,1:0,0:0
    //       1:0,1:0,1:0,0:4,0:4,0:0,1:0,1:0,0:4
    //       0:4,0:0,0:0,1:0,1:0,1:0,1:0,0:0,0:0
    //       0:3,0:0,1:0,1:0,1:0,1:0,0:0,0:0,0:0
    //       0:3,1:0,1:0,1:0,0:0,0:4,0:4,0:0,0:0
    //       1:0,1:0,0:0,0:4,1:0,1:0,1:0,1:0,0:4
    //       0:0,0:4,0:4,1:0,1:0,1:0,1:0,0:0,0:0
    //       1:0,0:0,1:0,1:0,1:0,1:0,0:4,1:0,0:4
    //       0:3,1:0,1:0,0:4,1:0,0:0,0:0,1:0,1:0
    //
    // Each cell is delimited by a comma and inside each cell there is a string that follows the format x:y
    // - 'x' can be 0 if the cell is usable or 1 if the cell is invalid
    // - 'y' can be 0 if the cell doesn't have special requirements, 3 if 3 or 4 players are required to place a tile on it,
    //    4 if only 4 players are required
    //
    // Each line is split into 9 strings (using ',' as a delimiter)
    // Each string is parsed and the information is stored inside three variables: invalidCheck, threePlayersTile, fourPlayersTile
    // Then a couple is created using this information and ultimately put inside the board in the corresponding position


    public LivingRoom(String filename, int numberofplayers){

        try {
            Scanner scanner = new Scanner(new File(filename));
            int row = 0;
            Deck deck = new Deck();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tileInformation = line.split(",");

                for (int col = 0; col < 9; col++) {
                    int invalidCheck = Integer.parseInt(tileInformation[col].substring(0,1));
                    int threePlayersTile = 0;
                    int fourPlayersTile = 0;
                    int check = Integer.parseInt(tileInformation[col].substring(2));

                    if ( check == 3 && numberofplayers >= 3 ) {
                        threePlayersTile = 1;
                    } else if ( check == 4 && numberofplayers == 4 ) {
                        fourPlayersTile = 1;
                    }

                    if ( invalidCheck == 1) {
                        Couple couple = new Couple(invalidCheck);
                        board[row][col] = couple;
                    } else if ( numberofplayers == 2 && ( threePlayersTile == 0 && fourPlayersTile == 0 ) ){
                        Couple couple = new Couple(deck.draw());
                        board[row][col] = couple;
                    } else if ( numberofplayers == 3 && ( ( threePlayersTile == 0 && fourPlayersTile == 0 ) || threePlayersTile == 1 ) )  {
                        Couple couple = new Couple(deck.draw());
                        board[row][col] = couple;
                    } else if ( numberofplayers == 4 ) {
                        Couple couple = new Couple(deck.draw());
                        board[row][col] = couple;
                    } else {
                        Couple couple = new Couple(invalidCheck);
                        board[row][col] = couple;
                    }
                }
                row++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}