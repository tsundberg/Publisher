package se.somath.publisher;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import se.somath.publisher.excpetion.PublishException;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public void publish(String sourceDirectory, String targetDirectory) {
        String defaultFileName = "index.html";

        try {
            List<String> content = readSourceFile(sourceDirectory, defaultFileName);

            // 3 formatera html i string buffern
            // 4 lägg till includes, byt ut alla rader med include till det verkliga värdet med pre taggar före och efter och rätt encodade

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

    private void writeTargetFile(String targetDirectory, List<String> content, String fileName) throws IOException {
        String targetFileName = targetDirectory + File.separator + fileName;
        File targetFile = new File(targetFileName);
        createTargetDirectory(targetFile);

        FileUtils.writeLines(targetFile, content);
    }

    private void createTargetDirectory(File targetFile) throws IOException {
        File targetDirectory = targetFile.getParentFile();
        if (!targetDirectory.mkdirs()) {
            throw new IOException("Could not create " + targetDirectory.getAbsolutePath());
        }
    }


}
