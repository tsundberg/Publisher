package se.somath.publisher.includer;

import se.somath.publisher.excpetion.DirectoryNotFoundException;

import java.io.File;

public class DirectoryLocator {
    public File locateDirectory(String root) {
        File defaultWorkingDirectory = new File(root);

        return locateDirectory(root, defaultWorkingDirectory);
    }

    public File locateDirectory(String root, File initialDirectory) {
        String cleanedRoot = removeLeadingFileSystemCharacters(root);
        File workingDirectory = prepareWorkingDirectory(initialDirectory);

        if (workingDirectory.isDirectory()) {
            while (workingDirectory.isDirectory()) {
                if (cleanedRoot.equals(workingDirectory.getName())) {
                    return workingDirectory;
                }

                workingDirectory = workingDirectory.getParentFile();
                if (workingDirectory == null) {
                    throw new DirectoryNotFoundException("Could not locate <" + root + "> in <" + initialDirectory.getAbsolutePath() + ">");
                }
            }
        } else {
            throw new DirectoryNotFoundException("The candidate <" + root + "> is not a directory in <" + initialDirectory.getAbsolutePath() + ">");
        }

        return initialDirectory;
    }

    private String removeLeadingFileSystemCharacters(String root) {
        String cleanedRoot = root;
        int firstCharacter = 1;
        while (cleanedRoot.startsWith(".") || cleanedRoot.startsWith("/")) {
            cleanedRoot = cleanedRoot.substring(firstCharacter);
        }

        return cleanedRoot;
    }

    private File prepareWorkingDirectory(File initialDirectory) {
        String[] pathParts = cleanPath(initialDirectory);

        for (int candidateLength = pathParts.length; candidateLength > 0; candidateLength--) {
            String candidatePath = buildCandidateDirectoryName(pathParts, candidateLength);
            File candidateFile = new File(candidatePath);
            if (candidateFile.isDirectory()) {
                return candidateFile;
            }
        }

        return initialDirectory;
    }

    private String[] cleanPath(File initialDirectory) {
        String path = initialDirectory.getAbsolutePath();

        String[] pathParts = path.split("/");
        for (int i = 0; i < pathParts.length; i++) {
            pathParts[i] = pathParts[i].replaceAll("\\.", "");
        }
        return pathParts;
    }

    private String buildCandidateDirectoryName(String[] pathParts, int candidateLength) {
        StringBuilder candidatePath = new StringBuilder();
        for (int j = 0; j < candidateLength; j++) {
            candidatePath.append(pathParts[j]);
            candidatePath.append("/");
        }
        return candidatePath.toString();
    }
}
