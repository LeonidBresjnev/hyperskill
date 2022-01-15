package animals;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class play_en extends play{
    private static final String[] clarification = new String[]{"I'm not sure I caught you: was it yes or no?",
            "Funny, I still don't understand, is it yes or no?",
            "Oh, it's too complicated for me: just tell me yes or no.",
            "Could you please simply say yes or no?",
            "Oh, no, don't try to confuse me: say yes or no."};
    private static final String[] farewells = new String[]{"Bye.",
            "Bye bye!",
            "See you later",
            "I'm off.",
            "Goodbye."};
    private static final Pattern secondword = Pattern.compile("^It\\s(\\w*)\\s(.*)");

    play_en(String filetype) {
        super(filetype);
        int hour = now.getHour();
        System.out.println(hour < 5 ? "Hi, Night Owl" :
                hour < 12 ? "Good morning" :
                        hour < 18 ? "Good afternoon" :
                                "Good evening");
        if (top == null) {
            System.out.println("I want to learn about animals.\n" +
                    "Which animal do you like most?");

            String input1 = scanner.nextLine().toLowerCase();
            String animal1 = input1
                    .replaceAll("^the ", "")
                    .replaceAll("^a ", "")
                    .replaceAll("^an ", "").trim();
            top = new Animal(animal1, "Is it " + noun(input1) + " " + animal1 + "?");
            System.out.println("Wonderful! I've learned so much about animals!\n" +
                    "Let's play a game!");
        }

        System.out.println("\nWelcome to the animal expert system!");
    }
    private static String noun(String input) {
        String noun;
        String  animal1 = input.toLowerCase()
                .replaceAll("^the ","")
                .replaceAll("^a ","")
                .replaceAll("^an ","");
        if (input.toLowerCase().matches("^a[n]?\\s.*")) {
            noun = input.toLowerCase().split(" ")[0] + " ";
        } else if (animal1.matches("^[aeiouy].*")) {
            noun = "an ";
        } else {
            noun = "a ";
        }
        return noun.trim();
    }


    private Animal addnewanimal(Animal newanimalnode, String newanimal, String animal1, Animal node0) {
        String  animal2 = newanimal
                .replaceAll("^the ","")
                .replaceAll("^a ","")
                .replaceAll("^an ","").trim();

        System.out.printf("Specify a fact that distinguishes %s %s from %s %s.\n" +
                        "The sentence should be of the format: 'It can/has/is ...\n",
                noun(animal1), animal1, noun(newanimal), animal2);
        newanimalnode.question = "Is it " + noun(animal2) + " " + animal2 + "?";
        newanimalnode.node = animal2;
        String fact;
        while (true) {
            fact = capitalize(scanner.nextLine());
            if (fact.matches("^It\\s(can|has|is).*")) {
                break;
            } else {
                System.out.println("The examples of a statement:\n" +
                        " - It can fly\n" +
                        " - It has horn\n" +
                        " - It is a mammal\n" +
                        "Specify a fact that distinguishes a cat from a shark.\n" +
                        "The sentence should be of the format: 'It can/has/is ...'.");
            }
        }

        System.out.println("Is the statement correct for " + noun(animal2) + " " + animal2 + "?");
        boolean correct2;
        Animal newfact = new Animal(fact, fact);
        if (yesorno()) {
            newfact.yes = newanimalnode;
            newfact.no = node0;
            correct2 = true;
        } else {
            newfact.no = newanimalnode;
            newfact.yes = node0;
            correct2 = false;
        }
        newfact.parent = node0.parent;
        newanimalnode.parent = newfact;
        node0.parent = newfact;

        System.out.println("I have learned the following facts about animals:");
        Matcher matcher = secondword.matcher(fact);
        if (matcher.matches()) {

            switch (matcher.group(1)) {
                case "is":
                    System.out.printf("- The %s %s %s.\n- The %s %s %s.\n",
                            animal1, !correct2 ? "is" : "isn't",
                            matcher.group(2),
                            animal2, correct2 ? "is" : "isn't",
                            matcher.group(2));
                    newfact.notfact = String.format("It isn't %s.", matcher.group(2));
                    newfact.fact = String.format("It is %s.", matcher.group(2));
                    break;
                case "can":
                    System.out.printf("- The %s %s %s.\n- The %s %s %s.\n",
                            animal1, !correct2 ? "can" : "can't",
                            matcher.group(2),
                            animal2, correct2 ? "can" : "can't",
                            matcher.group(2));
                    newfact.notfact = String.format("It can't %s.", matcher.group(2));
                    newfact.fact = String.format("It can %s.", matcher.group(2));
                    break;
                case "has":
                    System.out.printf("- The %s %s %s.\n- The %s %s %s.\n",
                            animal1, !correct2 ? "has" : "doesn't have",
                            matcher.group(2),
                            animal2, correct2 ? "has" : "doesn't have",
                            matcher.group(2));
                    newfact.notfact = String.format("It doesn't have %s.", matcher.group(2));
                    newfact.fact = String.format("It has %s.", matcher.group(2));
                    break;
            }
            System.out.println("I can distinguish these animals by asking the question:");
            if (matcher.group(1).equals("has")) {
                newfact.question = String.format("Does it have %s?\n", matcher.group(2));
            } else {
                newfact.question = String.format("%s it %s?\n", capitalize(matcher.group(1)), matcher.group(2));
            }
            System.out.println(newfact.question);
        }
        return newfact;
    }

    private boolean yesorno() {
        while (true) {
            String confirm = scanner.nextLine().toLowerCase().trim();
            if (confirm.matches("(y)?(yes)?(yeah)?(yep)?(sure)?(right)?(affirmative)?" +
                    "(correct)?(indeed)?(you bet)?(exactly)?(you said it)?[.!]?")) {
                return true;
            } else if (confirm.matches("(n)?(no)?(no way)?(nah)?(nope)?(negative)?" +
                    "(i don't think so)?(yeah no)?[.!]?")) {
                return false;
            } else {
                System.out.println(clarification[random.nextInt(clarification.length - 1)]);
            }
        }
    }

    public void menu() {
        int input;
        while (true) {
            while (top.parent != null) {
                top = top.parent;
            }
            System.out.println("What do you want to do:\n" +
                    "\n" +
                    "1. Play the guessing game\n" +
                    "2. List of all animals\n" +
                    "3. Search for an animal\n" +
                    "4. Calculate statistics\n" +
                    "5. Print the Knowledge Tree\n" +
                    "0. Exit");
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (input < 0 || input > 5) {
                    throw new NumberFormatException("Input between 0 and 5");
                }
                switch (input) {
                    case 0:
                        while (top.parent != null) {
                            top = top.parent;
                        }
                        savetree(top);

                        System.out.println(farewells[random.nextInt(farewells.length - 1)]);
                        return;
                    case 1: letsplay();
                        break;
                    case 2:
                        knownanimals.clear();

                        while (top.parent != null) {
                            top = top.parent;
                        }
                        this.list(top);
                        knownanimals.stream().sorted().forEach(x -> System.out.println(" - " + x));
                        System.out.println();
                        break;
                    case 3: System.out.println("Enter the animal:");
                        knownanimals.clear();
                        String target = scanner.nextLine();
                        System.out.printf("facts about the %s\n", target);
                        search(top, target);
                        knownanimals.forEach(x -> System.out.println(" - " + x));
                        System.out.println();
                        break;
                    case 4: System.out.println("The Knowledge Tree stats\n");
                        int[] stats = {0, 0, 0, 0, -1, 0};
                        statistics(top, stats, 0);
                        System.out.printf("- root node                    %s\n" +
                                        "- total number of nodes        %d\n" +
                                        "- total number of animals      %d\n" +
                                        "- total number of statements   %d\n" +
                                        "- height of the tree           %d\n" +
                                        "- minimum animal's depth       %d\n" +
                                        "- average animal's depth       %.1f\n",
                                top.fact, stats[0] + stats[1], stats[0], stats[1], stats[3] - 1, stats[4], (float)stats[2] / stats[0]);

                }
            }  catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    private void letsplay() {

        Animal node = top;
        while (true) {
            System.out.println("You think of an animal, and I guess it.\n" +
                    "Press enter when you're ready.");
            scanner.nextLine();
            while (node.parent != null) {
                node = node.parent;
            }
            Animal prev = null;
            boolean yesno= false;
            while (node.no != null && node.yes != null) {
                System.out.print(node.question);
                prev = node;
                if (yesorno()) {
                    node = node.yes;
                    yesno = true;
                } else {
                    node = node.no;
                    yesno = false;

                }
            }

            System.out.println(node.question);
            if (yesorno()) {
                System.out.println("Nice! I Won");

            } else {
                System.out.println("I give up. What animal do you have in mind?");
                Animal newanimal = new Animal("","");
                Animal newfact = addnewanimal(newanimal, scanner.nextLine(), node.node, node);
                if (prev != null) {
                    if (yesno) {
                        prev.yes = newfact;
                    } else { prev.no = newfact;}
                }
            }
            System.out.println("Would you like to play again?");
            if (!yesorno()) {
                while (node.parent != null) {
                    node = node.parent;
                }
                break;
            }
        }
    }
}

