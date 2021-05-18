package it.polimi.ingsw.view.cli;

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

        @Override
        public String resetStyle() {
            return super.resetStyle();
        }

        @Override
        public String clearLine() {
            return super.clearLine();
        }

        @Override
        public String clear() {
            return super.clear();
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

        @Override
        public String resetStyle() {
            return super.resetStyle();
        }

        @Override
        public String clearLine() {
            return super.clearLine();
        }

        @Override
        public String clear() {
            return super.clear();
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

        @Override
        public String resetStyle() {
            return super.resetStyle();
        }

        @Override
        public String clearLine() {
            return super.clearLine();
        }

        @Override
        public String clear() {
            return super.clear();
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

        @Override
        public String resetStyle() {
            return super.resetStyle();
        }

        @Override
        public String clearLine() {
            return super.clearLine();
        }

        @Override
        public String clear() {
            return super.clear();
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

        @Override
        public String resetStyle() {
            return super.resetStyle();
        }

        @Override
        public String clearLine() {
            return super.clearLine();
        }

        @Override
        public String clear() {
            return super.clear();
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

        @Override
        public String resetStyle() {
            return super.resetStyle();
        }

        @Override
        public String clearLine() {
            return super.clearLine();
        }

        @Override
        public String clear() {
            return super.clear();
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

        @Override
        public String resetStyle() {
            return super.resetStyle();
        }

        @Override
        public String clearLine() {
            return super.clearLine();
        }

        @Override
        public String clear() {
            return super.clear();
        }
    };

    public String getTextColor(){ return "\u001B[0m"; }
    public String getBackgroundColor(){ return "\u001B[0m"; }
    public String resetStyle(){ return "\u001B[0m"; }
    public String clearLine(){ return "\33[1A\33[2K"; }
    public String clear(){ return "\033[H\033[2J"; }
}
