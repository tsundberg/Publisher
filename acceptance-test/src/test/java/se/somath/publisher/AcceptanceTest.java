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
        String fileName = "target/index.html";

        return findFile(htmlFiles, fileName);
    }

    private String getExpectedFileName() {
        Collection<File> htmlFiles = getAllHtmlFiles();
        String fileName = "resources/expected-index.html";

        return findFile(htmlFiles, fileName);
    }

    private Collection<File> getAllHtmlFiles() {
        File currentDir = new File(".");
        String[] extensions = {"html"};
        boolean recursive = true;
        return FileUtils.listFiles(currentDir, extensions, recursive);
    }

    private String findFile(Collection<File> htmlFiles, String fileNameEnding) {
        String locatedFileName = null;
        for (File candidateFile : htmlFiles) {
            if (candidateFile.getAbsolutePath().endsWith(fileNameEnding)) {
                locatedFileName = candidateFile.getAbsolutePath();
            }
        }
        return locatedFileName;
    }

    private void assertFileContentLineForLine(String expectedFile, String actualFile) throws FileNotFoundException {
        Reader actualReader = new FileReader(actualFile);
        Reader expectedReader = new FileReader(expectedFile);

        LineIterator actual = new LineIterator(actualReader);
        LineIterator expected = new LineIterator(expectedReader);

        int row = 0;
        while (actual.hasNext()) {
            row++;
            String errorMessage = "\nsource file: " + expectedFile + " \nline: " + row;
            assertThat(errorMessage, actual.nextLine(), is(expected.nextLine()));
        }
        assertFalse("The actual and target files has different length", expected.hasNext());
    }
}