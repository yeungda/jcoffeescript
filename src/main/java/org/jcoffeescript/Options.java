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

import java.util.LinkedList;
import java.util.Collection;
import java.util.Iterator;

public class Options {
    private Collection<String> javascriptOptions = new LinkedList<String>();
    public String coffeescriptLibrary = null;

    public Options( String[] args ) throws JCoffeeScriptCompileException {
        // Set the options that we know about as members of the Options class.
        // Assume all other options are meant for coffeescript.
        for( int i = 0; i < args.length; i++ ){
            if ( args[i].equals("--coffeescriptjs") ) {
                if ( i == args.length - 1 ) {
                    throw new JCoffeeScriptCompileException(
                        "--coffeescriptjs requires an argument.");
                }
                coffeescriptLibrary = args[++i];
            } else if ( args[i].indexOf("--") == 0 ) {
                javascriptOptions.add( args[i].substring(2) );
            }
        }
    }

    public String toJavaScript() {
        String json = "{";
        int count = 0;
        Iterator<String> iter = javascriptOptions.iterator();

        while( iter.hasNext() ) {
            String arg = iter.next();

            if ( count++ > 0 ) {
                json += ", " + arg + ": true";
            } else {
                json += arg + ": true";
            }
        }

        return json + "}";
    }
}
