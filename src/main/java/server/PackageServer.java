package server;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.Repository;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PackageServer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(PackageServer.class);

    private final ServerSocket serverSocket;
    private final ExecutorService pool;
    private final Repository repo;

    public PackageServer(int portNumber, int poolSize, Repository repository) throws IOException {
        serverSocket = new ServerSocket(portNumber);
        pool = Executors.newFixedThreadPool(poolSize);
        repo = repository;
    }

    @Override
    public void run() {
        try {
            while (true) {
                pool.execute(new ConnectionHandler(serverSocket.accept(), repo));
            }
        } catch (IOException ex) {
            pool.shutdown();
        }
    }

}
