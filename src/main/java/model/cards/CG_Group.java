package model.cards;

import model.Position;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;


public class CG_Group extends CommonGoalCard {
    private int ID;
    public CG_Group(int id) {
        this.ID = id;
    }

    public int getID() {
        return this.ID;
    }
}
