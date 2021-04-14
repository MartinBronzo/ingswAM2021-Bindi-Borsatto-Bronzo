package it.polimi.ingsw;

import it.polimi.ingsw.DevCards.DevGrid;
import it.polimi.ingsw.FaithTrack.FaithTrack;
import it.polimi.ingsw.FaithTrack.ReportNumOrder;
import it.polimi.ingsw.Interfaces.Deck;
import it.polimi.ingsw.LeaderCard.LeaderCard;
import it.polimi.ingsw.LeaderCard.LeaderCards;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainBoardLB {
    private static MainBoardLB instance;
    protected Deck leaderCardsDeck;
    protected List<PlayerBoard> playerBoardsList;
    protected int numberOfPlayers;
    protected FaithTrack faithTrack;
    protected int numberOfLeaderCardsToGive;

    //Inizializzazione FaithTrack (e anche il reportNumOrder deve essere creato) e settaggio per i vari playerBoard del faithtrack
    //Inizializzazione PopeTiles: controllare che il numero di pope tiles sia uguale e che l'ordine sia sempre lo stesso
    //Creazione delle LeaderCards e distribuzione carte ai players
    //=> creazioni con file config
    //GetPlayerBoards()

    public static MainBoardLB instance(int numberOfPlayers){
        if(instance == null)
            instance = new MainBoardLB(numberOfPlayers);
        return instance;
    }

    public MainBoardLB(int numberOfPlayers){
        this.leaderCardsDeck = null;
        this.numberOfPlayers = numberOfPlayers;
        this.playerBoardsList = new ArrayList<>(numberOfPlayers);
        this.numberOfLeaderCardsToGive = 4;
    }

    //In futuro questi metodi saranno private e chiamati da un unico metodo pubblico initGame objects
    public void initFaithTrack(File config){
        this.faithTrack = FaithTrack.instance(config);
        //Configurare anche il suo reportnUm ordrer a meno che nella configurazione del faithtrack con file non venga autmaticamente fatto
    }

    public void initPlayerBoards(){
        for(int i = 0; i < this.numberOfPlayers; i++)
            this.playerBoardsList.add(new PlayerBoard());
    }

    public void initPlayerBoardsFaithTrack(){
        for(PlayerBoard pB: playerBoardsList)
            pB.setPlayerFaithLevelFaithTrack(this.faithTrack);
    }

    /**
     * Initiates all the LeaderCards and creates a deck of them
     * @param config the file where to read the description of the LeaderCards
     */
    public void initLeaderCards(File config) throws ParserConfigurationException, NegativeQuantityException, SAXException, IOException {
        this.leaderCardsDeck = new LeaderCardDeck(config);
        this.leaderCardsDeck.shuffle();
        //Collections.shuffle(this.playerBoardList);

        List<LeaderCard> tmpList;
        for(PlayerBoard pB: this.playerBoardsList){
            tmpList = new ArrayList<>();
            for(int i = 0; i < this.numberOfLeaderCardsToGive; i++)
                tmpList.add((LeaderCard) this.leaderCardsDeck.drawFromDeck());
            pB.setNotPlayedLeaderCards(tmpList);
        }
        //Da qualche parte nel controller implementare il fatto che di queste che riceve il player ne può mantenere solo una certa
    }





}
