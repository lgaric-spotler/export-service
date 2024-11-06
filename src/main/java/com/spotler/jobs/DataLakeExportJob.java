package com.spotler.jobs;

import com.google.inject.Inject;
import com.spotler.ExportServiceConfiguration;
import com.spotler.core.business.service.DataLakeService;
import com.spotler.core.exporter.SFTPExporter;
import com.spotler.model.datalake.Environment;
import com.spotler.model.datalake.Usage;
import com.spotler.model.datalake.User;
import com.spotler.util.CSVWriterUtil;
import com.spotler.util.RetryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.io.Writer;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class DataLakeExportJob extends Job {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final AtomicBoolean isRunning = new AtomicBoolean(false);
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
        if (!isRunning.compareAndSet(false, true)) {
            LOGGER.warn("DataLakeExportJob is already running.");
            return;
        }

        try {
            LOGGER.info("DataLakeExport START!");

            List<Environment> environmentData = RetryUtil.retry("GetEnvironmentData", dataLakeService::getEnvironmentData, maxRetries);
            RetryUtil.retry("ExportEnvironmentData", () -> exportEnvironmentData(environmentData), maxRetries);

            List<User> userData = RetryUtil.retry("GetUserData", dataLakeService::getUserData, maxRetries);
            RetryUtil.retry("ExportUserData", () -> exportUserData(userData), maxRetries);

            List<Usage> usageData = RetryUtil.retry("GetUsageData", dataLakeService::getUsageData, maxRetries);
            RetryUtil.retry("ExportUsageData", () -> exportUsageData(usageData), maxRetries);

            LOGGER.info("DataLakeExport END!");
        } catch (Exception e) {
            LOGGER.error("Error while exporting data to DataLake!", e);
        } finally {
            isRunning.set(false);
        }
    }

    private void exportEnvironmentData(List<Environment> environmentData) {
        LOGGER.info("Exporting environment data...");
        try (Writer environmentDataWriter = CSVWriterUtil.write(environmentData)) {
            sftpExporter.uploadFile(environmentDataWriter.toString(), String.format("/ftp/mailplus/MailPlus_Environment_%s.csv", LocalDate.now()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("Environment data export done!");
    }

    private void exportUserData(List<User> userData) {
        LOGGER.info("Exporting user data...");
        try (Writer environmentDataWriter = CSVWriterUtil.write(userData)) {
            sftpExporter.uploadFile(environmentDataWriter.toString(), String.format("/ftp/mailplus/MailPlus_User_%s.csv", LocalDate.now()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("User data export done!");
    }

    private void exportUsageData(List<Usage> usageData) {
        LOGGER.info("Exporting usage data...");
        try (Writer environmentDataWriter = CSVWriterUtil.write(usageData)) {
            sftpExporter.uploadFile(environmentDataWriter.toString(), String.format("/ftp/mailplus/MailPlus_Usage_%s.csv", LocalDate.now()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("Usage data export done!");
    }

    public boolean isRunning() {
        return isRunning.get();
    }
}
