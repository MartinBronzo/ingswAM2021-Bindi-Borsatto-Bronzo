package it.polimi.ingsw.model.leaderCard;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.UnmetRequirementException;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.leaderCard.leaderEffects.Effect;
import it.polimi.ingsw.model.leaderCard.leaderEffects.WhiteMarbleLeaderEffect;
import it.polimi.ingsw.model.board.PlayerResourcesAndCards;
import it.polimi.ingsw.model.resources.ResourceType;

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

    /**
     * Sets the NotPlayedCards inside this LeaderCards
     * @param notPlayedCards the NotPlayedCards to be set inside this LeaderCards
     */
    public void setNotPlayedCards(List<LeaderCard> notPlayedCards) {
        if (!this.areNotPlayedCardsSet) {
            this.notPlayedCards = LeaderCards.cloneList(notPlayedCards);
            this.areNotPlayedCardsSet = true;
        }
    }

    /**
     * Returns a copy of the specified LeaderCard list
     * @param original the LeaderCard list to be cloned
     * @return a copy of the specified LeaderCard list
     */
    public static List<LeaderCard> cloneList(List<LeaderCard> original) {
        List<LeaderCard> result = new ArrayList<>();
        for (LeaderCard lD : original) {
            result.add(new LeaderCard(lD));
        }
        return result;
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
        if (tmp.isOneShotCard())
            alreadyUsedOneShotCard.add(leaderCard);
        return tmp;
    }

    /**
     * Returns the effect of the LeaderCard whose position in the ordered collection of active LeaderCards is given as a parameter
     *
     * @param cardIndex the position inside the ordered collection of active LeaderCards of the desired LeaderCard
     * @return the effect of the desired LeaderCard
     * @throws IllegalArgumentException if the given index is out of bound
     */
    public Effect getEffectFromCard(int cardIndex) throws IllegalArgumentException {
        if (cardIndex >= this.activeCards.size())
            throw new IllegalArgumentException("The player doesn't hold this card (the given index is out of bound)!");
        return this.activeCards.get(cardIndex).getEffect();
    }

    /**
     * Returns the LeaderCard whose position in the ordered collection of not player LeaderCards is given as a parameter
     *
     * @param cardIndex the position inside the ordered collection of not player LeaderCards of the desired LeaderCard (it is a non-negative integer)
     * @return the desired LeaderCard
     * @throws IllegalArgumentException if the given index is out of bound
     */
    public LeaderCard getNotPlayedLeaderCardFromIndex(int cardIndex) throws IllegalArgumentException {
        if (cardIndex >= this.notPlayedCards.size())
            throw new IllegalArgumentException("The player doesn't hold this card (the given index is out of bound)!");
        return this.notPlayedCards.get(cardIndex);
    }

    /**
     * Adds the leader Card to the list of activated one shot cards
     *
     * @param leaderCard to be add to oneshot cards
     * @return true whether the card is correctly added or false if the card isn't a one shot card or if it is present in one shot cards list
     * @throws IllegalArgumentException if the card is not active or if the player doesn't hold it
     * @throws NullPointerException     if leadercard is null
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
        return LeaderCards.cloneList(this.activeCards);
    }
    /*    public List<LeaderCard> getActiveCards() {
        if (this.activeCards.isEmpty() == true)
            //    throw new IllegalActionException("The player has no active cards!");
            return new ArrayList<>();
        List<LeaderCard> result = new ArrayList<>();
        for (LeaderCard leaderCard : this.activeCards)
            result.add(new LeaderCard(leaderCard));
        return result;
    }*/

    /**
     * Returns a copy of the not-played card list
     *
     * @return a copy of the not-played card list
     */
    public List<LeaderCard> getNotPlayedCards() {
        return LeaderCards.cloneList(this.notPlayedCards);
    }
    /*    public List<LeaderCard> getNotPlayedCards() {
        if (this.notPlayedCards.isEmpty() == true)
            return new ArrayList<>();
        List<LeaderCard> result = new ArrayList<>();
        for (LeaderCard leaderCard : this.notPlayedCards)
            result.add(new LeaderCard(leaderCard));
        return result;
    }*/

    /**
     * Returns a copy of the one-shot LeaderCards which were used once before
     *
     * @return a copy of the already used one-shot LeaderCards
     */
    public List<LeaderCard> getAlreadyUsedOneShotCard() {
        return LeaderCards.cloneList(alreadyUsedOneShotCard);
    }
/*    public List<LeaderCard> getAlreadyUsedOneShotCard() {
        if (this.alreadyUsedOneShotCard.isEmpty() == true)
            return new ArrayList<>();
        List<LeaderCard> result = new ArrayList<>();
        for (LeaderCard leaderCard : this.alreadyUsedOneShotCard)
            result.add(new LeaderCard(leaderCard));
        return result;
    }*/


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
        return !this.notPlayedCards.contains(leaderCard);
    }


/*    public List<LeaderCard> getNotPlayedCards() {
        return new ArrayList<>(notPlayedCards);
    }*/

    /**
     * Constructs a copy of the specified LeaderCards object
     *
     * @param original the LeaderCards object to be cloned
     */
    public LeaderCards(LeaderCards original) {
        this.activeCards = LeaderCards.cloneList(original.activeCards);
        this.notPlayedCards = LeaderCards.cloneList(original.notPlayedCards);
        this.areNotPlayedCardsSet = original.areNotPlayedCardsSet;
        this.alreadyUsedOneShotCard = LeaderCards.cloneList(original.alreadyUsedOneShotCard);

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof LeaderCards))
            return false;
        LeaderCards tmp = (LeaderCards) obj;
        return this.activeCards.containsAll(tmp.activeCards) && tmp.activeCards.containsAll(this.activeCards) &&
                this.notPlayedCards.containsAll(tmp.notPlayedCards) && tmp.notPlayedCards.containsAll(this.notPlayedCards) &&
                this.areNotPlayedCardsSet == tmp.areNotPlayedCardsSet &&
                this.alreadyUsedOneShotCard.containsAll(tmp.alreadyUsedOneShotCard) && tmp.alreadyUsedOneShotCard.containsAll(this.alreadyUsedOneShotCard);
    }

    /**
     * Returns whether the NotPlayedCards have been set yet
     * @return true if the NotPlayedCards have been set yet, false otherwise
     */
    public boolean isAreNotPlayedCardsSet() {
        return areNotPlayedCardsSet;
    }

    /**
     * Returns the amount of NotActiveCards this LeaderCards holds
     * @return the amount of NotActiveCards this LeaderCards holds
     */
    public int getNotActiveCardsSize() {
        return this.notPlayedCards.size();
    }

    /**
     * Returns the amount of ActiveCards this LeaderCards holds
     * @return the amount of ActiveCards this LeaderCards holds
     */
    public int getActiveCardsSize() {
        return this.activeCards.size();
    }

    /**
     * Returns whether a WhiteMarble LeaderCard is active in this LeaderCards
     * @return true if there is an active WhiteMarble LeaderCard in this LeaderCards, false otherwise
     */
    public boolean checkIfWhiteMarbleActive() {
        for (LeaderCard lD : this.activeCards)
            if (lD.getEffect() instanceof WhiteMarbleLeaderEffect)
                return true;
        return false;
    }
}
