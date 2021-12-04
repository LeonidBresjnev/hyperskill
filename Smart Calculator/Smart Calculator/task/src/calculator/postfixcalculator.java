package calculator;

import java.math.BigInteger;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

public class postfixcalculator {
    final private static Deque<String> result = new ArrayDeque<>();
    final private static Deque<String> operatorStack = new ArrayDeque<>();
    final static Map<String, Integer> priority  = new HashMap<>() {{
        put("+", 1);
        put("-", 1);
        put("*", 2);
        put("/", 2);
        put("^", 3);
    }};

    String[] terms;

    postfixcalculator(String infix) {
        String infix1 = infix
                .replaceAll("\\(", " ( ")
                .replaceAll("\\)", " ) ")
                .replaceAll("\\+", " + ")
                .replaceAll("-[^\\d]", " - ")
                .replaceAll("\\*", "*")
                .replaceAll("/", " / ")
                .replaceAll("\\^", " ^ ")
                .replaceAll("[\\s]{2,}", " ")
                .trim();
        terms = infix1.split(" ");
    }

    void setTerms(String infix) {
        String infix1 = infix
                .replaceAll("\\(", " ( ")
                .replaceAll("\\)", " ) ")
                .replaceAll("\\+", " + ")
                .replaceAll("-[^\\d]", " - ")
                .replaceAll("\\*", " * ")
                .replaceAll("/", " / ")
                .replaceAll("\\^", " ^ ")
                .replaceAll("[\\s]{2,}", " ")
                .trim();
        terms = infix1.split(" ");
    }

    void makeResultStack() throws NumberFormatException{
        short parantheses = 0;
        for (String aString : terms) {
            if (!priority.containsKey(aString) && !aString.matches("[()]")) {
                result.add(aString);
            } else if (priority.containsKey(aString)){
                if (operatorStack.isEmpty() || "(".equals(operatorStack.getLast())) {
                    operatorStack.offerLast(aString);
                } else if (priority.get(aString) > priority.get(operatorStack.getLast())) {
                    operatorStack.offerLast(aString);
                } else {
                    while (!operatorStack.isEmpty() && !"(".equals(operatorStack.getLast()) &&
                            priority.get(aString) <= priority.get(operatorStack.getLast())) {
                        result.offerLast(operatorStack.pollLast());
                    }
                    operatorStack.offerLast(aString);
                }
            } else if ("(".equals(aString)) {
                operatorStack.offerLast(aString);
                parantheses++;
            } else if (")".equals(aString)) {
                if (parantheses <= 0) {
                    System.out.println("Invalid expression");
                    throw new NumberFormatException("Unmatched paranthesis");
                }
                while (!operatorStack.isEmpty() && !"(".equals(operatorStack.getLast())) {
                    result.offerLast(operatorStack.pollLast());
                    parantheses--;
                }
                if (operatorStack.isEmpty()) {
                    System.out.println("Invalid expression");
                    throw new NumberFormatException("Unmatched paranthesis");
                }
                operatorStack.pollLast();
            }
        }
        if (parantheses != 0) {
            System.out.println("Invalid expression");
            throw new NumberFormatException("Unmatched paranthesis");
        }
        while (!operatorStack.isEmpty()) {
            result.offerLast(operatorStack.pollLast());
        }
        //System.out.println();
        //result.forEach(x-> System.out.print(x + " "));
    }

    BigInteger calculateResultStack() {
        final Deque<BigInteger> calculation = new ArrayDeque<>();
        while (!result.isEmpty()) {
            if (priority.containsKey(result.peekFirst()) && calculation.size() >= 2) {
                String operator = result.pollFirst();
                switch (operator) {
                    case ("+"): {  calculation.offerLast(calculation.pollLast().add(calculation.pollLast()));
                        break;}
                    case "-": {calculation.offerLast(calculation.pollLast().negate().add(calculation.pollLast()));
                        break;}
                    case "*": {calculation.offerLast(calculation.pollLast().multiply(calculation.pollLast()));
                        break;}
                    case "/":  {
                        BigInteger den = calculation.pollLast();
                        BigInteger num = calculation.pollLast();
                        calculation.offerLast(num.divide(den));
                        break;}
                case "^": {
                    BigInteger b = calculation.pollLast();
                    BigInteger a = calculation.pollLast();
                    calculation.offerLast(a.pow(b.intValue()));
                        break;
                    }
                    default: break;
                }
            } else if (priority.containsKey(result.peekFirst())) {
                System.out.println("Invalid expression");
                System.exit(0);
            } else {
                calculation.offerLast(new BigInteger(result.pollFirst()));
            }
        }
        // calculation.forEach(x-> System.out.print(x + " "));
        return calculation.peekFirst();
    }
}
