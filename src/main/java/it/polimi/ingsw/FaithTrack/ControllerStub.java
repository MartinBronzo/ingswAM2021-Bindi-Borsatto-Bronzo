package it.polimi.ingsw.FaithTrack;

import it.polimi.ingsw.Observer;
import it.polimi.ingsw.Subject;

public class ControllerStub implements Observer {
    private Subject subject;

    public ControllerStub(Subject subject) {
        this.subject = subject;
    }


    @Override
    public String update() {
        return "Activate Vatican Report: " + ((PopeCell) subject).getReportNum();
    }

    public boolean update(boolean tmp){
        return true;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
