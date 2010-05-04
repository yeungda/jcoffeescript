if "java" == RUBY_PLATFORM then
    require 'java'
    class CoffeeScriptRunner

        def initialize
          @main = org.mozilla.javascript.tools.shell.Main
          @context = org.mozilla.javascript.Context.enter()
          @context.setOptimizationLevel(-1)
          @globals = @main.loadScriptFromSource(@context, "NARWHAL_HOME='/Users/dyeung/Projects/coffeescript-rhino/narwhal'", "<cmd>", 1, nil)
          source = File.read("/Users/dyeung/Projects/coffeescript-rhino/narwhal/engines/rhino/bootstrap.js")
          @script = @main.loadScriptFromSource(@context, source, "<cmd>", 1, nil)
        end

        def compile(file)
          theGlobal = @context.initStandardObjects();
          objects = ["/Users/dyeung/Projects/coffeescript-rhino/narwhal/engines/rhino/bin/narwhal-rhino", "-o", "/Users/dyeung/Projects/coffeescript-rhino/coffee-script/lib/test.js", file]
          scriptableObject = @context.initStandardObjects(theGlobal);
          argumentsArray = @context.newArray(theGlobal, objects.to_java);
          theGlobal.defineProperty("arguments", argumentsArray, 2);

          @main.evaluateScript(@globals, @context, scriptableObject)
          @main.evaluateScript(@script, @context, scriptableObject)
          scriptableObject.get("js")
        end
    end
    runner = CoffeeScriptRunner.new
    puts(runner.compile("/Users/dyeung/Projects/coffeescript-rhino/coffee-script/lib/hello.coffee"))
    puts(runner.compile("/Users/dyeung/Projects/coffeescript-rhino/coffee-script/lib/hello.coffee"))
end


