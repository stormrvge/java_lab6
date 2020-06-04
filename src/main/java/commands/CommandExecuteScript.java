package commands;

import client.Client;
import io.FileHandler;
import logic.CollectionManager;
import logic.Packet;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Execute script
 */

public class CommandExecuteScript extends Command {
    Client client;
    Invoker invoker;
    ArrayList<String> file_already_run;
    ArrayList<Packet> packetArrayList;

    private static final int max_recursion_depth = 1000;
    private int current_depth;

    public CommandExecuteScript(Client client, Invoker invoker) {
        this.client = client;
        this.invoker = invoker;
        this.file_already_run = new ArrayList<>();
        this.current_depth = 0;
        this.packetArrayList = new ArrayList<>();
    }

    public ArrayList<Packet> execute(String[] cmd_args) {
        ++current_depth;
        final int num_args = 1;


        if ((cmd_args.length - 1) != num_args) {
            System.err.println("Execute script cannot take " + (cmd_args.length - 1) + " arguments.");
        } else if (max_recursion_depth == current_depth) {
            System.err.println("Maximum recursion depth reached!");
            System.err.println("Ignore execute_script commands!");
        } else {
            try {
                String cmd;
                FileHandler file = new FileHandler(cmd_args[1], FileHandler.READ);
                boolean is_founded = false;


                for (String file_run : file_already_run) {
                    if (file_run.equals(cmd_args[1])) {
                        System.err.println("Recursion detected!");
                        System.err.println("Current recursion depth: " + ++this.current_depth);
                        System.err.println("Max recursion depth: " + max_recursion_depth);
                        is_founded = true;
                        break;
                    }
                }

                if (!is_founded) {
                    file_already_run.add(cmd_args[1]);
                }

                while ((cmd = file.readline()) != null) {
                    cmd = cmd.trim().replace('\t', ' ');
                    while (cmd.contains("  ")) {
                        cmd = cmd.replace("  ", " ");
                    }
                    Command command = invoker.createCommand(cmd);
                    String[] args = invoker.getArgs();
                    if (!invoker.getCommandName().equals("execute_script")) {
                        Packet packet = new Packet(command, args);
                        packetArrayList.add(packet);
                    }
                    else if (invoker.getCommandName().equals("execute_script")) {
                        String[] recStr = new String[2];
                        recStr[0] = invoker.getCommandName();
                        String[] argsInv = invoker.getArgs();
                        recStr[1] = args[0];

                        execute(recStr);
                    }
                }

                --current_depth;
                file.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

        if (current_depth == 0) {
            return packetArrayList;
        }
        return null;
    }

    @Override
    public String execOnServer(CollectionManager collectionManager, Object objects) {
        return null;
    }
}