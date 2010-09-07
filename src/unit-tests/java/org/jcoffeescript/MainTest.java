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

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.StringContains.containsString;

public class MainTest {

    @Test
    public void shouldCompileScriptPipedToInputStreamAndPrintToOutputStream() throws IOException {
        assertThat(compilingByPiping("a = 1"), containsString("a = 1;"));
    }

    @Test
    public void shouldWrapWhenNoArgs() {
        assertThat(compilingByPiping("a = 1"), startsWith("(function() {"));
    }

    @Test
    public void shouldNotWrapWhenNoWrapArgsSupplied() throws IOException {
        assertThat(compilingByPiping("a = 1", "--no-wrap"), not(startsWith("(function() {")));
    }

    private String compilingByPiping(String input, String... args) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        new Main().execute(args, printStream, new ByteArrayInputStream(input.getBytes()));
        printStream.close();
        return byteArrayOutputStream.toString();
    }
}