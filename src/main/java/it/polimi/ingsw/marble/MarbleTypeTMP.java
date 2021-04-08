package it.polimi.ingsw.marble;

import it.polimi.ingsw.LeaderCard.leaderEffects.Effect;
import it.polimi.ingsw.ResourceType;
import it.polimi.ingsw.exceptions.NegativeQuantityException;

import java.util.HashMap;

public enum MarbleTypeTMP {
    BLUEMARBLE {
        @Override
        public Marble getMarble() {
            return blueMarble;
        }
    },
    GREYMARBLE {
        @Override
        public Marble getMarble() {
            return greyMarble;
        }
    },
    PURPLEMARBLE {
        @Override
        public Marble getMarble() { return purpleMarble;
        }
    },
    REDMARBLE {
        @Override
        public Marble getMarble() {
            return redMarble;
        }
    },
    WHITEMARBLE {
        @Override
        public Marble getMarble() {
            return whiteMarble;
        }
    },
    YELLOWMARBLE {
        @Override
        public Marble getMarble() {
            return yellowMarble;
        }
    };

    private final static Marble blueMarble= new BlueMarble();
    private final static Marble greyMarble = new GreyMarble();
    private final static Marble purpleMarble = new PurpleMarble();
    private final static Marble redMarble = new RedMarble();
    private final static Marble whiteMarble = new WhiteMarble();
    private final static Marble yellowMarble = new YellowMarble();

    public Marble getMarble() { return null;
    }


}

