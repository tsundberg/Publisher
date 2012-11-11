package se.somath.publisher.builder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.somath.publisher.excpetion.DirectoryNotFoundException;
import se.somath.publisher.excpetion.TooManyFilesFoundException;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Inspired from http://mama.indstate.edu/users/ice/tree/
 */
public class FileTreeBuilderTest {
    private String rootName;
    private File root;

    @Before
    public void createTestDirectory() throws IOException {
        rootName = "./target/test-directory-to-be-deleted/my-app";
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

    @Test(expected = DirectoryNotFoundException.class)
    public void shouldBuildAFileLayoutTreeWithOutFile() {
        String givenRoot = "/undefined";
        boolean onlyDirs = false;

        FileTreeBuilder fileTreeBuilder = new FileTreeBuilder();
        fileTreeBuilder.buildFileTree(givenRoot, onlyDirs);
    }

    @Test
    public void shouldBuildFileTreeWithDefaultFileFiltering() throws IOException {
        List<String> expected = new LinkedList<String>();
        expected.add("<pre>");
        expected.add("my-app");
        expected.add("|-- pom.xml");
        expected.add("|-- readme.txt");
        expected.add("`-- src");
        expected.add("    |-- main");
        expected.add("    |   |-- java");
        expected.add("    |   |   `-- com");
        expected.add("    |   |       |-- foo");
        expected.add("    |   |       |   |-- Bap.java");
        expected.add("    |   |       |   `-- Bapa.java");
        expected.add("    |   |       `-- mc");
        expected.add("    |   |           `-- App.java");
        expected.add("    |   `-- resources");
        expected.add("    |       `-- app.properties");
        expected.add("    `-- test");
        expected.add("        `-- java");
        expected.add("            `-- com");
        expected.add("                `-- mc");
        expected.add("                    `-- AppTest.java");
        expected.add("</pre>");

        boolean onlyDirs = false;
        FileTreeBuilder fileTreeBuilder = new FileTreeBuilder();
        List<String> actual = fileTreeBuilder.buildFileTree(rootName, onlyDirs);

        assertThat(actual, is(expected));
    }

    @Test
    public void shouldBuildFileTreeWithoutFileFiltering() throws IOException {
        List<String> expected = new LinkedList<String>();
        expected.add("<pre>");
        expected.add("my-app");
        expected.add("|-- pom.xml");
        expected.add("|-- readme.txt");
        expected.add("|-- src");
        expected.add("|   |-- main");
        expected.add("|   |   |-- java");
        expected.add("|   |   |   `-- com");
        expected.add("|   |   |       |-- foo");
        expected.add("|   |   |       |   |-- Bap.java");
        expected.add("|   |   |       |   `-- Bapa.java");
        expected.add("|   |   |       `-- mc");
        expected.add("|   |   |           `-- App.java");
        expected.add("|   |   `-- resources");
        expected.add("|   |       `-- app.properties");
        expected.add("|   `-- test");
        expected.add("|       `-- java");
        expected.add("|           `-- com");
        expected.add("|               `-- mc");
        expected.add("|                   `-- AppTest.java");
        expected.add("`-- target");
        expected.add("    `-- classes");
        expected.add("</pre>");

        FileTreeBuilder fileTreeBuilder = new FileTreeBuilder();
        List<String> actual = fileTreeBuilder.buildFileTree(root, TrueFileFilter.INSTANCE);

        assertThat(actual, is(expected));
    }

    @Test(expected = TooManyFilesFoundException.class)
    public void shouldNotIncludeToManyFiles() throws IOException {
        FileTreeBuilder fileTreeBuilder = new FileTreeBuilder();
        String userHome = FileUtils.getUserDirectoryPath();
        boolean onlyDirs = false;

        fileTreeBuilder.buildFileTree(userHome, onlyDirs);
    }
}