package se.somath.publisher;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.junit.Test;

import java.io.*;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class AcceptanceTest {

    @Test
    public void shouldVerifyThatProcessedFileIsEqualToExpected() throws IOException {
        String targetFileName = getActualFileName();
        String expectedFileName = getExpectedFileName();

        assertFileContentLineForLine(expectedFileName, targetFileName);
    }

    private String getActualFileName() {
        Collection<File> htmlFiles = getAllHtmlFiles();
        String wantedFileName = "target/index.html";

        return findFile(htmlFiles, wantedFileName);
    }

    private String getExpectedFileName() {
        Collection<File> htmlFiles = getAllHtmlFiles();
        String wantedFileName = "resources/expected-index.html";

        return findFile(htmlFiles, wantedFileName);
    }

    private void assertFileContentLineForLine(String expectedFile, String actualFile) throws FileNotFoundException {
        Reader actualReader = new FileReader(actualFile);
        Reader expectedReader = new FileReader(expectedFile);

        LineIterator actualIterator = new LineIterator(actualReader);
        LineIterator expectedIterator = new LineIterator(expectedReader);

        assertEachLine(actualFile, actualIterator, expectedIterator);
        assertFalse("The actual and target files has different length", expectedIterator.hasNext());
    }

    private Collection<File> getAllHtmlFiles() {
        File currentDir = new File(".");
        String[] extensions = {"html"};
        boolean recursive = true;

        return FileUtils.listFiles(currentDir, extensions, recursive);
    }

    private String findFile(Collection<File> htmlFiles, String wantedFileName) {
        for (File candidateFile : htmlFiles) {
            if (candidateFile.getAbsolutePath().endsWith(wantedFileName)) {
                return candidateFile.getAbsolutePath();
            }
        }

        return "file not found";
    }

    private void assertEachLine(String actualFile, LineIterator actualIterator, LineIterator expectedIterator) {
        int lineNumber = 0;
        while (actualIterator.hasNext()) {
            lineNumber++;
            String errorMessage = "\nsource file: " + actualFile + " \nline number: " + lineNumber;
            String actual = actualIterator.nextLine().trim();
            String expected = expectedIterator.nextLine().trim();

            assertThat(errorMessage, actual, is(expected));
        }
    }
}