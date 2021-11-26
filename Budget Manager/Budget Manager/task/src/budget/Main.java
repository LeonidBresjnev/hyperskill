package budget;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.io.IOException;
import java.io.FileWriter;

class purchase implements Comparable<purchase>{

    
    final private String thing;
    final private double price;

    purchase(String thing, double price) {
        this.thing = thing;
        this.price = price;
    }

    String getThing() {
        return this.thing;
    }

    double getPrice() {
        return this.price;
    }

    @Override
    public int compareTo(purchase otherPurchase) {
        return Double.compare(otherPurchase.getPrice(),this.price);
    }
}

class Main{
    static java.util.Scanner myScanner = new java.util.Scanner(System.in);
    static List<String> types = new ArrayList<>(java.util.Arrays.asList("Food", "Clothes", "Entertainment","Other"));
    static java.util.Map<String,List<purchase>> allLists = new java.util.HashMap<>();
    static File myfile = null;
    static java.util.Scanner fileScanner = null;

    interface Analyzer {
        void sort();
    }

    static class SortAll implements Analyzer {

        @Override
        public void sort() {
            if (allLists.isEmpty()) {
                System.out.println("The purchase list is empty!\n");
            } else {
                double total = 0d;
                List<purchase> templist = new ArrayList<>();
                for (String myType : types) {
                    if (allLists.containsKey(myType)) {
                        for (purchase myPurchase : allLists.get(myType)) {
                            templist.add(myPurchase);
                            total += myPurchase.getPrice();
                        }
                    }
                }
                System.out.println("All:");
                templist.stream().sorted().collect(Collectors.toList()).forEach(x -> System.out.printf(x.getThing().trim() + " " + "$%.02f\n", x.getPrice()));
                System.out.printf("Total: $%.02f\n\n", total);
            }
        }
    }

    static class SortType implements Analyzer {

        @Override
        public void sort() {
            double grandtotal = 0d;
            System.out.println("Types:");
            List<String> typeList = new ArrayList<>();
            List<Double> priceList = new ArrayList<>();

            for (String myType : types) {
                if (allLists.containsKey(myType)) {
                    double total = 0d;
                    for (purchase myPurchase : allLists.get(myType)) {
                        total += myPurchase.getPrice();
                    }
                    grandtotal += total;
                    priceList.add(total);
                    typeList.add(myType);

                }
            }
            boolean onemore;
            do {
                onemore = false;
                for (int i = 1; i < typeList.size(); i++) {
                    if (priceList.get(i) > priceList.get(i - 1)) {
                        onemore = true;
                        double temp = priceList.get(i);
                        priceList.set(i , priceList.get(i - 1));
                        priceList.set(i -1, temp);

                        String temp2 = typeList.get(i);
                        typeList.set(i , typeList.get(i - 1));
                        typeList.set(i -1, temp2);
                    }
                }
            } while (onemore);
            for (int i = 0; i < typeList.size(); i++){
                System.out.printf(typeList.get(i) + " - $%.02f\n", priceList.get(i));
            }
            System.out.printf("All $%.02f\n\n", grandtotal);

        }
    }

    static class Sortcertaintypes implements Analyzer {


        @Override
        public void sort() {
            System.out.println("Choose the type of purchase\n" +
                    "1) Food\n" +
                    "2) Clothes\n" +
                    "3) Entertainment\n" +
                    "4) Other");
            double total = 0d;
            short input = myScanner.nextShort();
            myScanner.nextLine();
            System.out.println();
            if (allLists.containsKey(types.get(input - 1))) {
                List<purchase> templist = new ArrayList<>();
                for (purchase myPurchase : allLists.get(types.get(input - 1))) {
                    templist.add(myPurchase);
                    total += myPurchase.getPrice();
                }
                templist.stream().sorted().collect(Collectors.toList()).forEach(x -> System.out.printf(x.getThing().trim() + " " + "$%.02f\n", x.getPrice()));
                System.out.printf("Total: $%.02f\n\n", total);
            } else {
                System.out.println("The purchase list is empty!\n");
            }

        }
    }
    static void save() {
        try {
            myfile = new File("purchases.txt");
            boolean newFile = myfile.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter("purchases.txt");
            myWriter.write(Double.toString(balance)+ "\n");
            for (String myType : types) {
                if (allLists.containsKey(myType)) {
                    for (purchase myPurchase : allLists.get(myType)) {
                        myWriter.write(myType + " " + myPurchase.getPrice() + " " + myPurchase.getThing() + "\n");
                    }
                }
            }
            myWriter.close();
            System.out.println("Purchases were saved!");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    static void load() {
        //clear existing list;
        for (String myType : types) {
            if (allLists.containsKey(myType)) {
                allLists.get(myType).clear();
                allLists.remove(myType);
            }
        }
        try {
            myfile = new File("purchases.txt");
            fileScanner = new java.util.Scanner(myfile);
            if (fileScanner.hasNextLine()) {
                balance = fileScanner.nextDouble();
             //   fileScanner.nextLine();
            }
            while (fileScanner.hasNextLine()) {
                String myType = fileScanner.next();
                double myPrice = fileScanner.nextDouble();
                String myThing = fileScanner.nextLine();

                purchase myPurchase = new purchase(myThing, myPrice);
                if (!allLists.containsKey(myType)) {
                    allLists.put(myType, new ArrayList<>());
                }
                allLists.get(myType).add(myPurchase);
            }
            fileScanner.close();
            System.out.println("Purchases were loaded!");
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


    }
    static void show() {
        if (allLists.isEmpty()) {
            System.out.println("The purchase list is empty");
        } else {
            do {
                System.out.println("Choose the type of purchases");
                int bound = types.size();
                IntStream.range(0, bound).mapToObj(i -> i + 1 + ") " + types.get(i)).forEach(System.out::println);
                System.out.print("5) All\n6) Back\n");
                short input = myScanner.nextShort();
                myScanner.nextLine();
                System.out.println();
                if (input == 6) {
                    break;
                } else if (input <= 4) {
                    String type = types.get(input - 1);
                    if (allLists.isEmpty()) {
                        System.out.println("\nThe purchase list is empty!");
                    } else if (allLists.containsKey(type)) {
                        double total = 0d;
                        System.out.println(type);
                        for (purchase myPurchase : allLists.get(type)) {
                            System.out.println(myPurchase.getThing().trim() + " $" + String.format("%.02f", myPurchase.getPrice()));
                            total += myPurchase.getPrice();
                        }
                        System.out.printf("Total sum: $%.02f\n", total);
                    } else {
                        System.out.println(type + ":\n The purchase list is empty!");
                    }
                } else {
                    double total = 0d;
                    System.out.println("All:");
                    for (String myType : types) {
                        if (allLists.containsKey(myType)) {
                            for (purchase myPurchase : allLists.get(myType)) {
                                System.out.println(myPurchase.getThing().trim() + " $" + String.format("%.02f", myPurchase.getPrice()));
                                total += myPurchase.getPrice();
                            }
                        }
                    }
                    System.out.printf("Total: $%.02f\n", total);
                }
                System.out.println();
            } while (true);
        }
    }

    static void buy() {
        do {
            System.out.println("\nChoose the type of purchase");
            IntStream.range(0, types.size()).mapToObj(i -> i + 1 + ") " + types.get(i)).forEach(System.out::println);
            System.out.println("5) Back");
            short input = myScanner.nextShort();
            System.out.println();
            if (input == 5) {
                break;
            } else {
                if (!allLists.containsKey(types.get(input - 1))) {
                    allLists.put(types.get(input - 1), new ArrayList<>());
                }
                System.out.println("Enter purchase name:");
                String thing = myScanner.next() + myScanner.nextLine();
                System.out.println("Enter its price:");
                double price =  myScanner.nextDouble();
                purchase Mypurchase = new purchase(thing, price);
                allLists.get(types.get(input - 1)).add(Mypurchase);
                balance -= price;
                System.out.println("Purchase was added!");
            }
            System.out.println();
        } while (true) ;
    }

    static void analyze() {
        do {
            System.out.println("How do you want to sort?\n" +
                    "1) Sort all purchases\n" +
                    "2) Sort by type\n" +
                    "3) Sort certain type\n" +
                    "4) Back");

            short input = myScanner.nextShort();
            myScanner.nextLine();
            System.out.println();
            Analyzer myAnalyzer;
            if (input == 1) {
                myAnalyzer = new SortAll(); // create a message sender
            } else if (input == 2) {
                myAnalyzer = new  SortType();
            } else if (input == 3) {
                myAnalyzer = new Sortcertaintypes();
            } else {
                break;
            }
            myAnalyzer.sort();
        } while (true);
    }

    static double balance = 0d;

    public static void main(String[] args) {
        boolean onemore = true;
        do {
            System.out.println();
            System.out.print("Choose your action:\n" +
                    "1) Add income\n" +
                    "2) Add purchase\n" +
                    "3) Show list of purchases\n" +
                    "4) Balance\n" +
                    "5) Save\n" +
                    "6) Load\n" +
                    "7) Analyze (Sort)\n" +
                    "0) Exit\n");
            short choice = myScanner.nextShort();
            myScanner.nextLine();
            System.out.println("\n");
            switch (choice) {
                case (0): {onemore = false;
                    break;}
                case (1): {
                    System.out.println("Enter income:");
                    balance += myScanner.nextDouble();
                    System.out.println("Income was added!");
                    break;
                }
                case (2): {buy();
                    break;}
                case (3): {show();
                    break;}
                case (4): {System.out.printf("Balance: $%.02f\n", balance);
                    break;}
                case (5): {save();
                    break;}
                case (6): {load();break;}
                case (7): {analyze();break;}
            }
        } while (onemore);
        System.out.println("Bye!\n");
    }
}