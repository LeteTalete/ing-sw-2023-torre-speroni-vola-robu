package it.polimi.ingsw.responses;

import java.rmi.RemoteException;

/**response after a login request*/

public class LoginResponse implements Response {
    public final String token;
    public final String name;
    public final boolean b;
    public final boolean first;
    /**loginResponse constructor.
     * @param username - username of the client.
     * @param f - boolean signalling whether the client needs to create a waiting room.
     * @param isValid - boolean signalling whether the login failed or succeeded.
     * @param tokenGiven - token given by the server to identify the client.*/
    public LoginResponse(String username, boolean isValid, String tokenGiven, boolean f){
        this.b = isValid;
        this.token = tokenGiven;
        this.name = username;
        this.first = f;
    }
    @Override
    public void handleResponse(ResponseHandler responseHandler) throws RemoteException {
        responseHandler.handle(this);
    }


}
