package Server;


import Model.GameActionObject;
import Model.LoginObject;
import Model.User;

import java.io.*;
import java.net.Socket;

/**
 * Created by henrik on 13/04/15.
 */
public class Worker extends Thread implements Runnable {

    private enum stages{
        LOGGIN_IN, LOGGED_IN;
    }

    private Socket client;
    private Server srv;
    private stages stage = stages.LOGGIN_IN;
    private dbHandler database;
    private User usr;

    private ObjectInputStream objIn;
    private ObjectOutputStream objOut;

    private boolean isReady;
    private String mark;

    public Worker(Socket client, Server srv) {

        this.client = client;
        this.srv = srv;
        isReady = false;
        mark = null;

    }

    private void setUpStreams() {

        try {

            objIn = new ObjectInputStream(client.getInputStream());
            objOut = new ObjectOutputStream(client.getOutputStream());

        }catch(IOException e) {

            System.err.print(e);

        }
    }

    private byte[] arrayConcat(byte[] a, byte[] b) {

        int aLength = a.length;
        int bLength = b.length;
        byte[] arrow = (" > ").getBytes();
        int arrLength = arrow.length;
        byte[] c = new byte[aLength+bLength+arrLength];

        System.arraycopy(a,0,c,0,aLength);
        System.arraycopy(arrow,0,c,aLength, arrLength);
        System.arraycopy(b,0,c,(aLength + arrLength),bLength);

        return c;

    }

    private void createUser(User usr) {

        try {

            database = new dbHandler();
            int statusCode = database.insertUser(usr);
            ObjectOutputStream ObjOut = new ObjectOutputStream(client.getOutputStream());

            ObjOut.writeInt(statusCode);
            ObjOut.flush();

            if(statusCode == 0) {

                this.usr = database.getUsr(usr);
                ObjOut.writeObject(this.usr);
                ObjOut.flush();
                stage = stages.LOGGED_IN;
                database.close();
                setUpStreams();
                srv.incThreadCounter();

            }else{

                database.close();
                client.close();
                srv.removeWorker(this);

            }

        }catch(IOException e) {

            System.err.print(e);

        }
    }

    private void loginUser(LoginObject login) {

        try {

            database = new dbHandler();
            int statusCode = database.login(login);
            ObjectOutputStream ObjOut = new ObjectOutputStream(client.getOutputStream());

            ObjOut.writeInt(statusCode);
            ObjOut.flush();

            if(statusCode == 0) {

                this.usr = database.getUsr(login);
                ObjOut.writeObject(this.usr);
                ObjOut.flush();
                stage = stages.LOGGED_IN;
                database.close();
                setUpStreams();
                srv.incThreadCounter();

            }else{

                database.close();
                client.close();
                srv.removeWorker(this);

            }

        }catch(IOException e) {

            System.err.print(e);

        }
    }

    public void gaObjectInc(GameActionObject gao, User usr) {

        switch (gao.getAction()) {
            case 1:
                isReady = true;
                srv.incMessage(usr, "is ready");
                srv.playerReady();
                break;
            case 2:
                srv.appendToLog("Player turned you down");
            case 3:

                if(srv.setMark(gao, mark)) {

                    //Valid move
                    send(new GameActionObject(6, gao.getVal()));

                }else {

                    //Invalid move
                    send(new GameActionObject(7, gao.getVal()));

                }
                break;
        }
    }

    public boolean isReady() {

        return isReady;

    }

    public void setReady(boolean isReady) {

        this.isReady = isReady;

    }

    public String getMark() {

        return mark;

    }

    public void setMark(String mark) {

        this.mark = mark;

        if(mark.equals("X")) {

            send(new GameActionObject(4,-1));

        }else {

            send(new GameActionObject(5, -1));

        }
    }

    public void run() {

        if(stage == stages.LOGGIN_IN) {

            try {

                ObjectInputStream ObjIn = new ObjectInputStream(client.getInputStream());
                Object obj = ObjIn.readObject();

                if(obj instanceof User) {

                    createUser((User)obj);

                }else if(obj instanceof LoginObject) {

                    loginUser((LoginObject)obj);

                }

            }catch(IOException e) {

                System.err.print(e);

            }catch(ClassNotFoundException e) {

                System.err.print(e);

            }
        }

        if(stage == stages.LOGGED_IN) {

            //setUpStreams();

            while (true) {

                try {

                    Object obj = objIn.readObject();

                    if(obj instanceof String) {

                        srv.incMessage(usr, obj);

                    }else if(obj instanceof GameActionObject) {

                        gaObjectInc((GameActionObject) obj, usr);

                    }


                }catch(IOException e) {

                    try {

                        srv.appendToLog("Client disconnected, thread " + getId() + " is terminating..");
                        client.close();
                        srv.removeWorker(this);
                        srv.decThreadCounter();
                        break;

                    }catch(IOException ex) {

                        System.err.print(ex);

                    }

                }catch(ClassNotFoundException e){

                    System.err.print(e);

                }
            }
        }
    }

    public void send(Object obj) {

        try {

            objOut.writeObject(obj);
            objOut.flush();

        }catch(IOException e) {

            System.err.print(e);

        }
    }

    public void proposeNewGame() {

        try {

            GameActionObject gao = new GameActionObject(0, -1);
            objOut.writeObject(gao);
            objOut.flush();

        }catch(IOException e) {

            System.err.print(e);

        }
    }
}
