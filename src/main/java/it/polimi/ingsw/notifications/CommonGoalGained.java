package it.polimi.ingsw.notifications;

import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.responses.ResponseHandler;

import java.rmi.RemoteException;

public class CommonGoalGained implements Response {
    private final String name;
    private final int goalCard;

    public CommonGoalGained(String n, int card) {
        this.name = n;
        this.goalCard = card;
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
}
