package it.polimi.ingsw.responses;

import java.rmi.RemoteException;

public class LoginResponse implements Response {
    public final String token;
    public final String name;
    public final boolean b;
    public final boolean first;
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
