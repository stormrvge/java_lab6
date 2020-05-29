package commands;

import logic.CollectionManager;

/**
 * This class of print_unique_distance command. This class just call method from Control Unit.
 */
public class CommandPrintUniqueDistance extends Command {
    /**
     * This overridden method just uses method from Control Unit.
     * @param collectionManager - collection manager on server.
     * @return
     */
    @Override
    public String execOnServer(CollectionManager collectionManager, Object args) {
        return collectionManager.print_unique_distance();
    }
}
