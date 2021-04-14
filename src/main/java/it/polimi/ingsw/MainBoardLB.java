package it.polimi.ingsw;

import it.polimi.ingsw.DevCards.DevGrid;
import it.polimi.ingsw.FaithTrack.FaithTrack;
import it.polimi.ingsw.FaithTrack.ReportNumOrder;
import it.polimi.ingsw.Interfaces.Deck;
import it.polimi.ingsw.LeaderCard.LeaderCards;

import java.io.File;
import java.util.List;

public class MainBoardLB {
    private static MainBoardLB instance;
    protected Deck leaderCardsDeck;

    //Inizializzazione FaithTrack (e anche il reportNumOrder deve essere creato) e settaggio per i vari playerBoard del faithtrack
    //Inizializzazione PopeTiles: controllare che il numero di pope tiles sia uguale e che l'ordine sia sempre lo stesso
    //Creazione delle LeaderCards e distribuzione carte ai players
    //=> creazioni con file config

    public static MainBoardLB instance(){
        if(instance == null)
            instance = new MainBoardLB();
        return instance;
    }

    public MainBoardLB(){
        this.leaderCardsDeck = new LeaderCardDeck();
    }


    /**
     * Initiates all the LeaderCards and creates a deck of them
     * @param config the file where to read the description of the LeaderCards
     */
    public void initLeaderCards(File config){

    }


}
