package Model;
import java.sql.*;

/**
 * Created by henrik on 17/04/15.
 */
public class dbHandler {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/TicTacToe";
    private static final String USER = "root";
    private static final String PASS = "";
    private static dbHandler handler;

    private dbHandler() {}

    public static dbHandler getInstance() {

        if(handler == null) {

            handler = new dbHandler();

        }
        return handler;
    }

    public int connect() {

        Connection conn;
        Statement stmt;

        try {

            Class.forName(JDBC_DRIVER);

            System.out.println("Connecting to database");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            System.out.println("Making a statement");
            stmt = conn.createStatement();
            String sql = "INSERT INTO User(name, surname, email, pass, username) " +
                    "VALUES ('Henrik', 'Swahn', 'h_swahn@hotmail.com', '12345', 'Swahn')";
            stmt.execute(sql);

        }catch(ClassNotFoundException e) {

            System.err.print(e);

        }catch(SQLException e) {

            System.err.print(e);

        }
        return 0;
    }
}
