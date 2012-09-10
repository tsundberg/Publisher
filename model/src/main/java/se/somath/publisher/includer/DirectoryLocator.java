package se.somath.publisher.includer;

import se.somath.publisher.excpetion.DirectoryNotFoundException;

import java.io.File;

public class DirectoryLocator {
    public File locateDirectory(String root) {
        File defaultWorkingDirectory = new File(".");

        return locateDirectory(root, defaultWorkingDirectory);
    }

    public File locateDirectory(String root, File workingDirectory) {
        String cleanedRoot = removeLeadingFileSystemCharacters(root);

        while (workingDirectory.isDirectory()) {
            if (cleanedRoot.equals(workingDirectory.getName())) {
                return workingDirectory;
            }

            workingDirectory = workingDirectory.getParentFile();
            if (workingDirectory == null) {
                throw new DirectoryNotFoundException("Could not locate " + root);
            }
        }

        return workingDirectory;
    }

    private String removeLeadingFileSystemCharacters(String root) {
        String cleanedRoot = root;
        int firstCharacter = 1;
        while (cleanedRoot.startsWith(".") || cleanedRoot.startsWith("/")) {
            cleanedRoot = cleanedRoot.substring(firstCharacter);
        }

        return cleanedRoot;
    }
}
