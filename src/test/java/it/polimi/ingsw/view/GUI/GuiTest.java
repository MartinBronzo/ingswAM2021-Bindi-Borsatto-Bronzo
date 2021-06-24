package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.devCards.DevCard;
import it.polimi.ingsw.model.devCards.DevCardColour;
import it.polimi.ingsw.model.devCards.DevSlots;
import it.polimi.ingsw.model.faithTrack.PopeTile;
import it.polimi.ingsw.model.faithTrack.ReportNum;
import it.polimi.ingsw.model.leaderCard.LeaderCard;
import it.polimi.ingsw.model.leaderCard.leaderEffects.Effect;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.GuiClient;
import it.polimi.ingsw.view.gui.ViewComponents.devCards.DevSlotOnlyView;
import it.polimi.ingsw.view.gui.ViewComponents.faith.FaithTrackOnlyView;
import it.polimi.ingsw.view.gui.ViewComponents.leaderCards.CardCheckbox;
import it.polimi.ingsw.view.gui.ViewComponents.leaderCards.LeaderCardPanel;
import it.polimi.ingsw.view.gui.ViewComponents.production.baseProd.CheckBaseProd;
import it.polimi.ingsw.view.gui.ViewComponents.production.baseProd.DragAndDropBaseProd;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.BackButton;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.CancelButton;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.SubmitButton;
import it.polimi.ingsw.view.gui.ViewComponents.utils.InstructionPanel;
import it.polimi.ingsw.view.gui.mainViews.*;
import it.polimi.ingsw.view.gui.mainViews.panels.ActualPlayerBoardPanel;
import it.polimi.ingsw.view.gui.mainViews.panels.BeginningDecisionsPanel;
import it.polimi.ingsw.view.gui.mainViews.panels.MoveResourceChoice;
import it.polimi.ingsw.view.lightModel.Game;
import it.polimi.ingsw.view.lightModel.Player;
import it.polimi.ingsw.view.lightModel.player.DepotShelf;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuiTest {

    static CardCheckbox check;
    static JFrame gameFrame;
    static DragAndDropBaseProd dragAndDropBaseProd;

    public static void main(String[] args) throws InterruptedException, NegativeQuantityException {
        gameFrame= new JFrame();
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        gameFrame.setSize(screenSize.width, screenSize.height - 100);
        gameFrame.validate();

        //addInstructionPanel();

        //test for the leaderCheckboxPanel display
        //addLeaderCheckBoxPanel();

        //test for the moveResourceChoice display
        //moveResourceChoice();

        //test
        //seeOthersPlayers();

        //test for base production
        baseProduction();

        //test change of player view
        //seeOthersPlayers();

        //test for setBeginning resources
        //setBeginningResources();

        //test main panel
        //showPlayerBoard();

        //test leaderCardPanel
        //leaderCardPanel();

        //test for combobox
        //comboTest();

        //test for devslots only view
        //devSlotOnlyView();

        //onlyViewsInPlayerboard();

        //faithTrackOnlyView();

        //printButtons(gameFrame);

        gameFrame.setVisible(true);

        //test of the leaderCheckboxPanel's checkbox functionality
        /*Thread.sleep(5000);
        System.out.println(check.getSelectedLeaderIndexes());*/

        //test for base production choices
        /*Thread.sleep(5000);
        System.out.println("input: " + dragAndDropBaseProd.getInputs() + " output: " + dragAndDropBaseProd.getOutput());*/

    }

    private static void faithTrackOnlyView() {
        List<PopeTile> popeTiles = new ArrayList<>();
        popeTiles.add(new PopeTile(3, ReportNum.REPORT1));
        popeTiles.add(new PopeTile(5, ReportNum.REPORT2));
        popeTiles.add(new PopeTile(7, ReportNum.REPORT3));

        Player player = new Player();
        player.setNickName("Obi-Wan");
        player.setPopeTiles(popeTiles);
        player.setFaithPosition(5);

        FaithTrackOnlyView faithTrackOnlyView = new FaithTrackOnlyView(player);
        gameFrame.add(faithTrackOnlyView);

    }

    private static void onlyViewsInPlayerboard() throws NegativeQuantityException {
        DevCard devCard = new DevCard(1, DevCardColour.BLUE, 3, new HashMap<>(), new HashMap<>(), new HashMap<>(), "src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-1-1.png");
        DevCard devCard2 = new DevCard(2, DevCardColour.BLUE, 3, new HashMap<>(), new HashMap<>(), new HashMap<>(), "src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-19-1.png");
        DevCard devCard3 = new DevCard(3, DevCardColour.BLUE, 3, new HashMap<>(), new HashMap<>(), new HashMap<>(), "src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-19-1.png");

        DevSlots devSlots = new DevSlots();
        devSlots.addDevCard(0, devCard);
        devSlots.addDevCard(0, devCard2);
        devSlots.addDevCard(0, devCard3);

        List<PopeTile> popeTiles = new ArrayList<>();
        popeTiles.add(new PopeTile(3, ReportNum.REPORT1));
        popeTiles.add(new PopeTile(5, ReportNum.REPORT2));
        popeTiles.add(new PopeTile(7, ReportNum.REPORT3));

        PanelManager panelManager = PanelManager.createInstance(new GuiClient());
        Player player = new Player();
        player.setNickName("Obi-Wan");
        player.setPopeTiles(popeTiles);
        player.setFaithPosition(5);
        HashMap<ResourceType, Integer> res = new HashMap<>();
        res.put(ResourceType.COIN, 2);
        res.put(ResourceType.SERVANT, 3);
        res.put(ResourceType.STONE, 5);
        res.put(ResourceType.SHIELD, 0);
        player.setStrongBox(res);
        player.addDepotShelf(new DepotShelf(ResourceType.COIN, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.SHIELD, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.SERVANT, 2));
        player.setDevSlots(devSlots);
        player.setUsedLeaders(new ArrayList<>());
        player.setUnUsedLeaders(new ArrayList<>());
        Game game = new Game();
        game.addPlayer(player);
        panelManager.setGameModel(game);
        panelManager.setResourcesToTake(2);
        panelManager.setNickname("Obi-Wan");

        ActualPlayerBoardPanel actualPlayerBoardPanel = new ActualPlayerBoardPanel(player);
        gameFrame.add(actualPlayerBoardPanel);
    }

    private static void devSlotOnlyView() throws NegativeQuantityException {
        DevCard devCard = new DevCard(1, DevCardColour.BLUE, 3, new HashMap<>(), new HashMap<>(), new HashMap<>(), "src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-1-1.png");
        DevCard devCard2 = new DevCard(2, DevCardColour.BLUE, 3, new HashMap<>(), new HashMap<>(), new HashMap<>(), "src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-19-1.png");


        DevSlots devSlots = new DevSlots();
        devSlots.addDevCard(0, devCard);
        devSlots.addDevCard(0, devCard2);


        DevSlotOnlyView devSlotOnlyView = new DevSlotOnlyView(devSlots.getDevSlot(0));
        gameFrame.add(devSlotOnlyView);
        //devSlotsOnlyView.setVisible(true);
    }

    private static void comboTest() {
        String[] nicks = {"a", "b"};
        JPanel panel = new JPanel();
        JComboBox<String> comboBox = new JComboBox<>(nicks);
        comboBox.setPreferredSize(new Dimension(100,100));
        comboBox.setPrototypeDisplayValue("text here");
        panel.add(comboBox);
        gameFrame.add(panel);
    }

    private static void leaderCardPanel() {
        ArrayList<LeaderCard>activeLeaderCards = new ArrayList<>();
        ArrayList<LeaderCard>discardedLeaderCards = new ArrayList<>();
        discardedLeaderCards.add(new LeaderCard(3, new ArrayList<>(), new Effect(), "src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-64-1.png"));
        discardedLeaderCards.add(new LeaderCard(3, new ArrayList<>(), new Effect(), "src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-63-1.png"));

        LeaderCardPanel leaderCardPanel = new LeaderCardPanel(activeLeaderCards, discardedLeaderCards);
        gameFrame.add(leaderCardPanel);
        //gameFrame.setVisible(true);
    }

    /*private static void showPlayerBoard() {
        ActualPlayerBoardPanel actualPlayerBoardPanel = new ActualPlayerBoardPanel();
        gameFrame.add(actualPlayerBoardPanel);
    }*/

    private static void setBeginningResources() {
        ArrayList<String> leaderList = new ArrayList<>();
        leaderList.add("src/main/resources/PUNCHBOARD/cerchio1.png");
        leaderList.add("src/main/resources/PUNCHBOARD/cerchio2.png");
        leaderList.add("src/main/resources/PUNCHBOARD/cerchio3.png");
        leaderList.add("src/main/resources/PUNCHBOARD/cerchio4.png");

        BeginningDecisionsPanel beginningDecisionsPanel = new BeginningDecisionsPanel(leaderList, 2, 2);
        gameFrame.add(beginningDecisionsPanel);
    }


    private static void addInstructionPanel(){
        InstructionPanel p = new InstructionPanel();

        p.setLabelText("This is a loooooooooooooooooooooooooooooooooooooooooooooooooong text");
        p.setVisible(true);

        p.setConfirmActionListener(event -> System.out.println("A click"));

        JPanel allPanel = new JPanel();
        allPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.RELATIVE;
        c.ipady = 40;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 2;
        c.gridy = 2;
        allPanel.add(p, c);

        gameFrame.add(allPanel);
    }

    private static void printButtons(JFrame f){
        JButton submit = new SubmitButton("Confirm");
        JButton back = new BackButton("Back");
        JButton undo = new CancelButton("Undo");

        f.add(submit);
        f.add(back);
        f.add(undo);
    }

    private static void addLeaderCheckBoxPanel() throws InterruptedException {
        List<String> leaderList = new ArrayList<>();
        leaderList.add("src/main/resources/PUNCHBOARD/croce.png");
        leaderList.add("src/main/resources/PUNCHBOARD/cerchio7.png");

        check = new CardCheckbox(leaderList, "use this Dev Card");
        gameFrame.add(check);
    }

    private static void moveResourceChoice(){
        MoveResourceChoice panel = new MoveResourceChoice();

        gameFrame.add(panel);
    }

    /*private static void seeOthersPlayers(){
        List<String> playersNames = new ArrayList<>();
        playersNames.add("satto");
        playersNames.add("ludo");
        playersNames.add("martin");

        MainPanel mainPanel = new MainPanel(playersNames, "satto");
        gameFrame.add(mainPanel);
    }*/

    private static void baseProduction(){
        gameFrame.setLayout(new BorderLayout());
        dragAndDropBaseProd = new DragAndDropBaseProd();
        dragAndDropBaseProd.setCheckDropFunction(new CheckBaseProd(dragAndDropBaseProd));
        gameFrame.add(dragAndDropBaseProd, BorderLayout.CENTER);
        JButton button = new CancelButton("Reset");
        button.addActionListener((e) -> {
            dragAndDropBaseProd.resetState();
        });
        SubmitButton submit = new SubmitButton("Submit");
        submit.addActionListener((e) -> {
            System.out.println("INPUTS: " + dragAndDropBaseProd.getInputs());
            System.out.println("OUTPUT: " + dragAndDropBaseProd.getOutput());
        });
        gameFrame.add(button, BorderLayout.PAGE_END);
        gameFrame.add(submit, BorderLayout.LINE_END);
        gameFrame.revalidate();

    }

}
