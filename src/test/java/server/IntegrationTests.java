package server;

import org.junit.Before;
import org.junit.Test;
import protocol.MessageHandler;
import protocol.ResponseCode;
import repository.Repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class IntegrationTests {

    private Repository repo;

    @Before
    public void setup() {
        repo = new Repository();
    }

    @Test
    public void shouldAddPackagesSuccessfully() {
        ResponseCode resp = MessageHandler.handle(repo, "INDEX|libc|");

        assertThat(repo.packageInstalled("libc"), is(true));
        assertThat(resp, is(ResponseCode.OK));
    }

    @Test
    public void shouldNotAddPackagesWithMissingDependencies() {
        ResponseCode resp = MessageHandler.handle(repo, "INDEX|git|libuv");

        assertThat(repo.packageInstalled("git"), is(false));
        assertThat(resp, is(ResponseCode.FAIL));
    }

    @Test
    public void shouldReturnErrorForInvalidMessage() {
        ResponseCode resp = MessageHandler.handle(repo, "WHAT|git|libuv");

        assertThat(resp, is(ResponseCode.ERROR));
    }

    @Test
    public void shouldRemovePackagesWithNoDependents() {
        ResponseCode resp = MessageHandler.handle(repo, "INDEX|libuv|");
        assertThat(repo.packageInstalled("libuv"), is(true));
        assertThat(resp, is(ResponseCode.OK));

        resp = MessageHandler.handle(repo, "REMOVE|libuv|");
        assertThat(repo.packageInstalled("libuv"), is(false));
        assertThat(resp, is(ResponseCode.OK));
    }

    @Test
    public void shouldNotRemovePackagesWithDependents() {
        ResponseCode resp = MessageHandler.handle(repo, "INDEX|libuv|");
        assertThat(repo.packageInstalled("libuv"), is(true));
        assertThat(resp, is(ResponseCode.OK));
        resp = MessageHandler.handle(repo, "INDEX|git|libuv");
        assertThat(repo.packageInstalled("git"), is(true));
        assertThat(resp, is(ResponseCode.OK));

        resp = MessageHandler.handle(repo, "REMOVE|libuv|");
        assertThat(repo.packageInstalled("libuv"), is(true));
        assertThat(resp, is(ResponseCode.FAIL));
    }

    @Test
    public void shouldReturnValidQueryResponses() {
        ResponseCode resp = MessageHandler.handle(repo, "INDEX|libuv|");
        assertThat(repo.packageInstalled("libuv"), is(true));
        assertThat(resp, is(ResponseCode.OK));

        resp = MessageHandler.handle(repo, "QUERY|libuv|");
        assertThat(repo.packageInstalled("libuv"), is(true));
        assertThat(resp, is(ResponseCode.OK));

        resp = MessageHandler.handle(repo, "QUERY|not-installed|");
        assertThat(repo.packageInstalled("not-installed"), is(false));
        assertThat(resp, is(ResponseCode.FAIL));
    }


}
