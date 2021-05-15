package it.polimi.ingsw.view;

import it.polimi.ingsw.network.messages.fromClient.GetFromMatrixMessage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class stringToMessage {
    public static GetFromMatrixMessage toMatrixMessage(String string) throws IllegalArgumentException{
        int colNumber;
        int rowNumber;
        List<Integer> leaderList;
        try {
            String[] infos = string.split(";");
            String[] rcInfos = infos[0].split("\\s+");
            if (rcInfos[0].equals("row")) {
                rowNumber = Integer.parseInt(rcInfos[1]);
                colNumber = 0;
            } else if (rcInfos[0].equals("column")) {
                rowNumber = 0;
                colNumber = Integer.parseInt(rcInfos[1]);
            } else {
                throw new IllegalArgumentException("String is not well formatted");
            }
            String[] leaderIds = infos[1].split("\\s+");
            leaderList = Arrays.stream(leaderIds).filter(id -> id.length() != 0).map(Integer::parseInt).collect(Collectors.toList());
        } catch (Exception e){
            throw new IllegalArgumentException("String is not well formatted");
        }
        return new GetFromMatrixMessage(rowNumber, colNumber, leaderList);
    }
}