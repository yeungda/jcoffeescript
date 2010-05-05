package org.jcoffeescript;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class CoffeeScriptCompilerTest {
    @Test
    public void shouldCompile() {
        assertThat(compiling("a: 1"), Matchers.containsString("a = 1"));
    }

    private String compiling(String coffeeScriptSource) {
        return new JCoffeeScriptCompiler().compile(coffeeScriptSource);
    }
}
