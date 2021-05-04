package it.polimi.ingsw.model.FaithTrack;

import it.polimi.ingsw.exceptions.LastVaticanReportException;
import it.polimi.ingsw.model.Interfaces.Observer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * FaithTrack class represents the FaithTrack. It consists of a list of the cells the Faith track in the board is made of.
 * It has many methods whose goal is to simply forward the requests to the right Cell (the given position of the Cell is for sure in the range of correct values because the FaithLevels
 * make sure of that). This class knows the order of the ReportNum.
 */
public class FaithTrack {
    /**
     * Contains all the Cells which make up the Faith track in the board
     */
    private List<Cell> track;
    /**
     * Contains the order of the ReportNum
     */
    private ReportNumOrder reportNumOrder;
    private boolean isReportNumOrderSet;

    /**
     * Constructs a FaithTrack. It saves the order of the Vatican Reports and it initiates the track
     *
     * @param reportNumOrder the order of the ReportNum of the Faith track
     */
    private FaithTrack(ReportNumOrder reportNumOrder) {
        this.track = new ArrayList<>();
        this.reportNumOrder = reportNumOrder;
        this.initTrack();
        this.isReportNumOrderSet = true;
    }

    /**
     * Constructs a FaithTrack. The constructed FaithTrack won't have a ReportNumOrder associated to it: this
     * will be done in a later moment
     *
     * @param config the file where to read the design of the track
     */
    //Forse è meglio che venga anche inizializzato automaticamente il reportNumOrder una volta lette le celle (si capisce leggendo come è l'ordine dei report che va nel report num order)
    private FaithTrack(File config) throws IOException, SAXException, ParserConfigurationException {
        this.track = new ArrayList<>();
        this.initTrack(config);
        this.isReportNumOrderSet = true;
    }

    /**
     * Returns a newly-constructed FaithTrack object with its ReportNumOrder already set.
     *
     * @param reportNumOrder the order of the Reports Num needed to build the instance of the class
     * @return a FaithTrack object
     */
    //The reason this class seems to implement the Singleton pattern even if it doesn't because it used to be a singleton.
    public static FaithTrack instance(ReportNumOrder reportNumOrder) {
        return new FaithTrack(reportNumOrder);
    }

    /**
     * Returns a newly-constructed FaithTrack object with its ReportNumOrder already set.
     *
     * @param config the file where to read the description of the FaithTrack
     * @return a FaithTrack object
     */
    public static FaithTrack instance(File config) throws ParserConfigurationException, SAXException, IOException {
        return new FaithTrack(config);
    }

    /**
     * Constructs the ensemble of cells which constitute the track. The initiation of the track is only done once: that's why the method is private and it is only called in
     * the private constructor
     *
     * @return true if the initiation went fine
     */
    // I fill the list one cell at the time
    private boolean initTrack() {
        //If we have already designed the Track, we can't change it here
        //if(!track.isEmpty()) return false;
        track.add(new Cell(0, ReportNum.REPORT1));
        track.add(new Cell(0, ReportNum.REPORT1));
        track.add(new Cell(0, ReportNum.REPORT1));
        track.add(new Cell(1, ReportNum.REPORT1));
        track.add(new Cell(0, ReportNum.REPORT1));
        track.add(new ReportCell(0, ReportNum.REPORT1));
        track.add(new ReportCell(2, ReportNum.REPORT1));
        track.add(new ReportCell(0, ReportNum.REPORT1));
        PopeCell popeCell1 = new PopeCell(0, ReportNum.REPORT1);
        track.add(popeCell1);
        track.add(new Cell(4, ReportNum.REPORT2));
        track.add(new Cell(0, ReportNum.REPORT2));
        track.add(new Cell(0, ReportNum.REPORT2));
        track.add(new ReportCell(6, ReportNum.REPORT2));
        track.add(new ReportCell(0, ReportNum.REPORT2));
        track.add(new ReportCell(0, ReportNum.REPORT2));
        track.add(new ReportCell(9, ReportNum.REPORT2));
        PopeCell popeCell2 = new PopeCell(0, ReportNum.REPORT2);
        track.add(popeCell2);
        track.add(new Cell(0, ReportNum.REPORT3));
        track.add(new Cell(12, ReportNum.REPORT3));
        track.add(new ReportCell(0, ReportNum.REPORT3));
        track.add(new ReportCell(0, ReportNum.REPORT3));
        track.add(new ReportCell(16, ReportNum.REPORT3));
        track.add(new ReportCell(0, ReportNum.REPORT3));
        track.add(new ReportCell(0, ReportNum.REPORT3));
        PopeCell popeCell3 = new PopeCell(20, ReportNum.REPORT3);
        track.add(popeCell3);

        Observer observer1 = new ControllerStub(popeCell1);
        Observer observer2 = new ControllerStub(popeCell2);
        Observer observer3 = new ControllerStub(popeCell3);
        popeCell1.attach(observer1);
        popeCell2.attach(observer2);
        popeCell3.attach(observer3);

        return true;
    }

    /**
     * Constructs the ensemble of cells which constitute the track. The initiation of the track is only done once: that's why the method is private and it is only called in
     * the private constructor
     *
     * @param config the file where to retrieve the information for the configuration
     * @return true if the initiation went fine
     */
    private boolean initTrack(File config) throws ParserConfigurationException, IOException, SAXException {
        Node elementNode;
        NodeList cellsList;
        Element el;

        String report;
        String type;
        int victoryPoints;

        this.track = new ArrayList<>();
        this.reportNumOrder = ReportNumOrder.instance();
        ReportNum reportNum;


        //an instance of factory that gives a document builder
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //an instance of builder to parse the specified xml file
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(config);
        doc.getDocumentElement().normalize();


        NodeList nodeList = doc.getElementsByTagName("Report");

        // nodeList is not iterable, so we are using for loop
        for (int itr = 0; itr < nodeList.getLength(); itr++) {
            Node node = nodeList.item(itr);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                report = eElement.getElementsByTagName("Name").item(0).getTextContent();
                reportNum = ReportNum.valueOf(report);
                reportNumOrder.addElementInOrder(reportNum);

                elementNode = eElement.getElementsByTagName("Elements").item(0);

                if (elementNode.getNodeType() == Node.ELEMENT_NODE) {
                    el = (Element) elementNode;
                    cellsList = el.getElementsByTagName("Cell");

                    for (int i = 0; i < cellsList.getLength(); i++) {
                        Node cellNode = cellsList.item(i);

                        if (cellNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element cellElement = (Element) cellNode;
                            type = cellElement.getElementsByTagName("Type").item(0).getTextContent();
                            victoryPoints = Integer.parseInt(cellElement.getElementsByTagName("Points").item(0).getTextContent());

                            if (type.equals("NORMAL"))
                                track.add(new Cell(victoryPoints, reportNum));
                            else if (type.equals("REPORT"))
                                track.add(new ReportCell(victoryPoints, reportNum));
                            else /*if(type.equals("POPE"))*/
                                track.add(new PopeCell(victoryPoints, reportNum));
                        }
                    }

                }
            }

        }

        return true;
    }

    /**
     * Sets the specified ReportNumOrder to the FaithTrack if it hasn't been set before
     *
     * @param reportNumOrder the ReportNumOrder to link to the FaithTrack
     * @return true if the ReportNumOrder has been set by this method, false if it had already been set
     */
    public boolean setReportNumOrder(ReportNumOrder reportNumOrder) {
        if (!isReportNumOrderSet) {
            this.reportNumOrder = reportNumOrder;
            this.isReportNumOrderSet = true;
            return true;
        }
        return false;
    }

    //This method is only used for testing purposes
    public boolean isReportNumOrderSet() {
        return isReportNumOrderSet;
    }

    /**
     * Calls the effect of the cell whose position is given as a parameter
     *
     * @param position the position of the cell
     */
    //This method was only used for testing purposes
    public boolean callCellEffectBasic(int position) {
        return track.get(position).effect();
    }

    /**
     * Calls the effect of the cell whose position is given as a parameter
     *
     * @param position the position of the cell
     * @return the effect of the cell
     * @throws LastVaticanReportException if the last cell (a PopeCell) was reached activating the last Vatican Report
     */
    public boolean callCellEffect(int position) throws LastVaticanReportException {
        if (position < track.size() - 1)
            return track.get(position).effect();
        //TODO: come estendere in modo tale che non è sempre detto l'ultima cella è la ultima cella papale
        throw new LastVaticanReportException("Last Vatican Report was activated!", track.get(position).effect());
    }

    /**
     * Returns the victory points of the cell whose position is given as a parameter
     *
     * @param position the position of the cell
     * @return the victory points of the cell
     */
    public int getCellVictoryPoints(int position) {
        return track.get(position).getVictoryPoints();
    }

    /**
     * Returns the ReportNum of the cell whose position is given as a parameter
     *
     * @param position the position of the cell
     * @return the ReportNum of the cell
     */
    public ReportNum getCellReportNum(int position) {
        return track.get(position).getReportNum();
    }

    /**
     * States whether the cell whose position is given as a parameter activates the Pope Tile in a Vatican Report whose corresponding ReportNum is given as a parameter
     *
     * @param position  the position of the cell
     * @param reportNum the ReportNum of the activated Vatican Report
     * @return true if the PopeTile must be activated, false otherwise
     */
    public boolean callCellActivateTile(int position, ReportNum reportNum) {
        return track.get(position).activatePopeTile(reportNum);
    }

    /**
     * Returns the size of the track
     *
     * @return the size of the track
     */
    public int getTrackSize() {
        return track.size();
    }

    //Method used for testing purposes
    /*public static void main(String args[]){
        FaithTrack ft = FaithTrack.instance();
        ft.initTrack();

        int i = 0;
        while(i < ft.track.size()){
            Cell tmp = ft.track.get(i);
            System.out.println(tmp.getReportNum() + " " +  tmp.getVicoryPoints() + " at position " + i);
            i++;
        }
        System.out.print("Cella ultima posizione: " + ft.getCellVictoryPoints(25 - 1));
    }*/

    //This method is only used for testing purposes
    public ReportNumOrder getReportNumOrder() {
        return this.reportNumOrder;
    }

    //This method is only used for testing purposes
    //NOT TO BE USED IN THE GAME
    //Once upon a time this method was actually useful
    @Deprecated
    public static void deleteState() {
        ;
    }

    /**
     * Returns the index of the specified ReportNum in the ordered list of ReportNums stored in the FaithTrack
     *
     * @param reportNum the ReportNum
     * @return the index of the specified ReportNum
     */
    public int getPopeTileIndex(ReportNum reportNum) {
        return reportNumOrder.getOrder(reportNum);
    }

    //This method is only used for testing purposes
    public Cell getCell(int position) {
        return this.track.get(position);
    }

    /**
     * Attaches the specified observer to the PopeTiles in the FaithTrack
     *
     * @param observer the observer to be attached
     */
    public void attachObserverToPopeTiles(Observer observer) {
        for (Cell c : this.track)
            if (c instanceof PopeCell)
                ((PopeCell) c).attach(observer);
    }


}
