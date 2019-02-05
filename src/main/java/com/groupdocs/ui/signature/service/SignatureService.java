package com.groupdocs.ui.signature.service;

import com.groupdocs.ui.common.entity.web.FileDescriptionEntity;
import com.groupdocs.ui.common.entity.web.LoadDocumentEntity;
import com.groupdocs.ui.common.entity.web.PageDescriptionEntity;
import com.groupdocs.ui.common.entity.web.request.LoadDocumentPageRequest;
import com.groupdocs.ui.common.entity.web.request.LoadDocumentRequest;
import com.groupdocs.ui.signature.entity.request.*;
import com.groupdocs.ui.signature.entity.web.SignatureFileDescriptionEntity;
import com.groupdocs.ui.signature.entity.web.SignaturePageEntity;
import com.groupdocs.ui.signature.entity.web.SignedDocumentEntity;
import com.groupdocs.ui.signature.entity.xml.OpticalXmlEntity;
import com.groupdocs.ui.signature.entity.xml.TextXmlEntity;

import java.util.List;

/**
 * Service for working with signature api
 */
public interface SignatureService {

    /**
     * Get list of files in directory
     *
     * @param fileTreeRequest model with path parameter
     * @return list of files
     */
    List<SignatureFileDescriptionEntity> getFileList(SignatureFileTreeRequest fileTreeRequest);

    /**
     * Load document descriptions
     *
     * @param loadDocumentRequest document request data
     * @return list of document descriptions
     */
    LoadDocumentEntity getDocumentDescription(LoadDocumentRequest loadDocumentRequest);

    /**
     * Load document page
     *
     * @param loadDocumentPageRequest document page request data
     * @return loaded document page
     */
    PageDescriptionEntity loadDocumentPage(LoadDocumentPageRequest loadDocumentPageRequest);

    /**
     * Save stamp signature
     *
     * @param saveStampRequest save signature request data
     * @return signature file description
     */
    FileDescriptionEntity saveStamp(SaveStampRequest saveStampRequest);

    /**
     * Save optical code signature
     *
     * @param saveOpticalCodeRequest save signature request data
     * @return optical code signature
     */
    OpticalXmlEntity saveOpticalCode(SaveOpticalCodeRequest saveOpticalCodeRequest);

    /**
     * Save test signature
     *
     * @param saveTextRequest save signature request data
     * @return text signature
     */
    TextXmlEntity saveText(SaveTextRequest saveTextRequest);

    /**
     * @param saveImageRequest save signature request data
     * @return signature file description
     */
    FileDescriptionEntity saveImage(SaveImageRequest saveImageRequest);

    /**
     * Delete signature file from local storage
     *
     * @param deleteSignatureFileRequest
     */
    void deleteSignatureFile(DeleteSignatureFileRequest deleteSignatureFileRequest);

    /**
     * Get list of fonts names
     *
     * @return list of fonts names
     */
    List<String> getFonts();

    /**
     * Load image of signature
     *
     * @param loadSignatureImageRequest
     * @return
     */
    SignaturePageEntity loadSignatureImage(LoadSignatureImageRequest loadSignatureImageRequest);

    /**
     * Sign document
     *
     * @param signDocumentRequest
     * @return
     */
    SignedDocumentEntity sign(SignDocumentRequest signDocumentRequest);
}
