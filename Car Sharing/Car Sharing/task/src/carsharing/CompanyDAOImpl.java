package carsharing;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAOImpl implements companyDAO {
    Connection conn;

    @Override
    public void closeDB() {
        try {
            if(conn!=null) {
                conn.close();
            }
        } catch(SQLException se){
            se.printStackTrace();
        } //end finally try
    }

    public CompanyDAOImpl(String dbFile) {

        // JDBC driver name and database URL
        final String JDBC_DRIVER = "org.h2.Driver";
        final String DB_URL = "jdbc:h2:./src/carsharing/db/" + dbFile;
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);
            //STEP 2: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,null,null);
            conn.setAutoCommit(true);
            //STEP 3: Execute a query
            //System.out.println("Creating table in given database...");

/*
            try (Statement stmt0 = conn.createStatement()) {
                stmt0.executeUpdate("DROP TABLE IF EXISTS CUSTOMER");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try (Statement stmt0 = conn.createStatement()) {
                stmt0.executeUpdate("DROP TABLE IF EXISTS CAR");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try (Statement stmt0 = conn.createStatement()) {
                stmt0.executeUpdate("DROP TABLE IF EXISTS COMPANY");
            } catch (SQLException e) {
                e.printStackTrace();
            }*/

            try (Statement stmt0 = conn.createStatement()) {
                stmt0.executeUpdate("CREATE TABLE COMPANY " +
                        "(ID INTEGER not NULL AUTO_INCREMENT, " +
                        " NAME VARCHAR(255) NOT NULL UNIQUE, " +
                        " PRIMARY KEY ( ID ))");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try (Statement stmt0 = conn.createStatement()) {
                stmt0.executeUpdate("CREATE TABLE car " +
                        "(ID INTEGER not NULL AUTO_INCREMENT, " +
                        " NAME VARCHAR(255) NOT NULL UNIQUE, " +
                        " COMPANY_ID INT NOT NULL, " +
                        " FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)," +
                        " PRIMARY KEY ( ID ))");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try (Statement stmt0 = conn.createStatement()) {
                stmt0.executeUpdate("CREATE TABLE CUSTOMER " +
                        "(ID INTEGER not NULL AUTO_INCREMENT, " +
                        " NAME VARCHAR(255) NOT NULL UNIQUE, " +
                        " RENTED_CAR_ID  INT, " +
                        " FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)," +
                        " PRIMARY KEY ( ID ))");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch(SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCompany(int companyID) {
        try (Statement stmt = this.conn.createStatement()){
            stmt.executeUpdate("DELETE FROM COMPANY WHERE ID = " + companyID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Company: ID " + companyID + ", deleted from database");
    }

    //retrive list of students from the database
    @Override
    public List<company> getAllCompanies() {
        List<company> allcompanies = new ArrayList<>();
        try (Statement stmt = conn.createStatement()){
            //System.out.println("statement created");
            try (ResultSet result = stmt.executeQuery("SELECT * FROM COMPANY ORDER BY ID;")) {
                while (result.next()) {
                    // Retrieve column values
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    allcompanies.add(new company(id, name));
                    //System.out.printf("id %d\n", id);
                    //System.out.printf("Name: %s\n", name);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allcompanies;
    }

    @Override
    public company getCompany(int id) {
        //System.out.println("get query:" + sql );
        try (Statement stmt = conn.createStatement()){
            try (ResultSet result = stmt.executeQuery("SELECT ID, NAME FROM COMPANY where id = " + id)) {
                if (result.next()) {
                    // Retrieve column values
                    int resultid = result.getInt("id");
                    String resultname = result.getString("name");
                    return new company(resultid, resultname);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public customer getCustomer(int id){
        //System.out.println("get query:" + sql );
        try (Statement stmt = conn.createStatement()){
            try (ResultSet result = stmt.executeQuery("SELECT ID, NAME, RENTED_CAR_ID FROM CUSTOMER where id = " + id)) {
                if (result.next()) {
                    // Retrieve column values
                    int resultid = result.getInt("ID");
                    String resultname = result.getString("NAME");
                    int resultcar = result.getInt("RENTED_CAR_ID");
                    return new customer(resultid, resultname, resultcar);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public car getCar(int id){
        //System.out.println("get query:" + sql );
        try (Statement stmt = conn.createStatement()){
            try (ResultSet result = stmt.executeQuery("SELECT ID, NAME, COMPANY_ID FROM CAR where id = " + id)) {
                if (result.next()) {
                    // Retrieve column values
                    int resultid = result.getInt("ID");
                    String resultname = result.getString("NAME");
                    int company_id = result.getInt("COMPANY_ID");
                    return new car(resultid, resultname, company_id);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void updateCompany(company mycompany) {
        try (Statement stmt = conn.createStatement()){
            stmt.executeUpdate("UPDATE COMPANY SET NAME = " + mycompany.getName()
                    + "WHERE ID = " + mycompany.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCustomer(customer myCustomer) {
        try (Statement stmt = conn.createStatement()){
            stmt.executeUpdate("UPDATE CUSTOMER SET RENTED_CAR_ID = "
                    + (myCustomer.getRentedCarId() == 0 ? " NULL " : myCustomer.getRentedCarId())
                    + "WHERE ID = " + myCustomer.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void newCompany(String name) {
        try (Statement stmt = conn.createStatement()){
            stmt.executeUpdate("INSERT INTO COMPANY (NAME) VALUES ('" + name + "')");
            System.out.println("company crated!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void newCar(String name, int companyid) {
        try (Statement stmt = conn.createStatement()){
            stmt.executeUpdate("INSERT INTO CAR (NAME, COMPANY_ID) VALUES ('" +
                    name + "', " + companyid + ")");
            System.out.println("The car was added!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void newCustomer(String name) {
        try (Statement stmt = conn.createStatement()){
            stmt.executeUpdate(
                    "INSERT INTO CUSTOMER (NAME) VALUES ('" + name + "')");
            System.out.println("The customer was added!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<customer> getAllCustomers(){
        List<customer> allcustomers = new ArrayList<>();
        try (Statement stmt = conn.createStatement()){
            //System.out.println("statement created");
            try (ResultSet result = stmt.executeQuery("SELECT * FROM CUSTOMER ORDER BY ID;")) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    int rented_car_id = result.getInt("RENTED_CAR_ID");
                    allcustomers.add(new customer(id, name, rented_car_id));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allcustomers;
    }


    public List<car> getAllCarsFromCompany(int company, boolean filter) {
        List<car> allcars = new ArrayList<>();
        try (Statement stmt = conn.createStatement()){
            try (ResultSet result = stmt.executeQuery("SELECT a.*, b.RENTED_CAR_ID FROM CAR as a " +
                    "LEFT JOIN CUSTOMER as b on a.id = b.RENTED_CAR_ID WHERE COMPANY_ID = " +
                    company +
                    " ORDER BY ID;")) {
                while (result.next()) {
                    int id = result.getInt("ID");
                    String name = result.getString("NAME");
                    int rented = result.getInt("RENTED_CAR_ID");
                    if (!filter || filter && rented == 0) {
                        allcars.add(new car(id, name, company));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allcars;
    }
}
