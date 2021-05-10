package it.polimi.ingsw.model.FaithTrack;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.Interfaces.Observer;
import it.polimi.ingsw.model.Interfaces.Subject;
import it.polimi.ingsw.model.PlayerBoard;

@Deprecated
@SuppressWarnings("deprecation")
public class ControllerStub implements Observer {
    private Subject subject;
    private FaithLevel fT1;
    private FaithLevel fT2;
    private FaithLevel fT3;
    private boolean neededToTryTheVaticanReport;
    private boolean x;
    private PlayerBoard playerBoard;
    private boolean withPlayerBoard;

    public ControllerStub(Subject subject) {
        this.subject = subject;
        neededToTryTheVaticanReport = false;
        x = false;
        withPlayerBoard = false;
    }

    public ControllerStub(Subject subject, FaithLevel fT1) {
        this.subject = subject;
        this.fT1 = fT1;
        neededToTryTheVaticanReport = true;
        x = true;
        withPlayerBoard = false;
    }

    public ControllerStub(Subject subject, FaithLevel fT1, FaithLevel fT2, FaithLevel fT3) {
        this.subject = subject;
        this.fT1 = fT1;
        this.fT2 = fT2;
        this.fT3 = fT3;
        neededToTryTheVaticanReport = true;
        x = false;
        withPlayerBoard = false;
    }

    public ControllerStub() {
        subject = null;
    }


    @Override
    public String update() {
        return "Activate Vatican Report: " + ((PopeCell) subject).getReportNum();
    }

    public void setNeededToTryTheVaticanReport(boolean neededToTryTheVaticanReport) {
        this.neededToTryTheVaticanReport = neededToTryTheVaticanReport;
    }

    public boolean update(Object object) {
        ReportNum reportNum = (ReportNum) object;
        if (neededToTryTheVaticanReport) {
            if (!x) {
                try {
                    this.fakeVaticanReport(reportNum);
                } catch (IllegalActionException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    this.fakeVaticanReportOnePlayer(reportNum);
                } catch (IllegalActionException e) {
                    e.printStackTrace();
                }
            }
        } else if (withPlayerBoard) {
            try {
                this.fakeVaticanReportWithPlayerBoard(reportNum);
            } catch (IllegalActionException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /*@Override
    public boolean update(Object object) {
        return false;
    }*/

    public void fakeVaticanReport(ReportNum reportNum) throws IllegalActionException {
        fT1.dealWithVaticanReport(reportNum);
        fT2.dealWithVaticanReport(reportNum);
        fT3.dealWithVaticanReport(reportNum);
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void fakeVaticanReportOnePlayer(ReportNum rN) throws IllegalActionException {
        fT1.dealWithVaticanReport(rN);
    }

    public ControllerStub(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
        neededToTryTheVaticanReport = false;
        this.withPlayerBoard = true;

    }

    public void fakeVaticanReportWithPlayerBoard(ReportNum rN) throws IllegalActionException {
        playerBoard.dealWithVaticanReport(rN);
    }
}
