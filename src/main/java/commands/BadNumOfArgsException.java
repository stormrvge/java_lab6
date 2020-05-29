package commands;

/**
 * Class for exception, if bad number of arguments in console.
 */
public class BadNumOfArgsException extends Exception {
    public BadNumOfArgsException() {
        super("Wrong number of arguments for this command!");
    }
}
