package commands;

import logic.CollectionManager;
import logic.Packet;
import logic.Route;

/**
 * This class of add_if_max command. This class just call method from Control Unit.
 */
public class CommandAddIfMin extends Command {

    /**
     * This overridden method just uses method from Control Unit.
     * @param object - object of route.
     * @return
     */
    public String execOnServer(CollectionManager collectionManager, Object object) {
        return collectionManager.add_if_min((Route) object);
    }

    @Override
    public Packet execOnClient(String ... args) {
        Route route = Route.generateObjectUserInput();
        return new Packet(this, route);
    }
}