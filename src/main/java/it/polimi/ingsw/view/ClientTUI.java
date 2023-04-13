package it.polimi.ingsw.view;

import java.util.*;

public class ClientTUI implements View{

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
}
