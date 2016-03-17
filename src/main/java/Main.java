import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.Repository;
import server.PackageServer;

import java.io.IOException;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final int PORT = 8080;

    public static void main(String[] args) {
        Repository repo = new Repository();

        try {
            new Thread(new PackageServer(PORT, repo)).start();
            logger.info(String.format("Server running on port %d", PORT));
        } catch (IOException ioe) {
            logger.error("Could not start server", ioe);
            ioe.printStackTrace();
        }
    }
}
