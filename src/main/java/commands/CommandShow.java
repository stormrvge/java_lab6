package commands;

import logic.CollectionManager;

/**
 * This class of show command. This class just call method from Control Unit.
 */
public class CommandShow extends Command {

    /**
     * This overridden method just uses method from Control Unit.
     * @param collectionManager - collection manager on server.
     * @return
     */
    @Override
    public String execOnServer(CollectionManager collectionManager, Object args) {
        return collectionManager.show();
    }
}
