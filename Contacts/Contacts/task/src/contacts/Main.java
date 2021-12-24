package contacts;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        contactStore contactProvider = new contactStore();
        phoneBook<contact> myPhoneBook =  new phoneBook<>();
     /*   try {
            FileInputStream fis = new FileInputStream(args[0]);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            contact[] templist= (contact[]) ois.readObject();
            for (contact aContact : templist) {
                myPhoneBook.add(aContact);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }*/
        final Scanner scanner = new Scanner(System.in);
        String input;
        do {
            System.out.print("\nEnter action (add, " + (!myPhoneBook.isEmpty() ? "remove, edit, count, search, " : "") + "info, exit):");
            input = scanner.nextLine().toLowerCase()
                    .replaceAll("[^\\w]","");
           // System.out.println(input);
            switch (input) {
                case "count": {
                    System.out.printf("The Phone Book has %d records.\n", myPhoneBook.size());
                    break;
                }
                case "search": {
                    String term = scanner.nextLine();
                    List<Integer> hits = new ArrayList<>();
                    int k = 0;
                    for (int i = 0; i < myPhoneBook.size(); i++) {
                        if (myPhoneBook.get(i).find(term)) {
                            hits.add(i);
                            System.out.println(++k + ". " + myPhoneBook.get(i).toString());
                        }
                    }
                    String searchinput;
                    do {
                        System.out.println(("\n[search] Enter action ([number], back, again):"));
                        searchinput = scanner.nextLine().toLowerCase();
                        if (searchinput.matches("\\d+")) {
                            myPhoneBook.get(hits.get(Integer.parseInt(searchinput) - 1))
                                    .info();
                        } else if ("again".equals(searchinput)) {
                            hits.clear();
                            k = 0;
                            for (int i = 0; i < myPhoneBook.size(); i++) {
                                if (myPhoneBook.get(i).find(term)) {
                                    hits.add(i);
                                    System.out.println(k++ + myPhoneBook.get(i).toString());
                                }
                            }
                        }
                    } while ("again".equals(searchinput));

                    break;
                }
                case "info" : {
                    myPhoneBook.print("No records");
                    if (myPhoneBook.size() > 0) {
                        try {
                            System.out.print("Select a record:");
                            int Index = Integer.parseInt(scanner.nextLine()) - 1;
                            if (Index < 0 || Index >= myPhoneBook.size()) {
                                throw new ArrayIndexOutOfBoundsException("Wrong index");
                            } else {
                                myPhoneBook.get(Index).info();
                            }
                        } catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
                            System.out.println(e.getMessage());
                        }
                    }
                    break;
                }
                case "edit": {
                    myPhoneBook.print("No records to edit");
                    if (myPhoneBook.size() > 1) {
                        try {
                            System.out.print("Select a record:");
                            int Index = Integer.parseInt(scanner.nextLine()) - 1;
                            if (Index < 0 || Index >= myPhoneBook.size()) {
                                throw new ArrayIndexOutOfBoundsException("Wrong index");
                            } else {
                                myPhoneBook.get(Index).edit();
                            }
                        } catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
                            System.out.println(e.getMessage());
                        }
                    } else if (myPhoneBook.size() == 1) {
                            myPhoneBook.get(0).edit();
                    }
                    break;
                }
                case "remove": {
                    myPhoneBook.print("No records to remove");
                    if (myPhoneBook.size() > 0) {
                        try {
                            System.out.print("Select a record:");
                            int Index = Integer.parseInt(scanner.nextLine()) - 1;
                            if (Index < 0 || Index >= myPhoneBook.size()) {
                                throw new ArrayIndexOutOfBoundsException("Wrong index");
                            }
                            myPhoneBook.remove(Index);
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    break;
                }
                case "add": {
                    myPhoneBook.add(contactProvider.orderContact());
                    System.out.println("The record created!");
                    break;
                }
                case "list": {
                    myPhoneBook.print("No records");
                    System.out.println("\n[list] Enter action ([number], back):");
                    String listinput = scanner.nextLine();
                    if (listinput.matches("\\d+")) {
                        myPhoneBook.get(Integer.parseInt(listinput) - 1).info();
                    }
                    break;
                }
                default: break;
            }
        } while (!"exit".equals(input));
    }
}
