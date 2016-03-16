package protocol;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import repository.Repository;

import java.util.Arrays;
import java.util.Collections;

import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_LIST;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static protocol.ResponseCode.*;


public class MessageHandlerTest {

    private Repository repo = mock(Repository.class);


    @Test
    public void shouldReturnErrorResponseIfMessageIsInvalid() {
        String message = "BROKEN|git|";
        ResponseCode resp = MessageHandler.handle(repo, message);
        assertThat(resp, is(ERROR));
    }

    @Test
    public void shouldReturnFailResponseIfPackageNotIndexed() {
        when(repo.packageInstalled("git")).thenReturn(false);

        String message = "QUERY|git|";
        ResponseCode resp = MessageHandler.handle(repo, message);
        assertThat(resp, is(FAIL));
    }

    @Test
    public void shouldReturnOKResponseIfPackageIndexed() {
        when(repo.packageInstalled("git")).thenReturn(true);

        String message = "QUERY|git|";
        ResponseCode resp = MessageHandler.handle(repo, message);
        assertThat(resp, is(OK));
    }

    @Test
    public void shouldReturnFailResponseIfPackageRemovalFails() {
        when(repo.removePackage("git")).thenReturn(false);

        String message = "REMOVE|git|";
        ResponseCode resp = MessageHandler.handle(repo, message);
        assertThat(resp, is(FAIL));
    }

    @Test
    public void shouldReturnOKResponseIfPackageRemovalSuccessful() {
        when(repo.removePackage("git")).thenReturn(true);

        String message = "REMOVE|git|";
        ResponseCode resp = MessageHandler.handle(repo, message);
        assertThat(resp, is(OK));
    }

    @Test
    public void shouldReturnFailResponseIfPackageIndexFails() {
        when(repo.addPackage("git", EMPTY_LIST)).thenReturn(false);

        String message = "INDEX|git|";
        ResponseCode resp = MessageHandler.handle(repo, message);
        assertThat(resp, is(FAIL));
    }

    @Test
    public void shouldReturnOKResponseIfPackageIndexSuccessful() {
        when(repo.addPackage("git", asList("cat"))).thenReturn(true);

        String message = "INDEX|git|cat";
        ResponseCode resp = MessageHandler.handle(repo, message);
        assertThat(resp, is(OK));
    }

}
