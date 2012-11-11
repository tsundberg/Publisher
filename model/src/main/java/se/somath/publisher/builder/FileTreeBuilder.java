package se.somath.publisher.builder;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import se.somath.publisher.excpetion.TooManyFilesFoundException;
import se.somath.publisher.includer.DirectoryLocator;
import se.somath.publisher.includer.FileFilter;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Inspired from http://mama.indstate.edu/users/ice/tree/
 */
public class FileTreeBuilder {
    private static final int MAX_NUMBER_OF_FILES_IN_TREE = 2048;
    private File rootDirectory;
    private int counter;
    private boolean onlyDirs;

    /**
     * Build a file tree and apply the default file filter
     *
     * @param root     the root directory to build from
     * @param onlyDirs set to true if the file tree only should include  directories
     * @return a list of Strings with all files properly formatted
     */
    public List<String> buildFileTree(String root, boolean onlyDirs) {
        DirectoryLocator locator = new DirectoryLocator();
        rootDirectory = locator.locateDirectory(root);
        this.onlyDirs = onlyDirs;
        IOFileFilter fileFilter = FileFilterUtils.and(FileFilter.createFileFilter(), FileFilter.createDirectoryFilter());
        return buildFileTree(rootDirectory, fileFilter);
    }

    /**
     * Build a file tree and apply the provided file filter
     *
     * @param root       the root directory to build from
     * @param fileFilter the file filter to apply
     * @return a list of Strings with all files properly formatted
     */
    List<String> buildFileTree(File root, IOFileFilter fileFilter) {
        File[] files = getFilteredFiles(root, fileFilter);
        if (files != null) {
            return buildTree(root, fileFilter, files);
        } else {
            return buildEmptyResult();
        }
    }

    private List<String> buildTree(File root, IOFileFilter fileFilter, File[] files) {
        List<String> result = new LinkedList<String>();
        result.add("<pre>");
        String applicationName = root.getName();
        result.add(applicationName);
        for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {
            File file = files[fileIndex];
            if (file.isFile()) {
                String fileRow = "|-- " + file.getName();
                if (maybeAdd(file)) {
                    result.add(fileRow);
                }
            }
            if (file.isDirectory()) {
                if (lastItem(files, fileIndex)) {
                    buildFileTree("", file, true, result, fileFilter);
                } else {
                    buildFileTree("", file, false, result, fileFilter);
                }
            }
        }
        result.add("</pre>");

        return result;
    }

    private List<String> buildEmptyResult() {
        List<String> result = new LinkedList<String>();
        result.add("<pre>");
        result.add("No files found");
        result.add("</pre>");
        return result;
    }

    private boolean lastItem(File[] files, int fileIndex) {
        return fileIndex == files.length - 1;
    }

    private void buildFileTree(String prefix, File file, boolean isTail, List<String> result, IOFileFilter fileFilter) {
        if (counter > MAX_NUMBER_OF_FILES_IN_TREE) {
            throw new TooManyFilesFoundException("Too many files found in <" + rootDirectory.getAbsolutePath() + ">");
        }
        counter++;

        String line;
        if (isTail) {
            line = prefix + "`-- " + file.getName();
        } else {
            line = prefix + "|-- " + file.getName();
        }

        if (maybeAdd(file)) {
            result.add(line);
        }

        File[] files = getFilteredFiles(file, fileFilter);
        if (files != null) {
            String newPrefix;
            if (isTail) {
                newPrefix = prefix + "    ";
            } else {
                newPrefix = prefix + "|   ";
            }
            for (int i = 0; i < files.length - 1; i++) {
                File current = files[i];
                buildFileTree(newPrefix, current, false, result, fileFilter);
            }
            if (hasChildren(files)) {
                File last = files[files.length - 1];
                buildFileTree(newPrefix, last, true, result, fileFilter);
            }
        }
    }

    private File[] getFilteredFiles(File root, IOFileFilter fileFilter) {
        File[] files = root.listFiles();
        if (files != null) {
            List<File> res = new LinkedList<File>();
            for (File candidate : files) {
                if (fileFilter.accept(candidate)) {
                    res.add(candidate);
                }
            }
            files = res.toArray(new File[res.size()]);
        }

        return files;
    }

    private boolean hasChildren(File[] children) {
        return children.length >= 1;
    }

    private boolean maybeAdd(File file) {
        return !onlyDirs || !file.isFile();
    }
}
