package it.polimi.ingsw.view.GUI.panels;
/*
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.leaderCard.LeaderCard;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.CardRequirementResource;
import it.polimi.ingsw.model.leaderCard.LeaderCardRequirements.Requirement;
import it.polimi.ingsw.model.leaderCard.leaderEffects.ExtraSlotLeaderEffect;
import it.polimi.ingsw.model.resources.ResourceType;
import it.polimi.ingsw.view.gui.GuiClient;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrag.DepotDrag;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotDrop.*;
import it.polimi.ingsw.view.gui.ViewComponents.depot.moveResources.MoveBetweenShelves;
import it.polimi.ingsw.view.gui.ViewComponents.depot.moveResources.MoveLeaderToShelf;
import it.polimi.ingsw.view.gui.ViewComponents.depot.moveResources.MoveShelfToLeader;
import it.polimi.ingsw.view.gui.ViewComponents.leaderCards.leaderCardsDrag.DragLeaderCards;
import it.polimi.ingsw.view.gui.ViewComponents.leaderCards.leaderCardsDrop.*;
//import it.polimi.ingsw.view.gui.ViewComponents.oldVersion.CollectCostsChoices;
//import it.polimi.ingsw.view.gui.ViewComponents.oldVersion.CollectMoveToLeaderOld;
import it.polimi.ingsw.view.gui.ViewComponents.depot.depotViews.DepotOnlyView;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrag.InfiniteResourcesDrag;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrag.LimitedResourcesDrag;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrop.CheckLimitedDrop;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrop.DiscardedResDrop;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrop.PanelDrop;
import it.polimi.ingsw.view.gui.ViewComponents.resources.resourcesDrop.RegisterLimitedDropInPlainPanel;
import it.polimi.ingsw.view.gui.ViewComponents.strongbox.StrongBoxDrag;
import it.polimi.ingsw.view.gui.ViewComponents.strongbox.StrongBoxDrop;
import it.polimi.ingsw.view.gui.ViewComponents.strongbox.StrongBoxOnlyView;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.CancelButton;
import it.polimi.ingsw.view.gui.ViewComponents.buttons.SubmitButton;
import it.polimi.ingsw.view.gui.ViewComponents.interfaces.RegisterDropInterface;
import it.polimi.ingsw.view.gui.ViewComponents.utils.DumbCheckDrop;
import it.polimi.ingsw.view.gui.ViewComponents.utils.MyDragGestureListener;
import it.polimi.ingsw.view.gui.ViewComponents.utils.MyDropTargetListener;
import it.polimi.ingsw.view.gui.ViewComponents.utils.ResetState;
import it.polimi.ingsw.view.gui.mainViews.panels.BeginningDecisionsPanel;
import it.polimi.ingsw.view.gui.mainViews.PanelManager;
import it.polimi.ingsw.view.lightModel.Game;
import it.polimi.ingsw.view.lightModel.Player;
import it.polimi.ingsw.view.lightModel.player.DepotShelf;
import org.xml.sax.SAXException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;*/

/**
 * THIS IS A TEST FOR THE BEGINNING DECISIONS DEPOT DRAG & DROP
 */
public class Test {}

  /*  public static void main(String[] args) throws InterruptedException, ParserConfigurationException, NegativeQuantityException, SAXException, IOException {
        //LUDO
        //createAndShowJFrame();
        //createAndShowJFrameWithChecksAdded();
        //createAndShowJFrameWithResourcesInside();
        //checkTrashCanDrop();
        //checkStrongBoxCanDrop();
        //checkDepotDrag();
        //checkLimitedResDrag();
        //checkStrongBoxDrag();
        //checkPanelDropStrongBoxDrag();
        //checkPanelDepotDrag();
        //checkDepotNStrongBoxDrag(); //NICE
        //checkLimitedResDragNicer();
        //checkLimitedResDragDepotDropTrashCanDrop();
        //checkDepotOnlyView();
        //checkStrongBoxOnlyView();
        //checkPlainPanelDropGettingInfo();
        //checkCollectorFunctionInLimitedResDragDepotDropTrashCanDrop();
        //checkResetDepotDrop();
        //checkResetTrashCanDrop();
        //checkResetDepotDropAndTrashCanDrop();
        //checkPlacingResourcesReset();
        //checkPlacingResourcesReset();
        //checkMoveToLeaderCard();
        //checkMoveShelfToLeaderEasier();
        //checkMoveLeaderToDepotEasier();
        //checkMoveBetweenShelves();

        //Già dei prototipi di panel completi
        //checkPlacingResourceFull();
        //checkPayingTheCostFull();

        //SATTO
        showSetBeginningDecisionsPanel();
        //showPlayerBoards();
    }
*/

    /*
    public static void showSetBeginningDecisionsPanel(){
        SwingUtilities.invokeLater(() -> {
            final int resToTake = 2;
            final int leaderToDiscard = 2;

            PanelManager panelManager = PanelManager.createInstance(new GuiClient());
            Player player = new Player();
            player.setNickName("Obi-Wan");
            player.addDepotShelf(new DepotShelf(null, 0));
            player.addDepotShelf(new DepotShelf(null, 0));
            player.addDepotShelf(new DepotShelf(null, 0));
            Game game = new Game();
            game.addPlayer(player);
            panelManager.setGameModel(game);
            panelManager.setResourcesToTake(resToTake);
            panelManager.setNickname("Obi-Wan");

            JFrame frame = new JFrame();
            frame.setLayout(new BorderLayout());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ArrayList<String> leaderList = new ArrayList<>();
            leaderList.add("src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-61-1.png");
            leaderList.add("src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-61-1.png");
            leaderList.add("src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-61-1.png");
            leaderList.add("src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-61-1.png");

            BeginningDecisionsPanel beginningDecisionsPanel = new BeginningDecisionsPanel(leaderList, resToTake, leaderToDiscard);
            frame.add(beginningDecisionsPanel, BorderLayout.CENTER);

            frame.setTitle("Depot test");
            frame.pack();
            frame.setVisible(true);
        });
    }

    public static void createAndShowJFrame() {
        SwingUtilities.invokeLater(() -> {

            //JFrame frame = createJFrame();
            JFrame frame = new JFrame();
            frame.setLayout(new BorderLayout());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            DnDDepot depotPanels = new DnDDepot();
            frame.add(depotPanels, BorderLayout.CENTER);
            frame.setTitle("Depot test");
            frame.pack();
            JButton submit = new SubmitButton("Confirm");
            submit.addActionListener(new CollectBeginningChoices(depotPanels.getDepot()));
            frame.add(submit, BorderLayout.PAGE_END);
            frame.setVisible(true);
        });
    }

    public static void createAndShowJFrameWithChecksAdded(){
        SwingUtilities.invokeLater(() -> {


            PanelManager panelManager = PanelManager.createInstance(new GuiClient());
            Player player = new Player();
            player.setNickName("Obi-Wan");
            player.addDepotShelf(new DepotShelf(null, 0));
            player.addDepotShelf(new DepotShelf(null, 0));
            player.addDepotShelf(new DepotShelf(null, 0));
            Game game = new Game();
            game.addPlayer(player);
            panelManager.setGameModel(game);
            panelManager.setResourcesToTake(2);
            panelManager.setNickname("Obi-Wan");

            //JFrame frame = createJFrame();
            JFrame frame = new JFrame();
            frame.setLayout(new BorderLayout());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            DepotDrop depotPanels = new DepotDrop();
            depotPanels.setCheckDropFunction(new CheckDropAtBeginningDecisionsTime(depotPanels));
            frame.add(depotPanels, BorderLayout.CENTER);
            frame.setTitle("Depot test");
            frame.pack();
            JButton submit = new SubmitButton("Confirm");
            submit.addActionListener(new CollectBeginningChoices(depotPanels));
            frame.add(submit, BorderLayout.PAGE_END);
            frame.setVisible(true);
        });

    }

     */

    /*public static void showPlayerBoards() throws ParserConfigurationException, NegativeQuantityException, SAXException, IOException, InterruptedException {
        Board board;
        DevGrid devGrid;
        File xmlDevCardsConfig;

        xmlDevCardsConfig = new File("DevCardConfig.xsd.xml");
        devGrid = new DevGrid(xmlDevCardsConfig);
        DevCard[][] devMatrix = devGrid.getDevMatrix();
        board = new Board();
        board.setDevMatrix(devMatrix);

        JFrame frame = new JFrame();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height - 100);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ActualPlayerBoardPanel actualPlayerBoardPanel = new ActualPlayerBoardPanel(board, new ArrayList<>(), new ArrayList<>());
        actualPlayerBoardPanel.setSize(100,100);
        frame.add(actualPlayerBoardPanel);
        frame.setVisible(true);
    }*/

    /*
    public static void createAndShowJFrameWithResourcesInside(){
        SwingUtilities.invokeLater(() -> {


            PanelManager panelManager = PanelManager.createInstance(new GuiClient());
            Player player = new Player();
            player.setNickName("Obi-Wan");
            player.addDepotShelf(new DepotShelf(ResourceType.COIN, 1));
            player.addDepotShelf(new DepotShelf(ResourceType.SHIELD, 1));
            player.addDepotShelf(new DepotShelf(ResourceType.SERVANT, 2));
            Game game = new Game();
            game.addPlayer(player);
            panelManager.setGameModel(game);
            panelManager.setResourcesToTake(2);
            panelManager.setNickname("Obi-Wan");

            //JFrame frame = createJFrame();
            JFrame frame = new JFrame();
            frame.setLayout(new BorderLayout());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            DnDDepot depotPanels = new DnDDepot();
            depotPanels.initFromInfiniteDrag(new CheckDropAtBeginningDecisionsTime(depotPanels.getDepot()));
            frame.add(depotPanels, BorderLayout.CENTER);
            frame.setTitle("Depot test");
            frame.pack();
            JButton submit = new SubmitButton("Confirm");
            submit.addActionListener(new CollectBeginningChoices(depotPanels.getDepot()));
            frame.add(submit, BorderLayout.PAGE_END);
            frame.setVisible(true);
        });

    }

    public static void checkTrashCanDrop(){
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        DiscardedResDrop trashCan = new DiscardedResDrop();
        trashCan.initFromInfiniteRes(new DumbCheckDrop());
        frame.add(trashCan, BorderLayout.CENTER);
        frame.setTitle("Trash Can test");
        InfiniteResourcesDrag res = new InfiniteResourcesDrag();
        frame.add(res, BorderLayout.PAGE_END);
        frame.pack();
        frame.setVisible(true);

    }

    public static void checkStrongBoxCanDrop(){
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        StrongBoxDrop strongBoxDrop = new StrongBoxDrop();
        strongBoxDrop.setTargetListenerAndCheckDropFunction(new DumbCheckDrop());
        frame.add(strongBoxDrop, BorderLayout.CENTER);
        frame.setTitle("StrongBox test");
        InfiniteResourcesDrag res = new InfiniteResourcesDrag();
        frame.add(res, BorderLayout.PAGE_END);
        frame.pack();
        frame.setVisible(true);

    }

    public static void checkDepotDrag(){
        SwingUtilities.invokeLater(() -> {

            PanelManager panelManager = PanelManager.createInstance(new GuiClient());
            Player player = new Player();
            player.setNickName("Obi-Wan");
            player.addDepotShelf(new DepotShelf(ResourceType.COIN, 1));
            player.addDepotShelf(new DepotShelf(ResourceType.SHIELD, 1));
            player.addDepotShelf(new DepotShelf(ResourceType.SERVANT, 2));
            Game game = new Game();
            game.addPlayer(player);
            panelManager.setGameModel(game);
            panelManager.setResourcesToTake(2);
            panelManager.setNickname("Obi-Wan");

            //JFrame frame = createJFrame();
            JFrame frame = new JFrame();
            frame.setLayout(new BorderLayout());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            DepotDrag depot = new DepotDrag();
            depot.init();
            frame.add(depot, BorderLayout.CENTER);
            StrongBoxDrop strongBoxDrop = new StrongBoxDrop();
            strongBoxDrop.setTargetListenerAndCheckDropFunction(new DumbCheckDrop());
            frame.add(strongBoxDrop, BorderLayout.PAGE_END);
            SubmitButton submitButton = new SubmitButton("confirm");
            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    depot.printChoices();
                }
            });
            frame.add(submitButton, BorderLayout.LINE_END);
            frame.setTitle("Depot drag test");
            frame.pack();
            frame.setVisible(true);
        });

    }

    public static void checkLimitedResDrag(){
        SwingUtilities.invokeLater(() -> {

            PanelManager panelManager = PanelManager.createInstance(new GuiClient());
            Player player = new Player();
            player.setNickName("Obi-Wan");
            player.addDepotShelf(new DepotShelf(ResourceType.COIN, 1));
            player.addDepotShelf(new DepotShelf(ResourceType.SHIELD, 1));
            player.addDepotShelf(new DepotShelf(ResourceType.SERVANT, 2));
            Game game = new Game();
            game.addPlayer(player);
            panelManager.setGameModel(game);
            panelManager.setResourcesToTake(2);
            panelManager.setNickname("Obi-Wan");

            //JFrame frame = createJFrame();
            JFrame frame = new JFrame();
            frame.setLayout(new BorderLayout());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            LimitedResourcesDrag limited = new LimitedResourcesDrag();
            HashMap<ResourceType, Integer> res = new HashMap<>();
            res.put(ResourceType.COIN, 2);
            res.put(ResourceType.STONE, 3);
            //limited.init(res);
            frame.add(limited, BorderLayout.CENTER);
            StrongBoxDrop strongBoxDrop = new StrongBoxDrop();
            strongBoxDrop.setTargetListenerAndCheckDropFunction(new DumbCheckDrop());
            frame.add(strongBoxDrop, BorderLayout.PAGE_END);
            frame.setTitle("Limited res drag test");
            frame.pack();
            frame.setVisible(true);
        });

    }

    public static void checkStrongBoxDrag(){
        //JFrame frame = createJFrame();
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        StrongBoxDrag strongBox = new StrongBoxDrag();
        HashMap<ResourceType, Integer> res = new HashMap<>();
        res.put(ResourceType.COIN, 2);
        res.put(ResourceType.STONE, 3);
        strongBox.init(res, new MyDragGestureListener());
        StrongBoxDrop strongBoxDrop = new StrongBoxDrop();
        strongBoxDrop.setTargetListenerAndCheckDropFunction(new DumbCheckDrop());
        frame.add(strongBoxDrop, BorderLayout.PAGE_END);
        frame.add(strongBox, BorderLayout.CENTER);
        frame.setTitle("StrongBox drag test");
        frame.pack();
        frame.setVisible(true);
    }

    public static void checkPanelDropStrongBoxDrag(){
        //Setting up the frame
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Setting up the strongBox drag
        StrongBoxDrag strongBox = new StrongBoxDrag();
        HashMap<ResourceType, Integer> res = new HashMap<>();
        res.put(ResourceType.COIN, 2);
        res.put(ResourceType.STONE, 3);
        res.put(ResourceType.SERVANT, 1);
        strongBox.init(res, new MyDragGestureListener());
        frame.add(strongBox, BorderLayout.CENTER);

        //Setting up the PanelDrop
        PanelDrop pDrop = new PanelDrop();
        HashMap<ResourceType, Integer> resToBeTaken = new HashMap<>(res);
        resToBeTaken.put(ResourceType.COIN, 1);
        resToBeTaken.remove(ResourceType.SERVANT);
        CheckLimitedDrop checker = new CheckLimitedDrop(resToBeTaken);
        RegisterDropInterface registerDrop = new RegisterLimitedDropInPlainPanel(checker, strongBox, new DepotDrag(), pDrop);
        MyDropTargetListener dListener = new MyDropTargetListener(pDrop, registerDrop, checker);
        pDrop.init(dListener);
        frame.add(pDrop, BorderLayout.PAGE_END);

        //Finishing it up
        frame.setTitle("StrongBox drag & Panel Drop test");
        frame.pack();
        frame.setVisible(true);
    }

    public static void checkPanelDepotDrag(){
        //Setting up the frame
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Setting up the Player
        PanelManager panelManager = PanelManager.createInstance(new GuiClient());
        Player player = new Player();
        player.setNickName("Obi-Wan");
        player.addDepotShelf(new DepotShelf(ResourceType.COIN, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.SHIELD, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.SERVANT, 2));
        Game game = new Game();
        game.addPlayer(player);
        panelManager.setGameModel(game);
        panelManager.setResourcesToTake(2);
        panelManager.setNickname("Obi-Wan");

        //Setting up the depot drag
        DepotDrag depotDrag = new DepotDrag();
        depotDrag.init();
        frame.add(depotDrag, BorderLayout.CENTER);

        //Setting up the PanelDrop
        PanelDrop pDrop = new PanelDrop();
        HashMap<ResourceType, Integer> resToBeTaken = new HashMap<>();
        resToBeTaken.put(ResourceType.COIN, 1);
        resToBeTaken.put(ResourceType.SERVANT, 1);
        CheckLimitedDrop checker = new CheckLimitedDrop(resToBeTaken);
        RegisterDropInterface registerDrop = new RegisterLimitedDropInPlainPanel(checker, new StrongBoxDrag(), depotDrag, pDrop);
        MyDropTargetListener dListener = new MyDropTargetListener(pDrop,registerDrop, checker);
        pDrop.init(dListener);
        frame.add(pDrop, BorderLayout.PAGE_END);

        //Finishing it up
        frame.setTitle("StrongBox drag & Panel Drop test");
        frame.pack();
        frame.setVisible(true);
    }

    public static void checkDepotNStrongBoxDrag(){
        //Setting up the frame
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Setting up the Player
        PanelManager panelManager = PanelManager.createInstance(new GuiClient());
        Player player = new Player();
        player.setNickName("Obi-Wan");
        player.addDepotShelf(new DepotShelf(ResourceType.COIN, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.SHIELD, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.SERVANT, 2));
        Game game = new Game();
        game.addPlayer(player);
        panelManager.setGameModel(game);
        panelManager.setResourcesToTake(2);
        panelManager.setNickname("Obi-Wan");

        //Setting up the depot drag
        DepotDrag depotDrag = new DepotDrag();
        depotDrag.init();
        frame.add(depotDrag, BorderLayout.CENTER);

        //Setting up the strongBox drag
        StrongBoxDrag strongBox = new StrongBoxDrag();
        HashMap<ResourceType, Integer> res = new HashMap<>();
        res.put(ResourceType.COIN, 2);
        res.put(ResourceType.STONE, 3);
        res.put(ResourceType.SERVANT, 1);
        strongBox.init(res, new MyDragGestureListener());
        frame.add(strongBox, BorderLayout.PAGE_START);

        //Setting up the PanelDrop
        PanelDrop pDrop = new PanelDrop();
        HashMap<ResourceType, Integer> resToBeTaken = new HashMap<>();
        resToBeTaken.put(ResourceType.COIN, 1);
        resToBeTaken.put(ResourceType.SERVANT, 1);
        CheckLimitedDrop checker = new CheckLimitedDrop(resToBeTaken);
        RegisterDropInterface registerDrop = new RegisterLimitedDropInPlainPanel(checker, strongBox, depotDrag, pDrop);
        MyDropTargetListener dListener = new MyDropTargetListener(pDrop,registerDrop, checker);
        pDrop.init(dListener);
        frame.add(pDrop, BorderLayout.PAGE_END);

        //Finishing it up
        frame.setTitle("StrongBox drag & Panel Drop test");
        frame.pack();
        frame.setVisible(true);
    }

    public static void checkLimitedResDragNicer(){
        //Setting up the frame
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Setting up the Player
        PanelManager panelManager = PanelManager.createInstance(new GuiClient());
        Player player = new Player();
        player.setNickName("Obi-Wan");
        player.addDepotShelf(new DepotShelf(ResourceType.COIN, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.SHIELD, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.SERVANT, 2));
        Game game = new Game();
        game.addPlayer(player);
        panelManager.setGameModel(game);
        panelManager.setResourcesToTake(2);
        panelManager.setNickname("Obi-Wan");

        //Setting up the LimitedRes drag
        LimitedResourcesDrag limitedResourcesDrag = new LimitedResourcesDrag();
        HashMap<ResourceType, Integer> res = new HashMap<>();
        res.put(ResourceType.COIN, 2);
        res.put(ResourceType.STONE, 3);
        res.put(ResourceType.SERVANT, 1);
        limitedResourcesDrag.init(res, new MyDragGestureListener());
        frame.add(limitedResourcesDrag, BorderLayout.PAGE_END);

        //Setting up the DepotDrop
        DepotDrop depot = new DepotDrop();
        depot.initFromFiniteDrag(new CheckDropInDepot(depot), limitedResourcesDrag);
        frame.add(depot, BorderLayout.CENTER);

        //Finishing it up
        frame.setTitle("StrongBox drag & Panel Drop test");
        frame.pack();
        frame.setVisible(true);
    }

    public static void checkLimitedResDragDepotDropTrashCanDrop(){
        //Setting up the frame
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Setting up the Player
        PanelManager panelManager = PanelManager.createInstance(new GuiClient());
        Player player = new Player();
        player.setNickName("Obi-Wan");
        player.addDepotShelf(new DepotShelf(ResourceType.COIN, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.SHIELD, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.SERVANT, 2));
        Game game = new Game();
        game.addPlayer(player);
        panelManager.setGameModel(game);
        panelManager.setResourcesToTake(2);
        panelManager.setNickname("Obi-Wan");

        //Setting up the LimitedRes drag
        LimitedResourcesDrag limitedResourcesDrag = new LimitedResourcesDrag();
        HashMap<ResourceType, Integer> res = new HashMap<>();
        res.put(ResourceType.COIN, 2);
        res.put(ResourceType.STONE, 3);
        res.put(ResourceType.SERVANT, 1);
        res.put(ResourceType.SHIELD, 1);
        limitedResourcesDrag.init(res, new MyDragGestureListener());
        frame.add(limitedResourcesDrag, BorderLayout.PAGE_END);

        //Setting up the DepotDrop
        DepotDrop depot = new DepotDrop();
        depot.initFromFiniteDrag(new CheckDropInDepot(depot), limitedResourcesDrag);
        //frame.add(depot, BorderLayout.CENTER);

        //Setting up the DiscardedDrop
        DiscardedResDrop trashCan = new DiscardedResDrop();
        trashCan.initFromFiniteRes(new DumbCheckDrop(), limitedResourcesDrag);
        //frame.add(trashCan, BorderLayout.PAGE_START);
        JLabel centralPanel = new JLabel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.X_AXIS));
        centralPanel.add(depot);
        centralPanel.add(trashCan);
        frame.add(centralPanel, BorderLayout.CENTER);

        //Finishing it up
        frame.setTitle("StrongBox drag & Panel Drop test");
        frame.pack();
        frame.setVisible(true);
    }

    public static void checkDepotOnlyView(){
        //Setting up the frame
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Setting up the Player
        PanelManager panelManager = PanelManager.createInstance(new GuiClient());
        Player player = new Player();
        player.setNickName("Obi-Wan");
        player.addDepotShelf(new DepotShelf(ResourceType.COIN, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.SHIELD, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.SERVANT, 2));
        Game game = new Game();
        game.addPlayer(player);
        panelManager.setGameModel(game);
        panelManager.setResourcesToTake(2);
        panelManager.setNickname("Obi-Wan");

        //Crating the depot
        DepotOnlyView depot = new DepotOnlyView(player.getNickName());
        frame.add(depot, BorderLayout.CENTER);

        //Finishing it up
        frame.setTitle("Depot Only view");
        frame.pack();
        frame.setVisible(true);
    }

    public static void checkStrongBoxOnlyView(){
        //Setting up the frame
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Setting up the Player
        PanelManager panelManager = PanelManager.createInstance(new GuiClient());
        Player player = new Player();
        player.setNickName("Obi-Wan");
        HashMap<ResourceType, Integer> res = new HashMap<>();
        res.put(ResourceType.COIN, 2);
        res.put(ResourceType.SERVANT, 3);
        res.put(ResourceType.STONE, 5);
        res.put(ResourceType.SHIELD, 0);
        player.setStrongBox(res);
        Game game = new Game();
        game.addPlayer(player);
        panelManager.setGameModel(game);
        panelManager.setResourcesToTake(2);
        panelManager.setNickname("Obi-Wan");

        //Crating the depot
        StrongBoxOnlyView strongBox = new StrongBoxOnlyView(player.getNickName());
        frame.add(strongBox, BorderLayout.CENTER);

        //Finishing it up
        frame.setTitle("StrongBox only view");
        frame.pack();
        frame.setVisible(true);
    }

    private static void checkPlainPanelDropGettingInfo() {
        //Setting up the frame
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Setting up the Player
        PanelManager panelManager = PanelManager.createInstance(new GuiClient());
        Player player = new Player();
        player.setNickName("Obi-Wan");
        player.addDepotShelf(new DepotShelf(ResourceType.COIN, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.SHIELD, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.SERVANT, 2));
        HashMap<ResourceType, Integer> res = new HashMap<>();
        res.put(ResourceType.COIN, 2);
        res.put(ResourceType.STONE, 3);
        res.put(ResourceType.SERVANT, 1);
        player.setStrongBox(res);
        Game game = new Game();
        game.addPlayer(player);
        panelManager.setGameModel(game);
        panelManager.setResourcesToTake(2);
        panelManager.setNickname("Obi-Wan");

        //Setting up the depot drag
        DepotDrag depotDrag = new DepotDrag();
        depotDrag.init();
        frame.add(depotDrag, BorderLayout.CENTER);

        //Setting up the strongBox drag
        StrongBoxDrag strongBox = new StrongBoxDrag();
        strongBox.init();
        frame.add(strongBox, BorderLayout.PAGE_START);

        //Setting up the PanelDrop
        PanelDrop pDrop = new PanelDrop();
        HashMap<ResourceType, Integer> resToBeTaken = new HashMap<>();
        resToBeTaken.put(ResourceType.COIN, 1);
        resToBeTaken.put(ResourceType.SERVANT, 1);
        CheckLimitedDrop checker = new CheckLimitedDrop(resToBeTaken);
        RegisterDropInterface registerDrop = new RegisterLimitedDropInPlainPanel(checker, strongBox, depotDrag, pDrop);
        MyDropTargetListener dListener = new MyDropTargetListener(pDrop,registerDrop, checker);
        pDrop.init(dListener);
        frame.add(pDrop, BorderLayout.PAGE_END);

        //Adding a Submit button which then prints the choices the player did
        SubmitButton submitButton = new SubmitButton("Submit");
        //submitButton.addActionListener(new CollectCostsChoices(pDrop));
        frame.add(submitButton, BorderLayout.LINE_END);

        //Finishing it up
        frame.setTitle("StrongBox drag & Panel Drop test");
        frame.pack();
        frame.setVisible(true);
    }

    public static void checkCollectorFunctionInLimitedResDragDepotDropTrashCanDrop(){
        //Setting up the frame
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Setting up the Player
        PanelManager panelManager = PanelManager.createInstance(new GuiClient());
        Player player = new Player();
        player.setNickName("Obi-Wan");
        player.addDepotShelf(new DepotShelf(ResourceType.COIN, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.SHIELD, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.SERVANT, 2));
        Game game = new Game();
        game.addPlayer(player);
        panelManager.setGameModel(game);
        panelManager.setResourcesToTake(2);
        panelManager.setNickname("Obi-Wan");

        //Setting up the LimitedRes drag
        LimitedResourcesDrag limitedResourcesDrag = new LimitedResourcesDrag();
        HashMap<ResourceType, Integer> res = new HashMap<>();
        res.put(ResourceType.COIN, 2);
        res.put(ResourceType.STONE, 3);
        res.put(ResourceType.SERVANT, 1);
        res.put(ResourceType.SHIELD, 1);
        limitedResourcesDrag.init(res);
        frame.add(limitedResourcesDrag, BorderLayout.PAGE_END);

        //Setting up the DepotDrop
        DepotDrop depot = new DepotDrop();
        depot.initFromFiniteDrag(new CheckDropInDepot(depot), limitedResourcesDrag);

        //Setting up the DiscardedDrop
        DiscardedResDrop trashCan = new DiscardedResDrop();
        trashCan.initFromFiniteRes(new DumbCheckDrop(), limitedResourcesDrag);

        //Uniting the Depot and the Discarded in a JPanel
        JLabel centralPanel = new JLabel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.X_AXIS));
        centralPanel.add(depot);
        centralPanel.add(trashCan);
        frame.add(centralPanel, BorderLayout.CENTER);

        //Adding the submit button
        SubmitButton button = new SubmitButton("Submit");
        button.addActionListener(new CollectPlacingResources(depot, trashCan));
        frame.add(button, BorderLayout.LINE_END);

        //Finishing it up
        frame.setTitle("StrongBox drag & Panel Drop test");
        frame.pack();
        frame.setVisible(true);
    }

    public static void checkResetDepotDrop(){
        //Setting up the frame
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Setting up the Player
        PanelManager panelManager = PanelManager.createInstance(new GuiClient());
        Player player = new Player();
        player.setNickName("Obi-Wan");
        player.addDepotShelf(new DepotShelf(null, 0));
        player.addDepotShelf(new DepotShelf(null, 0));
        player.addDepotShelf(new DepotShelf(null, 0));
        Game game = new Game();
        game.addPlayer(player);
        panelManager.setGameModel(game);
        panelManager.setResourcesToTake(2);
        panelManager.setNickname("Obi-Wan");

        //Setting up the DepotDrop
        DnDDepot depot = new DnDDepot();
        depot.initFromInfiniteDrag(new CheckDropAtBeginningDecisionsTime(depot.getDepot()));
        frame.add(depot, BorderLayout.CENTER);

        //Setting up the CancelButton
        CancelButton button = new CancelButton("Cancel");
        button.addActionListener(new ResetState(depot.getDepot()));
        frame.add(button, BorderLayout.LINE_END);

        //Finishing it up
        frame.setTitle("Depot reset");
        frame.pack();
        frame.setVisible(true);
    }

    public static void checkResetTrashCanDrop(){
        //Setting up the frame
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Setting up the LimitedRes drag
        LimitedResourcesDrag limitedResourcesDrag = new LimitedResourcesDrag();
        HashMap<ResourceType, Integer> res = new HashMap<>();
        res.put(ResourceType.COIN, 2);
        res.put(ResourceType.STONE, 3);
        res.put(ResourceType.SERVANT, 1);
        res.put(ResourceType.SHIELD, 1);
        limitedResourcesDrag.init(res);
        frame.add(limitedResourcesDrag, BorderLayout.PAGE_END);

        //Setting up the TrashCan
        DiscardedResDrop trashCan = new DiscardedResDrop();
        trashCan.initFromFiniteRes(new DumbCheckDrop(), limitedResourcesDrag);
        frame.add(trashCan, BorderLayout.CENTER);

        //Setting up the CancelButton
        CancelButton button = new CancelButton("Cancel");
        button.addActionListener(new ResetState(trashCan));
        frame.add(button, BorderLayout.LINE_END);

        //Finishing it up
        frame.setTitle("Depot reset");
        frame.pack();
        frame.setVisible(true);

    }

    public static void checkResetDepotDropAndTrashCanDrop(){
        //Setting up the frame
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Setting up the Player
        PanelManager panelManager = PanelManager.createInstance(new GuiClient());
        Player player = new Player();
        player.setNickName("Obi-Wan");
        player.addDepotShelf(new DepotShelf(ResourceType.COIN, 1));
        player.addDepotShelf(new DepotShelf(null, 0));
        player.addDepotShelf(new DepotShelf(ResourceType.SERVANT, 2));
        Game game = new Game();
        game.addPlayer(player);
        panelManager.setGameModel(game);
        panelManager.setResourcesToTake(2);
        panelManager.setNickname("Obi-Wan");

        //Setting up the LimitedRes drag
        LimitedResourcesDrag limitedResourcesDrag = new LimitedResourcesDrag();
        HashMap<ResourceType, Integer> res = new HashMap<>();
        res.put(ResourceType.COIN, 2);
        res.put(ResourceType.STONE, 3);
        res.put(ResourceType.SERVANT, 1);
        res.put(ResourceType.SHIELD, 1);
        limitedResourcesDrag.init(res);
        frame.add(limitedResourcesDrag, BorderLayout.PAGE_END);

        //Setting up the DepotDrop
        DepotDrop depot = new DepotDrop();
        depot.initFromFiniteDrag(new CheckDropInDepot(depot), limitedResourcesDrag);
        frame.add(depot, BorderLayout.CENTER);

        //Setting up the TrashCan
        DiscardedResDrop trashCan = new DiscardedResDrop();
        trashCan.initFromFiniteRes(new DumbCheckDrop(), limitedResourcesDrag);
        frame.add(trashCan, BorderLayout.PAGE_START);

        //Setting up the CancelButton
        CancelButton cancel = new CancelButton("Cancel");
        cancel.addActionListener(new ResetState(depot, trashCan, limitedResourcesDrag));

        //Setting up the Submit button
        SubmitButton submit = new SubmitButton("Submit");
        submit.addActionListener(new CollectPlacingResources(depot, trashCan));

        //Placing the buttons
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(cancel);
        panel.add(submit);
        frame.add(panel, BorderLayout.LINE_END);

        //Finishing it up
        frame.setTitle("Depot reset");
        frame.pack();
        frame.setVisible(true);
    }

    public static void checkPlacingResourcesReset(){
        //Setting up the frame
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Setting up the Player
        PanelManager panelManager = PanelManager.createInstance(new GuiClient());
        Player player = new Player();
        player.setNickName("Obi-Wan");
        player.addDepotShelf(new DepotShelf(ResourceType.COIN, 1));
        player.addDepotShelf(new DepotShelf(null, 0));
        player.addDepotShelf(new DepotShelf(ResourceType.SERVANT, 2));
        HashMap<ResourceType, Integer> res = new HashMap<>();
        res.put(ResourceType.COIN, 2);
        res.put(ResourceType.SERVANT, 3);
        res.put(ResourceType.STONE, 5);
        player.setStrongBox(res);
        Game game = new Game();
        game.addPlayer(player);
        panelManager.setGameModel(game);
        panelManager.setResourcesToTake(2);
        panelManager.setNickname("Obi-Wan");

        //Adding the DepotDrag
        DepotDrag depotDrag = new DepotDrag();
        depotDrag.init();
        frame.add(depotDrag, BorderLayout.CENTER);

        //Adding the StrongBoxDrag
        StrongBoxDrag strongBox = new StrongBoxDrag();
        strongBox.init();
        frame.add(strongBox, BorderLayout.PAGE_START);

        //Setting up the PanelDrop
        PanelDrop pDrop = new PanelDrop();
        HashMap<ResourceType, Integer> resToBeTaken = new HashMap<>();
        resToBeTaken.put(ResourceType.COIN, 1);
        resToBeTaken.put(ResourceType.SERVANT, 1);
        resToBeTaken.put(ResourceType.STONE, 1);
        CheckLimitedDrop checker = new CheckLimitedDrop(resToBeTaken);
        pDrop.init(checker, strongBox, depotDrag, pDrop);
        frame.add(pDrop, BorderLayout.PAGE_END);

        //Setting up the CancelButton
        CancelButton cancel = new CancelButton("Cancel");
        cancel.addActionListener(new ResetState(depotDrag, strongBox, pDrop));

        //Setting up the Submit button
        SubmitButton submit = new SubmitButton("Submit");
        //submit.addActionListener(new CollectCostsChoices(pDrop));

        //Placing the buttons
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(cancel);
        panel.add(submit);
        frame.add(panel, BorderLayout.LINE_END);

        //Finishing it up
        frame.setTitle("Depot reset");
        frame.pack();
        frame.setVisible(true);

    }

    @Deprecated
    private static void checkMoveToLeaderCard() {
        //Setting up the frame
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Setting up the Player
        PanelManager panelManager = PanelManager.createInstance(new GuiClient());
        Player player = new Player();
        player.setNickName("Obi-Wan");
        player.addDepotShelf(new DepotShelf(ResourceType.STONE, 1));
        player.addDepotShelf(new DepotShelf(null, 0));
        player.addDepotShelf(new DepotShelf(ResourceType.SERVANT, 2));
        Game game = new Game();
        game.addPlayer(player);
        panelManager.setGameModel(game);
        panelManager.setResourcesToTake(2);
        panelManager.setNickname("Obi-Wan");

        //Adding the DepotDrag
        DepotDrag depotDrag = new DepotDrag();
        depotDrag.init();
        frame.add(depotDrag, BorderLayout.CENTER);

        //Adding a LeaderCard
        List<Requirement> req = new ArrayList<>();
        req.add(new CardRequirementResource(ResourceType.COIN, 5));
        LeaderCard leader = new LeaderCard(3, req, new ExtraSlotLeaderEffect(ResourceType.STONE, 2), "src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-53-1.png");
        LeaderCardDrop leaderDrop = new LeaderCardDrop(leader, 1);

        //Adding another LeaderCard
        LeaderCard l2 = new LeaderCard(leader);
        LeaderCardDrop leaderDrop2 = new LeaderCardDrop(leader, 0);

        //Creating the listener
        List<LeaderCardDrop> leaderCards = new ArrayList<>();
        leaderCards.add(leaderDrop);
        leaderCards.add(leaderDrop2);
        CheckMoveToLeader checker = new CheckMoveToLeader(leaderCards);
        RegisterMoveToLeader registerDrop = new RegisterMoveToLeader(leaderCards, depotDrag);
        leaderDrop.init(checker, registerDrop);
        leaderDrop2.init(checker, registerDrop);

        //Adding the LeaderCards to the same panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(leaderDrop);
        panel.add(leaderDrop2);
        frame.add(panel, BorderLayout.PAGE_START);

        //Adding the submit button
        SubmitButton submit = new SubmitButton("Submit");
        //submit.addActionListener(new CollectMoveToLeaderOld(leaderCards));

        //Setting up the CancelButton
        CancelButton cancel = new CancelButton("Cancel");
        cancel.addActionListener(new ResetState(leaderDrop, leaderDrop2, depotDrag));

        //Placing the buttons
        JPanel buttons = new JPanel();
        buttons.add(cancel);
        buttons.add(submit);
        frame.add(buttons, BorderLayout.LINE_END);

        //Finishing it up
        frame.setTitle("LeaderCard");
        frame.pack();
        frame.setVisible(true);
    }

    private static void checkMoveShelfToLeaderEasier() {
        //Setting up the frame
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Creating the LeaderCards
        List<Requirement> req = new ArrayList<>();
        req.add(new CardRequirementResource(ResourceType.COIN, 5));
        LeaderCard leader = new LeaderCard(3, req, new ExtraSlotLeaderEffect(ResourceType.STONE, 2), "src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-53-1.png");
        LeaderCard l2 = new LeaderCard(3, req, new ExtraSlotLeaderEffect(ResourceType.SERVANT, 2), "src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-54-1.png");
        List<LeaderCard> active = new ArrayList<>();
        active.add(leader);
        active.add(l2);

        //Setting up the Player
        PanelManager panelManager = PanelManager.createInstance(new GuiClient());
        Player player = new Player();
        player.setNickName("Obi-Wan");
        player.addDepotShelf(new DepotShelf(ResourceType.STONE, 1));
        player.addDepotShelf(new DepotShelf(null, 0));
        player.addDepotShelf(new DepotShelf(ResourceType.SERVANT, 2));
        player.setUsedLeaders(active);
        HashMap<ResourceType, Integer> leaderSlots = new HashMap<>();
        //leaderSlots.put(ResourceType.STONE, 1);
        leaderSlots.put(ResourceType.SERVANT, 2);
        player.setLeaderSlots(leaderSlots);
        Game game = new Game();
        game.addPlayer(player);
        panelManager.setGameModel(game);
        panelManager.setResourcesToTake(2);
        panelManager.setNickname("Obi-Wan");

        //Adding the MoveShelfToLeader panel
        MoveShelfToLeader move = new MoveShelfToLeader();
        frame.add(move);


        //Finishing it up
        frame.setTitle("LeaderCard");
        frame.pack();
        frame.setVisible(true);
    }

    private static void checkMoveLeaderToDepotEasier() {
        //Setting up the frame
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Creating the LeaderCards
        List<Requirement> req = new ArrayList<>();
        req.add(new CardRequirementResource(ResourceType.COIN, 5));
        LeaderCard leader = new LeaderCard(3, req, new ExtraSlotLeaderEffect(ResourceType.STONE, 2), "src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-53-1.png");
        LeaderCard l2 = new LeaderCard(3, req, new ExtraSlotLeaderEffect(ResourceType.SERVANT, 2), "src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-54-1.png");
        List<LeaderCard> active = new ArrayList<>();
        active.add(leader);
        active.add(l2);

        //Setting up the Player
        PanelManager panelManager = PanelManager.createInstance(new GuiClient());
        Player player = new Player();
        player.setNickName("Obi-Wan");
        player.addDepotShelf(new DepotShelf(ResourceType.STONE, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.COIN, 2));
        player.addDepotShelf(new DepotShelf(ResourceType.SERVANT, 2));
        player.setUsedLeaders(active);
        HashMap<ResourceType, Integer> leaderSlots = new HashMap<>();
        leaderSlots.put(ResourceType.STONE, 1);
        leaderSlots.put(ResourceType.SERVANT, 2);
        player.setLeaderSlots(leaderSlots);
        Game game = new Game();
        game.addPlayer(player);
        panelManager.setGameModel(game);
        panelManager.setResourcesToTake(2);
        panelManager.setNickname("Obi-Wan");

        //Adding the MoveShelfToLeader panel
        MoveLeaderToShelf move = new MoveLeaderToShelf();
        frame.add(move);

        //Finishing it up
        frame.setTitle("LeaderCard");
        frame.pack();
        frame.setVisible(true);
    }

    public static void checkMoveBetweenShelves() {
        //Setting up the frame
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Creating the LeaderCards
        List<Requirement> req = new ArrayList<>();
        req.add(new CardRequirementResource(ResourceType.COIN, 5));
        LeaderCard leader = new LeaderCard(3, req, new ExtraSlotLeaderEffect(ResourceType.STONE, 2), "src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-53-1.png");
        LeaderCard l2 = new LeaderCard(3, req, new ExtraSlotLeaderEffect(ResourceType.SERVANT, 2), "src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-54-1.png");
        List<LeaderCard> active = new ArrayList<>();
        active.add(leader);
        active.add(l2);

        //Setting up the Player
        PanelManager panelManager = PanelManager.createInstance(new GuiClient());
        Player player = new Player();
        player.setNickName("Obi-Wan");
        player.addDepotShelf(new DepotShelf(ResourceType.STONE, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.COIN, 2));
        player.addDepotShelf(new DepotShelf(ResourceType.SERVANT, 2));
        player.setUsedLeaders(active);
        HashMap<ResourceType, Integer> leaderSlots = new HashMap<>();
        leaderSlots.put(ResourceType.STONE, 1);
        leaderSlots.put(ResourceType.SERVANT, 2);
        player.setLeaderSlots(leaderSlots);
        Game game = new Game();
        game.addPlayer(player);
        panelManager.setGameModel(game);
        panelManager.setResourcesToTake(2);
        panelManager.setNickname("Obi-Wan");

        //Adding the MoveShelfToLeader panel
        MoveBetweenShelves move = new MoveBetweenShelves();
        frame.add(move);

        //Finishing it up
        frame.setTitle("LeaderCard");
        frame.pack();
        frame.setVisible(true);
    }

    public static void checkPlacingResourceFull(){
        //Setting up the frame
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Creating the LeaderCards
        List<Requirement> req = new ArrayList<>();
        req.add(new CardRequirementResource(ResourceType.COIN, 5));
        LeaderCard leader = new LeaderCard(3, req, new ExtraSlotLeaderEffect(ResourceType.STONE, 2), "src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-53-1.png");
        LeaderCard l2 = new LeaderCard(3, req, new ExtraSlotLeaderEffect(ResourceType.SERVANT, 2), "src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-54-1.png");
        List<LeaderCard> active = new ArrayList<>();
        active.add(leader);
        active.add(l2);

        //Setting up the Player
        PanelManager panelManager = PanelManager.createInstance(new GuiClient());
        Player player = new Player();
        player.setNickName("Obi-Wan");
        player.addDepotShelf(new DepotShelf(ResourceType.COIN, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.SHIELD, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.SERVANT, 2));
        player.setUsedLeaders(active);
        HashMap<ResourceType, Integer> lSlot = new HashMap<>();
        lSlot.put(ResourceType.STONE, 2);
        player.setLeaderSlots(lSlot);
        panelManager.setPlayer(player);
        Game game = new Game();
        game.addPlayer(player);
        panelManager.setGameModel(game);
        panelManager.setResourcesToTake(2);
        panelManager.setNickname("Obi-Wan");

        //Setting up the LimitedRes drag
        LimitedResourcesDrag limitedResourcesDrag = new LimitedResourcesDrag();
        HashMap<ResourceType, Integer> res = new HashMap<>();
        res.put(ResourceType.COIN, 2);
        res.put(ResourceType.STONE, 3);
        res.put(ResourceType.SERVANT, 1);
        res.put(ResourceType.SHIELD, 1);
        limitedResourcesDrag.init(res);
        frame.add(limitedResourcesDrag, BorderLayout.PAGE_END);

        //Setting up the DepotDrop
        DepotDrop depot = new DepotDrop();
        depot.initFromFiniteDrag(new CheckDropInDepot(depot), limitedResourcesDrag);

        //Setting up the LeaderCards
        DropLeaderCards leaders = new DropLeaderCards();
        leaders.initFromFiniteRes(new CheckDropInLeader(), limitedResourcesDrag);

        //Setting up the DiscardedDrop
        DiscardedResDrop trashCan = new DiscardedResDrop();
        trashCan.initFromFiniteRes(new DumbCheckDrop(), limitedResourcesDrag);

        //Uniting the Depot, LeaderDrop and the Discarded in a JPanel
        JLabel centralPanel = new JLabel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.X_AXIS));
        centralPanel.add(depot);
        centralPanel.add(trashCan);
        centralPanel.add(leaders);
        frame.add(centralPanel, BorderLayout.CENTER);

        //Adding the submit button
        SubmitButton submit = new SubmitButton("Submit");
        submit.addActionListener(new CollectPlacingResources(depot, trashCan, leaders.getCards()));

        //Adding the Cancel button
        CancelButton cancel = new CancelButton("cancel");
        cancel.addActionListener(new ResetState(depot, leaders, trashCan, limitedResourcesDrag));

        //Uniting the buttons altogether
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.add(submit);
        buttons.add(cancel);
        frame.add(buttons, BorderLayout.LINE_END);

        //Finishing it up
        frame.setTitle("StrongBox drag & Panel Drop test");
        frame.pack();
        frame.setVisible(true);
    }

    public static void checkPayingTheCostFull(){
        //Setting up the frame
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Creating the LeaderCards
        List<Requirement> req = new ArrayList<>();
        req.add(new CardRequirementResource(ResourceType.COIN, 5));
        LeaderCard leader = new LeaderCard(3, req, new ExtraSlotLeaderEffect(ResourceType.STONE, 2), "src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-53-1.png");
        LeaderCard l2 = new LeaderCard(3, req, new ExtraSlotLeaderEffect(ResourceType.SERVANT, 2), "src/main/resources/Masters of Renaissance_Cards_FRONT/Masters of Renaissance_Cards_FRONT_3mmBleed_1-54-1.png");
        List<LeaderCard> active = new ArrayList<>();
        active.add(leader);
        active.add(l2);

        //Setting up the Player
        PanelManager panelManager = PanelManager.createInstance(new GuiClient());
        Player player = new Player();
        player.setNickName("Obi-Wan");
        player.addDepotShelf(new DepotShelf(ResourceType.COIN, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.SHIELD, 1));
        player.addDepotShelf(new DepotShelf(ResourceType.SERVANT, 2));
        player.setUsedLeaders(active);
        HashMap<ResourceType, Integer> resToStrongBox = new HashMap<>();
        resToStrongBox.put(ResourceType.COIN, 2);
        resToStrongBox.put(ResourceType.STONE, 3);
        resToStrongBox.put(ResourceType.SERVANT, 1);
        player.setStrongBox(resToStrongBox);
        HashMap<ResourceType, Integer> lSlot = new HashMap<>();
        lSlot.put(ResourceType.STONE, 1);
        lSlot.put(ResourceType.SERVANT, 2);
        player.setLeaderSlots(lSlot);
        Game game = new Game();
        game.addPlayer(player);
        panelManager.setGameModel(game);
        panelManager.setResourcesToTake(2);
        panelManager.setNickname("Obi-Wan");
        panelManager.setPlayer(player);

        //Setting up the depot drag
        DepotDrag depotDrag = new DepotDrag();
        depotDrag.init();

        //Setting up the strongBox drag
        StrongBoxDrag strongBox = new StrongBoxDrag();
        strongBox.init();

        //Setting up the LeaderCards
        DragLeaderCards leaders = new DragLeaderCards();

        //Creating a panel that can hold the three DragSource
        JPanel dragSources = new JPanel();
        dragSources.setLayout(new BoxLayout(dragSources, BoxLayout.X_AXIS));
        dragSources.add(depotDrag);
        dragSources.add(strongBox);
        dragSources.add(leaders);
        frame.add(dragSources, BorderLayout.CENTER);

        //Setting up the PanelDrop
        PanelDrop pDrop = new PanelDrop();
        HashMap<ResourceType, Integer> resToBeTaken = new HashMap<>();
        resToBeTaken.put(ResourceType.SERVANT, 1);
        resToBeTaken.put(ResourceType.STONE, 1);
        CheckLimitedDrop checker = new CheckLimitedDrop(resToBeTaken);
        RegisterDropInterface registerDrop = new RegisterLimitedDropInPlainPanel(checker, strongBox, depotDrag, leaders, pDrop);
        MyDropTargetListener dListener = new MyDropTargetListener(pDrop,registerDrop, checker);
        pDrop.init(dListener);
        frame.add(pDrop, BorderLayout.PAGE_END);

        //Creating the Submit button
        SubmitButton submit = new SubmitButton("submit");
        //submit.addActionListener(new CollectCostsChoices(pDrop));

        //Creating the cancel button
        CancelButton cancel = new CancelButton("cancel");
        cancel.addActionListener(new ResetState(depotDrag, strongBox, leaders, pDrop));

        //Creating a panel for the buttons
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
        buttons.add(submit);
        buttons.add(cancel);
        frame.add(buttons, BorderLayout.LINE_END);

        //Finishing it up
        frame.setTitle("StrongBox drag & Panel Drop test");
        frame.pack();
        frame.setVisible(true);
    }
}
/*
package it.polimi.ingsw.view.GUI.panels;

        import it.polimi.ingsw.model.resources.ResourceType;
        import it.polimi.ingsw.view.gui.ViewComponents.buttons.SubmitButton;

        import javax.swing.*;
        import javax.swing.border.TitledBorder;
        import java.awt.*;
        import java.awt.datatransfer.DataFlavor;
        import java.awt.datatransfer.Transferable;
        import java.awt.datatransfer.UnsupportedFlavorException;
        import java.awt.dnd.*;
        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.function.Consumer;

public class Test {

    public static void main(String[] args) {
        createAndShowJFrame();
    }

    public static void createAndShowJFrame() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                //JFrame frame = createJFrame();
                JFrame frame = new JFrame();
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JPanel depotPanels = createDepotPanels();
                frame.add(depotPanels, BorderLayout.CENTER);
                frame.setTitle("Depot test");
                frame.pack();
                frame.add( new SubmitButton("Confirm"), BorderLayout.PAGE_END);
                frame.setVisible(true);
            }
        });
    }

    private static JPanel createDepotPanels(){
        JPanel panelContainer = new JPanel();
        panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.Y_AXIS));

        ShelfDrop panel1 = createEmptyJPanel(1);
        new MyDropTargetListener(panel1);//this must be done or we wont be able to drop any image onto the empty panel
        panelContainer.add(panel1);

        ShelfDrop panel2 = createEmptyJPanel(2);
        new MyDropTargetListener(panel2);//this must be done or we wont be able to drop any image onto the empty panel
        panelContainer.add(panel2);

        ShelfDrop panel3 = createEmptyJPanel(3);
        new MyDropTargetListener(panel3);//this must be done or we wont be able to drop any image onto the empty panel
        panelContainer.add(panel3);


        try {
            panelContainer.add(createResourcesPanel());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return panelContainer;

    }

    private static JFrame createJFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setTitle("Test");

        ShelfDrop panel = createEmptyJPanel(1);
        new MyDropTargetListener(panel);//this must be done or we wont be able to drop any image onto the empty panel
        ShelfDrop panel2 = createEmptyJPanel(2);
        new MyDropTargetListener(panel2);//this must be done or we wont be able to drop any image onto the empty panel
        ShelfDrop panel3 = createEmptyJPanel(3);
        new MyDropTargetListener(panel3);//this must be done or we wont be able to drop any image onto the empty panel

        frame.add(panel, BorderLayout.NORTH);
        frame.add(panel2, BorderLayout.CENTER);

        try {
            frame.add(createResourcesPanel(), BorderLayout.SOUTH);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        frame.pack();

        return frame;
    }


    private static ShelfDrop createEmptyJPanel(int shelf) {
        final ShelfDrop p = new ShelfDrop(shelf);

        p.setBorder(new TitledBorder("Drag Image onto this panel "+ shelf));

        TransferHandler dnd = new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                if (!support.isDrop()) {
                    return false;
                }
                //only Strings
                if (!support.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean importData(TransferSupport support) {
                if (!canImport(support)) {
                    return false;
                }

                Transferable tansferable = support.getTransferable();
                Icon ico;
                try {
                    ico = (Icon) tansferable.getTransferData(DataFlavor.imageFlavor);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                p.add(new JLabel(ico));
                return true;
            }
        };

        p.setTransferHandler(dnd);

        return p;
    }

    private static JPanel createResourcesPanel() throws Exception {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Drag Resources from here to the Depot above"));

        JLabel label1 = new JLabel(new ImageIcon("src/main/resources/coins small.png"));
        JLabel label2 = new JLabel(new ImageIcon("src/main/resources/servant small.png"));
        JLabel label3 = new JLabel(new ImageIcon("src/main/resources/shield small.png"));
        JLabel label4 = new JLabel(new ImageIcon("src/main/resources/stone small.png"));

        MyDragGestureListener dlistener = new MyDragGestureListener();
        DragSource ds1 = new DragSource();
        ds1.createDefaultDragGestureRecognizer(label1, DnDConstants.ACTION_COPY, dlistener);

        DragSource ds2 = new DragSource();
        ds2.createDefaultDragGestureRecognizer(label2, DnDConstants.ACTION_COPY, dlistener);

        DragSource ds3 = new DragSource();
        ds3.createDefaultDragGestureRecognizer(label3, DnDConstants.ACTION_COPY, dlistener);
        DragSource ds4 = new DragSource();
        ds3.createDefaultDragGestureRecognizer(label4, DnDConstants.ACTION_COPY, dlistener);

        panel.add(label1);
        panel.add(label2);
        panel.add(label3);
        panel.add(label4);
        return panel;
    }
}
*//*
class MyDropTargetListener extends DropTargetAdapter {

    private DropTarget dropTarget;
    private ShelfDrop p;
    private Consumer<Icon> makeCall;

    public MyDropTargetListener(ShelfDrop panel) {
        p = panel;
        dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, this, true, null);
        makeCall = new RegisterDropFromInfiniteRes(panel);
    }

    @Override
    public void drop(DropTargetDropEvent event) {
        try {
            DropTarget test = (DropTarget) event.getSource();
            Component ca = (Component) test.getComponent();
            Point dropPoint = ca.getMousePosition();
            Transferable tr = event.getTransferable();

            if (event.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                Icon ico = (Icon) tr.getTransferData(DataFlavor.imageFlavor);

                if (ico != null) {

                    p.add(new JLabel(ico));
                    p.revalidate();
                    p.repaint();
                    event.dropComplete(true);
                    this.makeCall.accept(ico);
                }
            } else {
                event.rejectDrop();
            }
        } catch (Exception e) {
            e.printStackTrace();
            event.rejectDrop();
        }
    }

    public void addActionListener(Consumer<Icon> function){
        this.makeCall = function;
    }
}
*//*
class MyDragGestureListener implements DragGestureListener {

    @Override
    public void dragGestureRecognized(DragGestureEvent event) {
        JLabel label = (JLabel) event.getComponent();
        final Icon ico = label.getIcon();


        Transferable transferable = new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{DataFlavor.imageFlavor};
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                if (!isDataFlavorSupported(flavor)) {
                    return false;
                }
                return true;
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                return ico;
            }
        };
        event.startDrag(null, transferable);
    }
}
*/
/*
class ShelfDrop extends JPanel{
    private int shelfNumber;
    /**
     * In each depot panel we have this resToDepot list which takes track of all the resources moved to this depot. When  the confirm
     * button is clicked then all the list from all the three depot panels must be collected and sent to the model somehow
     */

    /*private List<Pair<Integer, ResourceType>> resToDepot;

    class Pair<Integer, ResourceType> {
        private Integer shelf;
        private ResourceType res;

        public Pair(Integer shelf, ResourceType res) {
            this.shelf = shelf;
            this.res = res;
        }

        public Integer getKey() {
            return shelf;
        }

        public ResourceType getValue() {
            return res;
        }

        public void setKey(Integer shelf) {
            this.shelf = shelf;
        }

        public void setValue(ResourceType res) {
            this.res = res;
        }
    }

    public ShelfDrop(int shelfNumber){
        super();
        this.shelfNumber = shelfNumber;
        resToDepot = new ArrayList<>();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    }

    public void addDecision(Integer shelf, ResourceType res){
        Pair<Integer, ResourceType> decision = new Pair<>(shelf, res);
        this.resToDepot.add(decision);
    }

    public int getShelfNumber() {
        return shelfNumber;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon(getDepotFileName(shelfNumber)).getImage(), 100, 100, null);
    }

    private String getDepotFileName(int shelf){
        switch (shelf){
            case 1:
                return "src/main/resources/shelf 1.png";
            case 2:
                return "src/main/resources/shelf 2.png";
            case 3:
                return "src/main/resources/shelf 3.png";
        }
        return "";
    }
}

class RegisterDropFromInfiniteRes implements Consumer<Icon>{
    private final ShelfDrop panel;

    public RegisterDropFromInfiniteRes(ShelfDrop panel){
        this.panel = panel;
    }
    @Override
    public void accept(Icon icon) {
        switch (icon.toString()){
            case "src/main/resources/coins small.png":
                panel.addDecision(panel.getShelfNumber(), ResourceType.COIN);
                break;
            case "src/main/resources/servant small.png":
                panel.addDecision(panel.getShelfNumber(), ResourceType.SERVANT);
                break;
            case "src/main/resources/shield small.png":
                panel.addDecision(panel.getShelfNumber(), ResourceType.SHIELD);
                break;
            case "src/main/resources/stone small.png":
                panel.addDecision(panel.getShelfNumber(), ResourceType.STONE);
                break;
        }
    }
}
*/

