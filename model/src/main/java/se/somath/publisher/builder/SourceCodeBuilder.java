package se.somath.publisher.builder;

import se.somath.publisher.formatter.HtmlEncoder;
import se.somath.publisher.includer.SourceCodeReader;
import se.somath.publisher.parser.IncludeSourceCodeParser;

import java.util.LinkedList;
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
        result.add("<div style=\"border: 1px inset; padding: 15px;\">");
    }

    private void addEncodedSourceCode(List<String> result, IncludeSourceCodeParser includeSourceCodeParser) {
        List<String> unFormattedSourceCode = readSourceFile(includeSourceCodeParser);

        if (includeSourceCodeParser.shouldIncludeMethod()) {
            unFormattedSourceCode = includeMethod(unFormattedSourceCode, includeSourceCodeParser);
        }

        HtmlEncoder htmlEncoder = new HtmlEncoder();
        List<String> formattedSourceCode = htmlEncoder.encode(unFormattedSourceCode);
        result.addAll(formattedSourceCode);
    }

    private List<String> includeMethod(List<String> sourceCode, IncludeSourceCodeParser includeSourceCodeParser) {
        String wantedMethod = includeSourceCodeParser.getMethod();

        int firstMethodRow = findFirstMethodRow(sourceCode, wantedMethod);
        int indent = calculateIndent(sourceCode, firstMethodRow);
        String methodEndMarker = createMethodEndMarker(indent);
        int lastMethodRow = findLastMethodRow(sourceCode, firstMethodRow, methodEndMarker);
        List<String> indentedResult = sourceCode.subList(firstMethodRow, lastMethodRow);

        return unIndentMethod(indent, indentedResult);
    }

    private int findFirstMethodRow(List<String> sourceCode, String wantedMethod) {
        int firstMethodRow = locateMethodSignature(sourceCode, wantedMethod);
        firstMethodRow = includeMethodMetaData(sourceCode, firstMethodRow);

        return firstMethodRow;
    }

    private int locateMethodSignature(List<String> sourceCode, String wantedMethod) {
        int firstMethodRow = 0;
        for (int i = 0; i < sourceCode.size(); i++) {
            String sourceRow = sourceCode.get(i);
            if (sourceRow.contains(wantedMethod)) {
                firstMethodRow = i;
                break;
            }
        }
        return firstMethodRow;
    }

    private int includeMethodMetaData(List<String> sourceCode, int firstMethodRow) {
        String methodRow = sourceCode.get(firstMethodRow);
        while (methodRow.length() > 0) {
            firstMethodRow--;
            methodRow = sourceCode.get(firstMethodRow);
        }
        firstMethodRow++;

        return firstMethodRow;
    }

    private int calculateIndent(List<String> sourceCode, int firstMethodRow) {
        String methodRow;
        methodRow = sourceCode.get(firstMethodRow);
        int indent = 0;

        char space = ' ';
        while (methodRow.charAt(indent) == space) {
            indent++;
        }

        return indent;
    }

    private String createMethodEndMarker(int indent) {
        String methodEndMarker = "";
        for (int i = 0; i < indent; i++) {
            methodEndMarker += " ";
        }
        methodEndMarker += "}";

        return methodEndMarker;
    }

    private int findLastMethodRow(List<String> sourceCode, int firstMethodRow, String methodEndMarker) {
        int lastMethodRow;
        String methodRow;
        lastMethodRow = firstMethodRow;
        methodRow = sourceCode.get(lastMethodRow);
        while (!methodRow.equals(methodEndMarker)) {
            lastMethodRow++;
            methodRow = sourceCode.get(lastMethodRow);
        }
        lastMethodRow++;

        return lastMethodRow;
    }

    private List<String> unIndentMethod(int indent, List<String> indentedResult) {
        List<String> result = new LinkedList<String>();
        for (String row : indentedResult) {
            result.add(row.substring(indent));
        }

        return result;
    }

    private void addEndPreTag(List<String> result) {
        result.add("</div>");
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
