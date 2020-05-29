package server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(27027);
        try {
            server.run();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
