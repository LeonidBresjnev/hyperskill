package carsharing;
import java.util.List;

public interface companyDAO {
    List<company> getAllCompanies();
    company getCompany(int id);
    void updateCompany(company myCompany);
    void newCompany(String name);
    void deleteCompany(int id);
    List<car> getAllCarsFromCompany(int company, boolean filter);
    void newCar(String name, int companyid);
    void newCustomer(String name);

    List<customer> getAllCustomers();
    customer getCustomer(int id);
    car getCar(int id);
    void updateCustomer(customer myCustomer);
    void closeDB();
}