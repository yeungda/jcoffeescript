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

import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class CoffeeScriptCompilerTest {
    @Test
    public void shouldCompileWithDefaultOptions() throws JCoffeeScriptCompileException {
        assertThat(compiling("a = 1"),
                allOf(
                        containsString("a = 1"),
                        containsFunctionWrapper()
                )
        );
    }

    @Test
    public void shouldCompileWithoutFunctionWrapper() throws JCoffeeScriptCompileException {
        assertThat(compiling("a = 1", "--bare"), not(containsFunctionWrapper()));
    }

    private Matcher<String> containsFunctionWrapper() {
        return allOf(startsWith("(function() {\n"), endsWith("\n}).call(this);\n"));
    }

    private String compiling(String coffeeScriptSource, String... options) throws JCoffeeScriptCompileException {
        return new JCoffeeScriptCompiler(options).compile(coffeeScriptSource);
    }
}
