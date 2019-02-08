package com.groupdocs.ui.signature.service;

import com.groupdocs.ui.signature.entity.request.SignDocumentRequest;
import com.groupdocs.ui.signature.entity.web.SignedDocumentEntity;

public interface SignService {
    /**
     * Sign document
     *
     * @param signDocumentRequest
     * @return
     */
    SignedDocumentEntity sign(SignDocumentRequest signDocumentRequest);
}
