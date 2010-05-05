package org.jcoffeescript;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Main {

    public static void main(String[] args) {
        new Main().execute(System.out, System.in);
    }

    public void execute(PrintStream out, InputStream in) {
        final InputStreamReader streamReader = new InputStreamReader(in);
        final StringBuilder builder = new StringBuilder();
        try {
            while (streamReader.ready()) {
                builder.append((char) streamReader.read());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        out.print(new JCoffeeScriptCompiler().compile(builder.toString()));
    }
}
