package io;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Class which works with files.
 * It has a few method which helps read information in file.
 */

public class InputFromFile {
    private FileReader reader;
    private Scanner scanner;

    /**
     * Constructor
     * @throws FileNotFoundException
     */

    public InputFromFile() throws FileNotFoundException {
        try {
            String path = System.getenv("Lab5");
            reader = new FileReader(path);
            scanner = new Scanner(reader);
        } catch (NullPointerException ex) {
            System.out.println("Can not get a path from environment.");
        }
    }

    /**
     * Constructor
     * @param path - the path to file
     * @throws FileNotFoundException
     */

    public InputFromFile(String path) throws FileNotFoundException {
        reader = new FileReader(path);
        scanner = new Scanner(reader);
    }

    /**
     * Reads whole file
     * @return the text of file
     * @throws IOException
     */

    public String readFile() throws IOException {
        String text = "";
        StringBuilder textBuilder = new StringBuilder(text);
        while(scanner.hasNextLine()) {
            textBuilder.append(scanner.nextLine());
        }
        reader.close();
        text = textBuilder.toString();
        return text;
    }

    /**
     * Reads line in file
     * @return the line of text
     */

    public String readLine() {
        String text;
        text = scanner.nextLine();
        return text;
    }

    /**
     * Check if the next line in file exists
     * @return the result of checking
     */

    public boolean hasNextLine() {
        boolean exist = false;
        if (scanner.hasNextLine()) {
            exist = true;
        } else {
            exist = false;
        }
        return exist;
    }

    /**
     * Closes file.
     * @throws IOException
     */

    public void closeFile() throws IOException {
        reader.close();
    }
}