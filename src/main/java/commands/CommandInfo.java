package commands;

import logic.CollectionManager;

/**
 * This class of info command. This class just call method from Control Unit.
 */
public class CommandInfo extends Command {

    /**
     * This overridden method just uses method from Control Unit.
     * @return
     */
    @Override
    public String execOnServer(CollectionManager collectionManager, Object args) {
        return collectionManager.info();
    }
}
