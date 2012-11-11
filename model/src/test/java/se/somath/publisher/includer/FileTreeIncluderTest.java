package se.somath.publisher.includer;

import org.junit.Test;
import se.somath.publisher.builder.FileTreeBuilder;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FileTreeIncluderTest {

    @Test
    public void shouldCallReadFileWithIncludeOnOneLine() {
        List<String> given = new LinkedList<String>();
        boolean onlyDirs = false;

        given.add("<include-file-tree root=\"fileRoot\"/>");

        FileTreeBuilder fileTreeBuilder = mock(FileTreeBuilder.class);
        Includer includer = new Includer();
        includer.setFileTreeBuilder(fileTreeBuilder);

        includer.addIncludes(given);

        verify(fileTreeBuilder).buildFileTree("fileRoot", onlyDirs);
    }

    @Test
    public void shouldOnlyIncludeDirectories() {
        List<String> given = new LinkedList<String>();

        given.add("<include-file-tree root=\"model\"");
        given.add("                   only-dirs=\"true\"/>");

        FileTreeBuilder fileTreeBuilder = new FileTreeBuilder();
        Includer includer = new Includer();
        includer.setFileTreeBuilder(fileTreeBuilder);

        List<String> actual = includer.addIncludes(given);

        assertNoFiles(actual);

    }

    private void assertNoFiles(List<String> actual) {
        for (String candidate : actual) {
            if (candidate.endsWith(".java") || candidate.endsWith(".xml")) {
                fail("Found a file: " + candidate + " and did not expect to find any file");
            }
        }
    }
}
