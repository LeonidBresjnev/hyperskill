package encryptdecrypt;

public class printtoterminal implements printer {
    @Override
    public void printchar(char ch) {
        System.out.print(ch);
    }
}
