package encryptdecrypt;

import java.io.FileWriter;
import java.io.IOException;

public class printtofile implements printer {
    final private FileWriter myWriter;
    printtofile(FileWriter myWriter) {
        this.myWriter = myWriter;
    }
    @Override
    public void printchar(char ch) {
        try {
            myWriter.write(ch);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
