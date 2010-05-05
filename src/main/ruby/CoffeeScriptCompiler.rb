# Copyright 2010 David Yeung
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

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


