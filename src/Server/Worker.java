package Server;


import Model.User;
import Model.dbHandler;

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
    private OutputStream out;
    private InputStream in;
    private byte[] inBytes;
    private stages stage = stages.LOGGIN_IN;
    private dbHandler database;
    private User usr;

    public Worker(Socket client, Server srv) {

        this.client = client;
        this.srv = srv;

    }

    private void setUpStreams() {

        try {

            out = client.getOutputStream();
            in = client.getInputStream();

        }catch(IOException e) {

            System.err.print(e);

        }
    }

    public void run() {

        if(stage == stages.LOGGIN_IN) {

            try {

                ObjectInputStream ObjIn = new ObjectInputStream(client.getInputStream());
                Object obj = ObjIn.readObject();
                client.shutdownInput();

                database = new dbHandler();
                int statusCode = database.insert(obj);

                ObjectOutputStream ObjOut = new ObjectOutputStream(client.getOutputStream());
                ObjOut.writeInt(statusCode);
                ObjOut.flush();
                client.shutdownOutput();

                if(statusCode == 0) {

                    usr = database.getUsr((User)obj);
                    stage = stages.LOGGED_IN;

                }
                database.close();

            }catch(IOException e) {

                System.err.print(e);

            }catch(ClassNotFoundException e) {

                System.err.print(e);

            }
        }

        if(stage == stages.LOGGED_IN) {

            setUpStreams();

            while (true) {

                inBytes = new byte[4096];
                try {

                    if (in.read(inBytes) != -1) {

                        srv.incMessage(new String(inBytes, "UTF-8"));

                    } else {

                        srv.appendToLog("Client disconnected, thread " + getId() + " is terminating..");
                        srv.decThreadCounter();
                        break;

                    }

                } catch (IOException e) {

                    System.err.print(e);

                }
            }
        }
    }

    public void send(Object obj) {

        if(obj instanceof String) {

            try {

                System.out.println(usr.getUsername());
                out.write(((String) obj).getBytes());
                out.flush();

            }catch(IOException e) {

                System.err.print(e);

            }
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
}
