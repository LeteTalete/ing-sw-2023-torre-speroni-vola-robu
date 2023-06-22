package it.polimi.ingsw.notifications;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.responses.ResponseHandler;

import java.rmi.RemoteException;

/**notification from the server about the update of the model*/

public class ModelUpdateNotification implements Response {
    private final ModelUpdate update;

    public ModelUpdateNotification(ModelUpdate updated) {
        this.update = updated;
    }

    @Override
    public void handleResponse(ResponseHandler responseHandler) throws RemoteException {
        responseHandler.handle(this);
    }

    public ModelUpdate getUpdate() {
        return update;
    }
}
