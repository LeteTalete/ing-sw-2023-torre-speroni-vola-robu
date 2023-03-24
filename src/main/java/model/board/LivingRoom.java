package model.board;

import model.enumerations.Couple;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LivingRoom{
    private Couple[][] board;

    public Space getCouple(int x,int y) {

        return null;
    }

    public void setCouple(int x, int y) {

    }

    // This method should:
    // - Read a .txt ( or .json file ) and recreate a 9x9 board like the one on the file
    public LivingRoom(String filename){

        try {
            Scanner scanner = new Scanner(new File(filename));
            int row = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                for (int col = 0; col < line.length(); col++) {

                    // To do:
                    // Decide which symbols should be written on the .txt file and their counterpart as couples
                    // EX: C means Cat and Pickable

                }
                row++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}