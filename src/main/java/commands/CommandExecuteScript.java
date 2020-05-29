package commands;

import io.InputFromFile;
import logic.CollectionManager;
import logic.Packet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class of command Execute Script.
 * This command execute file with script.
 */

public class CommandExecuteScript extends Command implements Serializable {
    private Invoker invoker;
    private static ArrayList<String> calls;
    private static ArrayList<Packet> packets;

    /**
     * Constructor
     * @param invoker - the object of Invoker.
     */

    public CommandExecuteScript(Invoker invoker) {
        this.invoker = invoker;
        calls = new ArrayList<>();
        packets = new ArrayList<>();
    }

    public boolean validateArgs(String ... args) {
        return args.length == 1;
    }

    /**
     * This method executes script.
     * @param collectionManager -the manager of collection
     */

    @Override
    public String execOnServer(CollectionManager collectionManager, Object object) {
        return collectionManager.execute_script();
    }

    public ArrayList<Packet> execOnClient(String userInput) {

        String[] args = userInput.split(" ");
        String path = args[1];

        boolean exit = false;

        try {
            for (String com : calls) {
                if (com.equals(path)) {
                    exit = true;
                }
            }
            if (exit) {
                System.out.println("Warning! The danger of infinite recursion: " +
                        "the same script is called more that once");
                return null;
            } else {
                InputFromFile input = new InputFromFile(path);
                String nextLine;
                while (input.hasNextLine()) {
                    nextLine = input.readLine();

                    if (exit) {
                        exit = false;
                        break;
                    }
                    calls.add(path);
                    try {
                        Command command = invoker.createCommand(nextLine);
                        if(nextLine.contains("execute_script")) {
                            CommandExecuteScript com = (CommandExecuteScript) command;
                            ArrayList<Packet> answer = com.execOnClient(nextLine);
                            packets.addAll(answer);
                        } else {
                            packets.add(command.execOnClient(nextLine));
                        }
                    } catch (NullPointerException ex) {
                        System.out.println();
                    }
                }

                input.closeFile();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found! Enter the correct path to the file!");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return packets;
    }
}