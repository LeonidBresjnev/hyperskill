package contacts;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;


public abstract class contactCreator {
    abstract contact createContact();

    contact orderContact() {
        return createContact();
    }
}

class contactStore extends contactCreator {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    contact createContact() {
        System.out.println("Enter the type (person, organization)");
        contactType type = null;
        try {
            type = contactType.valueOf(scanner.nextLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        if (type.equals(contactType.PERSON)) {
            System.out.println("Enter the name:");
            String name = scanner.nextLine();
            System.out.println("Enter the surname:");
            String surname = scanner.nextLine();
            System.out.println("Enter the birthday:");
            LocalDate birthday = null;
            try {
                birthday = LocalDate.parse(scanner.nextLine());
            } catch (DateTimeParseException e) {
                System.out.println("Bad birth date!");
            }
            System.out.println("Enter the gender (M, F):");
            String gender = scanner.nextLine();
            System.out.println("Enter the number:");
            String number = scanner.nextLine();
            return new person(name, surname, number, birthday, gender);
        } else if (type.equals(contactType.ORGANIZATION)) {
            System.out.println("Enter the organization name:");
            String name = scanner.nextLine();
            System.out.println("Enter the address:");
            String address = scanner.nextLine();
            System.out.println("Enter the number:");
            String number = scanner.nextLine();
            return new organization(name, number, address);
        } else return null;
    }


}