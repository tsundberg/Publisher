package se.somath.publisher;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Main {
    public void copyToTarget(String sourceDirectory, String targetDirectory) {
        File targetDir = new File(targetDirectory);
        String sourceFileName = sourceDirectory + File.separator + "index.html";
        File sourceFile = new File(sourceFileName);

        try {
            FileUtils.copyFileToDirectory(sourceFile, targetDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
