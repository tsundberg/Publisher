package se.somath.publisher.includer;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SourceCodeIncluderTest {

    @Test
    public void shouldCallReadFileWithIncludeOnOneLine() {
        List<String> given = new LinkedList<String>();

        given.add("<include root=\"fileRoot\" file=\"fileRelativeTheRoot\"/>");

        SourceCodeReader sourceCodeReader = mock(SourceCodeReader.class);
        SourceCodeIncluder includer = getSourceCodeIncluder(sourceCodeReader);

        includer.addIncludes(given);

        verify(sourceCodeReader).readFile("fileRoot", "fileRelativeTheRoot");
    }

    @Test
    public void shouldCallReadFileWithIncludeOnTwoLines() {
        List<String> given = new LinkedList<String>();

        given.add("<include root=\"fileRoot\" ");
        given.add("file=\"fileRelativeTheRoot\"/>");

        SourceCodeReader sourceCodeReader = mock(SourceCodeReader.class);
        SourceCodeIncluder includer = getSourceCodeIncluder(sourceCodeReader);

        includer.addIncludes(given);

        verify(sourceCodeReader).readFile("fileRoot", "fileRelativeTheRoot");
    }

    @Test
    public void shouldCallReadFileWithIncludeOnThreeLines() {
        List<String> given = new LinkedList<String>();

        given.add("<include ");
        given.add("root=\"fileRoot\" ");
        given.add("file=\"fileRelativeTheRoot\"/>");

        SourceCodeReader sourceCodeReader = mock(SourceCodeReader.class);
        SourceCodeIncluder includer = getSourceCodeIncluder(sourceCodeReader);

        includer.addIncludes(given);

        verify(sourceCodeReader).readFile("fileRoot", "fileRelativeTheRoot");
    }

    @Test
    public void shouldIgnoreIncludesInComments() {
        List<String> given = new LinkedList<String>();

        given.add("<!--");
        given.add("<include ");
        given.add("root=\"fileRoot\" ");
        given.add("file=\"fileRelativeTheRoot\"/>");
        given.add("-->");

        SourceCodeReader sourceCodeReader = mock(SourceCodeReader.class);
        SourceCodeIncluder includer = getSourceCodeIncluder(sourceCodeReader);

        List<String> actual = includer.addIncludes(given);

        verify(sourceCodeReader, never()).readFile("fileRoot", "fileRelativeTheRoot");
        assertThat(actual, is(given));
    }

    @Test
    public void shouldIncludeFile() {
        List<String> given = new LinkedList<String>();
        given.add("<p>");
        given.add("    And the result is:");
        given.add("</p>");
        given.add("");
        given.add("<include root=\"./model\"");
        given.add("         file=\"pom.xml\"/>");
        given.add("");
        given.add("<h2>Pre and code</h2>");

        SourceCodeReader sourceCodeReader = new SourceCodeReader();
        SourceCodeIncluder includer = getSourceCodeIncluder(sourceCodeReader);

        List<String> actual = includer.addIncludes(given);

        int projectRow = 5;
        String actualLine = actual.get(projectRow);
        assertTrue("Expect to find the beginning of a pom, but found: \n" + actualLine, actualLine.contains("project"));
    }


    private SourceCodeIncluder getSourceCodeIncluder(SourceCodeReader sourceCodeReader) {
        SourceCodeIncluder includer = new SourceCodeIncluder();
        includer.setSourceCodeReader(sourceCodeReader);

        IncludeParser includeParser = new IncludeParser();
        includer.setIncludeParser(includeParser);
        return includer;
    }
}
