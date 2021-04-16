package it.polimi.ingsw.LeaderCard;

import it.polimi.ingsw.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.LeaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.PlayerResourcesAndCards;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.UnmetRequirementException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class collects all the LeaderCards the player has throughout the game. It holds both played and not-played cards. Once a
 * card is discarded, this class loses track of it.
 */
public class LeaderCards {
    private List<LeaderCard> activeCards;
    private List<LeaderCard> notPlayedCards;
    private boolean areNotPlayedCardsSet;
    private List<LeaderCard> alreadyUsedOneShotCard;

    /**
     * Constructs a LeaderCards which hold only not-played cards. It is called at the beginning of the game
     *
     * @param notPlayedCards all the LeaderCards the player receives
     */
    public LeaderCards(List<LeaderCard> notPlayedCards) {
        this.activeCards = new ArrayList<>();
        this.notPlayedCards = new ArrayList<>(notPlayedCards);
        this.areNotPlayedCardsSet = true;
        this.alreadyUsedOneShotCard = new ArrayList<>();
    }

    /**
     * Constructs a LeaderCards which hold no Cards.
     */
    public LeaderCards() {
        this.activeCards = new ArrayList<>();
        this.notPlayedCards = new ArrayList<>();
        this.areNotPlayedCardsSet = false;
        this.alreadyUsedOneShotCard = new ArrayList<>();
    }

    public void setNotPlayedCards(List<LeaderCard> notPlayedCards) {
        if (this.areNotPlayedCardsSet == false) {
            this.notPlayedCards = notPlayedCards;
            this.areNotPlayedCardsSet = true;
        }
    }

    /**
     * Discards the specified LeaderCard if it hasn't been played by the Player, yet
     *
     * @param leaderCard the LeaderCard the player wants to discard
     * @return the resources the player gets when they discard the specified LeaderCard
     * @throws IllegalArgumentException if the player wants to discard an already active card
     */
    public HashMap<ResourceType, Integer> discardLeaderCard(LeaderCard leaderCard) throws IllegalArgumentException {
        if (activeCards.contains(leaderCard))
            throw new IllegalArgumentException("Played cards can't be discarded!");
        if (!notPlayedCards.contains(leaderCard))
            throw new IllegalArgumentException("The player can't discards cards they don't hold!");
        notPlayedCards.remove(leaderCard);
        return leaderCard.getOutputWhenDiscarded();
    }

    /**
     * Activates the specified LeaderCard if the player meets all the requirements
     *
     * @param leaderCard the LeaderCard tha player wants to activate
     * @return true if the card was correctly activated, false if the card was already activate
     */
    public boolean activateLeaderCard(LeaderCard leaderCard, PlayerResourcesAndCards playerResourcesAndCards) throws UnmetRequirementException {
        if (activeCards.contains(leaderCard))
            return false;
        if (!notPlayedCards.contains(leaderCard))
            throw new IllegalArgumentException("The player doesn't have this card!");
        if (leaderCard.checkRequirements(playerResourcesAndCards)) {
            notPlayedCards.remove(leaderCard);
            activeCards.add(leaderCard);
            return true;
        }
        return false;
    }

    /**
     * Returns the effect of the specified LeaderCard if it is active
     *
     * @param leaderCard the LeaderCard the player wants to use
     * @return the effect of the specified LeaderCard
     * @throws IllegalArgumentException if the card is not active or if the player doesn't hold it
     */
    public Effect getEffectFromCard(LeaderCard leaderCard) throws IllegalArgumentException, IllegalActionException {
        if (!this.activeCards.contains(leaderCard))
            if (!this.notPlayedCards.contains(leaderCard))
                throw new IllegalArgumentException("The player can't use the effect of a LeaderCard they don't have!");
            else
                throw new IllegalArgumentException("The player can't use the effect of a LeaderCard they haven't played yet!");
        if (alreadyUsedOneShotCard.contains(leaderCard))
            throw new IllegalActionException("This Card can only be used once: it has already been used!");
        Effect tmp = leaderCard.getEffect();
        if (tmp.isOneShotCard() == true)
            alreadyUsedOneShotCard.add(leaderCard);
        return tmp;
    }

    /**
     * Adds the leader Card to the list of activated one shot cards
     *
     * @param leaderCard to be add to oneshot cards
     * @return true whether the card is correctly added or false if the card isn't a one shot card or if it is present in one shot cards list
     * @throws IllegalArgumentException if the card is not active or if the player doesn't hold it
     * @throws NullPointerException if leadercard is null
     */
    public boolean addCardToActivatedOneShotCards(LeaderCard leaderCard) throws IllegalArgumentException, NullPointerException {
        if (leaderCard == null) throw new NullPointerException("leaderCard is null");
        if (!this.activeCards.contains(leaderCard))
            if (!this.notPlayedCards.contains(leaderCard))
                throw new IllegalArgumentException("The player can't use the effect of a LeaderCard they don't have!");
            else
                throw new IllegalArgumentException("The player can't use the effect of a LeaderCard they haven't played yet!");
        if (!alreadyUsedOneShotCard.contains(leaderCard) && leaderCard.getEffect().isOneShotCard()) {
            alreadyUsedOneShotCard.add(leaderCard);
            return true;
        }
        return false;
    }

    /**
     * Returns the points the player gets from all of their active cards
     *
     * @return the total point of the active cards
     */
    public int getLeaderCardsPoints() {
        int totalPoints = 0;
        for (LeaderCard card : activeCards)
            totalPoints = totalPoints + card.getVictoryPoints();
        return totalPoints;
    }

    /**
     * Returns the requirements of the specified LeaderCard if the player holds it
     *
     * @param leaderCard the LeaderCards the player wants to learn about
     * @return the list of requirements of the specified card
     * @throws IllegalArgumentException if the player doesn't hold the specified LeaderCard in any form (played or not-played)
     */
    public List<Requirement> getLeaderCardRequirements(LeaderCard leaderCard) throws IllegalArgumentException {
        if (!activeCards.contains(leaderCard) && !notPlayedCards.contains(leaderCard))
            throw new IllegalArgumentException("The player doesn't hold this card!");
        return leaderCard.getRequirementsListSafe();
    }


/*    public List<LeaderCard> getActiveCards() {
        return new ArrayList<>(activeCards);
    }
*/

    /**
     * Returns a copy of the active cards list
     *
     * @return a copy of the active cards list
     */
    public List<LeaderCard> getActiveCards() {
        if (this.activeCards.isEmpty() == true)
            //    throw new IllegalActionException("The player has no active cards!");
            return new ArrayList<>();
        List<LeaderCard> result = new ArrayList<>();
        for (LeaderCard leaderCard : this.activeCards)
            result.add(new LeaderCard(leaderCard));
        return result;
    }

    /**
     * Returns a copy of the not-played card list
     *
     * @return a copy of the not-played card list
     */
    public List<LeaderCard> getNotPlayedCards() {
        if (this.notPlayedCards.isEmpty() == true)
            return new ArrayList<>();
        List<LeaderCard> result = new ArrayList<>();
        for (LeaderCard leaderCard : this.notPlayedCards)
            result.add(new LeaderCard(leaderCard));
        return result;
    }

    /**
     * Returns a copy of the one-shot LeaderCards which were used once before
     *
     * @return a copy of the already used one-shot LeaderCards
     */
    public List<LeaderCard> getAlreadyUsedOneShotCard() {
        if (this.alreadyUsedOneShotCard.isEmpty() == true)
            return new ArrayList<>();
        List<LeaderCard> result = new ArrayList<>();
        for (LeaderCard leaderCard : this.alreadyUsedOneShotCard)
            result.add(new LeaderCard(leaderCard));
        return result;
    }

    /**
     * Returns whether the player holds the specified LeaderCard
     *
     * @param leaderCard a LeaderCard
     * @return true if the card is active, false if the card is not played, yet
     * @throws IllegalArgumentException if the player doesn't hold the card
     */
    public boolean isLeaderCardActive(LeaderCard leaderCard) throws IllegalArgumentException {
        if (!this.activeCards.contains(leaderCard) && !this.notPlayedCards.contains(leaderCard))
            throw new IllegalArgumentException("The player doesn't hold this card!");
        if (this.notPlayedCards.contains(leaderCard))
            return false;
        return true;
    }


/*    public List<LeaderCard> getNotPlayedCards() {
        return new ArrayList<>(notPlayedCards);
    }*/


}
