package se.somath.publisher.includer;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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

    private SourceCodeIncluder getSourceCodeIncluder(SourceCodeReader sourceCodeReader) {
        SourceCodeIncluder includer = new SourceCodeIncluder();
        includer.setSourceCodeReader(sourceCodeReader);

        IncludeParser includeParser = new IncludeParser();
        includer.setIncludeParser(includeParser);
        return includer;
    }
}
