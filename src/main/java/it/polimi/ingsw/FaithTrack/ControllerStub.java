package it.polimi.ingsw.FaithTrack;

import it.polimi.ingsw.Interfaces.Observer;
import it.polimi.ingsw.Interfaces.Subject;
import it.polimi.ingsw.exceptions.IllegalActionException;

public class ControllerStub implements Observer {
    private Subject subject;
    private FaithLevel fT1;
    private FaithLevel fT2;
    private FaithLevel fT3;
    private boolean neededToTryTheVaticanReport;

    public ControllerStub(Subject subject) {
        this.subject = subject;
        neededToTryTheVaticanReport = false;
    }

    public ControllerStub(Subject subject, FaithLevel fT1, FaithLevel fT2, FaithLevel fT3) {
        this.subject = subject;
        this.fT1 = fT1;
        this.fT2 = fT2;
        this.fT3 = fT3;
        neededToTryTheVaticanReport = true;
    }


    @Override
    public String update() {
        return "Activate Vatican Report: " + ((PopeCell) subject).getReportNum();
    }

    public boolean update(boolean tmp, ReportNum reportNum){
        if (neededToTryTheVaticanReport == true) {
            try {
                this.fakeVaticanReport(reportNum);
            } catch (IllegalActionException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void fakeVaticanReport(ReportNum reportNum) throws IllegalActionException {
        fT1.dealWithVaticanReport(reportNum);
        fT2.dealWithVaticanReport(reportNum);
        fT3.dealWithVaticanReport(reportNum);
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
