package minesweeper;
import java.util.HashSet;

class coordinate {
    boolean marked;
    private boolean mine;
    private boolean explored;
    private int count;

    coordinate() {
        this.marked = false;
        this.mine = false;
        this.count = 0;
        this.explored = false;
    }

    char show() {
        if (this.explored && this.count == 0) {
            return '/';
        } else if (this.explored && this.count > 0) {
            return Integer.toString(count).charAt(0);
        } else if (marked) {
            return '*';
        } else  return '.';
    }

    void setMine(boolean mine) {
        this.mine = mine;
    }
    void setMark(boolean mark) {
        this.marked = mark;
    }
    void setExplored(boolean explored) {
        this.explored = explored;
    }


    void setCount(int x) {
        this.count = x;
    }
    int getCount() {
        return this.count;
    }
    boolean isMarked() {return this.marked;}
    boolean isMine() { return this.mine; }
    boolean isExplored() { return this.explored; }

}

public class Main {
    static java.util.Scanner myScanner = new java.util.Scanner(System.in);
    static coordinate[] field = new coordinate[81];
    static HashSet<coordinate> mineSet = new HashSet<>();
    static int mines;
    static int marks;
    static int explored;
    static boolean initiated = false;



    static void initfield(int r, int c) {
        int fieldsleft = field.length - 1;
        int minesleft = mines;
        marks = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                field[9 * i + j] = new coordinate();
                if (minesleft > 0 && ((i != r) || (j != c))) {
                    if (Math.random() < (double) minesleft / (double) fieldsleft) {
                        minesleft--;
                        field[9 * i + j].setMine(true);
                        mineSet.add(field[9 * i + j]);
                    }
                }
                fieldsleft--;
            }
        }
        initiated = true;
    }

    static void printfield() {
        System.out.println(" |123456789|\n-|---------|");
        for (int i = 0; i < 9; i++) {
            System.out.print((i + 1) + "|");
            for (int j = 0; j < 9; j++) {
                System.out.print(initiated ? field[9 * i + j].show() : '.');
            }
            System.out.print("|\n");
        }
        System.out.print("-|---------|\n");
    }

    static void countneighbors(int i, int j) {
        int count = 0;
        if (!mineSet.contains(field[i * 9 + j])) {
            for (int k = Math.max(0, i - 1); k < Math.min(9, i + 2); k++) {
                for (int l = Math.max(0, j - 1); l < Math.min(9, j + 2); l++) {
                    count += (mineSet.contains(field[9 * k + l]) ? 1 : 0);
                }
            }
            if (count > 0) {
                field[i * 9 + j].setCount(count);
            }
        }
    }

    static boolean allminesmarked() {
        boolean allmarked = true;
        for (coordinate acoordinate : mineSet) {
            if (!acoordinate.isMarked()) {
                allmarked = false;
                break;
            }
        }
        return allmarked;
    }

    static void explore(int r, int c) {
        countneighbors(r, c);
        field[r * 9 + c].setExplored(true);
        explored++;
        if (field[r * 9 + c].getCount() == 0) {
            for (int k = Math.max(0, r - 1); k < Math.min(9, r + 2); k++) {
                for (int l = Math.max(0, c - 1); l < Math.min(9, c + 2); l++) {
                    if (!field[k * 9 + l].isExplored()) {
                        explore(k, l);
                    }
                }
            }
        }
    }

    static boolean play() {
        String action;
        while (!(marks == mineSet.size() && allminesmarked()) || explored + mineSet.size() < 81) {
            printfield();
            System.out.println("Set/unset mine marks or claim a cell as free:");
            int c =   myScanner.nextInt() - 1;
            int r =   myScanner.nextInt() - 1;
            if (!initiated) {
                initfield(r, c);
            }
            action = myScanner.next();
            if (action.equals("mine")) {
                if (field[r * 9 + c].getCount() > 0) {
                    System.out.println("There is a number here!");
                } else if (!field[r * 9 + c].isMarked()) {
                    field[r * 9 + c].setMark(true);
                    marks++;
                } else if (field[r * 9 + c].isMarked()) {
                    field[r * 9 + c].setMark(false);
                    marks--;
                }
            } else if (action.equals("free")) {
                if (field[r * 9 + c].isMine()) {
                    System.out.println("You stepped on a mine and failed!");
                    return false;
                } else explore(r, c);
            }
        }
        System.out.println("Congratulations! You found all mines!");
        return true;
    }

    public static void main(String[] args) {
        System.out.println("How many mines do you want on the field?");
        mines = myScanner.nextInt();
        play();
    }
}