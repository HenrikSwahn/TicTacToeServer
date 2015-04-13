package Main;

import GUI.Window;
import Server.Server;

/**
 * Created by henrik on 13/04/15.
 */
public class Main {

    public static void main(String[] args) {

        Server server = Server.getInstance();
        server.setPort(6066);
        Window window = new Window("Hello World");
        server.setWindow(window);
        new Thread(server).start();

    }
}
