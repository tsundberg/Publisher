package se.somath.publisher.formatter;

import java.util.LinkedList;
import java.util.List;

public class HtmlFormatter {
    private StringBuilder accumulator = null;
    private List<String> formatted = new LinkedList<String>();

    public List<String> format(List<String> unFormattedContent) {
        boolean inComment = false;
        boolean inParagraph = false;
        boolean inListItem = false;

        for (String line : unFormattedContent) {
            inComment = foundCommentOpening(inComment, line);
            addParagraphBeforeComment(inComment, inParagraph);

            inParagraph = foundParagraphOpening(inComment, inParagraph, line);
            inListItem = foundListItemOpening(inComment, inListItem, line);

            if (inParagraph && !inComment) {
                inParagraph = addParagraphLine(line);
                continue;
            }

            if (inListItem && !inComment) {
                inListItem = addListItemLine(line);
                continue;
            }

            inComment = foundCommentClosing(inComment, line);

            formatted.add(line);
        }

        return formatted;
    }

    private boolean foundCommentOpening(boolean inComment, String line) {
        if (line.contains("<!--")) {
            inComment = true;
        }
        return inComment;
    }

    private void addParagraphBeforeComment(boolean inComment, boolean inParagraph) {
        if (inComment && inParagraph && accumulator.length() > 0) {
            formatted.add(accumulator.toString());
            accumulator = new StringBuilder();
        }
    }

    private boolean foundParagraphOpening(boolean inComment, boolean inParagraph, String line) {
        if (!inComment && line.contains("<p>")) {
            inParagraph = true;
            accumulator = new StringBuilder();
        }
        return inParagraph;
    }

    private boolean foundListItemOpening(boolean inComment, boolean inListItem, String line) {
        if (!inComment && line.contains("<li>")) {
            inListItem = true;
            accumulator = new StringBuilder();
        }
        return inListItem;
    }

    private boolean addParagraphLine(String line) {
        String endTag = "</p>";
        return addAccumulatedLinesIfEndFound(line, endTag);
    }

    private boolean addListItemLine(String line) {
        String endTag = "</li>";
        return addAccumulatedLinesIfEndFound(line, endTag);
    }

    private boolean foundCommentClosing(boolean inComment, String line) {
        if (inComment && line.contains("-->")) {
            inComment = false;
        }
        return inComment;
    }

    private boolean addAccumulatedLinesIfEndFound(String line, String endTag) {
        String trimmedLine = addTrimmedLine(line);
        if (trimmedLine.contains(endTag)) {
            formatted.add(accumulator.toString());
            accumulator = null;
            return false;
        }
        return true;
    }

    private String addTrimmedLine(String line) {
        String trimmedLine = line.trim();
        accumulator.append(trimmedLine);
        accumulator.append(" ");
        return trimmedLine;
    }
}