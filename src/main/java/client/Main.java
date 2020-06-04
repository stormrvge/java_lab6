package client;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Incorrect number of arguments!");
            System.err.println("You should type domain name");
        } else  {
            int port = Integer.parseInt(args[1]);
            Client client = new Client(args[0], port);
            client.run();
        }
    }
}
