package it.polimi.ingsw.util;

public interface Observer<SubjectType extends Observable<Event>, Event extends Enum<Event>>
{
    void update(SubjectType o, Event arg);
}
