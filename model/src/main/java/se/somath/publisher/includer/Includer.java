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
            if (commentStart(candidate)) {
                inComment = true;
            }

            if (commentEnd(candidate)) {
                inComment = false;
            }

            if (completeIncludeLine(inComment, candidate)) {
                includeTag = candidate;
                result = getIncludeContent(result, includeTag);
                includeTag = "";
                continue;
            }

            if (initialIncludeLine(inComment, candidate)) {
                includeTag = candidate;
                startTagFound = true;
                continue;
            }

            if (endingIncludeLine(inComment, candidate)) {
                includeTag += candidate;
                result = getIncludeContent(result, includeTag);
                includeTag = "";
                startTagFound = false;
                continue;
            }

            if (continuingIncludeLine(inComment, startTagFound)) {
                includeTag += candidate;
            } else {
                result.add(candidate);
            }
        }

        return result;
    }

    private boolean commentStart(String candidate) {
        return candidate.startsWith("<!--");
    }

    private boolean commentEnd(String candidate) {
        return candidate.endsWith("-->");
    }

    private boolean completeIncludeLine(boolean inComment, String candidate) {
        return candidate.startsWith("<include") && candidate.endsWith("/>") && !inComment;
    }

    private boolean initialIncludeLine(boolean inComment, String candidate) {
        return candidate.startsWith("<include") && !candidate.endsWith("/>") && !inComment;
    }

    private boolean endingIncludeLine(boolean inComment, String candidate) {
        return !candidate.startsWith("<include") && candidate.endsWith("/>") && !inComment;
    }

    private boolean continuingIncludeLine(boolean inComment, boolean startTagFound) {
        return startTagFound && !inComment;
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