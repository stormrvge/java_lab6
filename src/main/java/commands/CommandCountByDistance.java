package commands;

import logic.CollectionManager;
import logic.Packet;

public class CommandCountByDistance extends Command {

    /**
     * This overridden method just uses method from Control Unit.
     * @param object - object of route.
     * @return
     */
    public String execOnServer(CollectionManager collectionManager, Object object) {
        return collectionManager.count_by_distance((Float) object);
    }

    @Override
    public Packet execOnClient(String ... args) {
        Float distance = Float.parseFloat(args[0]);
        return new Packet(this, distance);
    }
}
