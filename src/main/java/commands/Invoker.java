package commands;

import java.util.HashMap;

/**
 * Invoker class which need for registering and executing
 * commands from cmd args.
 */
public class Invoker {
    private final HashMap<String, Command> commandMap = new HashMap<>();
    public Invoker() {}
    private String[] commandName_split;

    /**
     * This method registering new commands to invoker.
     * @param commandName - command name in string data type.
     * @param command - instance of command.
     */
    public void register(String commandName, Command command) {
        commandMap.put(commandName, command);
    }

    /**
     *
     * @param commandName - Name of command from cmd arguments.
     */
    public Command createCommand(String commandName) {
        try {
            if(commandName.equals("")) {
                throw new NullPointerException();
            } else {
                commandName = commandName.replace("\t", " ").trim();
                commandName_split = commandName.split(" ");
                Command command = commandMap.get(commandName_split[0]);
                if (command == null) {
                    throw new NullPointerException();
                } else {
                    return command;
                }
            }
        } catch (NullPointerException e) {
            System.err.println("Calling unregistered command!");
        }

        return null;
    }

    public String[] getArgs() {
        String[] args = new String[commandName_split.length - 1];
        System.arraycopy(commandName_split, 1, args, 0, args.length);

        return args;
    }

    public String getCommandName() {
        return commandName_split[0];
    }
}