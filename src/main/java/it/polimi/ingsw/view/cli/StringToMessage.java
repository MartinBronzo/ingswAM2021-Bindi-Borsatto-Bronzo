package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.network.messages.fromClient.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used to create client response messages from cli commands. it checks if commands are well formatted
 */
public class StringToMessage {

    /**
     * Generates GetFromMatrixMessage for market related Commands
     * @param string to be transformed in GetFromMatrixMessage, Ex: "row 3; 1, 2, 4;"
     * @return the Message generated
     * @throws IllegalArgumentException if the string is not well formatted
     */
    public static GetFromMatrixMessage toMatrixMessageLine(String string) throws IllegalArgumentException {
        int colNumber;
        int rowNumber;
        List<Integer> leaderList;
        try {
            String[] infos = string.split(";");
            String[] rcInfos = infos[0].split("\\s+");
            if (rcInfos[0].equalsIgnoreCase("row")) {
                rowNumber = Integer.parseInt(rcInfos[1]);
                colNumber = 0;
            } else if (rcInfos[0].equalsIgnoreCase("column")) {
                rowNumber = 0;
                colNumber = Integer.parseInt(rcInfos[1]);
            } else {
                throw new IllegalArgumentException("String is not well formatted");
            }
            String[] leaderIds = infos[1].split(",");
            leaderList = Arrays.stream(leaderIds).map(String::strip).filter(id -> id.length() != 0).map(Integer::parseInt).collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalArgumentException("String is not well formatted");
        }
        return new GetFromMatrixMessage(rowNumber, colNumber, leaderList);
    }

    /**
     * Generates GetFromMatrixMessage for specific cell Commands
     * @param string to be transformed in GetFromMatrixMessage, Ex: 2; 3; 1, 2, 4;"
     * @return the Message generated
     * @throws IllegalArgumentException if the string is not well formatted
     */
    public static GetFromMatrixMessage toMatrixMessageCell(String string) throws IllegalArgumentException {
        int colNumber;
        int rowNumber;
        List<Integer> leaderList;
        try {
            String[] infos = Arrays.stream(string.split(";")).map(String::strip).toArray(String[]::new);
            rowNumber = Integer.parseInt(infos[0]);
            colNumber = Integer.parseInt(infos[1]);
            String[] leaderIds = infos[2].split(",");
            leaderList = Arrays.stream(leaderIds).map(String::strip).filter(id -> id.length() != 0).map(Integer::parseInt).collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalArgumentException("String is not well formatted");
        }
        return new GetFromMatrixMessage(rowNumber, colNumber, leaderList);
    }

    /**
     * Generates BuyDevCardMessage from cli command
     * @param string to be transformed in BuyDevCardMessage, Ex: "2; 3; 1, 2, 4; COIN 2 2, STONE 2 2; COIN 2, SERVANT 1; STONE 2, SERVANT 2; 4;"
     * @return the Message generated
     * @throws IllegalArgumentException if the string is not well formatted
     */
    public static BuyDevCardMessage toBuyDevCardMessage(String string) throws IllegalArgumentException {
        int colNumber;
        int rowNumber;
        List<Integer> leaderList;
        List<DepotParams> depotParamsList;
        HashMap<ResourceType, Integer> leaderMap;
        HashMap<ResourceType, Integer> strongboxMap;
        int devslot;
        try {
            String[] infos = Arrays.stream(string.split(";")).map(String::strip).toArray(String[]::new);
            rowNumber = Integer.parseInt(infos[0]);
            colNumber = Integer.parseInt(infos[1]);
            String[] leaderIds = infos[2].split(",");
            leaderList = Arrays.stream(leaderIds).map(String::strip).filter(id -> id.length() != 0).map(Integer::parseInt).collect(Collectors.toList());
            depotParamsList = Arrays.stream(infos[3].split(",")).map(String::strip).filter(id -> id.length() != 0).map(StringToMessage::toDepotParams).collect(Collectors.toList());
            leaderMap = toResourceHashMap(infos[4]);
            strongboxMap = toResourceHashMap(infos[5]);
            devslot = Integer.parseInt(infos[6].strip());
        } catch (Exception e) {
            throw new IllegalArgumentException("String is not well formatted");
        }
        return new BuyDevCardMessage(rowNumber, colNumber, leaderList, depotParamsList, leaderMap, strongboxMap, devslot);
    }

    /**
     * Generates BuyFromMarketMessage from cli command
     * @param string to be transformed in BuyFromMarketMessage, Ex: "row 3; 1, 2, 4; COIN 2 2, STONE 2 2; COIN 2, SERVANT 1; STONE 2, SERVANT 2;"
     * @return the Message generated
     * @throws IllegalArgumentException if the string is not well formatted
     */
    public static BuyFromMarketMessage toBuyFromMarketMessage(String string) throws IllegalArgumentException {
        int colNumber;
        int rowNumber;
        List<Integer> leaderList;
        List<DepotParams> depotParamsList;
        HashMap<ResourceType, Integer> leaderMap;
        HashMap<ResourceType, Integer> discardMap;
        try {
            String[] infos = string.split(";");
            String[] rcInfos = infos[0].split("\\s+");
            if (rcInfos[0].equalsIgnoreCase("row")) {
                rowNumber = Integer.parseInt(rcInfos[1]);
                colNumber = 0;
            } else if (rcInfos[0].equalsIgnoreCase("column")) {
                rowNumber = 0;
                colNumber = Integer.parseInt(rcInfos[1]);
            } else {
                throw new IllegalArgumentException("String is not well formatted");
            }
            String[] leaderIds = infos[1].split(",");
            leaderList = Arrays.stream(leaderIds).map(String::strip).filter(id -> id.length() != 0).map(Integer::parseInt).collect(Collectors.toList());
            depotParamsList = Arrays.stream(infos[2].split(",")).map(String::strip).filter(id -> id.length() != 0).map(StringToMessage::toDepotParams).collect(Collectors.toList());
            leaderMap = toResourceHashMap(infos[3]);
            discardMap = toResourceHashMap(infos[4]);
        } catch (Exception e) {
            throw new IllegalArgumentException("String is not well formatted");
        }
        return new BuyFromMarketMessage(rowNumber, colNumber, leaderList, depotParamsList, leaderMap, discardMap);
    }

    /**
     * Generates ActivateProductionMessage from cli command
     * @param string to be transformed in ActivateProductionMessage, Ex: "1, 2, 4; 2 COIN, 1 SERVANT; TRUE, COIN SERVANT, STONE; COIN 2 2, STONE 2 2;  COIN 2, SERVANT 1; STONE 2, SERVANT 2;"
     * @return the Message generated
     * @throws IllegalArgumentException if the string is not well formatted
     */
    public static ActivateProductionMessage toActivateProductionMessage(String string) throws IllegalArgumentException {
        List<Integer> devCardList;
        HashMap<Integer, ResourceType> leaderHashMap;
        BaseProductionParams baseProductionParams;
        List<DepotParams> depotParamsList;
        HashMap<ResourceType, Integer> leaderSlotMap;
        HashMap<ResourceType, Integer> strongboxMap;
        try {
            String[] infos = string.split(";");
            String[] devCardsIds = infos[0].split(",");
            devCardList = Arrays.stream(devCardsIds).map(String::strip).filter(id -> id.length() != 0).map(Integer::parseInt).collect(Collectors.toList());
            leaderHashMap = toLeadersHashMap(infos[1]);
            baseProductionParams = toBaseProductionParams(infos[2]);
            depotParamsList = Arrays.stream(infos[3].split(",")).map(String::strip).filter(id -> id.length() != 0).map(StringToMessage::toDepotParams).collect(Collectors.toList());
            leaderSlotMap = toResourceHashMap(infos[4]);
            strongboxMap = toResourceHashMap(infos[5]);
        } catch (Exception e) {
            throw new IllegalArgumentException("String is not well formatted");
        }
        return new ActivateProductionMessage(devCardList, leaderHashMap, baseProductionParams, depotParamsList, leaderSlotMap, strongboxMap);
    }

    /**
     * Generates GetProductionCostMessage from cli command
     * @param string to be transformed in GetProductionCostMessage, Ex: "1, 2, 4; 1, 2, 4; TRUE, COIN SERVANT, STONE;"
     * @return the Message generated
     * @throws IllegalArgumentException if the string is not well formatted
     */
    public static GetProductionCostMessage toGetProductionCostMessage(String string) throws IllegalArgumentException {
        List<Integer> devCardList;
        List<Integer> leaderList;
        BaseProductionParams baseProductionParams;
        try {
            String[] infos = string.split(";");
            devCardList = Arrays.stream(infos[0].split(",")).map(String::strip).filter(id -> id.length() != 0).map(Integer::parseInt).collect(Collectors.toList());
            leaderList = Arrays.stream(infos[1].split(",")).map(String::strip).filter(id -> id.length() != 0).map(Integer::parseInt).collect(Collectors.toList());
            baseProductionParams = toBaseProductionParams(infos[2]);
        } catch (Exception e) {
            throw new IllegalArgumentException("String is not well formatted");
        }
        return new GetProductionCostMessage(devCardList, leaderList, baseProductionParams);
    }

    /**
     * Generates DepotParams from cli sub commands
     * it's public just for Text Purpose
     * @param string to be transformed in DepotParams"
     * @return the DepotParams generated
     * @throws IllegalArgumentException if the string is not well formatted
     */
    public static DepotParams toDepotParams(String string) throws IllegalArgumentException {
        ResourceType resource;
        int qt;
        int shelf;
        try {
            String[] infos = Arrays.stream(string.split("\\s+")).filter(subString -> subString.length() != 0).toArray(String[]::new);
            resource = ResourceType.valueOf(infos[0].strip());
            qt = Integer.parseInt(infos[1].strip());
            shelf = Integer.parseInt(infos[2].strip());
        } catch (Exception e) {
            throw new IllegalArgumentException("String is not well formatted");
        }
        return new DepotParams(resource, qt, shelf);
    }

    /**
     * Generates an HashMap from cli sub commands
     * it's public just for Text Purpose
     * @param string to be transformed in a hashMap"
     * @return the hashMap generated
     * @throws IllegalArgumentException if the string is not well formatted
     */
    public static HashMap<ResourceType, Integer> toResourceHashMap(String string) throws IllegalArgumentException {
        ResourceType resource;
        int qt;
        List<String> subStrings;
        HashMap<ResourceType, Integer> hashMap = new HashMap<>();
        try {
            String[] infos = Arrays.stream(string.split(",")).map(String::strip).filter(subString -> subString.length() != 0).toArray(String[]::new);
            for (String info : infos) {
                subStrings = Arrays.stream(info.split("\\s+")).map(String::strip).filter(sub -> sub.length() != 0).collect(Collectors.toList());
                resource = ResourceType.valueOf(subStrings.get(0));
                qt = Integer.parseInt(subStrings.get(1));
                hashMap.put(resource, qt);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("String is not well formatted");
        }
        return hashMap;
    }

    /**
     * Generates an HashMap from cli sub commands
     * it's public just for Text Purpose
     * @param string to be transformed in a hashMap"
     * @return the hashMap generated
     * @throws IllegalArgumentException if the string is not well formatted
     */
    public static HashMap<Integer, ResourceType> toLeadersHashMap(String string) throws IllegalArgumentException {
        ResourceType resource;
        int leaderId;
        List<String> subStrings;
        HashMap<Integer, ResourceType> hashMap = new HashMap<>();
        try {
            String[] infos = Arrays.stream(string.split(",")).map(String::strip).filter(subString -> subString.length() != 0).toArray(String[]::new);
            for (String info : infos) {
                subStrings = Arrays.stream(info.split("\\s+")).map(String::strip).filter(sub -> sub.length() != 0).collect(Collectors.toList());
                resource = ResourceType.valueOf(subStrings.get(1));
                leaderId = Integer.parseInt(subStrings.get(0));
                hashMap.put(leaderId, resource);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("String is not well formatted");
        }
        return hashMap;
    }

    /**
     * Generates baseProductionParams from cli sub commands
     * it's public just for Text Purpose
     * @param string to be transformed in baseProductionParams"
     * @return the baseProductionParams generated
     * @throws IllegalArgumentException if the string is not well formatted
     */
    public static BaseProductionParams toBaseProductionParams(String string) throws IllegalArgumentException {
        boolean activated;
        List<ResourceType> resourceInput;
        List<ResourceType> resourceOutput;
        try {
            String[] infos = Arrays.stream(string.split(",")).map(String::strip).toArray(String[]::new);
            activated = Boolean.parseBoolean(infos[0]);
            resourceInput = Arrays.stream(infos[1].split("\\s+")).map(String::strip).filter(subString -> subString.length() != 0).map(ResourceType::valueOf).collect(Collectors.toList());
            resourceOutput = Arrays.stream(infos[2].split("\\s+")).map(String::strip).filter(subString -> subString.length() != 0).map(ResourceType::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalArgumentException("String is not well formatted");
        }
        return new BaseProductionParams(activated, resourceInput, resourceOutput);
    }

    /**
     * Generates DiscardLeaderAndExtraResBeginningMessage from cli command
     * @param string to be transformed in DiscardLeaderAndExtraResBeginningMessage, Ex: "1, 2, 4; COIN 2 3, STONE 1 4;"
     * @return the Message generated
     * @throws IllegalArgumentException if the string is not well formatted
     */
    public static DiscardLeaderAndExtraResBeginningMessage toDiscardLeaderAndExtraResBeginningMessage(String string) throws IllegalArgumentException {
        List<Integer> leaderCardIds;
        List<DepotParams> depotParamsList;
        try {
            String[] infos = Arrays.stream(string.split(";")).map(String::strip).toArray(String[]::new);
            leaderCardIds = Arrays.stream(infos[0].split(",")).map(String::strip).filter(id -> id.length() != 0).map(id -> Integer.parseInt(id) - 1).collect(Collectors.toList());
            depotParamsList = Arrays.stream(infos[1].split(",")).map(String::strip).filter(id -> id.length() != 0).map(StringToMessage::toDepotParams).collect(Collectors.toList());
        } catch (Exception e) {
            throw new IllegalArgumentException("String is not well formatted");
        }
        return new DiscardLeaderAndExtraResBeginningMessage(leaderCardIds, depotParamsList);
    }

    /**
     * Generates LeaderMessage from cli command to discard or activate leaders
     * @param string to be transformed in LeaderMessage, Ex: "1;"
     * @return the Message generated
     * @throws IllegalArgumentException if the string is not well formatted
     */
    public static LeaderMessage toLeaderMessage(String string) throws IllegalArgumentException {
        int leaderId;
        try {
            String[] infos = Arrays.stream(string.split(";")).map(String::strip).toArray(String[]::new);
            leaderId = Integer.parseInt(infos[0]);
        } catch (Exception e) {
            throw new IllegalArgumentException("String is not well formatted");
        }
        return new LeaderMessage(leaderId);
    }

    /**
     * Generates SetNumPlayerMessage from cli command to set the desired number of players in the game
     * @param string to be transformed in SetNumPlayerMessage, Ex: "1;"
     * @return the Message generated
     * @throws IllegalArgumentException if the string is not well formatted
     */
    public static SetNumPlayerMessage toSetNumPlayerMessage(String string) throws IllegalArgumentException {
        int numberOfPlayers;
        try {
            String[] infos = Arrays.stream(string.split(";")).map(String::strip).toArray(String[]::new);
            numberOfPlayers = Integer.parseInt(infos[0]);
        } catch (Exception e) {
            throw new IllegalArgumentException("String is not well formatted");
        }
        return new SetNumPlayerMessage(numberOfPlayers);
    }

    /**
     * Generates MoveBetweenShelvesMessage from cli command to to send move between shelf requests
     * @param string to be transformed in MoveBetweenShelvesMessage, Ex: "1; 2;"
     * @return the Message generated
     * @throws IllegalArgumentException if the string is not well formatted
     */
    public static MoveBetweenShelvesMessage toMoveBetweenShelvesMessage(String string) throws IllegalArgumentException {
        int sourceShelf, destShelf;
        try {
            String[] infos = Arrays.stream(string.split(";")).map(String::strip).toArray(String[]::new);
            sourceShelf = Integer.parseInt(infos[0]);
            destShelf = Integer.parseInt(infos[1]);
        } catch (Exception e) {
            throw new IllegalArgumentException("String is not well formatted");
        }
        return new MoveBetweenShelvesMessage(sourceShelf, destShelf);
    }

    /**
     * Generates MoveLeaderToShelfMessage from cli command to to send move from leader to shelf requests
     * @param string to be transformed in MoveLeaderToShelfMessage, Ex: "COIN; 1; 2;"
     * @return the Message generated
     * @throws IllegalArgumentException if the string is not well formatted
     */
    public static MoveLeaderToShelfMessage toMoveLeaderToShelfMessage(String string) throws IllegalArgumentException {
        ResourceType resource;
        int quantity, destShelf;
        try {
            String[] infos = Arrays.stream(string.split(";")).map(String::strip).toArray(String[]::new);
            resource = ResourceType.valueOf(infos[0]);
            quantity = Integer.parseInt(infos[1]);
            destShelf = Integer.parseInt(infos[2]);
        } catch (Exception e) {
            throw new IllegalArgumentException("String is not well formatted");
        }
        return new MoveLeaderToShelfMessage(resource, quantity, destShelf);
    }

    /**
     * Generates MoveShelfToLeaderMessage from cli command to to send move shelf to leader requests
     * @param string to be transformed in MoveShelfToLeaderMessage, Ex: "1; 2;"
     * @return the Message generated
     * @throws IllegalArgumentException if the string is not well formatted
     */
    public static MoveShelfToLeaderMessage toMoveShelfToLeaderMessage(String string) throws IllegalArgumentException {
        int numShelf, quantity;
        try {
            String[] infos = Arrays.stream(string.split(";")).map(String::strip).toArray(String[]::new);
            numShelf = Integer.parseInt(infos[0]);
            quantity = Integer.parseInt(infos[1]);
        } catch (Exception e) {
            throw new IllegalArgumentException("String is not well formatted");
        }
        return new MoveShelfToLeaderMessage(numShelf, quantity);
    }


}