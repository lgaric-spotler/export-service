package com.spotler;

import com.spotler.jobs.config.DataLakeConfiguration;
import io.dropwizard.Configuration;

import java.net.URI;

public class ExportServiceConfiguration extends Configuration {
    private URI accountManagementUrl;
    private DataLakeConfiguration dataLake;

    public DataLakeConfiguration getDataLake() {
        return dataLake;
    }

    public void setDataLake(DataLakeConfiguration dataLake) {
        this.dataLake = dataLake;
    }

    public URI getAccountManagementUrl() {
        return accountManagementUrl;
    }

    public void setAccountManagementUrl(URI accountManagementUrl) {
        this.accountManagementUrl = accountManagementUrl;
    }
}
