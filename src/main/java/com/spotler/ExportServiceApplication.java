package com.spotler;

import com.google.inject.Injector;
import com.spotler.jobs.DataLakeExportJob;
import com.spotler.resources.AdminResource;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.AdminEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ru.vyarus.dropwizard.guice.GuiceBundle;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class ExportServiceApplication extends Application<ExportServiceConfiguration> {

    private GuiceBundle guiceBundle;

    public static void main(final String[] args) throws Exception {
        new ExportServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "ExportService";
    }

    @Override
    public void initialize(final Bootstrap<ExportServiceConfiguration> bootstrap) {
        this.guiceBundle = GuiceBundle.builder().modules(new ExportServiceModule()).build();
        bootstrap.addBundle(guiceBundle);
    }

    @Override
    public void run(final ExportServiceConfiguration configuration,
                    final Environment environment) {
        //createDataLakeExportJob(environment, guiceBundle.getInjector());
        createTestDataLakeExportJob(environment, guiceBundle.getInjector());

        Injector injector = guiceBundle.getInjector();
        environment.jersey().register(injector.getInstance(AdminResource.class));
    }

    private void createDataLakeExportJob(Environment environment, Injector injector) {
        DataLakeExportJob dataLakeExportJob = injector.getInstance(DataLakeExportJob.class);
        long delay = Duration.between(LocalTime.now(), LocalTime.MIDNIGHT).toMillis();

        environment.lifecycle()
                .scheduledExecutorService("DataLakeExportJob-%d")
                .build()
                .scheduleAtFixedRate(dataLakeExportJob, delay, 1, TimeUnit.DAYS);
    }

    private void createTestDataLakeExportJob(Environment environment, Injector injector) {
        DataLakeExportJob dataLakeExportJob = injector.getInstance(DataLakeExportJob.class);

        environment.lifecycle()
                .scheduledExecutorService("DataLakeExportJob-%d")
                .build()
                .scheduleAtFixedRate(dataLakeExportJob, 0, 10L, TimeUnit.SECONDS);
    }
}
