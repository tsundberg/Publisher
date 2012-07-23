package se.somath.publisher.formatter;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HtmlFormatterTest {
    private HtmlFormatter formatter = new HtmlFormatter();

    @Test
    public void shouldRemoveAllLineBreaksInOneLineParagraph() {
        List<String> given = new LinkedList<String>();
        given.add("<p>");
        given.add("    This is an indented line. ");
        given.add("</p>");

        List<String> expected = new LinkedList<String>();
        expected.add("<p> This is an indented line. </p> ");

        List<String> actual = formatter.format(given);

        assertThat(actual, is(expected));
    }

    @Test
    public void shouldRemoveAllLineBreaksInTwoLineParagraph() {
        List<String> given = new LinkedList<String>();
        given.add("<p>");
        given.add("    This is an indented line. ");
        given.add("    This is a second indented line. ");
        given.add("</p>");

        List<String> expected = new LinkedList<String>();
        expected.add("<p> This is an indented line. This is a second indented line. </p> ");

        List<String> actual = formatter.format(given);

        assertThat(actual, is(expected));
    }

    @Test
    public void shouldNotFormatOutCommentedLines() {
        List<String> given = new LinkedList<String>();
        given.add("<p>");
        given.add("    This is an indented line. ");
        given.add("<!--");
        given.add("    This is an out commented line. ");
        given.add("-->");
        given.add("</p>");

        List<String> expected = new LinkedList<String>();
        expected.add("<p> This is an indented line. ");
        expected.add("<!--");
        expected.add("    This is an out commented line. ");
        expected.add("-->");
        expected.add("</p> ");

        List<String> actual = formatter.format(given);

        assertThat(actual, is(expected));
    }

    @Test
    public void shouldReturnCompleteHtmlDocument() {
        List<String> given = new LinkedList<String>();
        given.add("<html>");
        given.add("<p>");
        given.add("    This is an indented line. ");
        given.add("    This is a second indented line. ");
        given.add("</p>");
        given.add("</html>");

        List<String> expected = new LinkedList<String>();
        expected.add("<html>");
        expected.add("<p> This is an indented line. This is a second indented line. </p> ");
        expected.add("</html>");

        List<String> actual = formatter.format(given);

        assertThat(actual, is(expected));
    }

    @Test
    public void shouldFormatLongListItems() {
        List<String> given = new LinkedList<String>();
        given.add("    <li>This is a long list item that has been divided into more then one line in the source but should not be divided");
        given.add("        in the transformed result.");
        given.add("    </li>");

        List<String> expected = new LinkedList<String>();
        expected.add("<li>This is a long list item that has been divided into more then one line in the source but should not be divided in the transformed result. </li> ");

        List<String> actual = formatter.format(given);

        assertThat(actual, is(expected));
    }

    @Test
    public void shouldFormatShortListItems() {
        List<String> given = new LinkedList<String>();
        given.add("    <li>This is short list</li>");

        List<String> expected = new LinkedList<String>();
        expected.add("<li>This is short list</li> ");

        List<String> actual = formatter.format(given);

        assertThat(actual, is(expected));
    }
}
