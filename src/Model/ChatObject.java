package Model;

import java.io.Serializable;

/**
 * Created by Nilj on 26/04/15.
 */
public class ChatObject implements Serializable {

    private String msg;

    public ChatObject() {

    }

    public String getMsg() {

        return msg;

    }

    public void setMsg(String msg) {

        this.msg = msg;

    }
}