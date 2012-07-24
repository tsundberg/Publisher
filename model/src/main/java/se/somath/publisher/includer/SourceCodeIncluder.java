package se.somath.publisher.includer;

import java.util.LinkedList;
import java.util.List;

public class SourceCodeIncluder {
    private SourceCodeReader sourceCodeReader;
    private IncludeParser includeParser = new IncludeParser();

    public List<String> addIncludes(List<String> content) {

        List<String> result = new LinkedList<String>();

        String includeTag = "";
        boolean startTagFound = false;
        List<String> sourceCode = null;

        for (String candidate : content) {
            if (candidate.startsWith("<include") && candidate.endsWith("/>")) {
                includeTag = candidate;

                includeParser.parse(includeTag);
                includeTag = "";

                String root = includeParser.getRoot();
                String fileName = includeParser.getFileName();

                sourceCodeReader.readFile(root, fileName);
            }

            if (candidate.startsWith("<include") && !candidate.endsWith("/>")) {
                includeTag = candidate;
                startTagFound = true;
                continue;
            }

            if (!candidate.startsWith("<include") && candidate.endsWith("/>")) {
                includeTag += candidate;

                includeParser.parse(includeTag);
                includeTag = "";

                String root = includeParser.getRoot();
                String fileName = includeParser.getFileName();

                sourceCodeReader.readFile(root, fileName);

                startTagFound = false;
            }

            if (startTagFound) {
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

    public void setSourceCodeReader(SourceCodeReader sourceCodeReader) {
        this.sourceCodeReader = sourceCodeReader;
    }

    public void setIncludeParser(IncludeParser includeParser) {
        this.includeParser = includeParser;
    }
}
