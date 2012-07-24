package se.somath.publisher.includer;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class IncludeParserTest {

    @Test
    public void shouldParseRootAndFileName() {
        IncludeParser parser = new IncludeParser();

        String expectedRoot = "fileRoot";
        String expectedFileName = "fileRelativeTheRoot";
        String given = "<include root=\"" + expectedRoot + "\" file=\"" + expectedFileName + "\"/>";

        parser.parse(given);

        String actualRoot = parser.getRoot();
        assertThat(actualRoot, is(expectedRoot));

        String actualFileName = parser.getFileName();
        assertThat(actualFileName, is(expectedFileName));
    }
}
