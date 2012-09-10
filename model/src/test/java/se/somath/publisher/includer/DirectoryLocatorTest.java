package se.somath.publisher.includer;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.somath.publisher.excpetion.DirectoryNotFoundException;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DirectoryLocatorTest {
    private File testDirectory;

    @Before
    public void prepareTestDirectory() {
        testDirectory = new File("target/directoryLocationTest");
        assertTrue(testDirectory.mkdirs());
    }

    @After
    public void cleanTestDirectory() throws IOException {
        FileUtils.deleteDirectory(testDirectory);
    }

    @Test
    public void shouldLocateRootDirectory() {
        String workingDirName = testDirectory.getPath() + File.separator + "/root/sub";
        File workingDirectory = new File(workingDirName);
        assertTrue(workingDirectory.mkdirs());

        DirectoryLocator directoryLocator = new DirectoryLocator();
        File expected = workingDirectory.getParentFile();

        File actual = directoryLocator.locateDirectory("./root", workingDirectory);

        assertThat(actual, is(expected));
    }

    @Test(expected = DirectoryNotFoundException.class)
    public void shouldNotLocateRootDirectoryFromDefaultDirectory() {
        DirectoryLocator directoryLocator = new DirectoryLocator();
        directoryLocator.locateDirectory("./root");
    }

    @Test(expected = DirectoryNotFoundException.class)
    public void shouldNotLocateRootDirectory() {
        String workingDirName = testDirectory.getPath() + File.separator + "/root/sub";
        File workingDirectory = new File(workingDirName);
        assertTrue(workingDirectory.mkdirs());
        DirectoryLocator directoryLocator = new DirectoryLocator();

        directoryLocator.locateDirectory("./undefined", workingDirectory);
    }
}
