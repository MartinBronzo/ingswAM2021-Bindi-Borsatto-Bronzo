package it.polimi.ingsw.view.cli;

/**
 * This Enumeration contains the colors used in the CLI views for this application.
 */
public enum AnsiCommands {
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

    public String getTextColor() {
        return "\u001B[0m";
    }

    public String getBackgroundColor() {
        return "\u001B[0m";
    }

    public static String resetStyle() {
        return "\u001B[0m";
    }

    public static String clearLine() {
        return "\33[1A\33[2K";
    }

    public static String clear() {
        return "\033[H\033[2J";
    }
}
