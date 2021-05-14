package it.polimi.ingsw.model.marble;

public enum MarbleType {
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
        public Marble getMarble() {
            return purpleMarble;
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
        @Override
        public Boolean isWhiteMarble() {
            return false;
        }
    },
    YELLOWMARBLE {
        @Override
        public Marble getMarble() {
            return yellowMarble;
        }
    };

    private final static Marble blueMarble = new BlueMarble();
    private final static Marble greyMarble = new GreyMarble();
    private final static Marble purpleMarble = new PurpleMarble();
    private final static Marble redMarble = new RedMarble();
    private final static Marble whiteMarble = new WhiteMarble();
    private final static Marble yellowMarble = new YellowMarble();

    public Marble getMarble() {
        return null;
    }
    public Boolean isWhiteMarble() {
        return false;
    }

}
