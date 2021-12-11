package banking;

public class account {
    long id;
    String cardNumber;
    String pin;
    float balance;

    account(long id, String cardNumber, String pin, float balance) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }
}
