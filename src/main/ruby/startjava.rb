if "java" == RUBY_PLATFORM then
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
puts(compiler.compile('a:1'))


