package bullscows;
import java.util.Scanner;
import java.util.Random;
import java.util.HashSet;



public class Main {
    static Scanner scanner = new Scanner(System.in);
    private static String generateCode(int digits, int symbols) {
        Random random = new Random();
        long pseudoRandomNumber = random.nextLong();
        String input = new String("");
        HashSet<Integer> hset =  new HashSet<Integer>();

        int k;
        for (int i = 0; i < digits; i++) {
            do {
                k = random.nextInt(symbols);
                k = k + 48 + (k >= 10 ? 39 : 0);
            } while (!hset.add(k));
            input = input + Character.toString(k);
        }
        System.out.println("The secret is prepared: " + "*".repeat(digits) + "(0-" + (symbols <= 10? (symbols - 1) :  "9, a-" + ((char)(86 + symbols)) + ")"));

        System.out.println("Okay, let's start a game!");
        return input;

    }

    public static void main(String[] args) {
        //generate random number;
        System.out.println("Input the length of the secret code:");
        int digits = 0;
        try {
            digits = Integer.parseInt(scanner.nextLine());
        }
        catch (Exception e){
            System.out.println("error");
            System.exit(0);
        }
        if (digits >= 36 || digits <= 0){
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            System.exit(0);
        }

        System.out.println("Input the number of possible symbols in the code:");
        int symbols=0;
        try {
            System.out.println("error");
            symbols = Integer.parseInt(scanner.nextLine());
        }
        catch (Exception e){
            System.exit(0);
        }
        if (digits > symbols || symbols > 36 ) {
            System.out.println("Error: it's not possible to generate a code with a length of " + digits + " with " + symbols + " unique symbols.");
            System.exit(0);
        }


        String input = generateCode(digits, symbols);
              //  System.out.println("secret: " + input);
        String guess;
        int bulls;
        int cows;
        int turn = 0;

        do {
            turn++;
            System.out.printf("Turn %d\n", turn);
            bulls = 0;
            cows = 0;
            guess = scanner.nextLine();
            for (int j = 0; j < guess.length(); j++) {
                for (int i = 0; i < input.length(); i++) {
                    if (guess.substring(j, j + 1).equals(input.substring(i, i + 1))) {
                        if (i == j) {
                            bulls++;
                        } else {
                            cows++;
                        }
                    }
                }
            }
            if (bulls + cows > 0) {
                System.out.printf("Grade: %d bull(s) and %d cow(s).\n", bulls, cows, input);
            } else {
                System.out.printf("Grade: None.\n", input);
            }
        } while (bulls < digits);
        System.out.println("Congratulations! You guessed the secret code.");
    }
}
