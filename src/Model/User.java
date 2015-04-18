package Model;

import java.io.Serializable;

/**
 * Created by henrik on 17/04/15.
 */
public class User implements Serializable {

    private String name;
    private String surname;
    private String email;
    private String pass;
    private String username;

    public User(String name, String surname, String email, String pass, String username) {

        this.name = name;
        this.surname = surname;
        this.email = email;
        this.pass = pass;
        this.username = username;

    }

    public String getName() {

        return name;

    }

    public void setName(String name) {

        this.name = name;

    }

    public String getSurname() {

        return surname;

    }

    public void setSurname(String surname) {

        this.surname = surname;

    }

    public String getEmail() {

        return email;

    }

    public void setEmail(String email) {

        this.email = email;

    }

    public String getPass() {

        return pass;

    }

    public void setPass(String pass) {

        this.pass = pass;

    }

    public String getUsername() {

        return username;

    }

    public void setUsername(String username) {

        this.username = username;

    }
}
