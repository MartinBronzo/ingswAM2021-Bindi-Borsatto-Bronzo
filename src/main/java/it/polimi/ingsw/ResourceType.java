package it.polimi.ingsw;

/*enum class to manage base Resource*/
/*every constant has his method which returns true only if is used for the same constant
* can be used also equals method, but this is specific for every constant*/
public enum ResourceType {
    COIN {
        @Override
        public boolean isCoin() {
            return true;
        }
    },
    STONE {
        @Override
        public boolean isStone() {
            return true;
        }
    },
    SERVANT {
        @Override
        public boolean isServant() {
            return true;
        }
    },
    SHIELD {
        @Override
        public boolean isShield() {
            return true;
        }
    },
    FAITHPOINT {
        @Override
        public boolean isFaithPoint() {
            return true;
        }
    };

    /*enum base methods: return false, because they're not the overrided metods*/
    public boolean isCoin(){
        return false;
    }
    public boolean isStone(){
        return false;
    }
    public boolean isServant(){
        return false;
    }
    public boolean isShield(){
        return false;
    }
    public boolean isFaithPoint(){
        return false;
    }
}
