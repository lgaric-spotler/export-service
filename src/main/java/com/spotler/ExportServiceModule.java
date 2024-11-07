package com.spotler;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.spotler.core.exporter.SFTPExporter;
import com.spotler.service.SessionService;
import com.spotler.service.impl.SessionServiceImpl;
import io.dropwizard.db.ManagedDataSource;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.oauth1.ConsumerCredentials;
import org.glassfish.jersey.client.oauth1.OAuth1ClientSupport;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Feature;

public class ExportServiceModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public ManagedDataSource mysqlConnection(ExportServiceConfiguration config, MetricRegistry metricRegistry) {
        return config.getDatabase().build(metricRegistry, "MysqlConnection");
    }

    @Provides
    public DSLContext dslContext(ManagedDataSource managedDataSource) {
        return DSL.using(managedDataSource, SQLDialect.MARIADB);
    }

    @Provides
    @Singleton
    @Named("mailPlus")
    public JerseyClient jerseyClient(ClientBuilder clientBuilder, ExportServiceConfiguration config) {
        JerseyClient client = (JerseyClient) clientBuilder.build();
        client.property(ClientProperties.CONNECT_TIMEOUT, config.getMailPlus().getTimeout());
        client.property(ClientProperties.READ_TIMEOUT, config.getMailPlus().getTimeout());

        ConsumerCredentials credentials = new ConsumerCredentials(config.getMailPlus().getConsumerKey(),
                config.getMailPlus().getConsumerSecret());

        Feature feature = OAuth1ClientSupport.builder(credentials).signatureMethod("HMAC-SHA1").feature().build();
        client.register(feature);
        return client;
    }

    @Provides
    @Singleton
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new Jdk8Module());
        return objectMapper;
    }

    @Provides
    public ClientBuilder clientBuilder(ObjectMapper objectMapper) {
        JacksonJsonProvider jacksonJsonProvider = new JacksonJsonProvider();
        jacksonJsonProvider.setMapper(objectMapper);
        ClientConfig clientConfig = new ClientConfig(jacksonJsonProvider);
        clientConfig.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        return ClientBuilder.newBuilder().withConfig(clientConfig);
    }

    @Provides
    public SessionService sessionService(final ExportServiceConfiguration configuration, ClientBuilder clientBuilder) {
        return new SessionServiceImpl(clientBuilder, configuration.getAccountManagementUrl().toString());
    }

    @Provides
    @Named("dataLake")
    public SFTPExporter sftpExporter(final ExportServiceConfiguration configuration) {
        return new SFTPExporter(configuration.getDataLake().getSftpHost(), configuration.getDataLake().getSftpUsername(), configuration.getDataLake().getSftpPassword());
    }
}
