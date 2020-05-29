package client;

import commands.*;
import io.Input;
import logic.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class Client {
    private static SocketChannel channel;
    private Input input;
    private static Invoker invoker;
    private int port;
    private String hostname;
    private static boolean close = false;
    private int tryingConnect = 0;

    private static final Logger logger = LoggerFactory.getLogger(Client.class);


    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void run() {
        invoker = new Invoker();
        input = new Input();


        try {
            try {
                SocketAddress addr = new InetSocketAddress(hostname, port);
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

                channel.close();
                input.closeInput();
                System.out.println("Client was closed...");
                logger.info("Client was disconnected from server.");
            } catch (SocketException e) {
                System.out.println("Cant connect to the server. Server is down.");
                logger.info("Cant connect to the server. Server is down.");
                for (; tryingConnect < 10;) {
                    Thread.sleep(1000);
                    ++tryingConnect;
                    run();
                }
                closeConnection();
            }
        } catch (NullPointerException | IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleRequest(String userInput) throws IOException {
        Command command = invoker.createCommand(userInput);

        if (command != null) {
            String[] args = invoker.getArgs();

            if (userInput.contains("execute_script")) {
                CommandExecuteScript cmd = (CommandExecuteScript) command;
                ArrayList<Packet> packets = cmd.execOnClient(userInput);
                if (packets != null) {
                    for (Packet p : packets) {
                        sendPacket(p, userInput);
                        logger.info("Packet " + invoker.getCommandName() + " was sent to server.");
                    }
                }
            }
            else {
                Packet packet = command.execOnClient(args);
                sendPacket(packet, userInput);
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


    private void registerCommands(Invoker invoker) {
        invoker.register("show", new CommandShow());
        invoker.register("execute_script", new CommandExecuteScript( invoker));
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

    public static void closeConnection()  {
        close = true;
        logger.info("Client want to close connection.");
    }

    public void sendPacket(Packet packet, String userInput) throws IOException {
        if (packet != null) {
            byte[] message = serializeObject(packet);

            ByteBuffer wrap = ByteBuffer.allocate(1024);
            wrap = (ByteBuffer) ((Buffer)wrap).clear();
            wrap = ByteBuffer.wrap(message);

            channel.write(wrap);
            //System.out.println("Client sent command " + userInput + " to server.");
            System.out.println();


            ByteBuffer msg = ByteBuffer.allocate(4096);

            if (channel.isConnected() && !invoker.getCommandName().equals("exit")) {
                while (channel.read(msg) <= 0) {}
                channel.read(msg);
                String input = new String(msg.array());
                System.out.println(input);
                logger.info("Client read message from server.");
            }
        }
    }
}
