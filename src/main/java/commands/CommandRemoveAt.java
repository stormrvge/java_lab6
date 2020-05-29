package commands;

import logic.CollectionManager;
import logic.Packet;

public class CommandRemoveAt extends Command {

    /**
     * This overridden method just uses method from Control Unit.
     * @param args - arguments from console.
     * @return
     */
    @Override
    public String execOnServer(CollectionManager collectionManager, Object args) {
        return collectionManager.remove_at((Integer) args);
    }

    @Override
    public Packet execOnClient(String ... args) {
        Integer index = Integer.parseInt(args[0]);
        return new Packet(this, index);
    }
}
