package it.polimi.ingsw;

import it.polimi.ingsw.Interfaces.Deck;
import it.polimi.ingsw.LeaderCard.LeaderCard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * This card holds all the LeaderCards the MainBoard has at the beginning of the game.
 */
public class LeaderCardDeck implements Deck {
    List<LeaderCard> leaderCards;

    /**
     * Constructs an empty deck of LeaderCards
     */
    public LeaderCardDeck(){
        this.leaderCards = new ArrayList<>();
    }

    /**
     * Constructs a deck of the specified LeaderCards
     * @param leaderCards the LeaderCards to be stored in the deck
     */
    public LeaderCardDeck(List<LeaderCard> leaderCards){
        this.leaderCards = leaderCards;
    }

    /**
     * Adds the specified LeaderCard to the deck
     * @param leaderCard a LeaderCard to be added
     */
    public void addLeaderCard(LeaderCard leaderCard){
        this.leaderCards.add(leaderCard);
    }

    /**
     * Draws a LeaderCard from the deck (it removes it)
     * @return the drawn LeaderCard
     */
    @Override
    public Object drawFromDeck() {
        LeaderCard leaderCard = this.leaderCards.get(0);
        this.leaderCards.remove(leaderCard);
        return leaderCard;
    }

    /**
     * Draws the LeaderCard at the top of the deck (it removes it)
     * @return the drawn LeaderCard
     */
    @Override
    public Object getFirst() {
        LeaderCard leaderCard = this.leaderCards.get(0);
        this.leaderCards.remove(leaderCard);
        return leaderCard;
    }

    @Override
    public boolean shuffle() {
        Collections.shuffle(this.leaderCards);
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.leaderCards.isEmpty();
    }

    @Override
    public int size() {
        return this.leaderCards.size();
    }

    public List<LeaderCard> getCopyLeaderCards(){
        List<LeaderCard> result = new ArrayList<>();
        for(LeaderCard lD: this.leaderCards)
            result.add(new LeaderCard(lD));
        return result;
    }
}
