package parser;

import org.junit.Test;

import java.util.Collections;

import static java.util.Collections.EMPTY_LIST;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static parser.Command.INDEX;
import static parser.MessageParser.parseMessage;


public class MessageParserTest {


    @Test(expected = InvalidMessageFormatException.class)
    public void shouldThrowExceptionIfMessageContainsLessThan3Components() throws InvalidMessageFormatException {
        String message = "INDEX|git";
        parseMessage(message);
    }

    @Test(expected = InvalidMessageFormatException.class)
    public void shouldThrowExceptionIfMessageContainsMoreThan3Components() throws InvalidMessageFormatException {
        String message = "INDEX|git|dogs|";
        parseMessage(message);
    }

    @Test(expected = InvalidMessageFormatException.class)
    public void shouldThrowExceptionIfInvalidCommandReceived() throws InvalidMessageFormatException {
        String message = "SOMETHING|git|";
        parseMessage(message);
    }

    @Test(expected = InvalidMessageFormatException.class)
    public void shouldThrowExceptionIfPackageNameContainsInvalidChars() throws InvalidMessageFormatException {
        String message = "INDEX|ab$cd|";
        parseMessage(message);
    }

    @Test(expected = InvalidMessageFormatException.class)
    public void shouldThrowExceptionIfDependenciesContainInvalidChars() throws InvalidMessageFormatException {
        String message = "INDEX|git|asd:d,kl*d";
        parseMessage(message);
    }

    @Test
    public void shouldParseValidMessageWithDeps() throws InvalidMessageFormatException {
        String message = "INDEX|git|libc,libuv";
        Message msg = parseMessage(message);
        assertThat(msg.command, is(INDEX));
        assertThat(msg.packageName, is("git"));
        assertThat(msg.dependencies.toArray(), is(new String[]{"libc", "libuv"}));
    }

    @Test
    public void shouldParseValidMessageWithoutDeps() throws InvalidMessageFormatException {
        String message = "INDEX|avce00|";
        Message msg = parseMessage(message);
        assertThat(msg.command, is(INDEX));
        assertThat(msg.packageName, is("avce00"));
        assertThat(msg.dependencies, is(EMPTY_LIST));
    }

}
