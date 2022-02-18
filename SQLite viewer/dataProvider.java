package viewer;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class dataProvider {
    SQLiteDataSource dataSource ;

    dataProvider(String databasename) {
        dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + databasename);
        System.out.println("Database connected");
    }

    List<String> getTables() {
        List<String> tables = new ArrayList<>();
        try ( Connection con = dataSource.getConnection()) {
            ResultSet rs = con.getMetaData().getTables(null,null, "%",null);
            while (rs.next()) {
                tables.add(rs.getString(3));
            }
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("connect error");
        }
        return  tables;
    }

    table getData(String query) {
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(query)) {
                    ResultSetMetaData meta = resultSet.getMetaData();
                    table dataset = new table(meta.getColumnCount());
                    for (int i = 1; i <= meta.getColumnCount(); i++) {
                        dataset.colnames[i-1] = meta.getColumnName(i);
                    }
                    Object[] row;
                    while (resultSet.next()) {
                        row = new Object[meta.getColumnCount()];

                        for (int i = 1; i <= meta.getColumnCount(); i++) {
                            row[i-1] = resultSet.getObject(i);
                        }
                        dataset.data.add(row);
                    }
                    return dataset;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
