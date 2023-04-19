package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientHandler;
import it.polimi.ingsw.view.ClientGUI;
import it.polimi.ingsw.view.ClientTUI;
import it.polimi.ingsw.view.View;

import java.util.Scanner;
import java.lang.String;

public class ClientExecutable {
    private static ClientHandler client;
    private static View clientView;

    public static void main(String[] args) {
        String interfaceC = interfaceChoice();
        //if it's equal to GUI then we start the GUI
        if(interfaceC.equals("GUI")){
            clientView = new ClientGUI();
        }else {
            clientView = new ClientTUI();
        }
        client = new ClientHandler(clientView);
    }


    /**this is to ask the player to select which interface they'd like to use**/
    private static String interfaceChoice() {
        String input;
        Scanner in = new Scanner(System.in);
        do{
            System.out.println("GUI[G] or TUI[T]?");
            input = in.nextLine();
            /**it could be that the user writes lowercase g or t, so we have to convert the input into
            capital letter to make it easier to compare**/
            input = input.toUpperCase();
            if(input.equals("G")){
                input = "GUI";
            }else if(input.equals("T")){
                input = "TUI";
            }
        }while(!input.equals("GUI") && !input.equals("TUI"));
        return input;
    }
}

