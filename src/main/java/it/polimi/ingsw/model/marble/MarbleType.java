package it.polimi.ingsw.model.marble;

import it.polimi.ingsw.model.faithTrack.ReportCell;
import it.polimi.ingsw.view.cli.AnsiCommands;

/**
 * This Enumeration represents the type of the Marbles that can be found in the Market.
 */
public enum MarbleType {
    /**
     * The Blue Marble.
     */
    BLUEMARBLE {
        @Override
        public Marble getMarble() {
            return blueMarble;
        }

        @Override
        public String toString() {
            return AnsiCommands.BLUE.getTextColor() + "Ⓑ" + AnsiCommands.resetStyle();
        }

        @Override
        public String getUrl() {return "/PUNCHBOARD/blueMarble.png";}
    },
    /**
     * The Grey Marble.
     */
    GREYMARBLE {
        @Override
        public Marble getMarble() {
            return greyMarble;
        }

        @Override
        public String toString() {
            return AnsiCommands.WHITE.getTextColor() + "Ⓖ" + AnsiCommands.resetStyle();
        }

        @Override
        public String getUrl() {return "/PUNCHBOARD/greyMarble.png";}
    },
    /**
     * The Purple Marble.
     */
    PURPLEMARBLE {
        @Override
        public Marble getMarble() {
            return purpleMarble;
        }

        @Override
        public String toString() {
            return AnsiCommands.PURPLE.getTextColor() + "Ⓟ" + AnsiCommands.resetStyle();
        }

        @Override
        public String getUrl() {return "/PUNCHBOARD/purpleMarble.png";}
    },
    /**
     * The Red Marble.
     */
    REDMARBLE {
        @Override
        public Marble getMarble() {
            return redMarble;
        }

        @Override
        public String toString() {
            return AnsiCommands.RED.getTextColor() + "Ⓡ" + AnsiCommands.resetStyle();
        }

        @Override
        public String getUrl() {return "/PUNCHBOARD/redMarble.png";}
    },
    /**
     * The White Marble.
     */
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
            return AnsiCommands.resetStyle() + "ⓦ" + AnsiCommands.resetStyle();
        }

        @Override
        public String getUrl() {return "/PUNCHBOARD/whiteMarble.png";}
    },
    /**
     * The Yellow Marble.
     */
    YELLOWMARBLE {
        @Override
        public Marble getMarble() {
            return yellowMarble;
        }

        @Override
        public String toString() {
            return AnsiCommands.YELLOW.getTextColor() + "Ⓨ" + AnsiCommands.resetStyle();
        }

        @Override
        public String getUrl() {return "/PUNCHBOARD/yellowMarble.png";}
    };

    private final static Marble blueMarble = new BlueMarble();
    private final static Marble greyMarble = new GreyMarble();
    private final static Marble purpleMarble = new PurpleMarble();
    private final static Marble redMarble = new RedMarble();
    private final static Marble whiteMarble = new WhiteMarble();
    private final static Marble yellowMarble = new YellowMarble();

    /**
     * Returns the Marble object corresponding to this enumeration value
     * @return the Marble object corresponding to this enumeration value
     */
    public Marble getMarble() {
        return null;
    }

    /**
     * Returns the path to the image corresponding to this marble
     * @return the path to the image corresponding to this marble
     */
    public String getUrl() {
        return null;
    }

    /**
     * Returns whether this enumeration value is the one of a WhiteMarble
     * @return true if this enumeration value is the one of a WhiteMarble, false otherwise
     */
    public Boolean isWhiteMarble() {
        return false;
    }
}
