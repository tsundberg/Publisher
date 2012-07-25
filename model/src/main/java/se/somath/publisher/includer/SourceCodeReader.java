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
        File rootDir = locateRootDirectory(root);
        Collection<File> files = listFiles(rootDir);
        File sourceCodeFile = findWantedSourceCodeFile(fileName, files);

        return readSourcecodeContent(sourceCodeFile);
    }

    private File locateRootDirectory(String root) {
        File rootDir = new File(root);

        while (!rootDir.isDirectory()) {
            rootDir = rootDir.getParentFile();
        }
        return rootDir;
    }

    private Collection<File> listFiles(File rootDir) {
        IOFileFilter fileFilter = createFileFilter();
        IOFileFilter dirFilter = createDirectoryFilter();

        return FileUtils.listFiles(rootDir, fileFilter, dirFilter);
    }

    private File findWantedSourceCodeFile(String fileName, Collection<File> files) {
        File sourceCodeFile = null;

        for (File candidate : files) {
            String candidatePath = candidate.getPath();
            if (candidatePath.endsWith(fileName)) {
                sourceCodeFile = candidate;
                break;
            }
        }
        return sourceCodeFile;
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
}
