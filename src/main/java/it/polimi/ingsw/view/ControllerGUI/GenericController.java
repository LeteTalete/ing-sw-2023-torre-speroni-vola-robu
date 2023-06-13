package it.polimi.ingsw.view.ControllerGUI;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.CommandParsing;
import it.polimi.ingsw.client.IClientConnection;
import it.polimi.ingsw.client.ResponseDecoder;
import it.polimi.ingsw.network.IClientListener;
import it.polimi.ingsw.network.IRemoteController;
import it.polimi.ingsw.view.ClientGUI;
import it.polimi.ingsw.view.GUIApplication;
import it.polimi.ingsw.view.View;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.registry.Registry;

public class GenericController {

    private GUIApplication guiApplication;

    public void setGuiApplication(GUIApplication guiApplication){
        this.guiApplication = guiApplication;
    }

    public GUIApplication getGuiApplication(){
        return this.guiApplication;
    }

}
