package se.somath.publisher;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Example {

    @Test
    public void shouldBeIncluded() {
        assertTrue(true);
    }

    @Test
    public void shouldAlsoBeIncluded() {
        assertTrue(true);

        assertFalse(false);
    }
}
