package commands;

import client.Client;
import logic.CollectionManager;
import logic.Packet;
import server.Server;


public class CommandExit extends Command {


    public Packet execOnClient(String ... args) {
        Client.disconnect();
        return new Packet(this, args);
    }

    @Override
    public void serverCmd(CollectionManager collectionManager, Object args) {
        Server.stopServer();
    }
}
