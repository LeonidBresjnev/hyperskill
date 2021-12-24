package contacts;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

class wrongPhoneNumberException extends Exception {
    public wrongPhoneNumberException(String msg) {
        super(msg);
    }
}

public abstract class contact {

    protected String number;
    protected LocalDateTime datecreated;
    protected LocalDateTime dateedited;
    protected contactType type;

    contact(String number, contactType type) {
        this.setNumber(number);
        this.type = type;
        this.datecreated = LocalDateTime.now();
        this.dateedited = LocalDateTime.now();
    }

    boolean checkNumber(String number) {
        Pattern pattern = Pattern
                .compile("^\\+?(\\(\\w+\\)|\\w+[ -]\\(\\w{2,}\\)|\\w+)([ -]\\w{2,})*");
        return pattern.matcher(number).matches();
    }

    public boolean setNumber(String number) {
        if (checkNumber(number)) {
            this.number = number;
            System.out.println("The record updated!");
            return true;
        }
        else {
            this.number = "";
            System.out.println("Wrong number format!");
            return false;
        }
    }



    abstract void info();
    abstract public String toString();
    abstract public void edit();
    abstract public boolean find(String str);

    public boolean hasNumber() {
        return (!"".equals(this.number));
    }
}


