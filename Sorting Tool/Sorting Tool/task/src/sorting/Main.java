package sorting;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class objectAndCount<T extends Comparable<T>>{
    int count;
    T object;

    objectAndCount(int count, T object) {
        this.count = count;
        this.object = object;
    }

    public int compareTo(objectAndCount anOtherObject) {
        if (this.count < anOtherObject.count) {
            return -1;
        } else if (this.count > anOtherObject.count) {
            return 1;
        } else {
            return this.object.compareTo((T) anOtherObject.object);
        }
    }
}

abstract class Sorter<T extends Comparable<T>> extends ArrayList {

    public static Scanner scanner = null;

    Sorter(File dataSource) {
        if (dataSource == null) {
            scanner = new Scanner(System.in);
        } else {
            try {
                scanner = new Scanner(dataSource);
            } catch (FileNotFoundException e){
                System.out.println(e.getMessage());
            }
        }
        fill();
        scanner.close();
    }

    abstract Sorter fill();

    abstract T getMax();

    int getFrequency() {
        return Collections.frequency(this, getMax());
    }

    int getFrequencyPerc() {
        return getFrequency() * 100 / this.size();
    }

    abstract void printInfo();

}

class LongSorter<T extends Long> extends Sorter {
    LongSorter(File dataSource) {
        super(dataSource);
    }

    LongSorter fill() {
        while (scanner.hasNextLong()) {
            String input = scanner.next();
            try {
                this.add(Long.valueOf(input));
            } catch (InputMismatchException e) {
                    System.out.println(input + " is not a long. It will be skipped.");
            }
        }
        return this;
    }

    Long getMax() {
        return Collections.max(this, Long::compareTo);
    }

    void printInfo() {
        System.out.printf("Total numbers: %d.\nThe greatest number: %d (%d time(s), %d%%).\n", this.size(), getMax(), getFrequency(), getFrequencyPerc());
    }
}

abstract class StringSorter<T extends String> extends Sorter {
    StringSorter(File dataSource) {
        super(dataSource);
    }

    String getMax() {
        //if equals then soft alphabetically
        return Collections.max(this, (String a, String b) -> a.length() - b.length() == 0 ? a.compareTo(b) : a.length() - b.length());
    }
}

class LineSorter extends StringSorter {
    LineSorter(File dataSource) {
        super(dataSource);
    }
    LineSorter fill() {
        while (scanner.hasNextLine()) {
            this.add(scanner.nextLine());
        }
        return this;
    }

    void printInfo() {
        System.out.printf("Total lines: %d.\nThe longest line:\n%s\n(%d time(s), %d%%).\n", this.size(), getMax(), getFrequency(), getFrequencyPerc());
    }
}

class WordSorter extends StringSorter {
    WordSorter(File dataSource) {
        super(dataSource);
    }
    WordSorter fill() {
        while (scanner.hasNext()) {
            this.add(scanner.next());
        }
        return this;
    }

    void printInfo() {
        System.out.printf("Total words: %d.\nThe longest word: %s (%d time(s), %d%%).\n", this.size(), getMax(), getFrequency(), getFrequencyPerc());
    }

}

class SortingTool {
    private Sorter sorter;
    private SortByEnum sortby;
    private File inFile;
    private File outFile;
    printer myPrinter;

    SortingTool(SortByEnum sortby, File inFile, File outFile) {
        this.inFile = inFile;
        this.outFile = outFile;
        this.sortby = SortByEnum.valueOf(sortby.name());
        if (outFile == null) {
            myPrinter = new printtoterminal();
        } else {
            myPrinter = new printtofile(outFile);
        }
    }


    public void setSorter(Sorter sorter) {
        this.sorter = sorter;
    }

    public void setSorterByType(String type) {
        switch (type.toLowerCase()) {
            case "long":
                setSorter(new LongSorter<>(inFile));
                break;
            case "line":
                setSorter(new LineSorter(inFile));
                break;
            default:
                setSorter(new WordSorter(inFile));
        }

    }

    public void sort() {
        if (this.sortby.equals(SortByEnum.bycount)) {
            List<objectAndCount> listbycount = new ArrayList<>();
            Map<Comparable, Integer> counter = new HashMap<>();

            for (int i = 0; i < sorter.size(); i++) {
                if (!counter.containsKey(sorter.get(i))){
                    counter.put((Comparable) sorter.get(i), 0);
                } else {
                    counter.replace((Comparable) sorter.get(i), 1 + counter.get(sorter.get(i)));
                }
            }
            for (Comparable myObject : counter.keySet()) {
                listbycount.add(new objectAndCount(counter.get(myObject), myObject));
            }
            myPrinter.printString(String.format("Total numbers: %d.\n", sorter.size()));
            listbycount.sort(objectAndCount::compareTo);
            listbycount.forEach(x -> myPrinter.printString(
                    x.object.toString() + ": " + x.count + "time(s), " + 100*x.count/sorter.size() + "%\n"));

        } else {
            Collections.sort(sorter);
            myPrinter.printString("Total numbers: " + sorter.size() + "\n");
            myPrinter.printString("Sorted data:");
            sorter.forEach(x -> myPrinter.printString(" " + x));
        }
    }
}

public class Main {
    public static void main(final String[] args) {
        String type = "word";
        SortByEnum sortType = SortByEnum.natural;
        File inFile = null;
        File outFile = null;
        for (int i = 0; i < args.length; i++) {
            if ("-datatype".equalsIgnoreCase(args[i])) {
                if (i + 1 == args.length ||
                        !args[i + 1].toLowerCase().matches("(long|word|line)")) {
                    System.out.println("No data type defined!");
                    System.exit(0);
                }
                type = args[i + 1].toLowerCase();
            } else if ("-sortingType".equalsIgnoreCase(args[i])) {
                if (i + 1 == args.length) {
                    System.out.println("No sorting type defined!");
                    System.exit(0);
                } else {
                    try {
                        sortType = SortByEnum.valueOf(args[i + 1].toLowerCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("No sorting type defined!");
                        System.exit(0);
                    }
                }
            } else if ("-inputFile".equalsIgnoreCase(args[i])) {
               // System.out.println("abc1");
                if (i + 1 == args.length) {
                    System.out.println("No inputfile defined!");
                    System.exit(0);
                } else {
                    try {
                        inFile = new File(args[i + 1]);
                    } catch (NullPointerException e) {
                        System.out.println(e.getMessage());
                    }
                }
            } else if ("-outputFile".equalsIgnoreCase(args[i])) {
                if (i + 1 == args.length) {
                    System.out.println("No outputFile defined!");
                    System.exit(0);
                } else {
                    try {
                        outFile = new File(args[i + 1]);
                    } catch (NullPointerException e) {
                        System.out.println(e.getMessage());
                    }
                }
            } else if (args[i].matches("\\-\\w*")) {
                System.out.println(args[i] + " is not a valid parameter. It will be skipped.");
            }
        }

        SortingTool st = new SortingTool(sortType, inFile, outFile);
        st.setSorterByType(type);
        st.sort();
    }
}