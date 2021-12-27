package carsharing;

public class car {
    private int id;
    private String name;
    private int companyid;

    car(int id, String name, int companyid) {
        this.id = id;
        this.name = name;
        this.companyid = companyid;
    }

    int getId() {
        return id;
    }

    String getName() {
        return name;
    }

    int getCompanyid() {
        return companyid;
    }
}
