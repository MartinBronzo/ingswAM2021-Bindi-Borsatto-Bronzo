package it.polimi.ingsw.controller;

class ServerTest {
    /*private Server server;
    private final static int portNumber =9047;
    private final static String hostName = "127.0.0.1";
    Thread serverThread;

    @BeforeEach
    void setUp() throws FileNotFoundException, InterruptedException {
        server = new Server(portNumber);
        GamesManagerSingleton.getInstance().resetSingleton();
        serverThread = new Thread(() -> {
            server.startServer();
        });
        serverThread.start();
        sleep(2000);
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        serverThread.interrupt();
        Thread.sleep(1000);
    }

    @Test
    void SetNicknameTest() throws InterruptedException, IllegalArgumentException, IOException {
        PipedWriter writer = new PipedWriter();
        PipedReader reader = new PipedReader(writer);
        PrintWriter printWriter = new PrintWriter(writer);
        BufferedReader bufferedReader = new BufferedReader(reader);

        CliClient client = new CliClient(portNumber, hostName, bufferedReader);
        Thread thread =  new Thread(() -> {
            client.startConnection();
            client.doConnection();
        });
        thread.start();
        sleep(1000);
        printWriter.println("setnickname");
        printWriter.println("Client2");
        //sleep(1000);
        sleep(1000);
        GameController gameController = GamesManagerSingleton.getInstance().getStartingGame();
        assertEquals(gameController.getState(), GameState.CONFIGURING);
        //assertEquals("Client1", client.getNickname());
        //assertEquals(gameController.getPlayers().get(0).getKey().getPlayerState(), PlayerState.WAITING4SETNUMPLAYER);
        printWriter.println("quit");
        serverThread.interrupt();
        thread.interrupt();
        sleep(1000);
    }

    @Test
    void SetNumPlayers() throws InterruptedException, IllegalArgumentException, IOException {
        PipedWriter writer = new PipedWriter();
        PipedReader reader = new PipedReader(writer);
        PrintWriter printWriter = new PrintWriter(writer);
        BufferedReader bufferedReader = new BufferedReader(reader);

        CliClient client = new CliClient(portNumber, hostName, bufferedReader);
        Thread thread =  new Thread(() -> {
            client.startConnection();
            client.doConnection();
        });
        thread.start();
        sleep(1000);
        printWriter.println("setnickname");
        printWriter.println("Client1");
        //sleep(1000);
        //assertEquals(client.getNickname(), "Client1");
        printWriter.println("setnumofplayers");
        printWriter.println("2");
        sleep(1000);

        GameController gameController = GamesManagerSingleton.getInstance().getStartingGame();
        assertEquals(GameState.WAITING4PLAYERS, gameController.getState());
        assertEquals(gameController.getPlayers().get(0).getKey().getPlayerState(), PlayerState.WAITING4GAMESTART);

        //GameController gameController = GamesManagerSingleton.getInstance().getGames().iterator().next();
        //assertEquals(gameController.getState(), GameState.CONFIGURING);
        printWriter.println("quit");
        serverThread.interrupt();
        thread.interrupt();
        sleep(1000);
    }*/
}