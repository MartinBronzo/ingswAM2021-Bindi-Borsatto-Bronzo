package it.polimi.ingsw.model.resources;

import it.polimi.ingsw.model.marble.MarbleType;

/**
 * enum class to manage base Resources
 * every constant has his method which returns true only if it's used for the same constant
 * can be used also equals method, but this is specific for every constant
 */
public enum ResourceType {
    COIN {
        @Override
        public boolean isCoin() {
            return true;
        }

        @Override
        public MarbleType getCorrespondingMarble() {
            return MarbleType.YELLOWMARBLE;
        }
    },
    STONE {
        @Override
        public boolean isStone() {
            return true;
        }

        @Override
        public MarbleType getCorrespondingMarble() {
            return MarbleType.GREYMARBLE;
        }
    },
    SERVANT {
        @Override
        public boolean isServant() {
            return true;
        }

        @Override
        public MarbleType getCorrespondingMarble() {
            return MarbleType.PURPLEMARBLE;
        }
    },
    SHIELD {
        @Override
        public boolean isShield() {
            return true;
        }

        @Override
        public MarbleType getCorrespondingMarble() {
            return MarbleType.BLUEMARBLE;
        }
    },
    FAITHPOINT {
        @Override
        public boolean isFaithPoint() {
            return true;
        }

        @Override
        public MarbleType getCorrespondingMarble() {
            return MarbleType.REDMARBLE;
        }
    };

//poi fare da lì il getMarble();

    /*
        enum base methods:
        @return false by default
     */

    /**
     * Returns whether the enumeration corresponds to a coin
     * @return true if the enumeration corresponds to a coin, false otherwise
     */
    public boolean isCoin() {
        return false;
    }

    /**
     * Returns whether the enumeration corresponds to a stone
     * @return true if the enumeration corresponds to a stone, false otherwise
     */
    public boolean isStone() {
        return false;
    }

    /**
     * Returns whether the enumeration corresponds to a servant
     * @return true if the enumeration corresponds to a servant, false otherwise
     */
    public boolean isServant() {
        return false;
    }

    /**
     * Returns whether the enumeration corresponds to a shield
     * @return true if the enumeration corresponds to a shield, false otherwise
     */
    public boolean isShield() {
        return false;
    }

    /**
     * Returns whether the enumeration corresponds to a faith point
     * @return true if the enumeration corresponds to a faith point, false otherwise
     */
    public boolean isFaithPoint() {
        return false;
    }

    /**
     * Returns the corresponding MarbleType of this enumeration value (that is, the Marble which returns this resource once the Marble is
     * picked from the Market)
     * @return the corresponding MarbleType of this enumeration value
     */
    public MarbleType getCorrespondingMarble() {
        return null;
    }
}
