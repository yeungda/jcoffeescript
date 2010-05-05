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

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.tools.shell.Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class JCoffeeScriptCompiler {
    private Context context;
    private ScriptableObject global;
    private Script compileScript;

    public JCoffeeScriptCompiler() {
        context = Context.enter();
        context.setOptimizationLevel(-1);
        global = context.initStandardObjects();
        loadCoffeeScriptCompiler(context, global);
        compileScript = Main.loadScriptFromSource(context, "CoffeeScript.compile(this.coffeeScriptSource);", "compile command", 1, null);
    }

    private void loadCoffeeScriptCompiler(Context context, ScriptableObject global) {
        final StringBuilder source = new StringBuilder(300000);
        readCoffeeScriptJsInto(source);
        final Script script = Main.loadScriptFromSource(context, source.toString(), "<cmd>", 1, null);
        Main.evaluateScript(script, context, global);
    }

    public String compile(String coffeeScriptSource) {
        final Scriptable compilationContext = context.initStandardObjects(global);
        compilationContext.put("coffeeScriptSource", compilationContext, coffeeScriptSource);
        return (String) Main.evaluateScript(compileScript, context, global);
    }

    private void readCoffeeScriptJsInto(StringBuilder stringBuilder) {
        final InputStream stream = getClass().getClassLoader().getResourceAsStream("org/jcoffeescript/coffee-script.js");
        Reader reader = new InputStreamReader(stream);
        try {
            while (reader.ready()) {
                stringBuilder.append((char) reader.read());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
