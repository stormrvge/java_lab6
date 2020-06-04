package commands;

import logic.CollectionManager;
import logic.Packet;

import java.io.Serializable;

public abstract class Command implements Serializable {
    public String execOnServer(CollectionManager collectionManager, Object args) {
        return null;
    }

    public Packet execOnClient(String ... args) {
        Packet packet = new Packet();
        packet.wrap(this);
        return packet;
    }

    public void serverCmd(CollectionManager collectionManager, Object args) {

    }
}
