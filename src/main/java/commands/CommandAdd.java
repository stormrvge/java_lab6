package commands;


import logic.CollectionManager;
import logic.Packet;
import logic.Route;

/**
 * This class of add command, which uses method add in Control Unit.
 */
public class CommandAdd extends Command {

    /**
     * This overridden method just uses method from Control Unit.
     * @param object - object of route.
     * @return
     */
    public String execOnServer(CollectionManager collectionManager, Object object) {
        return collectionManager.add((Route) object);
    }

    @Override
    public Packet execOnClient(String ... args) {
        Route route = Route.generateObjectUserInput();
        return new Packet(this, route);
    }
}
