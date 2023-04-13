package it.polimi.ingsw.view;


public interface View {
    void chooseConnection();
    String getConnectionType();
    String getUsername();
    void displayNotification(String message);
    int askAmountOfPlayers();
}
