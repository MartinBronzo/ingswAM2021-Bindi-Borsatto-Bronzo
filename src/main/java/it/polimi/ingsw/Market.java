package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.IllegalParameterException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.marble.*;
import java.util.*;

/*NOT YET IMPLEMENTED: Builder which read from xml Config File*/

/*this class implements the the game structure of the market. when buying it use the marbles to give the resources*/
public class Market {
    private final Marble[][] marketMatrix;
    private Marble marbleOnSlide;


    /*Classic Rules*/
    public Market() throws IllegalParameterException, IndexOutOfBoundsException{
        this(4,1,2,2,2,2);
    }

    /*Builder for every configuration; it needs exactly 13 marbles because the slots in the market are from the grid 4x3 + 1 on the slide
    * there can't be a negative number of a type of marble
    * each parameter refer to the number of marble of the same colour*/
    public Market(int nWhite, int nRed, int nGrey, int nBlue, int nYellow, int nPurple) throws IllegalParameterException {

        if (nWhite + nRed + nGrey + nBlue + nYellow + nPurple != 13) throw new IllegalParameterException("Market: number of Marbles is not 13");
        if (nWhite<0 || nRed<0 || nGrey<0 || nBlue<0 || nYellow<0 || nPurple<0) throw new IllegalParameterException("Market: number of Marbles can't be negative");

        this.marketMatrix = new Marble[3][4];

        List<Marble> configurationList = createConfigurationList(nWhite,nRed,nGrey,nBlue,nYellow,nPurple);

        for (int i=0; i<marketMatrix.length; i++){
            for (int j=0; j<marketMatrix[i].length; j++){
                marketMatrix[i][j] = configurationList.get(marketMatrix[i].length*i + j);
            }
        }

        this.marbleOnSlide = configurationList.get(configurationList.size()-1);
    }

    /*Method used when buying from market and selecting 1 of the 3 rows. Modify the references to the grid after buying action. it returns the resources purchased in an hashmap
    * the hash map isn't modified directly in this method to avoid invalid configurations*/
    public HashMap <ResourceType, Integer> moveRow(int rowNumber, Effect effect) throws IllegalParameterException, NullPointerException, NegativeQuantityException {
        if (rowNumber<0 || rowNumber>=3) throw new IllegalParameterException("moveRow Market: not valid rowNumber");
        if (effect==null) throw new NullPointerException("moveRow Market: not expected NULL effect");

        HashMap <ResourceType, Integer> resources = new HashMap<>();
        Marble tempMarble = marbleOnSlide;
        for (int j=0; j<marketMatrix[rowNumber].length; j++){
            marketMatrix[rowNumber][j].onActivate(resources,effect);
            if (j==0) marbleOnSlide = marketMatrix[rowNumber][j];
            else marketMatrix[rowNumber][j - 1] = marketMatrix[rowNumber][j];
        }
        marketMatrix[rowNumber][marketMatrix[rowNumber].length-1] = tempMarble;
        return resources;
    }

    /*Method used when buying from market and selecting 1 of the 4 columns. Modify the references to the grid after buying action. it returns the purchased resources in an hashmap
    * the hash map isn't modified directly in this method to avoid invalid configurations*/
    public HashMap <ResourceType, Integer> moveColumn(int columnNumber, Effect effect) throws IllegalParameterException, NullPointerException, NegativeQuantityException {
        if (columnNumber<0 || columnNumber>=4) throw new IllegalParameterException("moveColumn Market: not valid columnNumber");
        if (effect==null) throw new NullPointerException("moveColumn Market: not expected NULL effect");

        HashMap <ResourceType, Integer> resources = new HashMap<>();
        Marble tempMarble = marbleOnSlide;
        for (int j=0; j<marketMatrix.length; j++){
            marketMatrix[j][columnNumber].onActivate(resources,effect);
            if (j==0) marbleOnSlide = marketMatrix[j][columnNumber];
            else marketMatrix[j - 1][columnNumber] = marketMatrix[j][columnNumber];
        }
        marketMatrix[marketMatrix.length-1][columnNumber] = tempMarble;
        return resources;
    }

    /* same type marbles in the market refer to the same object, we instantiated only one marble for type*/
    /*create list of a certain number of references to Marbles with randomized order*/
    private List<Marble> createConfigurationList(int nWhite, int nRed, int nGrey, int nBlue, int nYellow, int nPurple){

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

}
