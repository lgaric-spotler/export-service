package com.spotler.jobs;

import com.google.inject.Inject;
import com.spotler.ExportServiceConfiguration;
import com.spotler.core.business.service.DataLakeService;
import com.spotler.core.exporter.SFTPExporter;
import com.spotler.model.datalake.Environment;
import com.spotler.model.datalake.User;
import com.spotler.util.CSVWriterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.io.Writer;
import java.time.LocalDate;
import java.util.List;

public class DataLakeExportJob extends Job {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final SFTPExporter sftpExporter;
    private final DataLakeService dataLakeService;
    private final int maxRetries;

    @Inject
    public DataLakeExportJob(@Named("dataLake") SFTPExporter sftpExporter, ExportServiceConfiguration exportServiceConfiguration, DataLakeService dataLakeService) {
        this.sftpExporter = sftpExporter;
        this.dataLakeService = dataLakeService;

        Integer maxRetriesConfig = exportServiceConfiguration.getDataLake().getMaxRetries();
        this.maxRetries = maxRetriesConfig == null ? 0 : maxRetriesConfig;
    }


    @Override
    protected void doRun() {
        LOGGER.info("DataLakeExport START!");
        int attempt = 1;

        boolean upload = false;
        if (upload) {
            try {
                exportData();
            } catch (Exception ex) {
                if (attempt <= maxRetries) {
                    LOGGER.error("Error occurred when exporting data to DataLake on attempt {}/{}! Retrying...", attempt, maxRetries, ex);
                    exportData();
                } else {
                    LOGGER.error("Error occurred when exporting data to DataLake on attempt {}/{}! Stopping...", attempt, maxRetries, ex);
                }
            }
        }

        LOGGER.info("DataLakeExport END!");
    }

    private void exportData() {
        exportEnvironmentData();
        exportUserData();
        exportUsageData();
    }

    private void exportEnvironmentData() {
        List<Environment> environmentData = dataLakeService.getEnvironmentData();

        try (Writer environmentDataWriter = CSVWriterUtil.write(environmentData)) {
            sftpExporter.uploadFile(environmentDataWriter.toString(), String.format("/ftp/mailplus/MailPlus_Environment_%s.csv", LocalDate.now()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void exportUserData() {
        List<User> userData = dataLakeService.getUserData();

        try (Writer environmentDataWriter = CSVWriterUtil.write(userData)) {
            sftpExporter.uploadFile(environmentDataWriter.toString(), String.format("/ftp/mailplus/MailPlus_User_%s.csv", LocalDate.now()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void exportUsageData() {

    }
}
