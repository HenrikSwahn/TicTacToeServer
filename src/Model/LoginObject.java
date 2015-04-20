package Model;

import java.io.Serializable;

/**
 * Created by henrik on 20/04/15.
 */
public class LoginObject implements Serializable {

    private String username;
    private String pass;

    public LoginObject(String username, String pass) {

        this.username = username;
        this.pass = pass;

    }

    public String getUsername() {

        return username;

    }

    public void setUsername(String username) {

        this.username = username;

    }

    public String getPass() {

        return pass;

    }

    public void setPass(String pass) {

        this.pass = pass;

    }
}