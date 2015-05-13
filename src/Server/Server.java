package Server;

import GUI.Window;
import Game.Game;
import Model.GameActionObject;
import Model.User;
import com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor;


import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by henrik on 13/04/15.
 */
public class Server implements Runnable {

    private final int nrThreads = 2;
    private static Server srv = null;
    private ServerSocket server;
    private Socket client;
    private int PORT;
    private Window win;
    private ExecutorService executors = Executors.newFixedThreadPool(nrThreads);
    private List<Worker> workers;
    private int currentThreads;
    private User srvUsr;
    private Game game;
    private String[] MARKS = {"X","O"};
    private int mCounter;

    private Server() {}

    public static Server getInstance() {

        if(srv == null)
            srv = new Server();

        dbHandler db = new dbHandler();
        db.createDatabase();
        db.close();

        return srv;

    }

    private void startServer() {

        try {

            currentThreads = 0;
            mCounter = 0;
            game = new Game();
            srvUsr = new User("Server", null, null, null, null);
            workers = new ArrayList<>(nrThreads);
            server = new ServerSocket(PORT);

        }catch(IOException e) {

            System.err.print(e);

        }
    }

    private boolean checkAmountOfThreads() {

        return currentThreads < nrThreads;

    }

    private String setMark() {

        return MARKS[mCounter++];

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

                win.appendToLog(srvUsr, "Now listening on " + PORT);

                while(true) {

                    client = server.accept();

                    if(checkAmountOfThreads()) {

                        Worker w = new Worker(client, this);
                        workers.add(w);
                        executors.execute(w);
                        win.appendToLog(srvUsr, "A new WorkerThread has been started id: " + w.getId());
                        win.appendToLog(srvUsr, "Nr of active threads: " + getNrOfThreads());

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

        win.appendToLog(srvUsr, obj);

    }

    public synchronized void incMessage(User usr, Object obj) {

        win.appendToLog(usr,obj);

        if(obj instanceof String) {

            String sendString = usr.getName() + " > " + obj;
            workers.forEach((Worker w) -> w.send(sendString));

        }
    }

    public void incThreadCounter() {

        currentThreads++;

        if(currentThreads == 2) {

            proposeNewGame();

        }
    }

    public void decThreadCounter() {

        if(currentThreads > 0) {

            currentThreads--;

        }
    }

    public synchronized int getNrOfThreads() {

        return currentThreads;

    }

    public void removeWorker(Worker w) {

        workers.remove(w);

    }

    public void proposeNewGame() {

        for(Worker w: workers) {
            w.proposeNewGame();
        }
    }

    public void playerReady() {

        int counter = 0;

        for(Worker w: workers) {

            counter++;
            if(!w.isReady())
                break;
        }

        if(counter == workers.size()) {

            appendToLog("Start a new game BOYS!");
            startNewGame();

        }
    }

    public void startNewGame() {

        workers.forEach((Worker w) -> {
            w.setMark(setMark());
            w.send(new GameActionObject(8, -1));
        });
        game = new Game();
        workers.get(workers.size()-2).send(new GameActionObject(10, -1)); //Unlock one player at first
        workers.get(workers.size()-1).send(new GameActionObject(9, -1)); //Lock one player at first
    }

    public boolean setMark(GameActionObject gao, String mark) {

        return game.setMark(gao.getVal(), mark);

    }

    public void updatePlayers(GameActionObject gao) {

        workers.forEach((Worker w) -> {
            w.send(gao);

            if(!w.getMark().equals(gao.getMark()) && gao.getAction() == 6) {
                w.send(new GameActionObject(10, -1));
                //Unlock player
            }
        });
    }

    public boolean checkIfGameOver(String mark) {

        return game.checkIfGameOver(mark);

    }

    public int getScore(String mark) {

        switch (mark) {
            case "X":
                return game.getxScore();
            case "O":
                return game.getoScore();
        }
        return -1;
    }
}
