package carsharing;

public class customer {
    private final int id;
    private final String name;
    private int rentedCarId;

    customer(int id, String name) {
        this.id = id;
        this.name = name;
    }
    customer(int id, String name, int rentedCarId) {
        this.id = id;
        this.name = name;
        this.rentedCarId = rentedCarId;
    }

    String getName() {
        return this.name;
    }

    int getRentedCarId() {
        return rentedCarId;
    }

    int getId() {
        return this.id;
    }
}
