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
import java.util.LinkedList;
import java.util.Collection;

public class Main {

    public static void main(String[] args) throws IOException {
        new Main().execute(args, System.out, System.in);
    }

    public void execute(String[] args, PrintStream out, InputStream in) throws IOException {
        final int BUFFER_OFFSET = 0;
        final int BUFFER_SIZE = 262144;

        InputStreamReader streamReader = new InputStreamReader(in);
        try {
            StringBuilder builder = new StringBuilder(BUFFER_SIZE);
            char[] buffer = new char[BUFFER_SIZE];
            int numCharsRead = streamReader.read(buffer, BUFFER_OFFSET, BUFFER_SIZE);
            while (numCharsRead >= 0) {
                builder.append(buffer, BUFFER_OFFSET, numCharsRead);
                numCharsRead = streamReader.read(buffer, BUFFER_OFFSET, BUFFER_SIZE);
            }
            
            Collection<Option> options = new LinkedList<Option>();

            if (args.length == 1 && args[0].equals("NO_WRAP")) {
                options.add(Option.NO_WRAP);
            }

            JCoffeeScriptCompiler compiler = new JCoffeeScriptCompiler(options);
            out.print(compiler.compile(builder.toString()));
            
        } catch (JCoffeeScriptCompileException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } finally {
            streamReader.close();
        }
    }
}
