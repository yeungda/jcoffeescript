/*
 * Copyright 2010 David Yeung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jcoffeescript;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.StringContains.containsString;

public class MainTest {

    static String[] argsArray = new String[1];

    @Before
    public void setUp() {
        argsArray[0] = "";
    }

    @Test
    public void ShouldCompileScript_PipedToInputStream_AndPrintToOutputStream() throws IOException {
        assertThat(piping("a = 1", argsArray), containsString("a = 1;"));
    }

    @Test
    public void ShouldWrap_When_NoArgs() throws IOException {
        assertThat(piping("a = 1", argsArray), startsWith("(function() {"));
    }

    @Test
    public void ShouldNotWrap_When_NoWrap_ArgsSupplied() throws IOException {
        argsArray[0] = "--no-wrap";
        assertThat(piping("a = 1", argsArray), not(startsWith("(function() {")));
    }

    private String piping(String input, String[] args) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        new Main().execute(args, printStream, new ByteArrayInputStream(input.getBytes()));
        printStream.close();
        return byteArrayOutputStream.toString();
    }
}