package se.somath.publisher.includer;

import se.somath.publisher.builder.FileTreeBuilder;
import se.somath.publisher.builder.SourceCodeBuilder;
import se.somath.publisher.parser.IncludeFileTreeParser;
import se.somath.publisher.parser.IncludeSourceCodeParser;

import java.util.LinkedList;
import java.util.List;

public class Includer {
    private SourceCodeBuilder sourceCodeBuilder = new SourceCodeBuilder();
    private IncludeSourceCodeParser includeSourceCodeParser = new IncludeSourceCodeParser();

    private FileTreeBuilder fileTreeBuilder = new FileTreeBuilder();
    private IncludeFileTreeParser includeFileTreeParser = new IncludeFileTreeParser();

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

                result = getIncludeContent(result, includeTag);
                includeTag = "";

                continue;
            }

            if (candidate.startsWith("<include") && !candidate.endsWith("/>") && !inComment) {
                includeTag = candidate;
                startTagFound = true;
                continue;
            }

            if (!candidate.startsWith("<include") && candidate.endsWith("/>") && !inComment) {
                includeTag += candidate;

                result = getIncludeContent(result, includeTag);
                includeTag = "";

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

    private List<String> getIncludeContent(List<String> result, String includeTag) {
        if (includeTag.contains("include-source-code")) {
            includeSourceCodeParser.parse(includeTag);
            return sourceCodeBuilder.getFormattedSourceCode(result, includeSourceCodeParser);
        }

        if (includeTag.contains("include-file-tree")) {
            includeFileTreeParser.parse(includeTag);
            String root = includeFileTreeParser.getRoot();
            List<String> fileTree = fileTreeBuilder.buildFileTree(root);
            result.addAll(fileTree);

            return result;
        }

        return result;
    }

    public void setIncludeSourceCodeParser(IncludeSourceCodeParser includeSourceCodeParser) {
        this.includeSourceCodeParser = includeSourceCodeParser;
    }

    public void setSourceCodeBuilder(SourceCodeBuilder sourceCodeBuilder) {
        this.sourceCodeBuilder = sourceCodeBuilder;
    }

    public void setFileTreeBuilder(FileTreeBuilder fileTreeBuilder) {
        this.fileTreeBuilder = fileTreeBuilder;
    }
}