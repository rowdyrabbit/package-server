import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.Repository;
import server.PackageServer;

import java.io.IOException;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Repository repo = new Repository();

        try {
            new Thread(new PackageServer(8080, repo)).start();
        } catch (IOException ioe) {
            logger.error("Could not start server", ioe);
            ioe.printStackTrace();
        }
    }
}
