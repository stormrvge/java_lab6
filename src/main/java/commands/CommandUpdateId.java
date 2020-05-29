package commands;

import logic.CollectionManager;
import logic.Packet;
import logic.Route;

/**
 * This class of add_if_max command. This class just call method from Control Unit.
 */
public class CommandUpdateId extends Command {

    /**
     * This overridden method just uses method from Control Unit.
     * @param object - object of route.
     * @return
     */
    public String execOnServer(CollectionManager collectionManager, Object object) {
        Object[] objects = (Object[]) object;
        return collectionManager.update_id((Integer) objects[0], (Route) objects[1]);
    }

    @Override
    public Packet execOnClient(String ... args) {
        Integer id = Integer.parseInt(args[0]);
        Route route = Route.generateObjectUserInput();
        Object[] objects = new Object[] {id, route};
        return new Packet(this, objects);
    }
}