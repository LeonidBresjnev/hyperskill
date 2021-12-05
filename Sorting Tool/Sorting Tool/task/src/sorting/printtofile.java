package sorting;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class printtofile implements printer {
    private FileWriter myWriter = null;
    printtofile(File outFile) {
        try {
            outFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.myWriter = new FileWriter(outFile);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void printString(String abc) {
        try {
            myWriter.write(abc);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void printint(int x) {
        try {
            myWriter.write(x);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}