package it.polimi.ingsw.view;


public interface View {
    void chooseConnection();
    String getConnectionType();
    String getUsername();
    void displayNotification(String message);
    int askAmountOfPlayers();
    //choose tiles from board
    //reorder tiles
    //place tiles on shelf
}
