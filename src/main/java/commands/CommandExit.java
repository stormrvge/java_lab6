package commands;

import client.Client;
import logic.Packet;


public class CommandExit extends Command {

    @Override
    public Packet execOnClient(String ... args) {
        Client.closeConnection();
        return new Packet(this, args);
    }
}
