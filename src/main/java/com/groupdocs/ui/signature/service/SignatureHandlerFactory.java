package com.groupdocs.ui.signature.service;

import com.groupdocs.signature.config.SignatureConfig;
import com.groupdocs.signature.handler.SignatureHandler;
import org.apache.commons.lang3.StringUtils;

import static com.groupdocs.ui.signature.util.directory.PathConstants.OUTPUT_FOLDER;
import static com.groupdocs.ui.signature.util.directory.SignatureDirectory.CERTIFICATE_DATA_DIRECTORY;
import static com.groupdocs.ui.signature.util.directory.SignatureDirectory.IMAGE_DATA_DIRECTORY;

public class SignatureHandlerFactory {

    public static SignatureHandler instance;

    /**
     * Create instance of SignatureHandler
     *
     * @param filesDirectory
     * @param dataDirectory
     * @return
     */
    public synchronized static SignatureHandler createHandler(String filesDirectory, String dataDirectory) {
        if (instance == null) {
            // create signature application configuration
            SignatureConfig config = new SignatureConfig();
            config.setStoragePath(filesDirectory);
            String directory = StringUtils.isEmpty(dataDirectory) ? filesDirectory : dataDirectory;
            config.setCertificatesPath(getFullDataPath(directory, CERTIFICATE_DATA_DIRECTORY.getPath()));
            config.setImagesPath(getFullDataPath(directory, IMAGE_DATA_DIRECTORY.getPath()));
            config.setOutputPath(getFullDataPath(directory, OUTPUT_FOLDER));

            instance = new SignatureHandler(config);
        }
        return instance;
    }

    public static String getFullDataPath(String dataDirectory, String partPath) {
        return String.format("%s%s", dataDirectory, partPath);
    }
}
