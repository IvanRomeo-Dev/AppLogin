package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
/**
 * Sample class for JDBC connection.
 *
 */
public class test {
    public static void main(String[] args) {
// Get connection as a resource.
        try (Connection conn = DriverManager.getConnection("jdbc:h2:c:\\tmp\\test")) {
// SQL statements
            String sqlDrop = "DROP TABLE IF EXISTS test;";
            String sqlCreate = "CREATE TABLE IF NOT EXISTS test (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(64) NOT NULL);";
            String sqlFirstInsert = "INSERT INTO test (name) VALUES('first');";
            String sqlSecondInsert = "INSERT INTO test (name) VALUES('second');";
            String sqlQuery = "SELECT * FROM test;";
// Get a statement
            try (Statement stmt = conn.createStatement()) {
// Execute drop and create
                System.out.println("Executing drop: " + stmt.executeUpdate(sqlDrop));
                System.out.println("Executing create: " + stmt.executeUpdate(sqlCreate));
// Use batch
                stmt.addBatch(sqlFirstInsert);
                stmt.addBatch(sqlSecondInsert);
                System.out.println("Executing batch: " + Arrays.toString(stmt.executeBatch()));
                ResultSet rs = stmt.executeQuery(sqlQuery);
                StringBuilder sb = new StringBuilder();
                sb.append("\nTable test:\nrow_num|id|name\n");
// Loop on rows.
                while (rs.next()) {
                    addRow(sb, rs);
                }
                System.out.println(sb.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Add read row to string builder
    private static void addRow(StringBuilder sb, ResultSet rs) throws SQLException {
// Get metadata in order to get the column count
        ResultSetMetaData rsmd = rs.getMetaData();
        int nrOfColumns = rsmd.getColumnCount();
// Add the row number
        sb.append(rs.getRow());
// Append columns
        for (int i = 1; i <= nrOfColumns; ++i) {
            sb.append('|').append(rs.getString(i));
        }
        sb.append('\n');
    }
}