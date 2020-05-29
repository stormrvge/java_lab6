package client;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Incorrect number of arguments!");
            System.err.println("You should type domain name");
        } else  {
            Client client = new Client(args[0], 27027);
            client.run();
        }
    }
}
