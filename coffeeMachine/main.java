import java.util.Scanner;

class machine {
    int water;
    int milk;
    int coffee;
    int money;
    int cups;
    Scanner myscanner = new Scanner(System.in);

    machine(int water,int milk,int coffee,int money,int cups){
        this.water = water;
        this.milk = milk;
        this.coffee = coffee;
        this.money = money;
        this.cups = cups;
    }

    boolean askforaction(){
        System.out.println("\nWrite action (buy, fill, take, remaining, exit):");
        
        String input = new String(myscanner.next());
        myscanner.nextLine();
        switch(input){
            case ("buy"): this.buy();
            break;
            case ("fill"): this.fill();
                break;
            case ("remaining"):
            this.printresources();
                break;
            case ("exit"):
                return true;
            case ("take"): System.out.println("\nI gave you $" + money);
            money = 0;
            break;
            default: break;
        }
        return false;
    }

    void fill(){
        System.out.println("\nWrite how many ml of water you want to add:");
        water += myscanner.nextInt();
        System.out.println("Write how many ml of milk you want to add:");
        milk += myscanner.nextInt();
        System.out.println("Write how many grams of coffee beans you want to add:");
        coffee += myscanner.nextInt();
        System.out.println("Write how many disposable cups of coffee you want to add:");
        cups += myscanner.nextInt();
        myscanner.nextLine();
    }

    boolean buy() {
        System.out.printf("\nWhat do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:\n");
        String order = myscanner.nextLine();
        if (order.toUpperCase().equals("BACK")) {
            return false;
        } else {
            int type = Integer.parseInt(order);
            int waterUse = 0;
            int milkUse = 0;
            int coffeeUse = 0 ;
            int price = 0;
            switch (type) {
                case (1):
                    waterUse = 250;
                    coffeeUse = 16;
                    price = 4;
                    break;
                case (2):
                    waterUse = 350;
                    coffeeUse = 20;
                    milkUse = 75;
                    price = 7;
                    break;
                case (3):
                    waterUse = 200;
                    coffeeUse = 12;
                    milkUse = 100;
                    price = 6;
                    break;
                default:
                    break;
            }
            if (this.water < waterUse) {
                System.out.println("Sorry, not enough water!");
            } else if (this.coffee < coffeeUse) {
                System.out.println("Sorry, not enough coffee!");
            } else if (this.milk < milkUse) {
                System.out.println("Sorry, not enough milk!");
            } else {
                System.out.println("I have enough resources, making you a coffee!");
                this.water -= waterUse;
                this.coffee -= coffeeUse;
                this.milk -= milkUse;
                this.money += price;
                cups -= 1;
            }
            return true;
        }
    }
    
    void printresources(){
        System.out.printf("\nThe coffee machine has:\n");
        System.out.printf("%d ml of water\n",water);
        System.out.printf("%d ml of milk\n",milk);
        System.out.printf("%d g of coffee beans\n",coffee);
        System.out.printf("%d disposable cups\n",cups);
        System.out.printf("$%d of money\n",money);
    }
}
     
    
class main {
    public static void main(String[] args) {
        machine mymachine = new machine(400,540,120,550,9);
      //  mymachine.printresources();
        do {

            } while (!mymachine.askforaction());
    }
}
