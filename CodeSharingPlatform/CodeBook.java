package platform;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.time.Duration;

public class CodeBook {
    final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:./src/codebook.db";

    private Connection conn = null;

    CodeBook() {
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);
            //STEP 2: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, null, null);
            conn.setAutoCommit(true);

            try (Statement stmt0 = conn.createStatement()) {
                stmt0.executeUpdate("CREATE TABLE IF NOT EXISTS CODES " +
                        "(UUID VARCHAR(64), " +
                        " CODE VARCHAR(255), " +
                        " CREATIONDATE TIMESTAMP," +
                        " TIME INTEGER," +
                        " VIEWS INTEGER," +
                        " TIMERESTRICTION INTEGER, " +
                        " VIEWSRESTRICTION INTEGER," +
                        " ID INTEGER not NULL AUTO_INCREMENT, " +
                        " PRIMARY KEY ( UUID ))");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch(Exception se) {
            se.printStackTrace();
        }
    }

    public String addCode(String code, int time, int views) {
        System.out.println(code);
        try {
            String uuid = UUID.randomUUID().toString();

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO CODES (UUID, CODE, CREATIONDATE, TIME, VIEWS,TIMERESTRICTION,VIEWSRESTRICTION ) VALUES ('" +
                            uuid.replaceAll("'","''") + "', '" +
                            code.replaceAll("'","''") + "', '" +
                            Timestamp.valueOf(LocalDateTime.now()) + "', " +
                            time + ", " +
                            views + ", " +
                            (time <= 0 ? 0 : 1) + ", " +
                            (views <= 0 ? 0 : 1) +
                    ')');
            ps.executeUpdate();
            System.out.println("The code was added!");
            return uuid;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Code getCode(String uuid) {
        try (Statement stmt = conn.createStatement()){
            try (ResultSet result = stmt.executeQuery("SELECT  Code, CREATIONDATE, TIME, VIEWS,TIMERESTRICTION, VIEWSRESTRICTION FROM CODES where UUID = '" + uuid + "'")) {
                if (result.next()) {

                    Duration seconds =  Duration.between(result.getTimestamp("CREATIONDATE").toLocalDateTime(), LocalDateTime.now());
                    int time;
                    boolean timerestrictions = result.getInt("TIMERESTRICTION") == 1;
                    if (result.getInt("TIMERESTRICTION") == 1) {
                        time = Math.max(result.getInt("TIME") - (int)seconds.toSeconds(), 0);
                    } else {
                        time = result.getInt("TIME");
                    }

                    int views = result.getInt("VIEWS");
                    boolean viewsrestriction = (result.getInt("VIEWSRESTRICTION") == 1);
                    if (viewsrestriction && views >= 0) {
                        views = views - 1 ;
                        try (Statement stmt2 = conn.createStatement()) {
                            stmt2.executeUpdate(
                                    "UPDATE CODES SET VIEWS = (VIEWS - 1)" +
                                            "WHERE UUID = '" + uuid + "'");
                            System.out.println("The views is updated!");

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    return new Code(result.getString("CODE"),
                            result.getTimestamp("CREATIONDATE").toLocalDateTime().toLocalDate(),
                            time,
                            views, timerestrictions, viewsrestriction);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Code> getLatestTen() {
        try (Statement stmt = conn.createStatement()){
            try (ResultSet result = stmt.executeQuery(
                    "SELECT Code, CREATIONDATE, TIME, VIEWS, TIMERESTRICTION, VIEWSRESTRICTION " +
                            "FROM CODES ORDER BY ID DESC")) {
                List<Code> returlist = new LinkedList<>();
                int counter = 0;
                while (result.next()) {
                    boolean viewsrestriction = (result.getInt("VIEWSRESTRICTION") == 1);
                    Duration seconds =  Duration.between(result.getTimestamp("CREATIONDATE").toLocalDateTime(), LocalDateTime.now());
                    int time;
                    boolean timerestrictions = result.getInt("TIMERESTRICTION") == 1;
                    if ((!viewsrestriction /*|| result.getInt("VIEWS") >= 0*/) &&
                            (!timerestrictions /*|| result.getInt("TIME") >= seconds.toSeconds()*/)) {
                        returlist.add(new Code(result.getString("CODE"),
                                result.getTimestamp("creationdate").toLocalDateTime().toLocalDate(),
                                (result.getInt("TIMERESTRICTION") == 1) ? 0 : result.getInt("TIME"),
                                result.getInt("VIEWS"),
                                (result.getInt("TIMERESTRICTION") == 1),
                                viewsrestriction));
                        counter++;
                        if (counter == 10) { break;}
                    }
                }
                return returlist;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
