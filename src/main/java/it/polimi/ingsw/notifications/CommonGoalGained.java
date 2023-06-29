package it.polimi.ingsw.notifications;

import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.responses.ResponseHandler;

import java.rmi.RemoteException;

/**notification from the server about a player winning a common goal card.*/

public class CommonGoalGained implements Response {
    private final String name;
    private final int goalCard;

    private final int points;

    /**CommonGoalGained constructor.
     * @param card - id of the common goal card.
     * @param n - username of the player who won the card
     * @param points - the points gained by gaining the common goal card
     */
    public CommonGoalGained(String n, int card, int points) {
        this.name = n;
        this.goalCard = card;
        this.points = points;
    }

    /**
     * Method handleResponse is used to handle a response sent by server socket
     * @param responseHandler - the response handler
     * @throws RemoteException
     */
    @Override
    public void handleResponse(ResponseHandler responseHandler) throws RemoteException {
        responseHandler.handle(this);
    }

    /**
     * Method getCard returns the id of the common goal card
     * @return the id of the common goal card
     */
    public int getCard() {
        return goalCard;
    }

    /**
     * method getName returns the username of the player who won the card
     * @return the username of the player who won the card
     */
    public String getName() {
        return name;
    }

    /**
     * method getPoints returns the points gained by gaining the common goal card
     * @return the points gained by gaining the common goal card
     */
    public int getPoints() {
        return points;
    }
}
