package server;

import commands.Command;
import commands.CommandExit;
import commands.CommandSave;
import commands.Invoker;
import logic.CollectionManager;
import logic.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;


public class Server {
    private int port;
    private static Socket clientSocket;
    private static ServerSocket server;
    private InputStream in;
    private ObjectOutputStream out;
    private BufferedReader reader;

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private Invoker invoker;
    private static CollectionManager manager;
    private static String path = System.getenv("lab6");
    private int numOfClients;


    public Server (int port) {
        this.port = port;
    }

    public void run() {
        try {
            server = new ServerSocket(port);
            server.setSoTimeout(1000);
            System.out.println("Server started on: " + server.getInetAddress());
            logger.info("Server was started correctly.");

            invoker = new Invoker();
            manager = new CollectionManager();
            logger.info("Collection and invoker were initialized correctly.");

            registerCommands(invoker);
            reader = new BufferedReader(new InputStreamReader(System.in));


            while (!server.isClosed()) {
                if (!reader.ready()) {
                    try {
                        acceptConnection();
                    } catch (SocketTimeoutException e) {
                        System.out.print("");
                    }

                }
                else if (reader.ready()) {
                    Command command = invoker.createCommand(reader.readLine().trim());
                    String[] args = invoker.getArgs();
                    command.serverCmd(manager, args);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            System.out.println("Server closed.");
            manager.save(path);
            logger.info("Collection saved.");
        }
    }



    private void acceptConnection() throws IOException {
        clientSocket = server.accept();
        ++numOfClients;
        logger.info("Client " + numOfClients + " connected: " + clientSocket.getInetAddress());
        manager.load(path);
        System.out.println("Server accepted client number " + numOfClients);

        if (clientSocket != null) {
            try {
                while (true) {
                    Packet packet = readMessage();
                    Command commandServer = packet.getCommand();
                    Object args = packet.getArgument();

                    if (commandServer != null) {
                        String message = commandServer.execOnServer(manager, args);
                        sendMessage(message);
                    }
                }
            } catch (NullPointerException | IOException e) {
                System.out.println("Client " + numOfClients + " was disconnected!");
                closeConnection();
            } catch (ClassNotFoundException e) {
                System.err.println("Class not found: " + e.getMessage());
                closeConnection();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                closeConnection();
            }
        }
    }

    private Packet readMessage() throws IOException, ClassNotFoundException {
        in = clientSocket.getInputStream();
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(in);

        return (Packet) objectInputStream.readObject();
    }

    private void sendMessage(String message) throws IOException, InterruptedException {
        Packet packet = new Packet(message);
        out.writeObject(packet);
        out.flush();
        Thread.sleep(50);
    }

    private static void closeConnection() {
        try {
            clientSocket.close();
            logger.info("Connection with client was closed.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void stopServer() {
        try {
            if (clientSocket != null) {
                closeConnection();
            }
            server.close();
            System.out.println("Server was stopped!");
            logger.info("Server was stopped.");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (NullPointerException e) {
            System.err.println();
        }

    }

    private void registerCommands(Invoker invoker) {
        invoker.register("exit", new CommandExit());
        invoker.register("save", new CommandSave());
        logger.info("All commands were registered!");
    }

    public static String parseIOException(IOException e) {
        String s = e.getMessage();

        if (s.contains("(") && s.contains(")")) {
            s = s.substring(s.indexOf("(") + 1);
            s = s.substring(0, s.indexOf(")"));
        }
        return s;
    }
}