package se.somath.publisher.includer;

import org.junit.Test;
import se.somath.publisher.parser.IncludeSourceCodeParser;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class IncludeSourceCodeParserTest {

    @Test
    public void shouldParseRootAndFileName() {
        IncludeSourceCodeParser parser = new IncludeSourceCodeParser();

        String expectedRoot = "fileRoot";
        String expectedFileName = "fileRelativeTheRoot";
        String given = "<include-source-code root=\"" + expectedRoot + "\" file=\"" + expectedFileName + "\"/>";

        parser.parse(given);

        String actualRoot = parser.getRoot();
        assertThat(actualRoot, is(expectedRoot));

        String actualFileName = parser.getFileName();
        assertThat(actualFileName, is(expectedFileName));
    }

    @Test
    public void shouldParseFileDisplayName() {
        IncludeSourceCodeParser parser = new IncludeSourceCodeParser();

        String expectedFileDisplayName = "fileDisplayName";
        String given = "<include-source-code fileDisplayName=\"" + expectedFileDisplayName + "\"/>";

        parser.parse(given);

        String actualFileDisplayName = parser.getFileDisplayName();
        assertThat(actualFileDisplayName, is(expectedFileDisplayName));
    }

    @Test
    public void shouldNotDisplayFileNameDefault() {
        IncludeSourceCodeParser parser = new IncludeSourceCodeParser();

        boolean expectedShouldDisplayFileName = true;
        String given = "<include-source-code />";

        parser.parse(given);

        boolean actualShouldDisplayFileName = parser.shouldDisplayFileName();
        assertThat(actualShouldDisplayFileName, is(expectedShouldDisplayFileName));
    }

    @Test
    public void shouldParseDisplayFileName() {
        IncludeSourceCodeParser parser = new IncludeSourceCodeParser();

        boolean expectedShouldDisplayFileName = false;
        String given = "<include-source-code displayFileName=\"false\"/>";

        parser.parse(given);

        boolean actualShouldDisplayFileName = parser.shouldDisplayFileName();
        assertThat(actualShouldDisplayFileName, is(expectedShouldDisplayFileName));
    }
}
