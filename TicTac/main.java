import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;

class tictac {
    Scanner myscanner ;
    String[][] pattern = new String[3][3];
    boolean turn;
    String userA;
    String userB;
    int xs = 0;
    int os = 0;
    Random rand;
    short status;
    boolean silent;

    tictac(String input, String userA, String userB, boolean silent){
        myscanner = new Scanner(System.in);
        boolean levelok = false;
        String levels;
        this.userA = userA;
        this.userB = userB;
        this.status = 0;
        this.silent = silent;
        //input = input.replace("_"," ");
        for (int r=0; r<3; r++) {
            for (int s=0; s<3; s++) {
                pattern[r][s] = input.substring(3*r+s,3*r+s+1);
                if (pattern[r][s].equals("X")) {
                    this.xs++;
                } else if (pattern[r][s].equals("O")) {
                    this.os++;
                }
            }
        }
        this.turn = (this.xs<=this.os);
        rand = new Random();
    }

    boolean newMove(){
        int a;
        int b;
        if (turn && "user".equals(userA) || !turn && "user".equals(userB)){
            String movestr = myscanner.nextLine();
            try {
                a = Integer.parseInt(movestr.substring(0, 1));
                b = Integer.parseInt(movestr.substring(2, 3));
            } catch (NumberFormatException nfe) {
                System.out.println("You should enter numbers!");
                return false;
            }
        } else if (turn && "medium".equals(userA) || !turn && "medium".equals(userB)){
            a = rand.nextInt(3)+1;
            b = rand.nextInt(3)+1;
            for (int r=0; r<3; r++){
                for (int s=0; s<3; s++){
                    if (pattern[r][(s+1) % 3 ].equals(pattern[r][(s+2) % 3 ]) && !"_".equals(pattern[r][(s+1) % 3 ]) && "_".equals(pattern[r][s])){
                        a = r + 1;
                        b = s + 1;
                        //   System.out.println("a="+a + "b="+b);
                    }
                }
            }
            for (int s = 0; s < 3; s++){
                for (int r = 0; r < 3; r++){
                    if (pattern[(r +1) % 3][s].equals(pattern[(r + 2) % 3][s]) && !"_".equals(pattern[(r + 1) % 3][s]) && "_".equals(pattern[r][s])){
                        a = r + 1;
                        b = s + 1;
                    }
                }
            }
            for (int s = 0; s < 3; s++){
                if (pattern[(s + 1) % 3 ][(s + 1) % 3 ].equals(pattern[(s+2) % 3 ][(s+2) % 3 ]) && !"_".equals(pattern[(s + 1) % 3][(s + 1) % 3]) && "_".equals(pattern[s][s])){
                    a = s + 1;
                    b = s + 1;
                } if (pattern[2- (s + 1) % 3 ][(s +1) % 3 ].equals(pattern[2- ((s+2) % 3)  ][(s+2) % 3 ]) && !"_".equals(pattern[2- (s + 1) % 3 ][(s +1) % 3 ])  && "_".equals(pattern[2-s][s])){
                    a = 3 - s;
                    b = s + 1;
                }
            }
        } else if (turn && "hard".equals(userA) || !turn && "hard".equals(userB)){
            short result;
            short bestresult = (short)(turn ? -2 : 2);
            int r0 = 5, s0 = 5;
            String patternstr = new String();
            for (int r=0; r<3; r++){
                for (int s=0; s<3; s++){
                    //  System.out.println("r:"+r+"s:"+s+pattern[r][s]);
                    if ("_".equals(pattern[r][s])){
                        if (r0 == 5 && s0 == 5){
                            r0 = r;
                            s0 = s;
                        }

                        patternstr = Arrays.deepToString(pattern)
                                .replaceAll("\\s+", "")
                                .replace("]","")
                                .replace("[","")
                                .replace(",","");
                        patternstr = patternstr.substring(0,3 * r + s) +
                                (turn ? "X" : "O") + patternstr.substring(3 * r + s + 1,patternstr.length());
                        tictac variant = new tictac(patternstr,"hard","hard",true);

                        result = variant.game();
                        if (result < bestresult && !turn ||
                                result > bestresult && turn){
                            r0 = r;
                            s0 = s;
                            bestresult = result;

                            if (turn && bestresult == 1 || !turn && bestresult == -1){break;};
                            //   System.out.println("r0"+r+"s0"+s+"best"+bestresult);
                        }
                    }
                }
            }
            a = r0 + 1;
            b = s0 + 1;
        } else {

            a = rand.nextInt(3)+1;
            b = rand.nextInt(3)+1;

        }



        if ((a<1)||(a>3)||(b<1)||(b>3)) {
            System.out.println("Coordinates should be from 1 to 3!");
            return false;
        } else if ("_".equals(pattern[a-1][b-1])){
            pattern[a - 1][b - 1] = turn?"X":"O";
            if (turn) {
                this.xs++;
            } else {
                this.os++;
            }

            if (!this.silent) {
                this.printPattern();
            }
            turn = !turn;
            return true;
        } else {
            if (!this.silent) {
                System.out.print("This cell is occupied! Choose another one!");
            }
            return false;
        }
    }

    void printPattern() {
        System.out.println("---------");
        for (int r=0; r<3; r++) {
            System.out.print("| ");
            for (int s=0; s<3; s++) {
                System.out.print(("_".equals(pattern[r][s])? " " : pattern[r][s]) + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    boolean checkStatus(){

        boolean xwon = false;
        boolean owon = false;

        for (int r=0; r<3; r++) {
            if ((pattern[r][1].equals(pattern[r][2]))&&(pattern[r][1].equals(pattern[r][0])) && (pattern[r][1].equals("X")||pattern[r][1].equals("O"))){
                switch (pattern[r][1] ){
                    case ("X"): xwon = true;
                        break;
                    case ("O"): owon = true;
                        break;
                    default: break;
                }
            }
        }
        for (int s=0; s<3; s++) {
            if ((pattern[1][s].equals(pattern[2][s]))&&(pattern[1][s].equals(pattern[0][s]))&& (pattern[1][s].equals("X")||pattern[1][s].equals("O"))){
                switch (pattern[0][s] ){
                    case ("X"): xwon = true;
                        break;
                    case ("O"): owon = true;
                        break;
                    default: break;
                }
            }
        }
        if ((pattern[0][0].equals(pattern[1][1]) && pattern[2][2].equals(pattern[1][1])
                || pattern[0][2].equals(pattern[1][1]) && pattern[2][0].equals(pattern[1][1]))&& (pattern[1][1].equals("X")||pattern[1][1].equals("O")) ){
            switch (pattern[1][1] ){
                case ("X"): xwon = true;
                    break;
                case ("O"): owon = true;
                    break;
                default: break;
            }
        }
        if ((Math.abs(xs-os)>=2)||(xwon && owon)) {
            if (!this.silent) {
                System.out.println("Impossible");
            }
            return true;
        }
        else if (xwon && !owon || !xwon && owon){
            if (!this.silent) {
                System.out.println((xwon ? "X" : "O") + " wins");
            }
            this.status = (short) (xwon ? 1 : -1);
            return true;
        }
        else if (os+xs == 9) {
            if (!this.silent) {
                System.out.println("Draw");
            }
            this.status = 0;
            return true;
        }
        else {
            if (!this.silent) {
                System.out.println("Game not finished");
            }
            this.status = 0;
            return false;
        }
    }

    short game() {
        if (this.xs + this.os == 9){
            return 0;
        }
        if (!this.silent) {
            System.out.print(turn ? "Enter the coordinates: " : "Making move level \"" + (turn ? userA : userB) + "\"\n");
        }
        this.newMove();
        if (!this.checkStatus()) {
            this.game();
        }
        return this.status;
    }
}
public class Main {

    public static void main(String[] args) {
        java.util.Scanner myreader = new java.util.Scanner(System.in);
        short n = myreader.newShort();
        myreader.
        int max = 0;
        int a = myreader.newInt();
        int b;
        int prev = myreader.newInt();
        for (i=1; i<n; i++){
            b = myreader.newInt();
            max = Math.max(max, a * b);
            a = b;
        }
        System.out.println(max);

        int i;
        for (i = 1; i< args.length; i=i+1) {
            if ("mode".equals(args[i])) {
                System.out.println(args[+1]);
                break;
            }
        }
        if (i == args.lengt){
            System.out.println("default");
        }

        String levels = new String("");
        Scanner myscanner = new Scanner(System.in);
        boolean levelok;
        do {
            do {
                levelok = false;
                try {
                    System.out.print("Input command:");
                    levels = myscanner.nextLine();
                    if (levels.split(" ").length == 3) {
                        if ("start".equals(levels.split(" ")[0]) &&
                                ("user".equals(levels.split(" ")[1])|| "easy".equals(levels.split(" ")[1]) || "medium".equals(levels.split(" ")[1])|| "hard".equals(levels.split(" ")[1]))&&
                                ("user".equals(levels.split(" ")[2])|| "easy".equals(levels.split(" ")[2]) || "medium".equals(levels.split(" ")[2])|| "hard".equals(levels.split(" ")[2]))  ) {
                            levelok = true;
                            tictac myTicTac = new tictac("_________",levels.split(" ")[1],levels.split(" ")[2],false);
                            myTicTac.printPattern();
                            System.out.println(myTicTac.game());

                        }
                    } else if ((levels.split(" ").length == 1)&& "exit".equals(levels.split(" ")[0])) {
                        levelok = true;
                    } else {
                        throw new IOException("Bad parameters!");
                    }
                } catch (IOException e){
                    System.out.println(e.getMessage());
                }
            } while (!levelok);
        } while (!"exit".equals(levels));
    }
}
