package it.polimi.ingsw.model.enumerations;

import java.io.Serializable;

public enum State implements Serializable {
    EMPTY,
    EMPTY_AND_UNUSABLE, // todo: this should be deleted.
    INVALID,
    PICKABLE,
    PICKED
}
