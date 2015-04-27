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
    //private OutputStream out;
    //private InputStream in;
    //private byte[] inBytes;
    private stages stage = stages.LOGGIN_IN;
    private dbHandler database;
    private User usr;

    private ObjectInputStream objIn;
    private ObjectOutputStream objOut;

    public Worker(Socket client, Server srv) {

        this.client = client;
        this.srv = srv;

    }

    private void setUpStreams() {

        try {

            //out = client.getOutputStream();
            //in = client.getInputStream();
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
                stage = stages.LOGGED_IN;
                database.close();

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
                stage = stages.LOGGED_IN;
                database.close();

            }else{

                database.close();
                client.close();
                srv.removeWorker(this);

            }

        }catch(IOException e) {

            System.err.print(e);

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

            setUpStreams();

            while (true) {

                try {

                    Object obj = objIn.readObject();
                    srv.incMessage(obj);

                }catch(IOException e) {

                    try {

                        srv.appendToLog("Client disconnected, thread " + getId() + " is terminating..");
                        srv.decThreadCounter();
                        client.close();
                        srv.removeWorker(this);
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
}
