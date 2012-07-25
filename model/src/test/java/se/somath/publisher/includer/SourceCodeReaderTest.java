package se.somath.publisher.includer;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class SourceCodeReaderTest {

    @Test
    public void shouldReadProjectMain() {
        SourceCodeReader reader = new SourceCodeReader();
        String root = "./model";
        String fileName = "/src/main/java/se/somath/publisher/Main.java";

        List<String> content = reader.readFile(root, fileName);

        int zeroLines = 0;
        assertTrue("Expected to find some lines in project Main class", content.size() > zeroLines);
    }

    @Test
    public void shouldNotReadIdeaProjectFile() {
        SourceCodeReader reader = new SourceCodeReader();
        String root = "./model";
        String fileName = "model.iml";

        List<String> content = reader.readFile(root, fileName);

        int zeroLines = 0;
        assertTrue("Did not expected to find any idea in project file. It should be excluded from files possible to locate.", content.size() == zeroLines);
    }

}
