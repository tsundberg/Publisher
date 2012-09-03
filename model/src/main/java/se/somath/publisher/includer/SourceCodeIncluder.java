package se.somath.publisher.includer;

import se.somath.publisher.formatter.HtmlEncoder;

import java.util.LinkedList;
import java.util.List;

public class SourceCodeIncluder {
    private SourceCodeReader sourceCodeReader = new SourceCodeReader();
    private IncludeSourceCodeParser includeSourceCodeParser = new IncludeSourceCodeParser();

    public List<String> addIncludes(List<String> content) {

        List<String> result = new LinkedList<String>();

        String includeTag = "";
        boolean inComment = false;
        boolean startTagFound = false;

        for (String candidate : content) {
            if (candidate.startsWith("<!--")) {
                inComment = true;
            }

            if (candidate.endsWith("-->")) {
                inComment = false;
            }

            if (candidate.startsWith("<include") && candidate.endsWith("/>") && !inComment) {
                includeTag = candidate;

                includeSourceCodeParser.parse(includeTag);
                includeTag = "";

                addFormattedSourceCode(result);
                continue;
            }

            if (candidate.startsWith("<include") && !candidate.endsWith("/>") && !inComment) {
                includeTag = candidate;
                startTagFound = true;
                continue;
            }

            if (!candidate.startsWith("<include") && candidate.endsWith("/>") && !inComment) {
                includeTag += candidate;

                includeSourceCodeParser.parse(includeTag);
                includeTag = "";

                addFormattedSourceCode(result);

                startTagFound = false;
                continue;
            }

            if (startTagFound && !inComment) {
                includeTag += candidate;
            } else {
                result.add(candidate);
            }
        }

        return result;
    }

    private void addFormattedSourceCode(List<String> result) {
        if (includeSourceCodeParser.shouldDisplayFileName()) {
            addFileName(result);
        }
        addStartPreTag(result);
        addEncodedSourceCode(result);
        addEndPreTag(result);
    }

    private void addStartPreTag(List<String> result) {
        result.add("<pre>");
    }

    private void addEncodedSourceCode(List<String> result) {
        List<String> unFormattedSourceCode = readSourceFile();
        HtmlEncoder htmlEncoder = new HtmlEncoder();
        List<String> formattedSourceCode = htmlEncoder.encode(unFormattedSourceCode);
        result.addAll(formattedSourceCode);
    }

    private void addEndPreTag(List<String> result) {
        result.add("</pre>");
    }

    private void addFileName(List<String> result) {
        String fileName = includeSourceCodeParser.getFileName();
        String fileDisplayName = includeSourceCodeParser.getFileDisplayName();
        if (fileDisplayName.length() > 0) {
            result.add("<p><i>" + fileDisplayName + "</i></p>");
        } else {
            result.add("<p><i>" + fileName + "</i></p>");
        }
    }

    private List<String> readSourceFile() {
        List<String> sourceCode;
        String root = includeSourceCodeParser.getRoot();
        String fileName = includeSourceCodeParser.getFileName();

        sourceCode = sourceCodeReader.readFile(root, fileName);
        return sourceCode;
    }

    public void setSourceCodeReader(SourceCodeReader sourceCodeReader) {
        this.sourceCodeReader = sourceCodeReader;
    }

    public void setIncludeSourceCodeParser(IncludeSourceCodeParser includeSourceCodeParser) {
        this.includeSourceCodeParser = includeSourceCodeParser;
    }
}