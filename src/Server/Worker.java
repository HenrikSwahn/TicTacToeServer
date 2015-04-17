package Server;


import Model.User;
import com.sun.deploy.util.ArrayUtil;
import com.sun.tools.javac.util.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by henrik on 13/04/15.
 */
public class Worker extends Thread implements Runnable {

    private Socket client;
    private Server srv;
    private OutputStream out;
    private InputStream in;
    private byte[] inBytes;
    private User usr;

    public Worker(Socket client, Server srv) {

        this.client = client;
        this.srv = srv;
        usr = new User(getId());

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

        setUpStreams();

        while(true) {

            inBytes = new byte[4096];
            try {

                if(in.read(inBytes) != -1) {

                    srv.incMessage(new String(inBytes, "UTF-8"));

                }else{

                    srv.appendToLog("Client disconnected, thread " + getId() + " is terminating..");
                    srv.decThreadCounter();
                    break;

                }

            }catch(IOException e) {

                System.err.print(e);

            }
        }
    }

    public void send(Object obj) {

        if(obj instanceof String) {

            try {

                out.write(
                        arrayConcat(usr.toString().getBytes(),
                                ((String) obj).getBytes())
                );
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
