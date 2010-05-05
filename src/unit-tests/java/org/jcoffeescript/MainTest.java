package org.jcoffeescript;

import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertThat;

public class MainTest {
    @Test
    public void shouldCompileScriptsPipedToInputStreamAndPrintToOutputStream() {
        assertThat(piping("a:1"), Matchers.containsString("a = 1"));
    }

    private String piping(String input) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(byteArrayOutputStream);
        new Main().execute(printStream, new ByteArrayInputStream(input.getBytes()));
        printStream.close();
        return byteArrayOutputStream.toString();
    }

}