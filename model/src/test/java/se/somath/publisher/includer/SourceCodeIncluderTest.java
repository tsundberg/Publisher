package se.somath.publisher.includer;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SourceCodeIncluderTest {

    @Test
    public void shouldIncludeAFile() {
        List<String> given = new LinkedList<String>();

        given.add("<include root=\"fileRoot\" file=\"fileRelativeTheRoot\"/>");

        SourceCodeIncluder includer = new SourceCodeIncluder();
        SourceCodeReader sourceCodeReader = mock(SourceCodeReader.class);
        includer.setSourceCodeReader(sourceCodeReader);

        IncludeParser includeParser = new IncludeParser();
        includer.setIncludeParser(includeParser);

        includer.getIncludes(given);

        verify(sourceCodeReader).readFile("fileRoot", "fileRelativeTheRoot");
    }


}
