package com.groupdocs.ui.signature.service;

import com.groupdocs.signature.config.SignatureConfig;
import com.groupdocs.signature.domain.enums.HorizontalAlignment;
import com.groupdocs.signature.domain.enums.VerticalAlignment;
import com.groupdocs.signature.handler.SignatureHandler;
import com.groupdocs.signature.options.OutputType;
import com.groupdocs.signature.options.SignatureOptionsCollection;
import com.groupdocs.signature.options.saveoptions.SaveOptions;
import com.groupdocs.ui.common.config.GlobalConfiguration;
import com.groupdocs.ui.common.entity.web.FileDescriptionEntity;
import com.groupdocs.ui.common.exception.TotalGroupDocsException;
import com.groupdocs.ui.signature.config.SignatureConfiguration;
import com.groupdocs.ui.signature.entity.request.SaveImageRequest;
import com.groupdocs.ui.signature.entity.request.SaveOpticalCodeRequest;
import com.groupdocs.ui.signature.entity.request.SaveStampRequest;
import com.groupdocs.ui.signature.entity.request.SaveTextRequest;
import com.groupdocs.ui.signature.entity.web.SignatureDataEntity;
import com.groupdocs.ui.signature.entity.xml.*;
import com.groupdocs.ui.signature.signer.BarCodeSigner;
import com.groupdocs.ui.signature.signer.QrCodeSigner;
import com.groupdocs.ui.signature.signer.Signer;
import com.groupdocs.ui.signature.signer.TextSigner;
import com.groupdocs.ui.signature.util.XMLReaderWriter;
import com.groupdocs.ui.signature.util.directory.SignatureDirectory;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

import static com.groupdocs.ui.common.util.Utils.getBufferedImage;
import static com.groupdocs.ui.common.util.Utils.getFileWithUniqueName;
import static com.groupdocs.ui.signature.service.SignatureHandlerFactory.getFullDataPath;
import static com.groupdocs.ui.signature.util.SignatureType.QR_CODE;
import static com.groupdocs.ui.signature.util.directory.PathConstants.OUTPUT_FOLDER;
import static com.groupdocs.ui.signature.util.directory.SignatureDirectory.*;

public class SaveSignatureServiceImpl implements SaveSignatureService {
    private static final Logger logger = LoggerFactory.getLogger(SaveSignatureServiceImpl.class);

    public static final String PNG = "png";

    private SignatureHandler signatureHandler;
    private SignatureConfiguration signatureConfiguration;

    public SaveSignatureServiceImpl(GlobalConfiguration globalConfiguration) {
        this.signatureConfiguration = globalConfiguration.getSignature();
        signatureHandler = SignatureHandlerFactory.createHandler(signatureConfiguration.getFilesDirectory(), signatureConfiguration.getDataDirectory());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public FileDescriptionEntity saveStamp(SaveStampRequest saveStampRequest) {
        String previewPath = getFullDataPath(signatureConfiguration.getDataDirectory(), STAMP_DATA_DIRECTORY.getPreviewPath());
        String xmlPath = getFullDataPath(signatureConfiguration.getDataDirectory(), STAMP_DATA_DIRECTORY.getXMLPath());
        try {
            // get/set parameters
            String encodedImage = saveStampRequest.getImage().replace("data:image/png;base64,", "");
            List<StampXmlEntity> stampData = saveStampRequest.getStampData();

            File file = getFileWithUniqueName(previewPath, "");
            byte[] decodedImg = Base64.getDecoder().decode(encodedImage.getBytes(StandardCharsets.UTF_8));
            Files.write(file.toPath(), decodedImg);
            // stamp data to xml file saving
            StampXmlEntityList stampXmlEntityList = new StampXmlEntityList();
            stampXmlEntityList.setStampXmlEntityList(stampData);
            String xmlFileName = FilenameUtils.removeExtension(file.getName());
            String fileName = String.format("%s%s%s.xml", xmlPath, File.separator, xmlFileName);
            new XMLReaderWriter<StampXmlEntityList>().write(fileName, stampXmlEntityList);

            FileDescriptionEntity savedImage = new FileDescriptionEntity();
            savedImage.setGuid(file.toPath().toString());
            // return loaded page object
            return savedImage;
        } catch (Exception ex) {
            logger.error("Exception occurred while saving stamp", ex);
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OpticalXmlEntity saveOpticalCode(SaveOpticalCodeRequest saveOpticalCodeRequest) {
        OpticalXmlEntity signatureData = saveOpticalCodeRequest.getProperties();
        // initiate signature data wrapper with default values
        SignatureDataEntity signatureDataEntity = getSignatureDataEntity(200, 270);

        // initiate signature options collection
        SignatureOptionsCollection collection = new SignatureOptionsCollection();

        Signer signer = QR_CODE.equals(saveOpticalCodeRequest.getSignatureType()) ?
                new QrCodeSigner(signatureData, signatureDataEntity) :
                new BarCodeSigner(signatureData, signatureDataEntity);

        collection.add(signer.signImage());

        SignatureDirectory dataDirectory = QR_CODE.equals(saveOpticalCodeRequest.getSignatureType()) ? QRCODE_DATA_DIRECTORY : BARCODE_DATA_DIRECTORY;
        String encodedImage = createAndSaveOpticalCode(signatureData, signatureDataEntity, dataDirectory, collection);
        signatureData.setEncodedImage(encodedImage);
        signatureData.setWidth(signatureDataEntity.getImageWidth());
        signatureData.setHeight(signatureDataEntity.getImageHeight());
        return signatureData;
    }

    /**
     * Create and save image with optical code
     *
     * @param signatureData
     * @param signatureDataEntity
     * @param dataDirectory
     * @param collection
     * @return encoded image
     */
    private String createAndSaveOpticalCode(OpticalXmlEntity signatureData, SignatureDataEntity signatureDataEntity, SignatureDirectory dataDirectory, SignatureOptionsCollection collection) {
        // get preview path
        String previewPath = getFullDataPath(signatureConfiguration.getDataDirectory(), dataDirectory.getPreviewPath());
        // get xml file path
        String xmlPath = getFullDataPath(signatureConfiguration.getDataDirectory(), dataDirectory.getXMLPath());
        try {
            if (signatureData.getTemp()) {
                BufferedImage bufImage = getBufferedImage(signatureDataEntity.getImageWidth(), signatureDataEntity.getImageHeight());
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(bufImage, PNG, os);
                InputStream is = new ByteArrayInputStream(os.toByteArray());
                return signWithImageToStream(collection, is);
            } else {
                File file = writeImageFile(signatureData.getImageGuid(), signatureDataEntity, previewPath);
                String fileName = FilenameUtils.removeExtension(file.getName());
                // Save data to xml file
                new XMLReaderWriter<OpticalXmlEntity>().write(String.format("%s%s%s.xml", xmlPath, File.separator, fileName), signatureData);
                return signWithImageToFile(previewPath, signatureData, collection, file.toPath().toString());
            }
        } catch (Exception e) {
            logger.error("Exception occurred while saving optical code signature", e);
            throw new TotalGroupDocsException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TextXmlEntity saveText(SaveTextRequest saveTextRequest) {
        String previewPath = getFullDataPath(signatureConfiguration.getDataDirectory(), TEXT_DATA_DIRECTORY.getPreviewPath());
        String xmlPath = getFullDataPath(signatureConfiguration.getDataDirectory(), TEXT_DATA_DIRECTORY.getXMLPath());
        TextXmlEntity signatureData = saveTextRequest.getProperties();
        // initiate signature data wrapper with default values
        SignatureDataEntity signatureDataEntity = getSignatureDataEntity(signatureData.getWidth(), signatureData.getHeight());
        File file = writeImageFile(signatureData.getImageGuid(), signatureDataEntity, previewPath);
        try {
            String fileName = FilenameUtils.removeExtension(file.getName());
            // Save data to xml file
            new XMLReaderWriter<TextXmlEntity>().write(String.format("%s%s%s.xml", xmlPath, File.separator, fileName), signatureData);
        } catch (JAXBException e) {
            logger.error("Exception occurred while saving text signature", e);
            throw new TotalGroupDocsException(e.getMessage(), e);
        }
        // initiate signer object
        TextSigner textSigner = new TextSigner(signatureData, signatureDataEntity);
        // initiate signature options collection
        SignatureOptionsCollection collection = new SignatureOptionsCollection();
        // generate unique file names for preview image and xml file
        collection.add(textSigner.signImage());
        String encodedImage = signWithImageToFile(previewPath, signatureData, collection, file.toPath().toString());
        signatureData.setEncodedImage(encodedImage);
        return signatureData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileDescriptionEntity saveImage(SaveImageRequest saveImageRequest) {
        try {
            String dataDirectoryPath = getFullDataPath(signatureConfiguration.getDataDirectory(), IMAGE_DATA_DIRECTORY.getPath());
            File file = getFileWithUniqueName(dataDirectoryPath, "");
            String encodedImage = saveImageRequest.getImage().replace("data:image/png;base64,", "");
            byte[] decodedImg = Base64.getDecoder().decode(encodedImage.getBytes(StandardCharsets.UTF_8));
            Files.write(file.toPath(), decodedImg);

            FileDescriptionEntity savedImage = new FileDescriptionEntity();
            savedImage.setGuid(file.getAbsolutePath());
            // return loaded page object
            return savedImage;
        } catch (Exception ex) {
            logger.error("Exception occurred while saving image signature", ex);
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
    }

    /**
     * Write image to file
     *
     * @param imageGuid      image file guid if it exists
     * @param signaturesData signature
     * @param previewPath    path to file
     * @return
     */
    private File writeImageFile(String imageGuid, SignatureDataEntity signaturesData, String previewPath) {
        File file = getFileWithUniqueName(previewPath, imageGuid);
        try {
            BufferedImage bufImage = getBufferedImage(signaturesData.getImageWidth(), signaturesData.getImageHeight());
            // save BufferedImage to file
            ImageIO.write(bufImage, PNG, file);
        } catch (Exception ex) {
            logger.error("Exception occurred while saving signatures image", ex);
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
        return file;
    }

    /**
     * Sign image with signature data for saving in stream
     *
     * @param collection  signature options
     * @param inputStream stream with image, for temporally sign
     * @return encoded image
     */
    private String signWithImageToStream(SignatureOptionsCollection collection, InputStream inputStream) {
        try {
            final SaveOptions saveOptions = new SaveOptions();
            saveOptions.setOutputType(OutputType.Stream);
            // sign generated image with signature
            SignatureConfig config = new SignatureConfig();
            config.setOutputPath(FileSystems.getDefault().getPath("").toAbsolutePath().toString());
            SignatureHandler<OutputStream> imgSignatureHandler = new SignatureHandler<>(config);
            ByteArrayOutputStream bos = (ByteArrayOutputStream) imgSignatureHandler.sign(inputStream, collection, saveOptions);
            byte[] bytes = bos.toByteArray();
            // encode ByteArray into String
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception ex) {
            logger.error("Exception occurred while saving optical code signature", ex);
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
    }

    /**
     * Sign image with signature data for saving in local storage
     *
     * @param previewPath   local storage path
     * @param signatureData signature
     * @param collection    signature options
     * @param path          path to file
     * @return encoded image
     */
    private String signWithImageToFile(String previewPath, XmlEntityWithImage signatureData, SignatureOptionsCollection collection, String path) {
        try {
            // set signing save options
            final SaveOptions saveOptions = new SaveOptions();
            saveOptions.setOutputType(OutputType.String);
            saveOptions.setOutputFileName(FilenameUtils.getName(path));
            saveOptions.setOverwriteExistingFiles(true);
            // set temporary signed documents path to image previews folder
            signatureHandler.getSignatureConfig().setOutputPath(previewPath);
            // sign generated image with signature
            signatureHandler.sign(path, collection, saveOptions);
            // set signed documents path back to correct path
            signatureHandler.getSignatureConfig().setOutputPath(getFullDataPath(signatureConfiguration.getDataDirectory(), OUTPUT_FOLDER));
            // set data for response
            signatureData.setImageGuid(path);
            // get signature preview as Base64 String
            byte[] bytes = signatureHandler.getPageImage(path, 1, "", null, 100);
            // encode ByteArray into String
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception ex) {
            logger.error("Exception occurred while saving optical code signature", ex);
            throw new TotalGroupDocsException(ex.getMessage(), ex);
        }
    }

    /**
     * Get filled signature data
     *
     * @param height
     * @param width
     * @return
     */
    private SignatureDataEntity getSignatureDataEntity(int height, int width) {
        SignatureDataEntity signatureDataEntity = new SignatureDataEntity();
        signatureDataEntity.setHorizontalAlignment(HorizontalAlignment.Center);
        signatureDataEntity.setVerticalAlignment(VerticalAlignment.Center);
        signatureDataEntity.setImageHeight(height);
        signatureDataEntity.setImageWidth(width);
        signatureDataEntity.setLeft(0);
        signatureDataEntity.setTop(0);
        return signatureDataEntity;
    }
}
