package encryptdecrypt;

import java.io.File;
import java.io.IOException ;
import java.util.List;
import java.util.ArrayList;
import java.io.FileWriter;

public class Main {

    public static void main(String[] args) {
        String mode = "enc";
        String alg = "shift";
        List<String> data = new ArrayList<>();
        FileWriter myWriter = null;
        int k = 0;
        for (int i = 0; i < args.length; i++) {
            if ("-mode".equals(args[i])) {
                if (i + 1 < args.length) mode = args[i + 1];
            } else if ("-data".equals(args[i])) {
                if (i + 1 < args.length) data.add(args[i + 1]);
            } else if ("-key".equals(args[i])) {
                if (i + 1 < args.length) k = Integer.parseInt(args[i + 1]);
            } else if ("-alg".equals(args[i])) {
                if (i + 1 < args.length) alg = args[i + 1];
            } else if ("-in".equals(args[i])) {
                if (i + 1 < args.length) {
                    try {
                        File myObj = new File(args[i+1]);
                        java.util.Scanner myReader = new java.util.Scanner(myObj);
                        while (myReader.hasNextLine()) {
                            data.add(myReader.nextLine());
                        }
                        myReader.close();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            } else if ("-out".equals(args[i])) {
                if (i + 1 < args.length) {
                    File outFile = new File(args[i+1]);
                    try {
                        outFile.createNewFile();
                    } catch (Exception e) {
                        //   System.out.println(e.getMessage());
                    }

                    try {
                        myWriter = new FileWriter(args[i+1]);
                    } catch (IOException  e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        printer myPrinter = new printtofile(myWriter);

        if ("unicode".equals(alg)) {
            k = ("enc".equals(mode) ? 1 : -1) * k;
            for (String myString : data) {
                for (char ch : myString.toCharArray()) {
                    myPrinter.printchar((char) (ch + k));
                }
            }
        } else if ("shift".equals(alg)) {

            k = ("enc".equals(mode) ? k : ('z' - 'a' + 1 - k));
            for (String myString : data) {
                for (char ch : myString.toCharArray()) {
                    if (ch >= 'a' && ch <= 'z')
                        myPrinter.printchar((char) ('a' - 1 + ((ch + k - 'a' + 1) % ('z' - 'a' + 1))));
                    else if (ch >= 'A' && ch <= 'Z')
                        myPrinter.printchar((char) ('A' - 1 + ((ch + k - 'A' + 1) % ('Z' - 'A' + 1))));
                    else myPrinter.printchar(ch);
                }
            }
        }

        try {
            myWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}