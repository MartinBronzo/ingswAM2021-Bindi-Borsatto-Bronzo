package it.polimi.ingsw.view.cli;

/**
 * This Enumeration contains the colors used in the CLI views for this application.
 */
public enum AnsiCommands {
    /**
     * The red color.
     */
    RED {
        @Override
        public String getTextColor() {
            return "\u001B[31m";
        }

        @Override
        public String getBackgroundColor() {
            return "\u001B[41m";
        }
    },
    /**
     * The green color.
     */
    GREEN {
        @Override
        public String getTextColor() {
            return "\u001B[32m";
        }

        @Override
        public String getBackgroundColor() {
            return "\u001B[42m";
        }
    },
    /**
     * The yellow color.
     */
    YELLOW {
        @Override
        public String getTextColor() {
            return "\u001B[33m";
        }

        @Override
        public String getBackgroundColor() {
            return "\u001B[43m";
        }

    },
    /**
     * The blue color.
     */
    BLUE {
        @Override
        public String getTextColor() {
            return "\u001B[34m";
        }

        @Override
        public String getBackgroundColor() {
            return "\u001B[44m";
        }

    },
    /**
     * The purple color.
     */
    PURPLE {
        @Override
        public String getTextColor() {
            return "\u001B[35m";
        }

        @Override
        public String getBackgroundColor() {
            return "\u001B[45m";
        }
    },
    /**
     * The white color.
     */
    WHITE {
        @Override
        public String getTextColor() {
            return "\u001B[37m";
        }

        @Override
        public String getBackgroundColor() {
            return "\u001B[47m";
        }
    },
    /**
     * The black color.
     */
    BLACK {
        @Override
        public String getTextColor() {
            return "\u001B[30m";
        }

        @Override
        public String getBackgroundColor() {
            return "\u001B[40m";
        }
    };

    /**
     * Returns the text color this enumeration value represents
     * @return the text color this enumeration value represents
     */
    public String getTextColor() {
        return "\u001B[0m";
    }

    /**
     * Returns the background color this enumeration value represents
     * @return the background color this enumeration value represents
     */
    public String getBackgroundColor() {
        return "\u001B[0m";
    }

    /**
     * Returns sets the style of the CLI to the default value
     * @return sets the style of the CLI to the default value
     */
    public static String resetStyle() {
        return "\u001B[0m";
    }

    /**
     * Returns the \n string
     * @return the \n string
     */
    public static String clearLine() {
        return "\33[1A\33[2K";
    }

    /**
     * Returns the clr string
     * @return the clr string
     */
    public static String clear() {
        return "\033[H\033[2J";
    }
}
