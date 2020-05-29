package server;

import commands.Command;
import logic.CollectionManager;
import logic.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port;
    private Socket clientSocket;
    private ServerSocket server;
    private InputStream in;
    private OutputStream out;

    private static final Logger logger = LoggerFactory.getLogger(Server.class);


    private CollectionManager manager;
    private String path = System.getenv("lab6");


    public Server (int port) {
        this.port = port;
    }

    public void run() throws IOException {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started on: " + server.getInetAddress());
            logger.info("Server was started correctly.");

            manager = new CollectionManager();
            logger.info("Collection was initialized correctly.");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            while (!(reader.ready() && reader.readLine().trim().equals("exit"))) {
                acceptConnection();
            }

            logger.info("Word \"exit\" has been entered.");
            logger.info("Stop receiving data.");
        } catch (IOException  e) {
            System.err.println(e.getMessage());
        } finally {
            System.out.println("Server closed.");
            manager.save(path);
            logger.info("Collection saved.");
            server.close();
            logger.info("Server closed.");
        }
    }



    private void acceptConnection() throws IOException {
        clientSocket = server.accept();
        logger.info("Client connected: " + clientSocket.getInetAddress());
        manager.load(path);
        System.out.println("Server accepted new client");

        if (clientSocket != null) {
            try {
                while (true) {
                    in = clientSocket.getInputStream();
                    out = clientSocket.getOutputStream();
                    ObjectInputStream objectInputStream = new ObjectInputStream(in);

                    Packet packet =  (Packet) objectInputStream.readObject();
                    Command commandServer = packet.getCommand();
                    Object args = packet.getArgument();

                    if (commandServer != null) {
                        out.write(commandServer.execOnServer(manager, args).getBytes());
                        out.flush();
                    }
                }
            } catch (NullPointerException | ClassNotFoundException | IOException e) {
                System.out.println("Client was disconnected!");
                closeConnection();
            }
        }
    }

    public void closeConnection() {
        try {
            clientSocket.close();
            logger.info("Connection with client was closed.");
            manager.save(path);
            logger.info("Collection saved.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
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
