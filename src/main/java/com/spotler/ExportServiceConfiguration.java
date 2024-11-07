package com.spotler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spotler.core.apiclients.config.MailPlusConfiguration;
import com.spotler.jobs.config.DataLakeConfiguration;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

public class ExportServiceConfiguration extends Configuration {
    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();
    private URI accountManagementUrl;
    private MailPlusConfiguration mailPlus;
    private DataLakeConfiguration dataLake;

    @JsonProperty("database")
    public DataSourceFactory getDatabase() {
        return database;
    }

    @JsonProperty("database")
    public void setDatabase(DataSourceFactory database) {
        this.database = database;
    }

    @JsonProperty("accountManagementUrl")
    public URI getAccountManagementUrl() {
        return accountManagementUrl;
    }

    @JsonProperty("accountManagementUrl")
    public void setAccountManagementUrl(URI accountManagementUrl) {
        this.accountManagementUrl = accountManagementUrl;
    }

    @JsonProperty("mailPlus")
    public MailPlusConfiguration getMailPlus() {
        return mailPlus;
    }

    @JsonProperty("mailPlus")
    public void setMailPlus(MailPlusConfiguration mailPlus) {
        this.mailPlus = mailPlus;
    }

    @JsonProperty("dataLake")
    public DataLakeConfiguration getDataLake() {
        return dataLake;
    }

    @JsonProperty("dataLake")
    public void setDataLake(DataLakeConfiguration dataLake) {
        this.dataLake = dataLake;
    }
}
