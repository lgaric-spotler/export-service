package com.spotler.core.business.service;

import com.spotler.ExportServiceConfiguration;
import com.spotler.service.SessionService;
import com.spotler.service.SpotlerAccountService;
import com.spotler.service.SpotlerUserService;
import com.spotler.service.impl.SpotlerAccountServiceImpl;
import com.spotler.service.impl.SpotlerUserServiceImpl;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;

public class AMSServiceFactory {

    private final SessionService sessionService;
    private final ClientBuilder clientBuilder;
    private final String accountManagementUrl;

    @Inject
    public AMSServiceFactory(SessionService sessionService, ClientBuilder clientBuilder, ExportServiceConfiguration exportServiceConfiguration) {
        this.sessionService = sessionService;
        this.accountManagementUrl = exportServiceConfiguration.getAccountManagementUrl().toString();
        this.clientBuilder = clientBuilder;
    }

    public SpotlerAccountService buildSpotlerAccountService() {
        String bearerToken = sessionService.getSystemSession().getBearerToken();
        return new SpotlerAccountServiceImpl(clientBuilder, accountManagementUrl, bearerToken);
    }

    public SpotlerUserService buildSpotlerUserService() {
        String bearerToken = sessionService.getSystemSession().getBearerToken();
        return new SpotlerUserServiceImpl(clientBuilder, accountManagementUrl, bearerToken);
    }
}