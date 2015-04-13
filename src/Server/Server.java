package Server;

import GUI.Window;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by henrik on 13/04/15.
 */
public class Server implements Runnable {

    private static Server srv = null;
    private ServerSocket server;
    private Socket client;
    private int PORT;
    private Window win;

    private Server() {}

    public static Server getInstance() {

        if(srv == null)
            srv = new Server();

        return srv;

    }

    public void setPort(int PORT) {

        this.PORT = PORT;

    }

    public void setWindow(Window win) {

        this.win = win;

    }

    public void run() {

        if(win != null) {

            startServer();
            
            try{

                win.appendToLog("Now listening on " + PORT);
                client = server.accept();
                OutputStream out = client.getOutputStream();
                out.write(("Hello From Server").getBytes());
                out.flush();
                out.close();
                client.close();

            }catch(IOException e) {

                System.err.print(e);

            }
        }
    }

    private void startServer() {

        try {

            server = new ServerSocket(PORT);

        }catch(IOException e) {

            System.err.print(e);

        }
    }
}
