package Server;
import Model.LoginObject;
import Model.User;

import java.sql.*;
import java.util.Objects;

/**
 * Created by henrik on 17/04/15.
 */
public class dbHandler {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/";
    private static final String USER = "root";
    private static final String PASS = "";
    private static final String DB_NAME = "TicTacToe";
    private Connection conn;

    public dbHandler() {

        try {

            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            createDatabase();

        }catch(ClassNotFoundException e) {

            System.err.print(e);

        }catch(SQLException e) {

            System.err.print(e);

        }
    }

    private void createTable() {

        try {

            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS User(" +
                    "name VARCHAR(40), " +
                    "surname VARCHAR(40)," +
                    "email VARCHAR(40)," +
                    "pass VARCHAR(40)," +
                    "username VARCHAR(40))";
            stmt.executeUpdate(sql);
            stmt.close();

        }catch(SQLException e) {

            e.printStackTrace();

        }
    }

    private void changeCatalog() {

        try {

            if(!conn.getCatalog().equals(DB_NAME)) {

                conn.setCatalog(DB_NAME);

            }
        }catch(SQLException e) {

            e.printStackTrace();

        }
    }

    public void createDatabase() {

        try {

            Statement stmt = conn.createStatement();
            String sql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            stmt.executeUpdate(sql);
            stmt.close();

            changeCatalog();
            createTable();

        }catch(SQLException e) {

            e.printStackTrace();

        }
    }

    public int insertUser(User usr) {

        String name = usr.getName();
        String surname = usr.getSurname();
        String email = usr.getEmail();
        String pass = usr.getPass();
        String username = usr.getUsername();

        try {

            changeCatalog();

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

            ps.execute();
            return 0;

        }catch(SQLException e) {

            System.err.print(e);

        }
        return 3;
    }

    public int login(LoginObject login) {

        try {

            changeCatalog();

            String sql = "SELECT * FROM User WHERE username=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,login.getUsername());
            ResultSet rs = ps.executeQuery();

            System.out.println(login.getUsername());
            if(!rs.isBeforeFirst()) {

                return 2;

            }

            rs.next();

            if(!rs.getString("pass").equals(login.getPass())) {

                return 1;

            }
        }catch(SQLException e) {

        }
        return 0;
    }

    public User getUsr(Object obj) {

        try {

            changeCatalog();

            String sql = "SELECT * FROM User WHERE username=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            if(obj instanceof User)
                ps.setString(1, ((User) obj).getUsername());
            else if(obj instanceof LoginObject)
                ps.setString(1, ((LoginObject) obj).getUsername());
            ResultSet rs = ps.executeQuery();
            rs.next();

            return new User(
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("email"),
                    rs.getString("pass"),
                    rs.getString("username")
            );

        }catch(SQLException e) {

            System.err.print(e);

        }

        return null;

    }

    public void close() {

        try {

            conn.close();

        }catch(SQLException e) {

            System.err.print(e);

        }
    }
}
