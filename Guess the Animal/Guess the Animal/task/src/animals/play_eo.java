package animals;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class play_eo extends play{
    private static final String[] clarification = new String[]{"I'm not sure I caught you: was it yes or no?",
            "Funny, I still don't understand, is it yes or no?",
            "Oh, it's too complicated for me: just tell me yes or no.",
            "Could you please simply say yes or no?",
            "Oh, no, don't try to confuse me: say yes or no."};
    private static final String[] farewells = new String[]{"Ĝis!", "Ĝis revido!","Estis agrable vidi vin!"};
    private static final Pattern secondword = Pattern.compile("^Ĝi\\s([^\\s]+)\\s(.*)",  Pattern.CASE_INSENSITIVE);
    Animal top;
    play_eo(String filetype) {
        super(filetype);
        this.filetype = filetype;
        int hour = now.getHour();
        System.out.println(hour < 5 ? "kiun beston vi plej ŝatas?" :
                hour < 12 ? "bonvenon al la sperta sistemo de la besto!" :
                        hour < 18 ? " Bonan tagon!" :
                                "Bonan vesperon!");
        top = loadtree();
        if (top == null) {
            System.out.println("I want to learn about animals.\n" +
                    "kiun beston vi plej ŝatas?");

            String input1 = scanner.nextLine().toLowerCase();
            String animal1 = input1
                    .replaceAll("^the ", "")
                    .replaceAll("^a ", "")
                    .replaceAll("^an ", "").trim();
            top = new Animal(animal1, "ĉu ĝi estas " + animal1 + "?");
            System.out.println("bonvenon al la sperta sistemo de la besto!\n" +
                    "Let's play a game!");
        }

        System.out.println("\nWelcome to the animal expert system!");
    }

    private Animal addnewanimal(Animal newanimalnode, String newanimal, String animal1, Animal node0) {
        String  animal2 = newanimal
                .replaceAll("^the ","")
                .replaceAll("^a ","")
                .replaceAll("^an ","").trim();

        System.out.printf("indiku fakton, kiu distingas %s de %s.\n",
                 animal1, animal2);
        newanimalnode.question = "ĉu ĝi estas " + animal2 + "?";
        newanimalnode.node = animal2;
        String fact;
        while (true) {
            fact = capitalize(scanner.nextLine());
            if (fact.matches("^Ĝi\\s(estas|povas|havas|loĝas).*")) {
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

        System.out.println("ĉu la aserto ĝustas por la " + animal2 + "?");
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
            System.out.printf("- la %s %s %s.\n- la %s %s %s.\n",
                    animal1, !correct2 ? matcher.group(1) : "ne " + matcher.group(1),
                    matcher.group(2),
                    animal2, correct2 ? matcher.group(1) : "ne " + matcher.group(1),
                    matcher.group(2));
            newfact.notfact = String.format("Ĝi ne " + matcher.group(1) + " %s.", matcher.group(2));
            newfact.fact = String.format("Ĝi " + matcher.group(1) +" %s.", matcher.group(2));

            System.out.println("I can distinguish these animals by asking the question:");
            newfact.question = String.format("Ĉu ĝi %s %s?\n", capitalize(matcher.group(1)), matcher.group(2));

            System.out.println(newfact.question);
        }
        return newfact;
    }

    private boolean yesorno() {
        while (true) {
            String confirm = scanner.nextLine().toLowerCase().trim();
            if (confirm.matches("(j)?(jes)?[.!]?")) {
                return true;
            } else if (confirm.matches("(n)?(ne)?(no way)?(nah)?(nope)?(negative)?" +
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
                    "1. Ludi la divenludon\n" +
                    "2. Listo de ĉiuj bestoj\n" +
                    "3. Serĉi beston\n" +
                    "4. Kalkuli statistikojn\n" +
                    "5. Printi la Sciarbon\n" +
                    "0. Eliri");
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
                    case 3: System.out.println("Enigu la nomon de besto:");
                        knownanimals.clear();
                        String target = scanner.nextLine();
                        System.out.printf("faktoj pri la %s\n", target);
                        search(top, target);
                        knownanimals.forEach(x -> System.out.println(" - " + x));
                        System.out.println();
                        break;
                    case 4: System.out.println("la statistiko\n");
                        int[] stats = {0, 0, 0, 0, -1, 0};
                        statistics(top, stats, 0);
                        System.out.printf("- radika nodo                  %s\n" +
                                        "- totala nombro de nodoj        %d\n" +
                                        "- totala nombro de bestoj      %d\n" +
                                        "- totala nombro de deklaroj   %d\n" +
                                        "- alteco de la arbo          %d\n" +
                                        "- minimuma profundo       %d\n" +
                                        "- averaĝa profundo        %.1f\n",
                                top.fact.replaceAll("Ĝi", "ĝi"), stats[0] + stats[1], stats[0], stats[1], stats[3] - 1, stats[4], (float)stats[2] / stats[0]);

                }
            }  catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    private void letsplay() {

        Animal node = top;
        System.out.println("Vi pensu pri besto, kaj mi divenos ĝin.\n" +
                "Premu enen kiam vi pretas.");
        while (true) {

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
                System.out.println("mi rezignas. kiun beston vi havas en la kapo?");
                Animal newanimal = new Animal("","");
                Animal newfact = addnewanimal(newanimal, scanner.nextLine(), node.node, node);
                if (prev != null) {
                    if (yesno) {
                        prev.yes = newfact;
                    } else { prev.no = newfact;}
                }
            }
            System.out.println("Pardonu, ĉu mi rajtas demandi vin denove: ĉu tio estis jes aŭ ne?");
            if (!yesorno()) {
                while (node.parent != null) {
                    node = node.parent;
                }
                break;
            }
        }
    }
}

