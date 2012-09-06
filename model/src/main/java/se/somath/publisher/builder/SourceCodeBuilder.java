package se.somath.publisher.builder;

import se.somath.publisher.formatter.HtmlEncoder;
import se.somath.publisher.includer.SourceCodeReader;
import se.somath.publisher.parser.IncludeSourceCodeParser;

import java.util.List;

public class SourceCodeBuilder {
    private SourceCodeReader sourceCodeReader = new SourceCodeReader();

    public List<String> getFormattedSourceCode(List<String> result, IncludeSourceCodeParser includeSourceCodeParser) {
        if (includeSourceCodeParser.shouldDisplayFileName()) {
            addFileName(result, includeSourceCodeParser);
        }
        addStartPreTag(result);
        addEncodedSourceCode(result, includeSourceCodeParser);
        addEndPreTag(result);

        return result;
    }

    private void addStartPreTag(List<String> result) {
        result.add("<pre>");
    }

    private void addEncodedSourceCode(List<String> result, IncludeSourceCodeParser includeSourceCodeParser) {
        List<String> unFormattedSourceCode = readSourceFile(includeSourceCodeParser);
        HtmlEncoder htmlEncoder = new HtmlEncoder();
        List<String> formattedSourceCode = htmlEncoder.encode(unFormattedSourceCode);
        result.addAll(formattedSourceCode);
    }

    private void addEndPreTag(List<String> result) {
        result.add("</pre>");
    }

    private void addFileName(List<String> result, IncludeSourceCodeParser includeSourceCodeParser) {
        String fileName = includeSourceCodeParser.getFileName();
        String fileDisplayName = includeSourceCodeParser.getFileDisplayName();
        if (fileDisplayName.length() > 0) {
            result.add("<p><i>" + fileDisplayName + "</i></p>");
        } else {
            result.add("<p><i>" + fileName + "</i></p>");
        }
    }

    private List<String> readSourceFile(IncludeSourceCodeParser includeSourceCodeParser) {
        List<String> sourceCode;
        String root = includeSourceCodeParser.getRoot();
        String fileName = includeSourceCodeParser.getFileName();

        sourceCode = sourceCodeReader.readFile(root, fileName);
        return sourceCode;
    }

    public void setSourceCodeReader(SourceCodeReader sourceCodeReader) {
        this.sourceCodeReader = sourceCodeReader;
    }
}
