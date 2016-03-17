package server;

import net.jodah.concurrentunit.Waiter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.Repository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class PackageServerTest {

    private static Repository repo = new Repository();
    private static ExecutorService executor;

    @BeforeClass
    public static void setup() throws Exception {
        executor = Executors.newSingleThreadExecutor();
        executor.submit(new PackageServer(8082, repo));
    }

    @Test
    public void shouldSeePackageUpdatesFromOtherThreads() throws Exception {
        final Waiter waiter = new Waiter();

        Client client1 = new Client();


        new Thread(() ->  {
            client1.out.println("INDEX|git|git-other");
            client1.out.println("INDEX|libc|");
            client1.out.println("INDEX|git|libc");
            try {
                String response = client1.in.readLine();
                assertThat(response, is("FAIL"));
                response = client1.in.readLine();
                assertThat(response, is("OK"));
                response = client1.in.readLine();
                assertThat(response, is("OK"));
                waiter.resume();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        waiter.await();
        assertThat(repo.packageInstalled("git"), is(true));
        assertThat(repo.packageInstalled("libc"), is(true));
        assertThat(repo.packageInstalled("git-other"), is(false));
    }


    @AfterClass
    public static void stop() {
        executor.shutdownNow();
    }


}
class Client {
    private Socket kkSocket;
    public PrintWriter out;
    public BufferedReader in;

    public Client() throws Exception {
        kkSocket = new Socket("localhost", 8082);
        out = new PrintWriter(kkSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
    }
}
