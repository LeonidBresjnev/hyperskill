package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        String url = "";
        for (int i = 0; i < args.length; i++) {
            if ("-fileName".equalsIgnoreCase(args[i])) {
                if (i + 1 == args.length) {
                    System.out.println("No input file defined!");
                    System.exit(0);
                } else {
                    url = "jdbc:sqlite:" + args[i + 1];
                }
            }
        }

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        long size = 0;
        // Statement creation
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                // Statement execution
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card (" +
                        "id INTEGER,\n" +
                        "number TEXT,\n" +
                        "pin TEXT,\n" +
                        "balance INTEGER DEFAULT 0)");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try (Statement statement = con.createStatement()) {
                try (ResultSet sizeSet = statement.executeQuery("SELECT count(1) as n FROM card")) {
                    while (sizeSet.next()) {
                        size = sizeSet.getLong("n");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        final java.util.Scanner scanner = new java.util.Scanner(System.in);
        short input = 0;
        do {
            System.out.println("1. Create an account\n" +
                    "2. Log into account\n" +
                    "0. Exit");

            try {
                String str =scanner.nextLine();
                input = Short.parseShort(str);
                System.out.println("input=" + str);
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }

            switch (input) {
                case 1: {
                    String pin = String.format("%04d", new Random().nextInt(10000));
                    long can = new Random().nextInt(1000000000);
                    long bin = 400000;
                    String cardNumber = String.format("%06d", bin) +
                            String.format("%09d", can);
                    int checksum = 0;
                    int a;
                    for (int i = 0; i < 15; i++){
                        a = (Character.getNumericValue(cardNumber.charAt(i))
                        * (1 + ((i + 1) % 2)));
                        a = a > 9 ? a - 9 : a;
                        checksum += a;
                    }
                    size++;
                    cardNumber = cardNumber + (10 - (checksum % 10)) % 10;
                    System.out.println("Your card has been created\n" +
                            "Your card number:\n" + cardNumber);
                    System.out.printf("Your card PIN:\n%s\n", pin);

                        // Statement creation

                    try (Connection con = dataSource.getConnection()) {
                        try (Statement statement = con.createStatement()) {
                            // Statement execution
                            statement.executeUpdate("insert into card (id, number, pin, balance)\n" +
                                    "VALUES ('" + size + "', '" + cardNumber + "', '" + pin + "', '0')");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 2: {
                    System.out.println("Enter your card number:");
                    String cardNumber = scanner.nextLine();
                    System.out.println("Enter your PIN:");
                    String pinNumber = scanner.nextLine();
                    account myAccount = null;
                    try (Connection con = dataSource.getConnection()) {
                        try (Statement statement = con.createStatement()) {
                            try (ResultSet sizeSet = statement.executeQuery(
                                    "SELECT id, pin, balance FROM card where number = " + cardNumber)) {
                                if (sizeSet.next() && pinNumber.equals(sizeSet.getString("pin"))) {
                                    System.out.println("You have successfully logged in!");
                                    myAccount = new account(
                                            sizeSet.getLong("id"),
                                            cardNumber,
                                            pinNumber,
                                            sizeSet.getFloat("balance")
                                    );
                                } else {
                                    System.out.println("Wrong card number or PINa!");
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (myAccount != null) {
                        loggedInMenu loggedIn = new loggedInMenu(dataSource, myAccount);
                        if (!loggedIn.StayIn) {
                            System.out.println("Bye!");
                            input = 0;
                            break;
                        }
                    }

                    break;
                }
                case 0: {
                    System.out.println("Bye!");
                    break;
                }
                default:
                    break;
            }
        } while (input != 0);
    }
}