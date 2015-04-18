package Model;
import java.sql.*;
import java.util.Objects;

/**
 * Created by henrik on 17/04/15.
 */
public class dbHandler {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/TicTacToe";
    private static final String USER = "root";
    private static final String PASS = "";
    private Connection conn;

    public dbHandler() {

        try {

            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

        }catch(ClassNotFoundException e) {

            System.err.print(e);

        }catch(SQLException e) {

            System.err.print(e);

        }
    }

    private int insertUser(User usr) {

        String name = usr.getName();
        String surname = usr.getSurname();
        String email = usr.getEmail();
        String pass = usr.getPass();
        String username = usr.getUsername();

        try {


            String sql = "SELECT * FROM User WHERE username=? OR email=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                if(username.equals(rs.getString("username"))) {
                    return 2;
                }else if(email.equals(rs.getString("email"))) {
                    return 1;
                }
            }

            sql = "INSERT INTO USER(name, surname, email, pass, username) VALUES(?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, surname);
            ps.setString(3, email);
            ps.setString(4, pass);
            ps.setString(5, username);

            if(ps.execute())
                return 0;



        }catch(SQLException e) {

            System.err.print(e);

        }
        return 3;
    }

    public int insert(Object obj) {

        if(obj instanceof User)
            return insertUser((User)obj);

        return -1;
    }
}
