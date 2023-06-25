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
     * @param n - username of the player who won the card*/
    public CommonGoalGained(String n, int card, int points) {
        this.name = n;
        this.goalCard = card;
        this.points = points;
    }

    @Override
    public void handleResponse(ResponseHandler responseHandler) throws RemoteException {
        responseHandler.handle(this);
    }

    public int getCard() {
        return goalCard;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }
}
