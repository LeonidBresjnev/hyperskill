package calculator;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    static  Scanner scanner = new Scanner(System.in);
    static Map<String, BigInteger> vars = new HashMap<>();
    static postfixcalculator myCalculator = new postfixcalculator("");
    public static void main(String[] args) {


        String input;
        final Pattern pattern = Pattern.compile("(-$|\\+$|=|[*]{2,}|//)");
        final Pattern pattern2 = Pattern.compile("[^\\d()+\\-*^\\s/]");
        do {
            String keyname = "";
            input = scanner.nextLine().trim();
            if (input.contains("=")) {
                keyname = input.substring(0,input.indexOf("=")).trim();
                input = input.substring(input.indexOf("=") + 1).trim();
            }
            //System.out.println(input);
            boolean onemore;
            do {
                String newstr = input
                        .replaceAll("[\\-]{2}","+")
                        .replaceAll("[\\+]{2,}","+")
                        .replaceAll("[\\s]{2,}"," ")
                        .replaceAll("(\\+\\-|\\-\\+)","-");
                if (!newstr.equals(input)) {
                    onemore = true;
                    input = newstr;
                } else {
                    onemore = false;
                }
            } while (onemore);

            if ("/exit".equals(input)) {
                break;
            } else if ("/help".equals(input)) {
                System.out.println("The program calculates the sum of numbers");
            } else if (input.matches("/.*")) {
                System.out.println("Unknown command");
            } else if (keyname.length()>0 && !keyname.matches("[a-zA-Z]+")) {
                System.out.println("Invalid identifier");
            } else if (input.matches("\\d[a-zA-Z]|[a-zA-Z]+\\d")) {
                System.out.println("Invalid assignment");
            } else if (pattern.matcher(input).find()) {
                System.out.println("Invalid expression");
            } else if (input.length() > 0) {
                /*String[] terms = input.split(" ");
                for (String aTerm : terms) {
                    if (aTerm.matches("[\\-]?[a-zA-Z]+")) {
                        String myKey = aTerm.replaceAll("-","").trim();
                        if (vars.containsKey(myKey)) {
                          //  System.out.println(vars.keySet().toString() + vars.values().toString());
                            value += vars.get(myKey) * (aTerm.contains("-") ? -1 : 1);

                        } else {
                            System.out.println("Unknown variable");
                        }
                    } else {
                        value += Integer.parseInt(aTerm);
                    }
                }*/
                for (String aString : vars.keySet()) {
                    input = input.replaceAll("^" + aString , vars.get(aString).toString());
                    input = input.replaceAll(aString + "$" , vars.get(aString).toString());
                    input = input.replaceAll(" " + aString + " ", " " + vars.get(aString).toString() + " ");
                }
                if (pattern2.matcher(input).find()) {
                    System.out.println("Unknown variable");
                } else {
                    // System.out.println(input);
                    myCalculator.setTerms(input);
                    try {
                        myCalculator.makeResultStack();
                        if (keyname.length() > 0) {
                            vars.put(keyname, myCalculator.calculateResultStack());
                        } else {
                            System.out.println(myCalculator.calculateResultStack());
                        }
                    } catch (NumberFormatException e) {
                        //System.out.println(e.getMessage());
                    }
                }
            }
        } while (true);
        System.out.println("Bye!");
    }
}