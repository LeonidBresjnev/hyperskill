package contacts;

import java.time.LocalDateTime;
import java.util.Scanner;

public class organization extends contact{
    private String address;
    private String name;

    organization(String name, String number, String address) {
        super(number, contactType.ORGANIZATION);
        this.name = name;
        this.address = address;
    }

    @Override
    public void info() {
        System.out.println("Organization name: " + this.name +
                "\nAddress: " + ("".equals(this.address) ? "[no data]" : this.address) +
                "\nNumber: " + (hasNumber() ? this.number : "[no number]") +
                "\nTime created: " + this.datecreated +
                "\nTime last edit: " + this.dateedited);
    }
    @Override
    public boolean find(String str) {
        str = str.toLowerCase();
        //System.out.println(this.name + str + this.name.toLowerCase().(str));
        return this.address.toLowerCase().matches(".*"+str +".*") ||
                this.name.toLowerCase().matches(".*"+str +".*") ||
                this.number.toLowerCase().matches(".*"+str +".*");
    }

    @Override
    public String toString() {
        return this.name;
    }

    public void  setAddress(String address) {
        this.address = address;
    }

    @Override
    public void edit() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Select a field (name, surname, birth, gender, number):");
        String entry = scanner.nextLine().toLowerCase();
        switch (entry) {
            case "number":
                System.out.print("Enter number:");
                this.dateedited = LocalDateTime.now();
                if (setNumber(scanner.nextLine())) {
                    System.out.println("The record updated!");
                }
                break;

            case "name":
                this.dateedited = LocalDateTime.now();
                System.out.print("Enter name:");
                setName(scanner.nextLine());
                break;

            case "address":
                System.out.print("Enter number:");
                this.dateedited = LocalDateTime.now();
                this.setAddress(scanner.nextLine());
                System.out.println("The record updated!");
                break;

            default: break;
        }
    }

    public void setName(String name) {
        this.name = name;
    }


}
