package com.groupdocs.ui.common.config;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class DefaultDirectories {
    public static final String LIC = "GroupDocs.Signature.Java.lic";
    public static final String LICENSES = "Licenses";
    public static final String DOCUMENT_SAMPLES = "DocumentSamples";

    public static String defaultLicenseDirectory() {
        Path defaultLic = FileSystems.getDefault().getPath(LICENSES + File.separator + LIC).toAbsolutePath();
        return defaultLic.toString();
    }

    public static String defaultSignatureDirectory() {
        return getDefaultDir("");
    }

    public static String getDefaultDir(String folder) {
        String dir = DOCUMENT_SAMPLES + File.separator + folder;
        Path path = FileSystems.getDefault().getPath(dir).toAbsolutePath();
        makeDirs(path.toFile());
        return path.toString();
    }

    private static void makeDirs(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static String relativePathToAbsolute(String path) {
        Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();

        if (StringUtils.isNotEmpty(path)) {
            for (Path root : rootDirectories) {
                if (path.startsWith(root.toString())) {
                    makeDirs(new File(path));
                    return path;
                }
            }
        }

        Path absolutePath = FileSystems.getDefault().getPath(path).toAbsolutePath();
        makeDirs(absolutePath.toFile());
        return absolutePath.toString();
    }
}
