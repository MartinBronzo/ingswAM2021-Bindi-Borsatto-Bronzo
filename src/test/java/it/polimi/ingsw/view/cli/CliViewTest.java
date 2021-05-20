package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.*;

class CliViewTest {

    @Test
    void printWelcome() {
        CliView.printWelcome();
    }

    @Test
    void printGameState() {
    }

    @Test
    void printError() {
        CliView.printError("bdsvjhke sbvhj lsdmzb dbjhj jedvk kgvwk kvgqdg jkcqwv kwcv");
        System.out.println("\n");
    }

    @Test
    void printinfo() {
        CliView.printInfo("bdsvjhke sbvhj lsdmzb dbjhj jedvk kgvwk kvgqdg jkcqwv kwcv");
        System.out.println("\n");
    }

    @Test
    void printSetUpView() {
        CliView.printSetUpView(2,0);
        System.out.println("\n");
        CliView.printSetUpView(2,1);
        System.out.println("\n");
        CliView.printSetUpView(2,2);
        System.out.println("\n");
    }

    @Test
    void printResourcesMap() {
        HashMap<ResourceType, Integer> map = new HashMap<>();
        map.put(ResourceType.COIN, 1);
        map.put(ResourceType.STONE, 2);
        map.put(ResourceType.SERVANT, 30);
        map.put(ResourceType.SHIELD, 4);
        map.put(ResourceType.FAITHPOINT, 50);
        CliView.printResourcesMap(map, "Master Kenobi, this morrow, local shopkeepers tenders thou:");
    }

    @Test
    void printFinalScores(){
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Darth Vader", 67);
        map.put("General Grievous", 33);
        map.put("General Kenobi", 1100);
        map.put("Jar Jar Binks", 1099);
        List<Map.Entry<String, Integer>> results = new LinkedList<>(map.entrySet());
        Collections.sort(results, (x, y) -> y.getValue().compareTo(x.getValue()));
        CliView.printFinalScores(results);
    }
}