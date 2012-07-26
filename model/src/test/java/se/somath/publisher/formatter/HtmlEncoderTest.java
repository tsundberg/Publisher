package se.somath.publisher.formatter;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HtmlEncoderTest {

    @Test
    public void shouldHtmlEncode() {
        HtmlEncoder encoder = new HtmlEncoder();
        List<String> given = new LinkedList<String>();
        given.add("&");
        given.add("<");
        given.add(">");
        given.add("'");
        given.add("\"");

        List<String> expected = new LinkedList<String>();
        expected.add("&amp;");
        expected.add("&lt;");
        expected.add("&gt;");
        expected.add("&apos;");
        expected.add("&quot;");

        List<String> actual = encoder.encode(given);

        assertThat(actual, is(expected));
    }
}
