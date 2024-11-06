package com.spotler;

import com.google.inject.Injector;
import com.spotler.core.business.ExportManager;
import com.spotler.resources.AdminResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ru.vyarus.dropwizard.guice.GuiceBundle;

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
        Injector injector = guiceBundle.getInjector();
        environment.jersey().register(injector.getInstance(AdminResource.class));

        injector.getInstance(ExportManager.class).scheduleExporters();
    }
}
