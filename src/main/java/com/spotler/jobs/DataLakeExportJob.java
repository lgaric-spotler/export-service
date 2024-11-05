package com.spotler.jobs;

import com.google.inject.Inject;
import com.spotler.core.apiclients.AMSClient;
import com.spotler.core.business.service.AccountServiceFactory;
import com.spotler.core.exporter.SFTPExporter;
import com.spotler.core.model.account.Account;
import com.spotler.core.model.account.DurationType;
import com.spotler.model.search.Filter;
import com.spotler.model.search.ListParams;
import com.spotler.service.SpotlerAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class DataLakeExportJob extends Job {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final AMSClient amsClient;
    private final SFTPExporter sftpExporter;

    @Inject
    public DataLakeExportJob(AMSClient amsClient, SFTPExporter sftpExporter) {
        this.amsClient = amsClient;
        this.sftpExporter = sftpExporter;
    }


    @Override
    protected void doRun() {
        LOGGER.info("DataLakeExport START!");

        boolean upload = false;
        if (upload) {
            exportEnvironmentData();
        }

        LOGGER.info("DataLakeExport END!");
    }

    private void exportEnvironmentData() {
        List<Account> accounts = amsClient.getAllAccounts();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             Writer writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8)) {
            writer.write("AccountID,AccountName,ProductType,CreationDate,LastLoginDate,ExpiryDate,TestAccount,SSOLicense,B2BLicense,IntegrationLicense\n");

            // Write account data
            for (Account account : accounts) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        account.getId(),
                        account.getName(),
                        account.getProductType(),
                        account.getCreatedAt(),
                        account.getLastLoginAt(),
                        account.getExpiresAt(),
                        "N", // test account?
                        "N", // SSO license
                        "N", // B2B license
                        "N" // Integration license
                        ));
            }
            writer.flush();

            try (InputStream inputStream = new ByteArrayInputStream(baos.toByteArray())) {
                sftpExporter.uploadFile(inputStream, "/ftp/mailplus/environment.csv");
            }
        } catch (Exception e) {
            LOGGER.error("Error exporting environment data", e);
        }
    }
}
