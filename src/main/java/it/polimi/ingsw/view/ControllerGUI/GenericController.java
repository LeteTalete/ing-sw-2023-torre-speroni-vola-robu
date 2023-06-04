package it.polimi.ingsw.view.ControllerGUI;

import it.polimi.ingsw.view.GUIApplication;

public class GenericController {

    private GUIApplication guiApplication;

    public void setGuiApplication(GUIApplication guiApplication){
        this.guiApplication = guiApplication;
    }

    public GUIApplication getGuiApplication(){
        return this.guiApplication;
    }

}
