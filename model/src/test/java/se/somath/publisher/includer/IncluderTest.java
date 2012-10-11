package se.somath.publisher.includer;

import org.junit.Test;
import se.somath.publisher.builder.SourceCodeBuilder;
import se.somath.publisher.parser.IncludeSourceCodeParser;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class IncluderTest {

    @Test
    public void shouldCallReadFileWithIncludeOnOneLine() {
        List<String> given = new LinkedList<String>();

        given.add("<include-source-code root=\"fileRoot\" file=\"fileRelativeTheRoot\"/>");

        SourceCodeReader sourceCodeReader = mock(SourceCodeReader.class);
        Includer includer = getSourceCodeIncluder(sourceCodeReader);

        includer.addIncludes(given);

        verify(sourceCodeReader).readFile("fileRoot", "fileRelativeTheRoot");
    }

    @Test
    public void shouldCallReadFileWithIncludeOnTwoLines() {
        List<String> given = new LinkedList<String>();

        given.add("<include-source-code root=\"fileRoot\" ");
        given.add("file=\"fileRelativeTheRoot\"/>");

        SourceCodeReader sourceCodeReader = mock(SourceCodeReader.class);
        Includer includer = getSourceCodeIncluder(sourceCodeReader);

        includer.addIncludes(given);

        verify(sourceCodeReader).readFile("fileRoot", "fileRelativeTheRoot");
    }

    @Test
    public void shouldCallReadFileWithIncludeOnThreeLines() {
        List<String> given = new LinkedList<String>();

        given.add("<include-source-code ");
        given.add("root=\"fileRoot\" ");
        given.add("file=\"fileRelativeTheRoot\"/>");

        SourceCodeReader sourceCodeReader = mock(SourceCodeReader.class);
        Includer includer = getSourceCodeIncluder(sourceCodeReader);

        includer.addIncludes(given);

        verify(sourceCodeReader).readFile("fileRoot", "fileRelativeTheRoot");
    }

    @Test
    public void shouldIgnoreIncludesInComments() {
        List<String> given = new LinkedList<String>();

        given.add("<!--");
        given.add("<include-source-code ");
        given.add("root=\"fileRoot\" ");
        given.add("file=\"fileRelativeTheRoot\"/>");
        given.add("-->");

        SourceCodeReader sourceCodeReader = mock(SourceCodeReader.class);
        Includer includer = getSourceCodeIncluder(sourceCodeReader);

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
        given.add("<include-source-code root=\"./model\"");
        given.add("         file=\"pom.xml\"/>");
        given.add("");
        given.add("<h2>Pre and code</h2>");

        SourceCodeReader sourceCodeReader = new SourceCodeReader();
        Includer includer = getSourceCodeIncluder(sourceCodeReader);

        List<String> actual = includer.addIncludes(given);

        assertFileName(actual, "pom.xml");
        assertPreTagStart(actual, 5);
        assertProjectRow(actual, 8);
        assertPreTagEnd(actual);
    }

    @Test
    public void shouldIncludeFileAndUseFileDisplayName() {
        List<String> given = new LinkedList<String>();
        given.add("<p>");
        given.add("    And the result is:");
        given.add("</p>");
        given.add("");
        given.add("<include-source-code root=\"./model\"");
        given.add("         file=\"pom.xml\"");
        given.add("         fileDisplayName=\"fileDisplayName.xml\"/>");
        given.add("");
        given.add("<h2>Pre and code</h2>");

        SourceCodeReader sourceCodeReader = new SourceCodeReader();
        Includer includer = getSourceCodeIncluder(sourceCodeReader);

        List<String> actual = includer.addIncludes(given);

        assertFileName(actual, "fileDisplayName.xml");
        assertPreTagStart(actual, 5);
        assertProjectRow(actual, 8);
        assertPreTagEnd(actual);
    }

    @Test
    public void shouldIncludeFileAndNotDisplayFileName() {
        List<String> given = new LinkedList<String>();
        given.add("<p>");
        given.add("    And the result is:");
        given.add("</p>");
        given.add("");
        given.add("<include-source-code root=\"./model\"");
        given.add("         file=\"pom.xml\"");
        given.add("         displayFileName=\"false\"/>");
        given.add("");
        given.add("<h2>Pre and code</h2>");

        SourceCodeReader sourceCodeReader = new SourceCodeReader();
        Includer includer = getSourceCodeIncluder(sourceCodeReader);

        List<String> actual = includer.addIncludes(given);

        assertNoFileName(actual);
        assertPreTagStart(actual, 4);
        assertProjectRow(actual, 7);
        assertPreTagEnd(actual);
    }

    @Test
    public void shouldIncludeMethodAndNotDisplayFileName() {
        List<String> given = new LinkedList<String>();
        given.add("<p>");
        given.add("    And the result is:");
        given.add("</p>");
        given.add("");
        given.add("<include-source-code root=\"./model\"");
        given.add("         file=\"/src/test/resources/se/somath/publisher/Example.java\"");
        given.add("         method=\"public void shouldBeIncluded()\"");
        given.add("         displayFileName=\"false\"/>");
        given.add("");
        given.add("<h2>Pre and code</h2>");

        List<String> expectedLines = new LinkedList<String>();
        expectedLines.add("@Test");
        expectedLines.add("public void shouldBeIncluded() {");
        expectedLines.add("    assertTrue(true);");
        expectedLines.add("}");

        SourceCodeReader sourceCodeReader = new SourceCodeReader();
        Includer includer = getSourceCodeIncluder(sourceCodeReader);

        List<String> actual = includer.addIncludes(given);

        assertNoFileName(actual);
        assertPreTagStart(actual, 4);
        assertIncludedMethod(actual, expectedLines);
        assertPreTagEnd(actual);
    }

    @Test
    public void shouldIncludeMethodWithEmptyLine() {
        List<String> given = new LinkedList<String>();
        given.add("<p>");
        given.add("    And the result is:");
        given.add("</p>");
        given.add("");
        given.add("<include-source-code root=\"./model\"");
        given.add("         file=\"/src/test/resources/se/somath/publisher/Example.java\"");
        given.add("         method=\"public void shouldAlsoBeIncluded()\"");
        given.add("         displayFileName=\"false\"/>");
        given.add("");
        given.add("<h2>Pre and code</h2>");

        List<String> expectedLines = new LinkedList<String>();
        expectedLines.add("@Test");
        expectedLines.add("public void shouldAlsoBeIncluded() {");
        expectedLines.add("    assertTrue(true);");
        expectedLines.add("");
        expectedLines.add("    assertFalse(false);");
        expectedLines.add("}");

        SourceCodeReader sourceCodeReader = new SourceCodeReader();
        Includer includer = getSourceCodeIncluder(sourceCodeReader);

        List<String> actual = includer.addIncludes(given);

        assertNoFileName(actual);
        assertPreTagStart(actual, 4);
        assertIncludedMethod(actual, expectedLines);
        assertPreTagEnd(actual);
    }

    private void assertPreTagStart(List<String> actual, int preTagStartRow) {
        String actualLine = actual.get(preTagStartRow);
        assertTrue("Expect to find a pre tag end, but found: \n" + actualLine + "\n\n", actualLine.contains("<pre>"));
    }

    private void assertProjectRow(List<String> actual, int projectRow) {
        String actualLine = actual.get(projectRow);
        assertTrue("Expect to find the beginning of a pom, but found: \n" + actualLine + "\n\n", actualLine.contains("project"));
    }

    private void assertPreTagEnd(List<String> actual) {
        int preEndTagRowOffset = 3;
        int preTagEndRow = actual.size() - preEndTagRowOffset;
        String actualLine = actual.get(preTagEndRow);
        assertTrue("Expect to find a pre tag end, but found: \n" + actualLine + "\n\n", actualLine.contains("</pre>"));
    }

    private void assertIncludedMethod(List<String> actualLines, List<String> expectedLines) {
        int startingPoint = 6;

        for (int i = 0; i < expectedLines.size(); i++) {
            String expected = expectedLines.get(i);
            String actual = actualLines.get(i + startingPoint);

            assertThat(actual, is(expected));
        }
    }

    private void assertFileName(List<String> actual, String fileDisplayName) {
        int fileNameRow = 4;
        String actualLine = actual.get(fileNameRow);
        assertTrue("Expect to find the filename in italics, but found: \n" + actualLine + "\n\n", actualLine.contains("<p><i>" + fileDisplayName + "</i></p>"));
    }

    private void assertNoFileName(List<String> actual) {
        for (String actualLine : actual) {
            assertFalse("Expected not to find the filename in italics and found " + actualLine, actualLine.contains("<p><i>pom.xml</i></p>"));
        }
    }

    private Includer getSourceCodeIncluder(SourceCodeReader sourceCodeReader) {
        Includer includer = new Includer();

        SourceCodeBuilder sourceCodeBuilder = new SourceCodeBuilder();
        sourceCodeBuilder.setSourceCodeReader(sourceCodeReader);
        includer.setSourceCodeBuilder(sourceCodeBuilder);

        IncludeSourceCodeParser includeParser = new IncludeSourceCodeParser();
        includer.setIncludeSourceCodeParser(includeParser);
        return includer;
    }
}