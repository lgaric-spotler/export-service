package com.spotler;

import com.google.inject.Injector;
import com.spotler.core.business.ExportManager;
import com.spotler.resources.AdminResource;
import io.dropwizard.Application;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ru.vyarus.dropwizard.guice.GuiceBundle;

public class ExportServiceApplication<T extends ExportServiceConfiguration> extends Application<ExportServiceConfiguration> {

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
        MigrationsBundle<T> migrationsBundle = new MigrationsBundle<>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(T t) {
                return t.getDatabase();
            }
        };

        this.guiceBundle = GuiceBundle.builder().modules(new ExportServiceModule()).dropwizardBundles(migrationsBundle).build();
        bootstrap.addBundle(guiceBundle);
    }

    @Override
    public void run(final ExportServiceConfiguration configuration,
                    final Environment environment) {
        Injector injector = guiceBundle.getInjector();
        environment.jersey().register(injector.getInstance(AdminResource.class));

        injector.getInstance(ExportManager.class).scheduleExporters();
    }
}
