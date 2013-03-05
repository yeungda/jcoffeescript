# JCoffeeScript

JCoffeeScript is a java library that compiles CoffeeScript 1.6.1.

### Usage
from the command prompt:
>    echo "a = 1" | java -jar jcoffeescript-1.6.1.jar
<pre>
      (function() {
          var a;
          a = 1;
       })();
</pre>

####Command Line (unix/windows): 
>    java -jar jcoffeescript-1.6.1.jar < foo.coffee > foo.js

####command line options:  
>    __--bare__   - compile the javascript without top-level function safety wrapper.  

####From java:
>    String javascript = new org.jcoffeescript.JCoffeeScriptCompiler().compile("a = 1");

####From jruby:
<code>
>     if "java" == RUBY_PLATFORM then
       # use jcoffeescript implementation
       require 'java'
       class CoffeeScriptCompiler
            def initialize
                @compiler = org.jcoffeescript.JCoffeeScriptCompiler.new
            end
            def compile(source)
                @compiler.compile(source)
            end
        end
    else
        # use shell out to coffee implementation
        require 'open3'
        class CoffeeScriptCompiler
            def compile(source)
                return Open3.popen3('coffee --eval --print') do |stdin, stdout, stderr|
                  stdin.puts source
                  stdin.close
                  stdout.read
                end
            end
        end
    end
    compiler = CoffeeScriptCompiler.new
    compiler.compile('a = 1')
</code>
#### Thanks
Thanks to Jeremy Ashkenas and all contributors to the coffeescript project.    
Thanks to Raphael Speyer for helping with the design.  
Thanks to Daniel Cassidy for putting a lot of work into the code.  
Thanks to PandaWood for maintaining the code.  
[JCoffeeScript Homepage](http://yeungda.github.com/jcoffeescript)
