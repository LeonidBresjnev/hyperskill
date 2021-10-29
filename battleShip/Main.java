package battleship;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

class wronglocationException extends Exception {
    public wronglocationException(String message) {
        super(message);
    }
}

class ship {
    int length;
    int r;
    int c;
    private boolean horizontal;
    private String name;
    private short hits;

    ship(int r,int c,int length,String name, boolean horizontal) {
        this.length = length;
        this.r = r;
        this.c = c;
        this.name = name;
        this.horizontal = horizontal;
        this.hits = 0;
    }

    boolean hitMe(int r0, int c0){
        if ((this.horizontal && (r0 == this.r) && (c0 >= this.c) && (c0 <= (this.c+this.length-1)))
                || (!this.horizontal && (c0 == this.c) && (r0 >= this.r) && (r0 <= (this.r+this.length-1)))) {
            this.hits++;
            return true;
        } else {
            return false;
        }
    }

    short getHits() {
        return this.hits;
    }

    int getLength() {
        return this.length;
    }

    boolean getHozizontal(){
        return this.horizontal;
    }
}

class sea {
    char[][] map = new char[10][10];
    List<ship> allships = new ArrayList<ship>();
    Scanner myscanner = new Scanner(System.in);
    private boolean fog;

    public static void promptEnterKey() {
        System.out.println("Press Enter and pass the move to another player");
        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    sea() {
        this.fog = false;
        for (int r=0; r<10;r++){
            for (int c=0; c<10;c++){
                map[r][c] = '~';
            }
        }
    }

    void setFog(boolean fog){
        this.fog = fog;
    }

    void printsea() {
        System.out.printf("\n ");
        for (int c=0; c<10;c++){
            System.out.print(" " + (c+1));
        }
        System.out.println();
        for (int r=0; r<10;r++){
            System.out.print((char)(r+65));
            for (int c=0; c<10;c++){
                System.out.print(" " + ((('O' == this.map[r][c]) && this.fog)? "~" : map[r][c]));
            }
            System.out.println();
        }
    }



    void newship(String name, int l) {
        System.out.println("Enter the coordinates of the " + name + " (" + l + " cells):");
        System.out.println();
        boolean okLocation = false;
        do {
            try {
                String start = myscanner.next();
                String end = myscanner.next();
                int r0 = (char)(start.substring(0,1).toCharArray()[0]-65);
                int c0 = Integer.parseInt(start.substring(1,start.length()))-1;
                int r1 = (char)(end.substring(0,1).toCharArray()[0]-65);
                int c1 = Integer.parseInt(end.substring(1,end.length()))-1;
                // System.out.println("r0="+r0 +" r1="+r1);
                // System.out.println("c0="+c0 +" c1="+c1);
                if (r0 != r1 && c0 != c1) {
                    throw new wronglocationException("Error! Wrong ship location! Try again:");
                } else if (Math.abs(r0 - r1 + c0 -c1) != l-1){
                    throw new wronglocationException("Error! Wrong length of the Submarine! Try again:");
                } else {
                    int neareastship = -1;
                    for (int j=0; j<allships.size(); j++){
                        for (int i=0; i<=l; i++){
                            if ((allships.get(j).getHozizontal() && (Math.abs(allships.get(j).r - (r0+(r1-r0)*i/l))<=1) && ((c0+(c1-c0)*i/l)>=allships.get(j).c-1)
                                    && ((c0+(c1-c0)*i/l)<=(allships.get(j).c+allships.get(j).length))) ||
                                    (!allships.get(j).getHozizontal() && (Math.abs(allships.get(j).c - (c0+(c1-c0)*i/l))<=1) && ((r0+(r1-r0)*i/l)>=allships.get(j).r-1)
                                            && ((r0+(r1-r0)*i/l)<=(allships.get(j).r+allships.get(j).length)))) {
                                neareastship = 0;
                                throw new wronglocationException("Error! You placed it too close to another one. Try again:");
                            }
                        }
                    }
                    if (neareastship == -1) {
                        okLocation = true;
                        allships.add(new ship(Math.min(r0, r1), Math.min(c0, c1), l, name, (r0 == r1) ? true : false));
                        for (int i = 0; i <= l; i++) {
                            map[r0 + (r1 - r0) * i / l][c0 + (c1 - c0) * i / l] = 'O';
                        }
                        this.printsea();
                    }
                }
            } catch (wronglocationException e) {
                System.out.println(e.getMessage());
            }
        } while (!okLocation);
    }

    void takeashot() {
        //System.out.println("Take a shot!");
        String start = "";
        int r0;
        int c0;
        do {
            try {
                start = myscanner.next();
                r0 = (char) (start.substring(0, 1).toCharArray()[0] - 65);
                c0 = Integer.parseInt(start.substring(1, start.length())) - 1;
                if (!((r0 >= 0) && (r0 <= 9) && (c0 >= 0) && (c0 <= 9))) {
                    throw new wronglocationException("Error! You entered the wrong coordinates! Try again:");
                }
                else {
                    break;
                }
            } catch (wronglocationException e) {
                System.out.println(e.getMessage());
            }
        } while (true);
        boolean hit = false;
        for (int j = 0; j < allships.size(); j++) {
            if (map[r0][c0] == 'X') {
                hit = true;
            } else if (allships.get(j).hitMe(r0, c0)) {
                hit = true;
                if (allships.get(j).getHits() ==  allships.get(j).getLength()) {
                    allships.remove(j);
                    System.out.println("You sank a ship!");
                }
            };
        }
        if (hit) {
            map[r0][c0] = 'X';
            if (allships.size() > 0 ) {
                System.out.println("You hit a ship! Specify a new target:");
            } else {
                System.out.println("You sank the last ship. You won. Congratulations!");
                promptEnterKey();
            }
        } else {
            map[r0][c0] = 'M';
            System.out.println("You missed. Try again:");
        }
    }
}
public class Main {
    public static void promptEnterKey() {
        System.out.println("Press Enter and pass the move to another player");
        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        sea mysea1 = new sea();
        sea mysea2 = new sea();
        sea[] both = new sea[]{mysea1, mysea2};
        boolean player1turn = true;
        for (int i = 0; i<=1; i++){
            System.out.println();
            System.out.println("Player " + ((player1turn) ? "1" : "2") + ", place your ships on the game field");
            both[i].printsea();
            System.out.println();
            both[i].newship("Aircraft Carrier", 5);
            both[i].newship("Battleship", 4);
            both[i].newship("Submarine", 3);
            both[i].newship("Cruiser", 3);
            both[i].newship("Destroyer2", 2);
            both[i].setFog(true);
            player1turn = !player1turn;
            promptEnterKey();
        }


        player1turn = true;
        System.out.println("The game starts!");


        while (mysea1.allships.size() * mysea2.allships.size() > 0) {
            System.out.println("Player " + ((player1turn) ? "1" : "2")  + ", it's your turn:");
            if (player1turn) {
                mysea1.setFog(false);
                mysea2.setFog(true);
                mysea2.printsea();
                System.out.println("---------------------");
                mysea1.printsea();
                mysea2.takeashot();
            } else {
                mysea2.setFog(false);
                mysea1.setFog(true);
                mysea1.printsea();
                System.out.println("---------------------");
                mysea2.printsea();
                mysea1.takeashot();
            }
            player1turn = !player1turn;
            promptEnterKey();
        }

        mysea1.setFog(false);
        mysea1.printsea();
    }
}
