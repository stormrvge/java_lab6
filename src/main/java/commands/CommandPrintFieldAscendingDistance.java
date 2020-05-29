package commands;

import logic.CollectionManager;

/**
 * This class of print_field_ascending_distance command. This class just call method from Control Unit.
 */
public class CommandPrintFieldAscendingDistance extends Command {

    /**
     * This overridden method just uses method from Control Unit.
     * @param collectionManager - collection manager on server.
     * @return
     */
    @Override
    public String execOnServer(CollectionManager collectionManager, Object args) {
        return collectionManager.print_field_ascending_distance();
    }
}
