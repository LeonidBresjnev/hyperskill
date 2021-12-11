package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;

public class loggedInMenu {
    boolean StayIn = true;
    account myAccount;
    SQLiteDataSource dataSource;
    loggedInMenu(SQLiteDataSource dataSource, account myAccount) {
        this.dataSource = dataSource;
        this.myAccount = myAccount;
        final java.util.Scanner scanner = new java.util.Scanner(System.in);
        short input = 5;
        do {
            System.out.println("1. Balance\n" +
                    "2. Add income\n" +
                    "3. Do transfer\n" +
                    "4. Close account\n" +
                    "5. Log out");
            try {
                input = Short.parseShort(scanner.nextLine());
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
            }
            switch (input) {
                case 1: {
                    System.out.printf("Balance %02f\n", this.myAccount.balance);
                    break;
                }
                case 2: {
                    System.out.println("Enter income:");
                    try (Connection con = dataSource.getConnection()) {
                        try (Statement statement = con.createStatement()) {
                            try {
                                this.myAccount.balance += Float.parseFloat(scanner.nextLine());
                                if (statement.executeUpdate(
                                    "UPDATE card SET balance =  " +  this.myAccount.balance + " where id = " + myAccount.id) > 0) {
                                    System.out.println("Income was added!");
                                } else {
                                    System.out.println("Income was not added!");
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }  catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 3: {
                    System.out.println("Enter card number:");
                    String transferTo = scanner.nextLine();
                    if (transferTo.length() != 16) {
                        System.out.println("Wrong Length!");
                        System.out.println("Probably you made a mistake in the card number. Please try again!");
                        break;
                    }
                    int checksum = 0;
                    int a;
                    for (int i = 0; i < 15; i++){
                        a = (Character.getNumericValue(transferTo.charAt(i))
                                * (1 + ((i + 1) % 2)));
                        a = a > 9 ? a - 9 : a;
                        checksum += a;
                    }
                    if (Integer.parseInt(transferTo.substring(15,16)) != (10 - (checksum % 10)) % 10) {
                        System.out.println("Luhn");
                        System.out.println("Probably you made a mistake in the card number. Please try again!");
                        break;
                    }

                    try (Connection con = dataSource.getConnection()) {
                        try (Statement statement = con.createStatement()) {
                            try (ResultSet sizeSet = statement.executeQuery(
                                    "SELECT id FROM card where number = " + transferTo)) {
                                if (sizeSet.next()) {
                                    System.out.println("Enter how much money you want to transfer:");
                                    float amount = Float.parseFloat(scanner.nextLine());
                                    if (amount <= this.myAccount.balance) {
                                        //update the two accounts
                                        this.myAccount.balance -= amount;
                                        try {
                                            statement.executeUpdate(
                                                    "UPDATE card SET balance =  "
                                                            +  this.myAccount.balance + " where id = "
                                                            + myAccount.id);
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }

                                        try {
                                            if (statement.executeUpdate(
                                                    "UPDATE card SET balance =  balance +" +  amount + " where number = " + transferTo) > 0) {
                                                System.out.println("Success!");
                                            } else {
                                                System.out.println("not success!");
                                            }
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        System.out.println("Not enough money!");
                                    }
                                } else {
                                    System.out.println("Such a card does not exist.");
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }  catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                }

                case 4: {
                    try (Connection con = dataSource.getConnection()) {
                        try (Statement statement = con.createStatement()) {
                            try {
                                if (statement.executeUpdate("delete from card where id = " + myAccount.id) > 0) {
                                    System.out.println("Your account is closed");
                                    input = 0;
                                    StayIn = false;
                                } else {
                                    System.out.println("Your account is not closed");
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }  catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 5: {
                    System.out.println("You have successfully logged out!");
                    break;
                }
                case 0: {
                    StayIn = false;
                    input = 5;
                    break;
                }
            }
        } while (input !=5);
    }
}

