package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.IllegalParameterException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.marble.*;
import java.util.*;

/*NOT YET IMPLEMENTED: Builder which read from xml Config File*/

/**Implements the the game structure of the market. when buying it use the marbles to give back the resources bought in a HashMap*/
public class Market {
    private final Marble[][] marketMatrix;
    private Marble marbleOnSlide;

    /**Builder which instantiated the marbles on the grid and on the slide according with Classic Rules*/
    public Market() throws IllegalParameterException, IndexOutOfBoundsException{
        this(4,1,2,2,2,2);
    }

    /**Builder for every configuration; it needs exactly 13 marbles because the slots in the market are from the grid 4x3 + 1 on the slide
     * @throws IllegalParameterException if a negative number of a marble is used to call this builder
     * @throws IllegalParameterException if the sum of the parameters is not 13
     * each parameter refers to the number of marble of the same colour
     * @param nBlue refers the number of BlueMarbles to be added in the Market
     * @param nGrey refers the number of GreyMarbles to be added in the Market
     * @param nPurple refers the number of PurpleMarbles to be added in the Market
     * @param nRed refers the number of RedMarbles to be added in the Market
     * @param nWhite refers the number of WhiteMarbles to be added in the Market
     * @param nYellow refers the number of YellowMarbles to be added in the Market
     * */
    public Market(int nWhite, int nRed, int nGrey, int nBlue, int nYellow, int nPurple) throws IllegalParameterException {

        if (nWhite + nRed + nGrey + nBlue + nYellow + nPurple != 13) throw new IllegalParameterException("Market: number of Marbles is not 13");
        if (nWhite<0 || nRed<0 || nGrey<0 || nBlue<0 || nYellow<0 || nPurple<0) throw new IllegalParameterException("Market: number of Marbles can't be negative");

        this.marketMatrix = new Marble[3][4];

        List<Marble> configurationList = createConfigurationList(nWhite,nRed,nGrey,nBlue,nYellow,nPurple);

        for (int i=0; i<marketMatrix.length; i++){
            for (int j=0; j<marketMatrix[i].length; j++){
                setMarbleInTheGrid(configurationList.get(marketMatrix[i].length*i + j), i, j);
            }
        }

        this.marbleOnSlide = configurationList.get(configurationList.size()-1);
    }

    /**
     * Method used when buying from market and selecting 1 of the 3 rows.
     * Modify the references to the grid after buying action according to game rules.
     * @param rowNumber is the chosen row in the Market grid starting from 0.
     * @param effect determines the bought resources when selecting whiteMarbles
     * @return the resources purchased in an hashmap
     * @throws IllegalParameterException if rowNumber is not valid [0...2]
     * @throws NullPointerException if effect is NULL
     * the hashmap is instantiated in this Method, but it isn't modified directly*/
    public HashMap <ResourceType, Integer> moveRow(int rowNumber, Effect effect) throws IllegalParameterException, NullPointerException {
        if (rowNumber<0 || rowNumber>=3) throw new IllegalParameterException("moveRow Market: not valid rowNumber");
        if (effect==null) throw new NullPointerException("moveRow Market: not expected NULL effect");

        HashMap <ResourceType, Integer> resources = new HashMap<>();
        Marble tempMarble = marbleOnSlide;
        for (int j=0; j<marketMatrix[rowNumber].length; j++){
            try {
                marketMatrix[rowNumber][j].onActivate(resources,effect);
            }catch (NegativeQuantityException | NullPointerException e){
                System.out.println("moveRow: Something extremely bad happened in the Market");
                return new HashMap<>();
            }

            if (j==0) marbleOnSlide=getMarbleInTheGrid(rowNumber,j);
            else setMarbleInTheGrid(marketMatrix[rowNumber][j], rowNumber, j-1);
        }
        setMarbleInTheGrid(tempMarble, rowNumber, marketMatrix[rowNumber].length-1);
        return resources;
    }

    /**
     * Method used when buying from market and selecting 1 of the 4 columns.
     * Modify the references to the grid after buying action according to game rules.
     * @param columnNumber is the chosen column in the Market grid starting from 0.
     * @param effect determines the bought resources when selecting whiteMarbles
     * @return the resources purchased in an hashmap
     * @throws IllegalParameterException if columnNumber is not valid [0...3]
     * @throws NullPointerException if effect is NULL
     * the hashmap is instantiated in this Method, but it isn't modified directly*/
    public HashMap <ResourceType, Integer> moveColumn(int columnNumber, Effect effect) throws IllegalParameterException, NullPointerException {
        if (columnNumber<0 || columnNumber>=4) throw new IllegalParameterException("moveColumn Market: not valid columnNumber");
        if (effect==null) throw new NullPointerException("moveColumn Market: not expected NULL effect");

        HashMap <ResourceType, Integer> resources = new HashMap<>();
        Marble tempMarble = marbleOnSlide;
        for (int j=0; j<marketMatrix.length; j++){
            try {
                marketMatrix[j][columnNumber].onActivate(resources,effect);
            }catch (NegativeQuantityException | NullPointerException e){
                System.out.println("moveColumn: Something extremely bad happened in the Market:");
                return new HashMap<>();
            }
            if (j==0) marbleOnSlide = getMarbleInTheGrid(j, columnNumber);
            else setMarbleInTheGrid(marketMatrix[j][columnNumber], j-1, columnNumber);
        }
        setMarbleInTheGrid(tempMarble,marketMatrix.length-1, columnNumber);
        return resources;
    }

    /* same type marbles in the market refer to the same object, we instantiated only one marble for type*/
    /*create list of a certain number of references to Marbles with randomized order*/
    /** Creates a List of Marbles and then Randomize the order
     *  Same type Marble refers to the same object
     * @param nBlue refers the number of BlueMarble references to be added to the list
     * @param nGrey refers the number of GreyMarbles references to be added to the list
     * @param nPurple refers the number of PurpleMarbles references to be added to the list
     * @param nRed refers the number of RedMarbles references to be added to the list
     * @param nWhite refers the number of WhiteMarbles references to be added to the list
     * @param nYellow refers the number of YellowMarbles references to be added to the list
     * @return a List of Marbles with randomized order
     * */
    private List<Marble> createConfigurationList(int nWhite, int nRed, int nGrey, int nBlue, int nYellow, int nPurple) throws IllegalParameterException{

        if (nWhite<0 || nRed<0 || nGrey<0 || nBlue<0 || nYellow<0 || nPurple<0) throw new IllegalParameterException("createConfigurationList: number of Marbles can't be negative");
        List<Marble> configurationList = new ArrayList<>();
        Marble[] marbles = {new WhiteMarble(), new RedMarble(), new GreyMarble(), new BlueMarble(), new PurpleMarble(), new YellowMarble()};
        int[] numbersOfMarbles = {nWhite,nRed,nGrey,nBlue,nPurple, nYellow};

        int i=0;
        for (Marble marble:marbles) {
            for (int c=0; c<numbersOfMarbles[i]; c++) {
                configurationList.add(marble);
            }
            i++;
        }

        Collections.shuffle(configurationList);
        return configurationList;
    }

    @Override
    public String toString() {
        String marketMatrixString;
        marketMatrixString = "";
        for (int i=0; i<marketMatrix.length; i++){
            for (int j=0; j<marketMatrix[i].length; j++){
                marketMatrixString=marketMatrixString.concat(marketMatrix[i][j].toString());
                marketMatrixString=marketMatrixString.concat(" ");
            }
            marketMatrixString=marketMatrixString.concat("\n");
        }

        return "Market{\n" +
                "marketMatrix=\n" +
                marketMatrixString +
                "\n, marbleOnSlide=" + marbleOnSlide +
                '}';
    }

    /**
     * get marble on the grid in a specific position
     * @param row is the chosen row of the grid starting from 0
     * @param column is the chosen column of the grid starting from 0
     * @throws IllegalParameterException when the chosen position in the grid is not valid [0...2]x[0...3]
     * @return the chosen Marble on the grid
     * */
    public Marble getMarbleInTheGrid(int row, int column) throws IllegalParameterException {
        if (row<0 || column<0 || row>=3 || column>=4) throw new IllegalParameterException("Not valid position in the grid 3x4");
        return marketMatrix[row][column];
    }

    /**
     * get marble on the slide
     * @return the marble on the slide
     * */
    public Marble getMarbleOnSlide() {
        return marbleOnSlide;
    }

    /**
     * set marble on the grid in a specific position
     * is private because only Market methods can change position of the Marbles in the Market
     * @param row is the chosen row of the grid starting from 0
     * @param column is the chosen column of the grid starting from 0
     * @param marble is the Marble to be set
     * @throws NullPointerException when the marble to be set is null
     * @throws IllegalParameterException when the chosen position in the grid is not valid [0...2]x[0...3]
     * */
    private void setMarbleInTheGrid(Marble marble, int row, int column) throws IllegalParameterException, NullPointerException {
        if (row<0 || column<0 || row>=3 || column>=4) throw new IllegalParameterException("Not valid position in the grid 3x4");
        if(marble==null) throw new NullPointerException("setMarbleInTheGrid: not accepting null parameter");
        this.marketMatrix[row][column] = marble;
    }

    /**
     * set marble on the slide
     * is private because only Market methods can change position of the Marbles in the Market
     * @param marbleOnSlide is the Marble to be set
     * @throws NullPointerException when the marble to be set is null
     * */
    private void setMarbleOnSlide(Marble marbleOnSlide) throws NullPointerException {
        if(marbleOnSlide==null) throw new NullPointerException("setMarbleOnSlide: not accepting null parameter");
        this.marbleOnSlide = marbleOnSlide;
    }
}
