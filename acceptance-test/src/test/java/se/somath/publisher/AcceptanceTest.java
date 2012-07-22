package se.somath.publisher;

import org.apache.commons.io.LineIterator;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class AcceptanceTest {
    @Test
    public void shouldVerifyThatProcessedFileIsEqualToExpected() throws IOException {
        // todo remove hardcoded path
        String sourceFileName = "/Users/tsu/projects/tsu/publisher/acceptance-test/target/index.html";
        String targetFileName = "/Users/tsu/projects/tsu/publisher/acceptance-test/src/main/resources/index.html";

        assertFileContentLineForLine(sourceFileName, targetFileName);
    }

    private void assertFileContentLineForLine(String sourceFileName, String targetFileName) throws FileNotFoundException {
        Reader actualReader = new FileReader(sourceFileName);
        Reader expectedReader = new FileReader(targetFileName);

        LineIterator actual = new LineIterator(actualReader);
        LineIterator expected = new LineIterator(expectedReader);

        int row = 0;
        while (actual.hasNext()) {
            row++;
            String errorMessage = "\nsource file: " + sourceFileName + " \nline: " + row;
            assertThat(errorMessage, actual.nextLine(), is(expected.nextLine()));
        }
        assertFalse("The actual and target files has different length", expected.hasNext());
    }

}
