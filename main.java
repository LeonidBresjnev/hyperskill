import java.util.Scanner;

class tictac {
    String[][] pattern = new String[3][3];
    boolean turn;


    tictac(String input){
        input = input.replace("_"," ");
        this.turn = true;
        for (int r=0; r<3; r++) {
            for (int s=0; s<3; s++) {
                pattern[r][s] = input.substring(3*r+s,3*r+s+1);
            }
        }
    }

    boolean newMove(String player,String movestr){
        short a;
        short b;
        try {
            a = Short.parseShort(movestr.substring(0,1));
            b = Short.parseShort(movestr.substring(2,3));
        } catch (NumberFormatException nfe) {
            System.out.println("You should enter numbers!");
            return false;
        }
        if ((a<1)||(a>3)||(b<1)||(b>3)) {
            System.out.println("Coordinates should be from 1 to 3!");
            return false;
        } else if (pattern[a-1][b-1].equals(" ")){
            pattern[a - 1][b - 1] = turn?"X":"O";
            this.printPattern();
            turn = !turn;
            return true;
        } else {
            System.out.print("This cell is occupied! Choose another one!");
            return false;
        }
    }

    void printPattern() {
        System.out.println("---------");
        for (int r=0; r<3; r++) {
            System.out.print("| ");
            for (int s=0; s<3; s++) {
                System.out.print(pattern[r][s] + " ");
                if (pattern[r][s].equals("X")) {
                } else if (pattern[r][s].equals("O")) {
                }
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    boolean checkStatus(){
        short xs = 0;
        short os = 0;
        for (int r=0; r<3; r++) {
            for (int s=0; s<3; s++) {
                if (pattern[r][s].equals("X")) {
                    xs++;
                } else if (pattern[r][s].equals("O")) {
                    os++;
                }
            }
        }

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
        if (((pattern[0][0].equals(pattern[1][1])) && (pattern[2][2].equals(pattern[1][1]))
           ||(pattern[0][2].equals(pattern[1][1]) && pattern[2][0].equals(pattern[1][1])))&& (pattern[2][2].equals("X")||pattern[2][2].equals("O")) ){
            switch (pattern[1][1] ){
                case ("X"): xwon = true;
                    break;
                case ("O"): owon = true;
                    break;
                default: break;
            }
        }
        if ((Math.abs(xs-os)>=2)||(xwon && owon)) {
            System.out.println("Impossible");
            return true;
        }
        else if (xwon && !owon || !xwon && owon){
            System.out.println((xwon?"X":"O") + " wins");
            return true;
        }
        else if (os+xs == 9) {
            System.out.println("Draw");
            return true;
        }
        else { System.out.println("Game not finished");
            return false;
        }
    }
}

class main{
    public static void main(String[] args) {
        Scanner myscanner = new Scanner(System.in);

        //System.out.print("Enter cells:");
        tictac myTicTac = new tictac("         ");
        myTicTac.printPattern();
        //
        do {
            do {
                System.out.print("Enter the coordinates: ");
            } while (!myTicTac.newMove("X", myscanner.nextLine()));
        } while (!myTicTac.checkStatus());
    }
}