package commands;

import logic.CollectionManager;

public class CommandClear extends Command {

    /**
     * This overridden method just uses method from Control Unit.
     * @param collectionManager - collection manager on server.
     * @return
     */
    @Override
    public String execOnServer(CollectionManager collectionManager, Object args) {
        return collectionManager.clear();
    }
}
