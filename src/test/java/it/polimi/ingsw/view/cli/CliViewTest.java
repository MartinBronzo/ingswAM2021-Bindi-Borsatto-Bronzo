package it.polimi.ingsw.view.cli;

import org.junit.jupiter.api.Test;

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
    }
}