package se.somath.publisher.includer;

import org.junit.Test;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class SourceCodeReaderTest {

    @Test
    public void shouldReadProjectMain() {
        SourceCodeReader reader = new SourceCodeReader();
        String root = "./model";
        String fileName = "src/main/java/se/somath/publisher/Main.java";

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

    @Test
    public void shouldSelectRootPomWhenRootHasEndingPathSeparator() {
        String root = "./example/module-separation/";
        String fileName = "pom.xml";
        Collection<File> fileCandidates = getFileCandidates();
        File expected = new File("/Users/tsu/Dropbox/blog/draft/maven-separate-integration-tests-and-production/./example/module-separation/pom.xml");

        SourceCodeReader reader = new SourceCodeReader();
        File actualFile = reader.findWantedSourceCodeFile(root, fileName, fileCandidates);

        assertThat(actualFile, is(expected));
    }

    @Test
    public void shouldSelectRootPomWithoutEndingPathSeparator() {
        String root = "./example/module-separation";
        String fileName = "pom.xml";
        Collection<File> fileCandidates = getFileCandidates();
        File expected = new File("/Users/tsu/Dropbox/blog/draft/maven-separate-integration-tests-and-production/./example/module-separation/pom.xml");

        SourceCodeReader reader = new SourceCodeReader();
        File actualFile = reader.findWantedSourceCodeFile(root, fileName, fileCandidates);

        assertThat(actualFile, is(expected));
    }

    private Collection<File> getFileCandidates() {
        Collection<File> fileCandidates = new LinkedList<File>();
        fileCandidates.add(new File("/Users/tsu/Dropbox/blog/draft/maven-separate-integration-tests-and-production/./example/module-separation/main/pom.xml"));
        fileCandidates.add(new File("/Users/tsu/Dropbox/blog/draft/maven-separate-integration-tests-and-production/./example/module-separation/main/src/main/java/se/somath/HelloWorld.java"));
        fileCandidates.add(new File("/Users/tsu/Dropbox/blog/draft/maven-separate-integration-tests-and-production/./example/module-separation/pom.xml"));
        fileCandidates.add(new File("/Users/tsu/Dropbox/blog/draft/maven-separate-integration-tests-and-production/./example/module-separation/test/pom.xml"));
        fileCandidates.add(new File("/Users/tsu/Dropbox/blog/draft/maven-separate-integration-tests-and-production/./example/module-separation/test/src/test/java/se/somath/HelloWorldTest.java"));

        return fileCandidates;
    }
}
