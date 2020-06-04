package commands;

import logic.CollectionManager;

public class CommandSave extends Command {

    @Override
    public void serverCmd(CollectionManager collectionManager, Object args) {
        try {
            String[] path = (String[]) args;
            collectionManager.save(path[0]);
            System.out.println("Collection was saved!");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Incorrect number of arguments!");
        }
    }
}
