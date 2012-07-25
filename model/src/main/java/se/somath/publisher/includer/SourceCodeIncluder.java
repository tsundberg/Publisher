package se.somath.publisher.includer;

import java.util.LinkedList;
import java.util.List;

public class SourceCodeIncluder {
    private SourceCodeReader sourceCodeReader = new SourceCodeReader();
    private IncludeParser includeParser = new IncludeParser();

    public List<String> addIncludes(List<String> content) {

        List<String> result = new LinkedList<String>();

        String includeTag = "";
        boolean inComment = false;
        boolean startTagFound = false;
        List<String> sourceCode = null;

        for (String candidate : content) {
            if (candidate.startsWith("<!--")) {
                inComment = true;
            }

            if (candidate.endsWith("-->")) {
                inComment = false;
            }

            if (candidate.startsWith("<include") && candidate.endsWith("/>") && !inComment) {
                includeTag = candidate;

                includeParser.parse(includeTag);
                includeTag = "";

                sourceCode = readSourceFile();
            }

            if (candidate.startsWith("<include") && !candidate.endsWith("/>") && !inComment) {
                includeTag = candidate;
                startTagFound = true;
                continue;
            }

            if (!candidate.startsWith("<include") && candidate.endsWith("/>") && !inComment) {
                includeTag += candidate;

                includeParser.parse(includeTag);
                includeTag = "";

                sourceCode = readSourceFile();

                startTagFound = false;
            }

            if (startTagFound && !inComment) {
                includeTag += candidate;
            } else {
                result.add(candidate);
            }

            if (sourceCode != null) {
                result.addAll(sourceCode);
            }
        }

        return result;
    }

    private List<String> readSourceFile() {
        List<String> sourceCode;
        String root = includeParser.getRoot();
        String fileName = includeParser.getFileName();

        sourceCode = sourceCodeReader.readFile(root, fileName);
        return sourceCode;
    }

    public void setSourceCodeReader(SourceCodeReader sourceCodeReader) {
        this.sourceCodeReader = sourceCodeReader;
    }

    public void setIncludeParser(IncludeParser includeParser) {
        this.includeParser = includeParser;
    }
}