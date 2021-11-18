package phonebook;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;

class entry {
    String name;
    long number;

    entry(long number , String name) {
        this.name = name;
        this.number = number;
    }
}

class handleDictionary {
    final private List<entry> dictionary;

    handleDictionary(String filePath) {
        File filedict = new File(filePath);
        dictionary  = new ArrayList<>();
        try (java.util.Scanner dictscanner = new java.util.Scanner(filedict)) {
            while (dictscanner.hasNext()) {
                dictionary.add(new entry(dictscanner.nextLong(), dictscanner.nextLine().trim()));
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage() + "\nNo file found: " + filedict.getPath());
        }
    }

    entry getEntry(int i) {
        return dictionary.get(i);
    }
    /*
    handleDictionary(ArrayList<entry> alist) {
        this.dictionary = (List<entry>) alist.clone();
    }*/

    void shuffle() {
        for (int i = 0; i < dictionary.size() -1; i++) {
            Collections.swap(dictionary, i, (int) Math.floor(Math.random() * dictionary.size()));
        }
    }
    boolean bubbleSort(int maxiterations) {
        System.out.print("Start sorting...");
        boolean allsorted;
        int counter = 0;
        do {
            counter++;
            if (counter >= maxiterations) {
                return false;
            }
            allsorted = true;
            for (int i = 0; i < dictionary.size() - 1; i++) {
                if (dictionary.get(i).name.compareTo(dictionary.get(i + 1).name) > 0) {
                    allsorted = false;
                    Collections.swap(dictionary, i, i+1);
                }
            }

        } while (!allsorted);
        System.out.print("done + \n");
        return true;
    }

    boolean quickSort(int left, int right) {
        int i = left, j = right;
        String pivot = dictionary.get(right).name;

        while (i<j) {
            if (dictionary.get(j - 1).name.compareTo(pivot)>0) {
                Collections.swap(dictionary, j - 1, j);
                j--;
            } else {
                Collections.swap(dictionary, j - 1, i);
                i++;
            }
        }
        //System.out.println(pivot);
        //System.out.println("i: " + i + " " + dictionary.get(i).name);
        // System.out.println("j: " + j + " " + dictionary.get(j).name);

/*
        for (int k = 0; k<dictionary.size(); k++) {
            System.out.println("k: " + k +   dictionary.get(k).name);
        }*/

        //    System.out.printf("i= %d, j=%d\n",i,j);
        //  dictionary.forEach(x -> System.out.println(x.name));
        if (left < i - 1) {
            quickSort(left,i - 1);
        }
        if (j + 1 < right) {
            quickSort(j + 1, right);
        }
        return true;
    }

    boolean issorted() {
        boolean rc = true;
        for (int i = 0; i < dictionary.size() -2; i++) {
            if (dictionary.get(i).name.compareTo(dictionary.get(i + 1).name)>0) {
                rc = false;
                break;
            }
        }
        return rc;
    }

    String linearSearch(String findPath) {
        File filefind = new File(findPath);
        try (java.util.Scanner findscanner = new java.util.Scanner(filefind)) {
            System.out.println("Start searching...");
            int findnumber = 0;
            int hits = 0;
            while (findscanner.hasNext()) {
                findnumber++;
                String findname = findscanner.nextLine().trim();
                for (entry myentry : dictionary) {

                    if (myentry.name.equals(findname)) {
                        hits++;
                        break;
                    }
                }
            }
            return findnumber + " / " + hits;
        } catch (FileNotFoundException e) {
            System.out.println( "No file found: " + filefind.getPath());
            return e.getMessage();
        }
    }

    String jumpSearch(String findPath) {
        File filefind = new File(findPath);
        try (java.util.Scanner findscanner = new java.util.Scanner(filefind)) {
            System.out.print("Start searching...");
            int findnumber = 0;
            int hits = 0;
            while (findscanner.hasNext()) {
                findnumber++;
                String findname = findscanner.nextLine().trim();
                int j = -1;
                int blocksize = (int) Math.floor(Math.sqrt(dictionary.size()));
                int i = blocksize - 1;
                while (dictionary.get(i).name.compareTo(findname)<0) {
                    j = i;
                    i = Math.min(i + blocksize, dictionary.size()-1);
                }

                if (dictionary.get(i).name.compareTo(findname)>=0) {
                    for (int k = i; k > j; k--){
                        if (dictionary.get(k).name.equals(findname)) {
                            hits++;
                            break;
                        }
                    }
                }
            }
            System.out.print("done\n");
            return findnumber + " / " + hits;
        } catch (FileNotFoundException e) {
            System.out.println( "No file found: " + filefind.getPath());
            return e.getMessage();        }
    }

    int size() {
        return dictionary.size();
    }

    private entry binarySearchsub(String value, int left, int right) {
        int mid = (right + left) / 2;

        if (dictionary.get(mid).name.equals(value)) {
            return dictionary.get(mid);
        } else if (dictionary.get(left).name.compareTo(value) > 0) {
            return null;
        } else if (dictionary.get(right).name.compareTo(value) < 0) {
            return null;
        } else if  (dictionary.get(mid).name.compareTo(value) > 0) {
            return binarySearchsub(value, left, mid);
        } else if  (dictionary.get(mid).name.compareTo(value) < 0 && right > mid) {
            return binarySearchsub(value, mid, right);
        } else return null;
    }

    String binarySearch(String findPath) {
        File filefind = new File(findPath);
        try (java.util.Scanner findscanner = new java.util.Scanner(filefind)) {
            System.out.println("Start searching...");
            int findnumber = 0;
            int hits = 0;
            while (findscanner.hasNext()) {
                findnumber++;
                String findname = findscanner.nextLine().trim();
                if ((binarySearchsub(findname, 0, dictionary.size() -1) != null)) {
                    hits++;
                }
            }
            return findnumber + " / " + hits;
        } catch (FileNotFoundException e) {
            System.out.println( "No file found: " + filefind.getPath());
            return e.getMessage();
        }
    }
}



public class Main {
    static String timeString(long time) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time - TimeUnit.MINUTES.toSeconds(minutes));
        long millis = time - TimeUnit.MINUTES.toMillis(minutes)- TimeUnit.SECONDS.toMillis(seconds);
        return String.format("%d min. %d sec. %d ms.",minutes,seconds,millis);
    }

    public static void main(String[] args) {
        handleDictionary dictionary = new handleDictionary("C:\\Users\\cob\\Downloads\\directory.txt");

        long starttime = System.currentTimeMillis();
        System.out.println("Start searching (linear search)..." );
        String found = dictionary.linearSearch("C:\\Users\\cob\\Downloads\\find.txt");
        long stoptime = System.currentTimeMillis();
        System.out.println("Found: " + found + " entries. Time taken: " + timeString(stoptime - starttime) );

        System.out.println("Start searching (bubble sort + jump search)...");
        starttime = System.currentTimeMillis();
        boolean sortsucces = dictionary.bubbleSort(500);
        long aftersorttime = System.currentTimeMillis();
        if (sortsucces) {
            found = dictionary.jumpSearch("C:\\Users\\cob\\Downloads\\find.txt");
            stoptime = System.currentTimeMillis();
            System.out.println("Found: " + found + " entries. Time taken: " + timeString(stoptime - starttime) );
            System.out.println("Sorting time: " +timeString(aftersorttime - starttime));
        } else {
            found = dictionary.linearSearch("C:\\Users\\cob\\Downloads\\find.txt");
            stoptime = System.currentTimeMillis();
            System.out.println("Found: " + found + " entries. Time taken: " + timeString(stoptime - starttime) );
            System.out.println("Sorting time: " +timeString(aftersorttime - starttime) + " - STOPPED, moved to linear search");
        }
        System.out.println("Searching time: " +timeString(stoptime - aftersorttime) + "\n");

        dictionary.shuffle();
        System.out.println("Start searching (quick sort + binary search)...");
        starttime = System.currentTimeMillis();
        sortsucces = dictionary.quickSort(0, dictionary.size() - 1);
        aftersorttime = System.currentTimeMillis();
        found = dictionary.binarySearch("C:\\Users\\cob\\Downloads\\find.txt");
        stoptime = System.currentTimeMillis();
        System.out.println("Found: " + found + " entries. Time taken: " + timeString(stoptime - starttime) );
        System.out.println("Sorting time: " +timeString(aftersorttime - starttime));
        System.out.println("Searching time: " +timeString(stoptime - aftersorttime) + "\n");

        starttime = System.currentTimeMillis();
        entry aentry;
        HashMap<String, Long> myHash = new HashMap<String, Long>();
        for (int i = 0; i <dictionary.size(); i++) {
            aentry = dictionary.getEntry(i);
            myHash.put(aentry.name, aentry.number);
        }
        System.out.println("hashsize:" + myHash.size());
        aftersorttime = System.currentTimeMillis();
        int n = 0;
        int x = 0;
        File filefind = new File("C:\\Users\\cob\\Downloads\\find.txt");
        try (java.util.Scanner findscanner = new java.util.Scanner(filefind)) {
           while (findscanner.hasNext()) {
               n++;
               if (myHash.containsKey(findscanner.nextLine().trim())) {
                   x++;
               }
           }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage() + "No file found: " + filefind.getPath());
        }
        stoptime = System.currentTimeMillis();
        System.out.println("Start searching (hash table)...");
        System.out.println("Found " + n + " / " + x + " entries. Time taken: " + timeString(stoptime - starttime));
        System.out.println("Creating time: "+ timeString(aftersorttime - starttime));
        System.out.println("Searching time: " + timeString(stoptime - aftersorttime));
    }
}


