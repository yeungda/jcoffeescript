require 'java'
puts org.jcoffeescript.JCoffeeScriptCompiler.new.compile(ARGV[0], false)
