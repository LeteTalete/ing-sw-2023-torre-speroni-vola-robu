package it.polimi.ingsw.notifications;

import it.polimi.ingsw.Updates.ModelUpdate;
import it.polimi.ingsw.responses.Response;
import it.polimi.ingsw.responses.ResponseHandler;

import java.rmi.RemoteException;

/**notification from the server about the update of the model*/

public class ModelUpdateNotification implements Response {
    private final ModelUpdate update;

    /**
     * modelUpdateNotification constructor
     * @param updated - the updated model
     */
    public ModelUpdateNotification(ModelUpdate updated) {
        this.update = updated;
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
     * Method getUpdate returns the updated model
     * @return the updated model
     */
    public ModelUpdate getUpdate() {
        return update;
    }
}
