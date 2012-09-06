package se.somath.publisher.includer;

import org.junit.Test;
import se.somath.publisher.builder.FileTreeBuilder;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FileTreeIncluderTest {

    @Test
    public void shouldCallReadFileWithIncludeOnOneLine() {
        List<String> given = new LinkedList<String>();

        given.add("<include-file-tree root=\"fileRoot\"/>");

        FileTreeBuilder fileTreeBuilder = mock(FileTreeBuilder.class);
        Includer includer = new Includer();
        includer.setFileTreeBuilder(fileTreeBuilder);

        includer.addIncludes(given);

        verify(fileTreeBuilder).buildFileTree("fileRoot");
    }


}
