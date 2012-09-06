package se.somath.publisher.builder;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import se.somath.publisher.includer.FileFilter;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Inspired from http://mama.indstate.edu/users/ice/tree/
 */
public class FileTreeBuilder {

    /**
     * Build a file tree and apply the default file filter
     *
     * @param root the root directory to build from
     * @return a list of Strings with all files properly formatted
     */
    public List<String> buildFileTree(String root) {
        File rootDirectory = new File(root);

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
    public List<String> buildFileTree(File root, IOFileFilter fileFilter) {
        List<String> result = new LinkedList<String>();

        File[] files = getFilteredFiles(root, fileFilter);

        result.add("<pre>");
        if (files != null) {
            String applicationName = root.getName();
            result.add(applicationName);
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isFile()) {
                    String fileRow = "|-- " + file.getName();
                    result.add(fileRow);
                }
                if (file.isDirectory()) {
                    if (i == files.length - 1) {
                        buildFileTree("", file, true, result, fileFilter);
                    } else {
                        buildFileTree("", file, false, result, fileFilter);
                    }
                }
            }
        } else {
            result.add("No files found");
        }
        result.add("</pre>");

        return result;
    }

    private void buildFileTree(String prefix, File file, boolean isTail, List<String> result, IOFileFilter fileFilter) {
        String line;
        if (isTail) {
            line = prefix + "`-- " + file.getName();
        } else {
            line = prefix + "|-- " + file.getName();
        }
        result.add(line);

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
}
