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

    @Test
    public void shouldParseFileDisplayName() {
        IncludeParser parser = new IncludeParser();

        String expectedFileDisplayName = "fileDisplayName";
        String given = "<include fileDisplayName=\"" + expectedFileDisplayName + "\"/>";

        parser.parse(given);

        String actualFileDisplayName = parser.getFileDisplayName();
        assertThat(actualFileDisplayName, is(expectedFileDisplayName));
    }

    @Test
    public void shouldNotDisplayFileNameDefault() {
        IncludeParser parser = new IncludeParser();

        boolean expectedShouldDisplayFileName = true;
        String given = "<include />";

        parser.parse(given);

        boolean actualShouldDisplayFileName = parser.shouldDisplayFileName();
        assertThat(actualShouldDisplayFileName, is(expectedShouldDisplayFileName));
    }

    @Test
    public void shouldParseDisplayFileName() {
        IncludeParser parser = new IncludeParser();

        boolean expectedShouldDisplayFileName = false;
        String given = "<include displayFileName=\"false\"/>";

        parser.parse(given);

        boolean actualShouldDisplayFileName = parser.shouldDisplayFileName();
        assertThat(actualShouldDisplayFileName, is(expectedShouldDisplayFileName));
    }
}
