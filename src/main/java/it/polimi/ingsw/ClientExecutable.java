package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.view.ClientGUI;
import it.polimi.ingsw.view.ClientTUI;
import it.polimi.ingsw.view.GUIApplication;
import it.polimi.ingsw.view.View;

import java.util.Scanner;
import java.lang.String;

public class ClientExecutable {
    private static ClientController clientController;
    private static View clientView;

    public static void main(String[] args) {
        String interfaceC = interfaceChoice();
        //if it's equal to GUI then we start the GUI

        if(interfaceC.equals("GUI")){
            clientView = new ClientGUI();
            clientController = new ClientController(clientView);
            GUIApplication.main(null);
        }else {
            clientView = new ClientTUI();
            clientController = new ClientController(clientView);
            clientView.chooseConnection(); // clientController.setupConnection();
        }
    }


    /**this is to ask the player to select which interface they'd like to use**/
    private static String interfaceChoice() {
        String input;
        Scanner in = new Scanner(System.in);
        do{
            System.out.println("GUI[G] or TUI[T]?");
            input = in.nextLine();
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

