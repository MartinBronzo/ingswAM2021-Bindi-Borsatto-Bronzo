package it.polimi.ingsw.model.marble;

import it.polimi.ingsw.view.cli.AnsiCommands;

public enum MarbleType {
    BLUEMARBLE {
        @Override
        public Marble getMarble() {
            return blueMarble;
        }
        @Override
        public String toString() {
            return AnsiCommands.BLUE.getTextColor() +"Ⓑ"+AnsiCommands.resetStyle();
        }
    },
    GREYMARBLE {
        @Override
        public Marble getMarble() {
            return greyMarble;
        }
        @Override
        public String toString() {
            return AnsiCommands.GREEN.getTextColor() +"Ⓖ"+AnsiCommands.resetStyle();
        }
    },
    PURPLEMARBLE {
        @Override
        public Marble getMarble() {
            return purpleMarble;
        }
        @Override
        public String toString() {
            return AnsiCommands.PURPLE.getTextColor() +"Ⓟ"+AnsiCommands.resetStyle();
        }
    },
    REDMARBLE {
        @Override
        public Marble getMarble() {
            return redMarble;
        }
        @Override
        public String toString() {
            return AnsiCommands.RED.getTextColor() +"Ⓡ"+AnsiCommands.resetStyle();
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
        @Override
        public String toString() {
            return AnsiCommands.WHITE.getTextColor() +"ⓦ"+AnsiCommands.resetStyle();
        }
    },
    YELLOWMARBLE {
        @Override
        public Marble getMarble() {
            return yellowMarble;
        }
        @Override
        public String toString() {
            return AnsiCommands.YELLOW.getTextColor() +"Ⓨ"+AnsiCommands.resetStyle();
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
