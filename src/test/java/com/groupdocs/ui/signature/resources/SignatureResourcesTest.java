package com.groupdocs.ui.signature.resources;

import com.groupdocs.ui.common.MainService;
import com.groupdocs.ui.common.config.GlobalConfiguration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.io.File;
import static org.assertj.core.api.Assertions.assertThat;


public class SignatureResourcesTest {
    @ClassRule
    public static final DropwizardAppRule<GlobalConfiguration> RULE =
            new DropwizardAppRule<>(MainService.class, System.getProperty("user.dir") + File.separator + "configuration.yml");

    @Test
    public void getView() {
        Client client = new JerseyClientBuilder(RULE.getEnvironment()).build("test client");
        Response response = client.target(
                String.format("http://localhost:%d/signature", RULE.getLocalPort()))
                .request()
                .get();
        assertThat(response.getStatus()).isEqualTo(200);
    }

    void loadFileTree() {
    }

    void loadDocumentDescription() {
    }

    void loadDocumentPage() {
    }

    void downloadDocument() {
    }

    void uploadDocument() {
    }

    void loadSignatureImage() {
    }

    void signDigital() {
    }

    void signImage() {
    }
}