package se.somath.publisher.includer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import se.somath.publisher.excpetion.PublishException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SourceCodeReader {
    private static final String[] EXCLUDED_FILE_TYPES = {"iml"};
    private static final String[] EXCLUDED_DIRECTORIES = {"target", ".git", ".idea"};

    public List<String> readFile(String root, String fileName) {
        Collection<File> files = listFiles(root);
        File sourceCodeFile = findWantedSourceCodeFile(root, fileName, files);

        return readSourcecodeContent(sourceCodeFile);
    }

    private Collection<File> listFiles(String root) {
        File rootDir = locateRootDirectory(root);
        IOFileFilter fileFilter = createFileFilter();
        IOFileFilter dirFilter = createDirectoryFilter();

        return FileUtils.listFiles(rootDir, fileFilter, dirFilter);
    }

    private File locateRootDirectory(String root) {
        File rootDir = new File(root);

        while (!rootDir.isDirectory()) {
            rootDir = rootDir.getParentFile();
        }
        return rootDir;
    }

    private IOFileFilter createFileFilter() {
        IOFileFilter fileFilter = FileFilterUtils.trueFileFilter();
        for (String fileType : EXCLUDED_FILE_TYPES) {
            IOFileFilter imlFilter = FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter(fileType));
            fileFilter = FileFilterUtils.and(fileFilter, imlFilter);
        }
        return fileFilter;
    }

    private IOFileFilter createDirectoryFilter() {
        IOFileFilter dirFilter = FileFilterUtils.trueFileFilter();
        for (String directoryName : EXCLUDED_DIRECTORIES) {
            IOFileFilter imlFilter = FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter(directoryName));
            dirFilter = FileFilterUtils.and(dirFilter, imlFilter);
        }
        return dirFilter;
    }

    File findWantedSourceCodeFile(String root, String fileName, Collection<File> files) {
        File sourceCodeFile = null;

        for (File candidate : files) {
            String candidatePath = getCandidatePath(candidate);
            String filePath = getFilePath(root, fileName);

            if (candidatePath.contains(filePath)) {
                return candidate;
            }
        }

        return sourceCodeFile;
    }

    private String getCandidatePath(File candidate) {
        try {
            return candidate.getCanonicalPath();
        } catch (IOException e) {
            throw new PublishException(e);
        }
    }

    private String getFilePath(String root, String fileName) {
        String fixedRoot = removeLeadingFileSeparator(root);
        fixedRoot = addEndingFileSeparator(fixedRoot);

        String fixedFileName = removeLeadingFileSeparator(fileName);

        return fixedRoot + fixedFileName;
    }

    private String addEndingFileSeparator(String root) {
        if (!root.endsWith(File.separator)) {
            root = root + File.separator;
        }
        return root;
    }

    private String removeLeadingFileSeparator(String path) {
        String pathSeparator = File.separator;

        if (path.startsWith("." + pathSeparator)) {
            int beginIndex = 2;
            path = path.substring(beginIndex);
        } else if (path.startsWith(pathSeparator)) {
            int beginIndex = 1;
            path = path.substring(beginIndex);
        }

        return path;
    }

    private List<String> readSourcecodeContent(File sourceCodeFile) {
        if (sourceCodeFile == null) {
            return Collections.emptyList();
        }

        try {
            return FileUtils.readLines(sourceCodeFile);
        } catch (IOException e) {
            throw new PublishException(e);
        }
    }
}