import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.tools.shell.Main;

import java.io.FileReader;
import java.io.IOException;

public class CoffeeScriptRunner {
    public static void main(String[] args) {
        final Context context = Context.enter();
        context.setOptimizationLevel(-1);
        final ScriptableObject theGlobal = context.initStandardObjects();
        
        final Object[] objects = {"/Users/dyeung/Projects/coffeescript-rhino/narwhal/engines/rhino/bin/narwhal-rhino", "-o", "/Users/dyeung/Projects/coffeescript-rhino/coffee-script/lib/test.js", "/Users/dyeung/Projects/coffeescript-rhino/coffee-script/lib/hello.coffee"};
        final ScriptableObject scriptableObject = context.initStandardObjects(theGlobal, false);
        final Scriptable argumentsArray = context.newArray(theGlobal, objects);
        theGlobal.defineProperty("arguments", argumentsArray, ScriptableObject.DONTENUM);
        String source = readFile("/Users/dyeung/Projects/coffeescript-rhino/narwhal/engines/rhino/bootstrap.js");
        final Script globals = Main.loadScriptFromSource(context, "NARWHAL_HOME='/Users/dyeung/Projects/coffeescript-rhino/narwhal'", "<cmd>", 1, null);
        Object o = Main.evaluateScript(globals, context, scriptableObject);

        final Script script = Main.loadScriptFromSource(context, source, "<cmd>", 1, null);
        o = Main.evaluateScript(script, context, scriptableObject);
        System.out.println("scriptableObject.toString() = " + scriptableObject.get("js"));
    }

    private static class CoffeeScriptRhinoOnlyRunner {
        public static void main(String[] args) {
        final Context context = Context.enter();
            final ScriptableObject theGlobal = context.initStandardObjects();
            final ScriptableObject scriptableObject = context.initStandardObjects(theGlobal, false);
            final Script globals = Main.loadScriptFromSource(context, "function require () {return {}}; window={}", "<cmd>", 1, null);
            Object o = Main.evaluateScript(globals, context, scriptableObject);

            evaluate(context, scriptableObject, "/Users/dyeung/Projects/coffeescript-rhino/coffee-script/lib/helpers.js");
            evaluate(context, scriptableObject, "/Users/dyeung/Projects/coffeescript-rhino/coffee-script/lib/nodes.js");
            evaluate(context, scriptableObject, "/Users/dyeung/Projects/coffeescript-rhino/coffee-script/lib/lexer.js");
            evaluate(context, scriptableObject, "/Users/dyeung/Projects/coffeescript-rhino/coffee-script/lib/parser.js");
            evaluate(context, scriptableObject, "/Users/dyeung/Projects/coffeescript-rhino/coffee-script/lib/coffee-script.js");
            System.out.println("scriptableObject.toString() = " + scriptableObject.get("js"));
        }

        private static void evaluate(Context context, ScriptableObject scriptableObject, String fileName) {
            Object o;
            String source = readFile(fileName);
            final Script script = Main.loadScriptFromSource(context, source, "<cmd>", 1, null);
            o = Main.evaluateScript(script, context, scriptableObject);
        }
    }
    private static String readFile(String fileName) {
        final StringBuffer buffer = new StringBuffer();
        try {
            final FileReader fileReader = new FileReader(fileName);
            while (fileReader.ready()) {
                buffer.append((char) fileReader.read());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return buffer.toString();
    }
}
