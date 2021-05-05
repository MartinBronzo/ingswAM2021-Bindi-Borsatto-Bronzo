package it.polimi.ingsw.model;

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

    /**
     * enum base methods:
     *
     * @return false by default
     */
    public boolean isCoin() {
        return false;
    }

    public boolean isStone() {
        return false;
    }

    public boolean isServant() {
        return false;
    }

    public boolean isShield() {
        return false;
    }

    public boolean isFaithPoint() {
        return false;
    }

    public MarbleType getCorrespondingMarble() {
        return null;
    }
}