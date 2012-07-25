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

                List<String> sourceCode = readSourceFile();
                result.addAll(sourceCode);
                continue;
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

                List<String> sourceCode = readSourceFile();
                result.addAll(sourceCode);

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