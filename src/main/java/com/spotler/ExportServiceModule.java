package com.spotler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.spotler.service.SessionService;
import com.spotler.service.impl.SessionServiceImpl;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import javax.inject.Singleton;
import javax.ws.rs.client.ClientBuilder;

public class ExportServiceModule extends AbstractModule {

    @Override
    protected void configure() {
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
}
