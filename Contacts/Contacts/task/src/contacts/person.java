package contacts;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

public class person extends contact {

    private String name;
    private String surname;
    private String gender;
    private LocalDate birthday;

    person(String name, String surname, String number, LocalDate birthday, String gender) {
        super(number, contactType.PERSON);
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    @Override
    public boolean find(String str) {
        str = str.toLowerCase();
        return      this.name.toLowerCase().matches(".*"+str +".*") || this.surname.toLowerCase().matches(".*"+str +".*")
                || (this.birthday != null && this.birthday.toString().toLowerCase().matches(".*"+str +".*"))
                || this.gender.toLowerCase().matches(".*"+str +".*")
                || this.number.toLowerCase().matches(".*"+str +".*");
    }

    @Override
    public String toString() {
        return name + " " + surname ;
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
                System.out.println("The record updated!");
                break;
            case "birthday":
                this.dateedited = LocalDateTime.now();
                System.out.print("Enter birthday:");
                setBirthday(LocalDate.parse(scanner.nextLine()));
                break;
            case "surname":
                this.dateedited = LocalDateTime.now();
                System.out.print("Enter surname:");
                setSurname(scanner.nextLine());
                break;
            case "gender":
                this.dateedited = LocalDateTime.now();
                System.out.print("Enter gender:");
                setGender(scanner.nextLine());
                System.out.println("The record updated!");
                break;
            default: break;
        }

    }

    @Override
    public void info() {
        System.out.println("Name: " + this.name  +
                "\nSurname: " + this.surname +
                "\nBirth date: " + (hasBirthday() ? this.birthday : "[no data]") +
                "\nGender: " + ("".equals(this.gender) ? "[no data]" : this.gender) +
                "\nNumber: " + (hasNumber() ? this.number : "[no number]") +
                "\nTime created: " + this.datecreated.toString() +
                "\nTime last edit: " + this.dateedited.toString());
    }

    boolean hasBirthday() {
        return this.birthday != null;
    }
/*
    public String getNumber() {
        return this.number;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }*/


}
