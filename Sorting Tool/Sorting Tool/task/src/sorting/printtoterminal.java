package sorting;

public class printtoterminal implements printer {
    @Override
    public void printString(String abc) {
        System.out.print(abc);
    }
    @Override
    public void printint(int x) {
        System.out.print(x);
    }
}