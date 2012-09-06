package se.somath.publisher;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import se.somath.publisher.excpetion.PublishException;
import se.somath.publisher.formatter.HtmlFormatter;
import se.somath.publisher.includer.SourceCodeIncluder;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public void publish(String sourceDirectory, String targetDirectory) {
        String defaultFileName = "index.html";

        try {
            List<String> content = readSourceFile(sourceDirectory, defaultFileName);
            content = formatHtml(content);
            content = addIncludes(content);
            writeTargetFile(targetDirectory, content, defaultFileName);
        } catch (FileNotFoundException e) {
            throw new PublishException(e);
        } catch (IOException e) {
            throw new PublishException(e);
        }
    }

    private List<String> readSourceFile(String sourceDirectory, String fileName) throws FileNotFoundException {
        List<String> content = new LinkedList<String>();
        String sourceFileName = sourceDirectory + File.separator + fileName;
        Reader sourceFileReader = new FileReader(sourceFileName);
        LineIterator sourceFileIterator = new LineIterator(sourceFileReader);

        while (sourceFileIterator.hasNext()) {
            String currentLine = sourceFileIterator.nextLine();
            content.add(currentLine);
        }

        return content;
    }

    private List<String> formatHtml(List<String> unFormattedContent) {
        HtmlFormatter formatter = new HtmlFormatter();
        return formatter.format(unFormattedContent);
    }

    private List<String> addIncludes(List<String> unIncludedContent) {
        SourceCodeIncluder includer = new SourceCodeIncluder();
        return includer.addIncludes(unIncludedContent);
    }

    private void writeTargetFile(String targetDirectory, List<String> content, String fileName) throws IOException {
        String targetFileName = targetDirectory + File.separator + fileName;
        File targetFile = new File(targetFileName);
        createTargetDirectory(targetFile);

        FileUtils.writeLines(targetFile, content);
    }

    private void createTargetDirectory(File targetFile) {
        File targetDirectory = targetFile.getParentFile();
        //noinspection ResultOfMethodCallIgnored
        targetDirectory.mkdirs();
    }
}
