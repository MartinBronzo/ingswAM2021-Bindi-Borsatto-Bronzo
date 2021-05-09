package it.polimi.ingsw.model.FaithTrack;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.PlayerBoard;
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
 * This class represents the Pope tiles that each player has on their Faith track.
 * There are three possible states for a PopeTile: the card has neither been activated nor discarded (this means
 * the corresponding Vatican Report has not been activated, yet); the card is active and therefore it won't ever be discarded
 * (it was activated in the Vatican Report which took place); or the card is discarded and therefore it won't ever be
 * activated (it was discarded in the Vatican Report which took place.
 */
public class PopeTile {
    /**
     * The Victory Points the tile may give at the end of the game.
     */
    private int points;
    /**
     * States in which Vatican Report the tile is either activated or discarded.
     */
    private ReportNum reportNum;
    /**
     * True if the card has been activated in the corresponding Vatican Report, false if it has not yet been activated or discarded.
     */
    private boolean activated;
    /**
     * True if the card has been discarded, false if it has not yet been discarded.
     */
    private boolean discarded;
    /**
     * False as long as the tile the Vatican Report of the tile doesn't take place, true afterwards. It is needed to make sure the tile doesn't change its
     * state after its Vatican Report (which can take place only one).
     */
    private boolean changed;

    /**
     * Constructs a PopeTile with the given points and ReportNum. When it is created, the tile is neither active, discarded nor changed yet.
     *
     * @param points    the points the tile has
     * @param reportNum the ReportNum of the Vatican Report the tile is either activated or discarded
     */
    public PopeTile(int points, ReportNum reportNum) {
        this.points = points;
        this.reportNum = reportNum;
        this.activated = false;
        this.discarded = false;
        this.changed = false;
    }

    /**
     * Returns the point of the cell based on the state of the cell
     *
     * @return the points of the cells if it is active, 0 otherwise (not changed yet or discarded)
     */
    public int getPoints() {
        if (activated)
            return points;
        return 0;
    }

    /**
     * Returns the ReportNum associated with the cell
     *
     * @return the said ReportNum
     */
    public ReportNum getReportNum() {
        return reportNum;
    }

    /**
     * States whether this cell has been activated yet
     *
     * @return true if the cell has been activated, false otherwise (the cell has not been activated yet or it has been already discarded)
     */
    public boolean isActivated() {
        return activated;
    }

    /**
     * States whether this cell has been activated yet
     *
     * @return true if the cell has been discarded, false otherwise (the cell has not been discarded yet or it has been already activated)
     */
    public boolean isDiscarded() {
        return discarded;
    }

    /**
     * Activates or discards the Tile as needed if the corresponding Vatican Report is taking place
     *
     * @param reportNum  the ReportNum of the Vatican Report taking place
     * @param toTurnTile true if the tile needs to be activated, false if it needs to be discarded
     * @throws IllegalActionException if the tile has already been changed
     */
    public void dealWithVaticanReport(ReportNum reportNum, boolean toTurnTile) throws IllegalActionException {
        if (this.changed)
            throw new IllegalActionException("This tile has already been changed. ReportNum: " + this.reportNum);
        if (reportNum.equals(this.reportNum)) {
            if (toTurnTile)
                this.activated = true;
            else
                this.discarded = true;
            this.changed = true;
        }
    }

    /**
     * States whether this tile has already been changed
     *
     * @return true if the tile has already been changed, false otherwise
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Clones this tile
     *
     * @return a clone of this tile
     */
    public PopeTile cloneTile() {
        PopeTile tmp = new PopeTile(this.points, this.reportNum);
        tmp.activated = this.activated;
        tmp.discarded = this.discarded;
        tmp.changed = this.changed;
        return tmp;
    }

    public PopeTile(PopeTile original) {
        this(original.points, original.reportNum);
        this.activated = original.activated;
        this.discarded = original.discarded;
        this.changed = original.changed;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof PopeTile))
            return false;
        PopeTile tmp = (PopeTile) obj;
        return tmp.changed == this.changed && tmp.discarded == this.discarded && tmp.activated == this.activated && tmp.points == this.points && tmp.reportNum == this.reportNum;
    }

    /**
     * Constructs all the PopeTiles needed in this game
     * @param config the configuration file where to read the PopeTiles configuration information
     * @param reportNumOrderToCompare the ReportNumOrder already created during the FaithTrack configuration via file: the order of the PopeTiles in the configuration file (the PopeTiles are ordered by
     *                                their ReportNums) must be the same as the one present in the given ReportNumOrder
     * @return a list of list of PopeTiles: each sublist contain PopeTiles with the same ReportNum
     * @throws ParserConfigurationException if there are problems in the parsing
     * @throws IOException if an IO operations fails
     * @throws SAXException if there is a general SAX error or warning
     * @throws IllegalArgumentException if the order of ReportNums in the file differs from the one given by the specified ReportNumOrder
     */
    public static List<List<PopeTile>> popeTileConfig (File config, ReportNumOrder reportNumOrderToCompare) throws ParserConfigurationException, IOException, SAXException, IllegalArgumentException {
        Node elementNode;
        NodeList tilesList;
        Element el;

        String report;
        String type;
        int victoryPoints;

        ReportNum reportNum;

        List<List<PopeTile>> tiles = new ArrayList<>();
        List<PopeTile> tmp;

        int repOrditr = 0;

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
                if(!reportNumOrderToCompare.getReportNum(repOrditr).equals(reportNum))
                    throw new IllegalArgumentException("The Vatican Reports in the PopeTile configuration file don't match the ones in the FaithTrack configuration file!");
                else
                    repOrditr++;

                tmp = new ArrayList<>();

                elementNode = eElement.getElementsByTagName("Tiles").item(0);

                if (elementNode.getNodeType() == Node.ELEMENT_NODE) {
                    el = (Element) elementNode;
                    tilesList = el.getElementsByTagName("Tile");

                    for (int i = 0; i < tilesList.getLength(); i++) {
                        Node cellNode = tilesList.item(i);

                        if (cellNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element cellElement = (Element) cellNode;
                            victoryPoints = Integer.parseInt(cellElement.getElementsByTagName("Points").item(0).getTextContent());

                            tmp.add(new PopeTile(victoryPoints, reportNum));

                        }
                    }

                    tiles.add(tmp);

                }
            }

        }


        return tiles;
    }



}
