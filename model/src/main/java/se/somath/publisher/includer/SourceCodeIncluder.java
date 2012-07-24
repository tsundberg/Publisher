package se.somath.publisher.includer;

import java.util.List;

public class SourceCodeIncluder {

    private SourceCodeReader sourceCodeReader;
    private IncludeParser includeParser;

    public List<String> getIncludes(List<String> content) {

        for (String candidate : content) {
            if (candidate.startsWith("<include")) {
                includeParser.parse(candidate);

                String root = includeParser.getRoot();
                String fileName = includeParser.getFileName();

                sourceCodeReader.readFile(root, fileName);

            }
        }


        return content;
    }

    public void setSourceCodeReader(SourceCodeReader sourceCodeReader) {
        this.sourceCodeReader = sourceCodeReader;
    }

    public void setIncludeParser(IncludeParser includeParser) {
        this.includeParser = includeParser;
    }
}
