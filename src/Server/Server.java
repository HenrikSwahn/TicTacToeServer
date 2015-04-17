package Server;

import GUI.Window;
import Model.dbHandler;


import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by henrik on 13/04/15.
 */
public class Server implements Runnable {

    private final int nrThreads = 5;
    private static Server srv = null;
    private ServerSocket server;
    private Socket client;
    private int PORT;
    private Window win;
    private ExecutorService executors = Executors.newFixedThreadPool(nrThreads);
    private List<Worker> workers;
    private int currentThreads;
    private dbHandler databaseHandler;

    private Server() {}

    public static Server getInstance() {

        if(srv == null)
            srv = new Server();

        return srv;

    }

    private void startServer() {

        try {

            currentThreads = 0;
            databaseHandler = dbHandler.getInstance();
            databaseHandler.connect();
            workers = new ArrayList<Worker>(nrThreads);
            server = new ServerSocket(PORT);

        }catch(IOException e) {

            System.err.print(e);

        }
    }

    private boolean checkAmountOfThreads() {

        return currentThreads < nrThreads;

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

                while(true) {

                    client = server.accept();

                    if(checkAmountOfThreads()) {

                        Worker w = new Worker(client, this);
                        workers.add(w);
                        executors.execute(w);
                        incThreadCounter();
                        win.appendToLog("A new WorkerThread has been started id: " + w.getId());
                        win.appendToLog("Nr of active threads: " + getNrOfThreads());

                    }else{

                        OutputStream out = client.getOutputStream();
                        out.write(("Server is full atm, try again later....").getBytes());
                        out.flush();
                        out.write(("disconnection....").getBytes());
                        out.flush();
                        out.close();
                        client.close();

                    }

                }

            }catch(IOException e) {

                System.err.print(e);

            }
        }
    }

    public synchronized  void appendToLog(Object obj) {

        win.appendToLog(obj);

    }

    public synchronized void incMessage(Object obj) {

        win.appendToLog(obj);
        for(Worker w:workers) {

            w.send(obj);

        }

    }

    public void incThreadCounter() {

        currentThreads++;

    }

    public synchronized void decThreadCounter() {

        if(currentThreads > 0)
            currentThreads--;



    }

    public synchronized int getNrOfThreads() {

        return currentThreads;

    }
}
