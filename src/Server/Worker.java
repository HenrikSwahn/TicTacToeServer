package Server;


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

                out.write(((String) obj).getBytes());
                out.flush();

            }catch(IOException e) {

                System.err.print(e);

            }
        }
    }
}
