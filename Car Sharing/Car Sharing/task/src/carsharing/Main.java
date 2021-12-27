package carsharing;

import java.util.List;

class menu {
    java.util.Scanner scanner;
    companyDAO myDAO;

    menu(String dbFile) {
        scanner = new java.util.Scanner(System.in);
        myDAO = new CompanyDAOImpl(dbFile);
    }

    void welcome() {
        while (true) {
            System.out.println("\n1. Log in as a manager\n" +
                    "2. Log in as a customer\n" +
                    "3. Create a customer\n" +
                    "0. Exit");
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input == 1) {
                    this.mainmenu();
                } else if (input == 2) {
                    this.customermain();
                } else if (input == 3) {
                    this.createcustomer();
                } else if (input == 0) {
                    myDAO.closeDB();
                    break;
                } else {
                    throw new NumberFormatException("wrong number");
                }
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    void createcustomer() {
        System.out.println("Enter the customer name:");
        myDAO.newCustomer(scanner.nextLine());
    }

    void customermain() {
        List<customer> myList = myDAO.getAllCustomers();
        if (myList.isEmpty()) {
            System.out.println("The customer list is empty!");
        } else {
            while (true) {
                System.out.println("Customer list:");
                for (int i = 0; i < myList.size(); i++) {
                    System.out.println((i + 1) + ". " + myList.get(i).getName());
                }
                System.out.println("0. Back");
                try {
                    int input = Integer.parseInt(scanner.nextLine());
                    if (input == 0) {
                        break;
                    } else if (input > 0 && input <= myList.size()) {
                        customermenu(myList.get(input - 1).getId());
                        break;
                    } else {
                        throw new NumberFormatException("Wrong number");
                    }
                } catch (NumberFormatException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    void customermenu(int customerid) {
        customer myCustomer = myDAO.getCustomer(customerid);
        while (true) {
            System.out.println("\n1. Rent a car\n" +
                    "2. Return a rented car\n" +
                    "3. My rented car\n" +
                    "0. Back");
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input == 1) {
                    if (myCustomer.getRentedCarId() != 0) {
                        System.out.println("'You've already rented a car!");
                    } else {
                        rentCarMenu(customerid);
                        myCustomer = myDAO.getCustomer(customerid);
                    }
                } else if (input == 2) {
                    if (myCustomer.getRentedCarId() == 0) {
                        System.out.println("You didn't rent a car!");
                    } else {
                        myCustomer = new customer(customerid, myCustomer.getName(), 0);
                        myDAO.updateCustomer(myCustomer);

                        System.out.println("You've returned a rented car!");
                    }
                } else if (input == 3) {
                    if (myCustomer.getRentedCarId() == 0) {
                        System.out.println("You didn't rent a car!");
                    } else {
                        car myCar = myDAO.getCar(myCustomer.getRentedCarId());
                        company mycompany = myDAO.getCompany(myCar.getCompanyid());
                        System.out.println("Your rented car:\n" + myCar.getName() +
                                "company:\n " + mycompany.getName());
                    }
                } else if (input == 0) {
                    break;
                } else {
                    throw new NumberFormatException("wrong number");
                }
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    void rentCarMenu(int customerid) {
        List<company> myList = myDAO.getAllCompanies();
        if (myList.isEmpty()) {
            System.out.println("The company list is empty!");
        } else {
            while (true) {
                System.out.println("Choose a company:");
                for (int i = 0; i< myList.size(); i++) {
                    System.out.println((i+1) + ". " + myList.get(i).getName());
                }
                System.out.println("0. Back");
                try {
                    int input = Integer.parseInt(scanner.nextLine());
                    if (input == 0) {
                        break;
                    } else if (input > 0 && input <= myList.size()) {
                        rentFromCompany(myList.get(input - 1).getId(), customerid);
                        break;
                    } else {
                        throw new NumberFormatException("Wrong number");
                    }
                } catch (NumberFormatException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    void rentFromCompany(int company, int customer) {
        List<car> myList = myDAO.getAllCarsFromCompany(company, true);

        while (true) {
            if (myList.isEmpty()) {
                System.out.println("The car list is empty!");
                break;
            } else {
                System.out.println("Choose a car:");
                for (int i = 0; i < myList.size(); i++) {
                    System.out.println((i + 1) + ". " + myList.get(i).getName());
                }
                try {
                    int input = Integer.parseInt(scanner.nextLine());
                    if (input == 0) {
                        break;
                    } else if (input > 0 && input <= myList.size()) {
                        myDAO.updateCustomer(new customer(customer, "",
                                myList.get(input - 1).getId()));
                        System.out.println("You rented '" + myList.get(input - 1).getName() +"'");
                        break;
                    } else {
                        throw new NumberFormatException("Wrong number");
                    }
                } catch (NumberFormatException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    void mainmenu() {
        while (true) {
            System.out.println("1. Company list\n" +
                    "2. Create a company\n" +
                    "0. Back");
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input == 1) {
                    companylistmenu();
                } else if (input == 2) {
                    System.out.println("Enter the company name:");
                    myDAO.newCompany(scanner.nextLine());
                } else if (input == 0) {
                    break;
                } else {
                    throw new NumberFormatException("wrong number");
                }
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    void companylistmenu() {
        List<company> myList = myDAO.getAllCompanies();
        if (myList.isEmpty()) {
            System.out.println("The company list is empty!");
        } else {
            while (true) {
                System.out.println("Choose the company:");
                for (int i = 0; i < myList.size(); i++) {
                    System.out.println((i + 1) + ". " + myList.get(i).getName());
                }
                System.out.println("0. Back");
                try {
                    int input = Integer.parseInt(scanner.nextLine());
                    if (input == 0) {
                        break;
                    } else if (input > 0 && input <= myList.size()) {
                        companymenu(myList.get(input -1).getId());
                        break;
                    } else {
                        throw new NumberFormatException("Wrong number");
                    }
                } catch (NumberFormatException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    void companymenu(int company) {
        company myCompany = myDAO.getCompany(company);

        while (true) {
            System.out.println("\n'" + myCompany.getName() + "' company");
            System.out.println("1. Car list\n" +
                    "2. Create a car\n" +
                    "0. Back");
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input == 1) {
                    this.carlistmenu(company);
                } else if (input == 2) {
                    System.out.println("Enter the car name:");
                    String name = scanner.nextLine();
                    myDAO.newCar(name, company);
                } else if (input == 0) {
                    break;
                } else {
                    throw new NumberFormatException("wrong number");
                }
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    void carlistmenu(int company) {
        List<car> myList = myDAO.getAllCarsFromCompany(company, false);
        if (myList.isEmpty()) {
            System.out.println("The car list is empty!");
        } else {
            System.out.println("Car list:");
            for (int i = 0; i < myList.size(); i++) {
                System.out.println((i + 1) + ". " + myList.get(i).getName());
            }
        }
    }
}


public class Main {

    public static void main(String[] args) {

        String defaultdbname = "carsharing";
        if (args.length>=2 && "-databasefileName".equals(args[0].toLowerCase())) {
            defaultdbname = args[1];
        }
        menu myMenu = new menu(defaultdbname);
        myMenu.welcome();
    }
}