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
