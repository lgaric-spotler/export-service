package com.spotler.core.business;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.spotler.jobs.DataLakeExportJob;
import io.dropwizard.setup.Environment;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class ExportManager {

    private final Injector injector;
    private final Environment environment;

    @Inject
    public ExportManager(Injector injector, Environment environment) {
        this.injector = injector;
        this.environment = environment;
    }

    public void scheduleExporters() {
        runDataLakeExport(false);
    }

    public void runDataLakeExport(boolean runNow) {
        DataLakeExportJob dataLakeExportJob = injector.getInstance(DataLakeExportJob.class);

        if (runNow) {
            environment.lifecycle()
                    .scheduledExecutorService("DataLakeExportJob-%d")
                    .build()
                    .schedule(dataLakeExportJob, 0, TimeUnit.SECONDS);
        } else {
            long delay = Duration.between(
                    LocalDateTime.now(),
                    LocalDateTime.of(LocalDateTime.now().toLocalDate().plusDays(1), LocalTime.MIDNIGHT)
            ).toMillis();
            environment.lifecycle()
                    .scheduledExecutorService("DataLakeExportJob-%d")
                    .build()
                    .scheduleAtFixedRate(dataLakeExportJob, delay, 1, TimeUnit.DAYS);
        }
    }
}
