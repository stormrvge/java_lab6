package logic;

import commands.Command;

import java.io.Serializable;

public class Packet implements Serializable {
    private Command command;
    private Object argument;

    public Packet() {
        this.command = null;
        this.argument = null;
    }

    public Packet(Command command, Object argument) {
        this.command = command;
        this.argument = argument;
    }


    public void wrap(Command command) {
        this.command = command;
    }

    public void wrap(Command command, Object argument) {
        this.command = command;
        this.argument = argument;
    }

    public Command getCommand() {
        return command;
    }

    public Object getArgument() {
        return argument;
    }
}

