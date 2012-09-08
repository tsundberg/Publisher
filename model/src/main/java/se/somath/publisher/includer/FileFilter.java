package se.somath.publisher.includer;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

public class FileFilter {
    private static final String[] EXCLUDED_FILE_TYPES = {"iml", "DS_Store"};
    private static final String[] EXCLUDED_DIRECTORIES = {"target", ".git", ".idea"};

    public static IOFileFilter createFileFilter() {
        IOFileFilter fileFilter = FileFilterUtils.trueFileFilter();
        for (String fileType : EXCLUDED_FILE_TYPES) {
            IOFileFilter imlFilter = FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter(fileType));
            fileFilter = FileFilterUtils.and(fileFilter, imlFilter);
        }
        return fileFilter;
    }

    public static IOFileFilter createDirectoryFilter() {
        IOFileFilter dirFilter = FileFilterUtils.trueFileFilter();
        for (String directoryName : EXCLUDED_DIRECTORIES) {
            IOFileFilter imlFilter = FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter(directoryName));
            dirFilter = FileFilterUtils.and(dirFilter, imlFilter);
        }
        return dirFilter;
    }
}
