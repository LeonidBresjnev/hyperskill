package converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class Main {
    static String fromdecimal(BigDecimal Dint, int base, boolean frac) {
        StringBuilder result = new StringBuilder();
        BigInteger left = Dint.toBigInteger();
        BigInteger bigbase = BigInteger.valueOf(base);
        int remainder;
        while (left.compareTo(BigInteger.ZERO) > 0) {
            remainder = left.mod(bigbase).intValue();
            result.append(Integer
                    .toString(remainder)
                    .replaceAll("^[0-9]{2}$", Character.toString((char) (remainder + 87))));
            left = left.divide(bigbase);
        }
        result.reverse();
        if ("".contentEquals(result)) {
            result.append("0");
        }
        BigDecimal fractional = Dint.remainder(BigDecimal.ONE);
//System.out.print(frac);
        if (frac) {
            result.append(".");
            BigDecimal bigbase2 = BigDecimal.valueOf(base);
            int count = 0;
            //System.out.println("fracpart");
            while (true || fractional.compareTo(BigDecimal.ZERO) > 0) {
                if (count++ == 5) { break;}
                fractional = fractional.multiply(bigbase2);
                remainder = fractional.intValue();
                fractional = fractional.remainder(BigDecimal.ONE);
                result.append(Integer
                        .toString(remainder)
                        .replaceAll("^[0-9]{2}$", Character.toString((char) (remainder + 87))));

            }
        }
        return result.toString();
    }

    static BigDecimal todecimal(String inputstr, int base) {
        BigDecimal decimal = new BigDecimal("0");
        BigDecimal bigBase = BigDecimal.valueOf(base);
        String intpart = inputstr.split("\\.")[0];

        for (int i = 0; i < intpart.length(); i++) {
            decimal = decimal.add(
                    BigDecimal
                            .valueOf(Integer.parseInt(
                                    Character.toString(intpart.charAt(intpart.length() - i - 1))
                                            .replaceAll("[a-z]",
                                    Integer.toString(((int)intpart.charAt(intpart.length() - i - 1))-87))))
                    .multiply(bigBase.pow(i)));
        }

        if (inputstr.split("\\.").length > 1) {
            String frac = inputstr.split("\\.")[1];
            //System.out.println("fracpart:" + frac);
            decimal.setScale(20, RoundingMode.HALF_UP);
            for (int i = 0; i < frac.length(); i++) {
                try {
                    decimal = decimal.add(
                            BigDecimal
                                    .valueOf(Integer.parseInt(
                                            Character.toString(frac.charAt(i))
                                                    .replaceAll("[a-z]",
                                                            Integer.toString(((int) frac.charAt(i)) - 87))))
                                    .setScale(20, RoundingMode.HALF_UP)
                                    .divide(bigBase.pow(1 + i), RoundingMode.HALF_UP));
                } catch (java.lang.ArithmeticException e) {
                    System.out.println(e.getMessage());
                    System.out.println(i + " " + frac.charAt(i));
                }
            }
        }

        return decimal;
    }

    static void convert(int sourcebase, int targetbase, String number){
        BigDecimal decimal = todecimal(number, sourcebase);
       // System.out.printf("decimal: %s, ",decimal.toString());
        System.out.printf("Conversion result: %s\n", fromdecimal(decimal, targetbase, number.matches(".*\\..*")));
    }

    static java.util.Scanner scanner = new java.util.Scanner(System.in);
    public static void main(String[] args) {
        String input;
        int sourcebase;
        int targetbase;
        while (true) {
            System.out.print(
                    "Enter two numbers in format: {source base} {target base} (To quit type /exit) ");

            input = scanner.nextLine();
            if ("/exit".equals(input)) {
                break;
            } else {
                sourcebase=Integer.parseInt(input.split(" ")[0]);
                targetbase=Integer.parseInt(input.split(" ")[1]);
            }
            while (true) {
                System.out.printf("Enter number in base %d to convert to base %d (To go back type /back) ", sourcebase, targetbase);
                input = scanner.nextLine();
                if ("/back".equals(input)) {
                    System.out.println();
                    break;
                }
                convert(sourcebase, targetbase, input);
                System.out.println();
            }
        }

    }
}