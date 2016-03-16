package server;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ConnectionHandler implements Runnable {
    private final Socket socket;
    private final Repository repo;

    private static final Logger logger = LoggerFactory.getLogger(ConnectionHandler.class);


    public ConnectionHandler(Socket socket, Repository repo) {
        this.socket = socket;
        this.repo = repo;
    }


    @Override
    public void run() {

        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                out.println("OK");
            }

        } catch (IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
            ioe.printStackTrace();
        }

    }
}
