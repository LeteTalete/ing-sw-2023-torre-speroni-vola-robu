package it.polimi.ingsw.view;

import java.util.*;

import static it.polimi.ingsw.ClientExecutable.connectNow;

public class ClientTUI implements View{

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

        //this method runs inside game.ClientExecutable class
        connectNow(connection);
    }
}
