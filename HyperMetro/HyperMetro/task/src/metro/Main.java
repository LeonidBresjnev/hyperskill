package metro;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {

        if (args.length < 1) {
            throw new IllegalArgumentException("Insufficient parameters");
        }

        try {
            Controller myController = new Controller(args[0]);
            myController.start();
        } catch (FileNotFoundException e) {
            System.out.println("Error! Such a file doesn't exist!");
        }

    }
}