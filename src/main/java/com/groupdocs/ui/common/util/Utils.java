package com.groupdocs.ui.common.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class Utils {
    /**
     * Rename file if exist
     *
     * @param directory directory where files are located
     * @param fileName  file name
     * @return new file with new file name
     */
    public static File getFreeFileName(String directory, String fileName) {
        File file = null;
        try {
            File folder = new File(directory);
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                int number = i + 1;
                String newFileName = FilenameUtils.removeExtension(fileName) + "-Copy(" + number + ")." + FilenameUtils.getExtension(fileName);
                file = new File(directory + File.separator + newFileName);
                if (file.exists()) {
                    continue;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * Create file in previewPath and name imageGuid
     * if the file is already exist, create new file with next number in name
     * examples, 001, 002, 003, etc
     *
     * @param previewPath path to file folder
     * @param imageGuid   file name
     * @return created file
     */
    public static File getFile(String previewPath, String imageGuid) {
        File folder = new File(previewPath);
        File[] listOfFiles = folder.listFiles();
        if (!StringUtils.isEmpty(imageGuid)) {
            return new File(imageGuid);
        } else {
            for (int i = 0; i <= listOfFiles.length; i++) {
                int number = i + 1;
                // set file name, for example 001
                String fileName = String.format("%03d", number);
                File file = new File(String.format("%s%s%s.png", previewPath, File.separator, fileName));
                // check if file with such name already exists
                if (file.exists()) {
                    continue;
                } else {
                    return file;
                }
            }
            return new File(String.format("%s%s001.png", previewPath, File.separator));
        }
    }
}
