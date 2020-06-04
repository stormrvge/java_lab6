package client;

import commands.*;
import io.Input;
import logic.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class Client {
    private static SocketChannel channel;
    private SocketAddress addr;
    private static Invoker invoker;
    private int port;
    private String hostname;
    private static boolean close = false;
    private static Input input;

    private static final Logger logger = LoggerFactory.getLogger(Client.class);


    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    void run() {
        invoker = new Invoker();
        input = new Input();

        try {
            try {
                addr = new InetSocketAddress(hostname, port);
                channel = SocketChannel.open(addr);
                channel.configureBlocking(false);
                System.out.println("Connected to server!");
                logger.info("Client was connected to server" + addr);

                registerCommands(invoker);

                while (!close) {
                    input.readCommand();
                    try {
                        handleRequest(input.getNextCommand());
                    } catch (IOException e) {
                        System.out.println();
                    }
                }
            } catch (SocketException e) {
                System.out.println("Cant connect to the server. Server is down.");
                logger.info("Cant connect to the server. Server is down.");
                reconnect();
            }
        } catch (NullPointerException | IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleRequest(String userInput) throws IOException, InterruptedException {
        Command command = invoker.createCommand(userInput);

        if (invoker.getCommandName().equals("exit")) {
            command.execOnClient();
        }
        else if (command != null) {
            String[] args = invoker.getArgs();

            if (userInput.contains("execute_script")) {
                CommandExecuteScript cmd = (CommandExecuteScript) command;
                ArrayList<Packet> packets = cmd.execute(userInput.split(" "));
                if (packets != null) {
                    for (Packet packet : packets) {
                        sendPacket(packet);
                        Thread.sleep(50);
                    }
                    logger.info("Packet with execute_script was sent to server.");
                }
            }
            else {
                Packet packet = command.execOnClient(args);
                sendPacket(packet);
                logger.info("Packet " + invoker.getCommandName() + " was sent to server.");
            }
        }
    }

    private static byte[] serializeObject(Object obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream out  = new ObjectOutputStream(b);

        out.writeObject(obj);
        return  b.toByteArray();
    }

    private void sendPacket(Packet packet) throws InterruptedException, IOException {
        if (packet != null) {
            byte[] message = serializeObject(packet);
            ByteBuffer wrap = ByteBuffer.wrap(message);

            try {
                channel.write(wrap);
                System.out.println();
                Thread.sleep(30);
            } catch (IOException e) {
                reconnect();
                System.err.println(e.getMessage());
            }
            readMessage();
        }
    }

    private void readMessage() throws IOException, InterruptedException {
        ByteBuffer msg = ByteBuffer.allocate(4096);

        if (channel.isConnected()) {
            channel.read(msg);

            if (msg.position() == 0) {
                System.out.println("Server is working in blocking mode. You must wait for your queue.");
                try {
                    System.out.println("Waiting...");
                    Thread.sleep(5000);
                    readMessage();
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                }

            }

            try {
                Packet packet = (Packet) deserialize(msg.array());
                String input = (String) packet.getArgument();
                if (input != null) {
                    System.out.println(input);
                }
            } catch (ClassNotFoundException e) {
                System.err.println(e.getMessage());
            }
            logger.info("Client read message from server.");
        }
    }

    private static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objStream = new ObjectInputStream(byteStream);

        return objStream.readObject();
    }

    private void reconnect() {
        System.out.println("Reconnecting...");
        try {
            for (int i = 0; i < 10; i++) {
                try {
                    channel = SocketChannel.open(addr);
                    run();
                    break;
                } catch (Exception e) {
                    System.err.println("No answer from server, trying: " + (i + 1));
                    logger.info("No answer from server, trying: " + (i + 1));
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }


    public static void disconnect()  {
        close = true;
        try {
            channel.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        input.closeInput();
        System.out.println("Client was closed...");
        logger.info("Client was disconnected from server.");
    }

    private void registerCommands(Invoker invoker) {
        invoker.register("show", new CommandShow());
        invoker.register("execute_script", new CommandExecuteScript( this, invoker));
        invoker.register("update_id", new CommandUpdateId());
        invoker.register("clear", new CommandClear());
        invoker.register("exit", new CommandExit());
        invoker.register("rm_at", new CommandRemoveAt());
        invoker.register("count_by_distance", new CommandCountByDistance());
        invoker.register("print_unique_distance", new CommandPrintUniqueDistance());
        invoker.register("add", new CommandAdd());
        invoker.register("add_if_min", new CommandAddIfMin());
        invoker.register("add_if_max", new CommandAddIfMax());
        invoker.register("rm_id", new CommandRemoveById());
        invoker.register("info", new CommandInfo());
        invoker.register("help", new CommandHelp());
        invoker.register("print_field_ascending_distance", new CommandPrintFieldAscendingDistance());
        logger.info("All commands were registered!");
    }
}
