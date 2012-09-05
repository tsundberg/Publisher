package se.somath.publisher.includer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Inspired from http://mama.indstate.edu/users/ice/tree/
 */
public class FileTreeIncluderTest {
    private File root;

    @Before
    public void createTestDirectory() throws IOException {
        String rootName = "./target/test-directory-to-be-deleted/my-app";
        root = new File(rootName);
        FileUtils.forceMkdir(root);
        FileUtils.touch(new File(rootName + "/pom.xml"));
        FileUtils.touch(new File(rootName + "/readme.txt"));
        FileUtils.touch(new File(rootName + "/src/main/java/com/foo/Bap.java"));
        FileUtils.touch(new File(rootName + "/src/main/java/com/foo/Bapa.java"));
        FileUtils.touch(new File(rootName + "/src/main/java/com/mc/App.java"));
        FileUtils.touch(new File(rootName + "/src/main/resources/app.properties"));
        FileUtils.touch(new File(rootName + "/src/test/java/com/mc/AppTest.java"));
        FileUtils.forceMkdir(new File(rootName + "/target/classes"));
    }

    @After
    public void clean() throws IOException {
        FileUtils.deleteDirectory(root.getParentFile());
    }

    @Test
    public void shouldBuildAFileLayoutTreeWithOutFile() {
        File givenRoot = new File("/undefined");

        List<String> expected = new LinkedList<String>();
        expected.add("<pre>");
        expected.add("No files found");
        expected.add("</pre>");

        FileTreeBuilder fileTreeBuilder = new FileTreeBuilder();
        List<String> actual = fileTreeBuilder.buildFileTree(givenRoot);

        assertThat(actual, is(expected));
    }

    @Test
    public void shouldBuildFileTreeWithDefaultFileFiltering() throws IOException {
        List<String> expected = new LinkedList<String>();
        expected.add("<pre>");
        expected.add("my-app");
        expected.add("|-- pom.xml");
        expected.add("|-- readme.txt");
        expected.add("`── src");
        expected.add("    |── main");
        expected.add("    │   |── java");
        expected.add("    │   │   `── com");
        expected.add("    │   │       |── foo");
        expected.add("    │   │       │   |── Bap.java");
        expected.add("    │   │       │   `── Bapa.java");
        expected.add("    │   │       `── mc");
        expected.add("    │   │           `── App.java");
        expected.add("    │   `── resources");
        expected.add("    │       `── app.properties");
        expected.add("    `── test");
        expected.add("        `── java");
        expected.add("            `── com");
        expected.add("                `── mc");
        expected.add("                    `── AppTest.java");
        expected.add("</pre>");

        FileTreeBuilder fileTreeBuilder = new FileTreeBuilder();
        List<String> actual = fileTreeBuilder.buildFileTree(root);

        assertThat(actual, is(expected));
    }

    @Test
    public void shouldBuildFileTreeWithoutFileFiltering() throws IOException {
        List<String> expected = new LinkedList<String>();
        expected.add("<pre>");
        expected.add("my-app");
        expected.add("|-- pom.xml");
        expected.add("|-- readme.txt");
        expected.add("|── src");
        expected.add("│   |── main");
        expected.add("│   │   |── java");
        expected.add("│   │   │   `── com");
        expected.add("│   │   │       |── foo");
        expected.add("│   │   │       │   |── Bap.java");
        expected.add("│   │   │       │   `── Bapa.java");
        expected.add("│   │   │       `── mc");
        expected.add("│   │   │           `── App.java");
        expected.add("│   │   `── resources");
        expected.add("│   │       `── app.properties");
        expected.add("│   `── test");
        expected.add("│       `── java");
        expected.add("│           `── com");
        expected.add("│               `── mc");
        expected.add("│                   `── AppTest.java");
        expected.add("`── target");
        expected.add("    `── classes");
        expected.add("</pre>");

        FileTreeBuilder fileTreeBuilder = new FileTreeBuilder();
        List<String> actual = fileTreeBuilder.buildFileTree(root, TrueFileFilter.INSTANCE);

        assertThat(actual, is(expected));
    }

    @Test
    @Ignore
    public void shouldCallReadFileWithIncludeOnOneLine() {
        List<String> given = new LinkedList<String>();
        given.add("<include-file-tree root=\"fileRoot\"/>");

        FileTreeBuilder fileTreeBuilder = mock(FileTreeBuilder.class);

        File root = new File("fileRoot");

        verify(fileTreeBuilder).buildFileTree(root);
    }

    @Test
    @Ignore
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
    @Ignore
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
    @Ignore
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
        assertThat(actual, CoreMatchers.is(given));
    }

    @Test
    @Ignore
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

        assertFileName(actual, "pom.xml");
        assertPreTagStart(actual, 5);
        assertProjectRow(actual, 7);
        assertPreTagEnd(actual);
    }

    @Test
    @Ignore
    public void shouldIncludeFileAndUseFileDisplayName() {
        List<String> given = new LinkedList<String>();
        given.add("<p>");
        given.add("    And the result is:");
        given.add("</p>");
        given.add("");
        given.add("<include root=\"./model\"");
        given.add("         file=\"pom.xml\"");
        given.add("         fileDisplayName=\"fileDisplayName.xml\"/>");
        given.add("");
        given.add("<h2>Pre and code</h2>");

        SourceCodeReader sourceCodeReader = new SourceCodeReader();
        SourceCodeIncluder includer = getSourceCodeIncluder(sourceCodeReader);

        List<String> actual = includer.addIncludes(given);

        assertFileName(actual, "fileDisplayName.xml");
        assertPreTagStart(actual, 5);
        assertProjectRow(actual, 7);
        assertPreTagEnd(actual);
    }

    @Test
    @Ignore
    public void shouldIncludeFileAndNotDisplayFileName() {
        List<String> given = new LinkedList<String>();
        given.add("<p>");
        given.add("    And the result is:");
        given.add("</p>");
        given.add("");
        given.add("<include root=\"./model\"");
        given.add("         file=\"pom.xml\"");
        given.add("         displayFileName=\"false\"/>");
        given.add("");
        given.add("<h2>Pre and code</h2>");

        SourceCodeReader sourceCodeReader = new SourceCodeReader();
        SourceCodeIncluder includer = getSourceCodeIncluder(sourceCodeReader);

        List<String> actual = includer.addIncludes(given);

        assertNoFileName(actual);
        assertPreTagStart(actual, 4);
        assertProjectRow(actual, 6);
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

    private SourceCodeIncluder getSourceCodeIncluder(SourceCodeReader sourceCodeReader) {
        SourceCodeIncluder includer = new SourceCodeIncluder();
        includer.setSourceCodeReader(sourceCodeReader);

        IncludeSourceCodeParser includeParser = new IncludeSourceCodeParser();
        includer.setIncludeSourceCodeParser(includeParser);
        return includer;
    }
}