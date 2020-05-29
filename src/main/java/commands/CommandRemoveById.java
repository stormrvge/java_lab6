package commands;

import logic.CollectionManager;
import logic.Packet;

/**
 * This class of remove_by_id command. This class just call method from Control Unit.
 */
public class CommandRemoveById extends Command {

    /**
     * This overridden method just uses method from Control Unit.
     * @param args - arguments from console.
     * @return
     */
    @Override
    public String execOnServer(CollectionManager collectionManager, Object args) {
        return collectionManager.remove_by_id((Integer) args);
    }

    @Override
    public Packet execOnClient(String ... args) {
        Integer id = Integer.parseInt(args[0]);
        return new Packet(this, id);
    }
}

