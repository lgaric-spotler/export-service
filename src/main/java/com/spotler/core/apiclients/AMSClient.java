package com.spotler.core.apiclients;

import com.google.inject.Inject;
import com.spotler.core.business.service.AMSServiceFactory;
import com.spotler.core.model.account.Account;
import com.spotler.core.model.account.DurationType;
import com.spotler.core.model.user.User;
import com.spotler.model.search.Filter;
import com.spotler.model.search.ListParams;
import com.spotler.service.SpotlerAccountService;
import com.spotler.service.SpotlerUserService;

import java.util.Collections;
import java.util.List;

public class AMSClient {

    private final AMSServiceFactory AMSServiceFactory;

    @Inject
    public AMSClient(AMSServiceFactory AMSServiceFactory) {
        this.AMSServiceFactory = AMSServiceFactory;
    }

    public List<Account> getAllAccounts() {
        SpotlerAccountService spotlerAccountService = AMSServiceFactory.buildSpotlerAccountService();
        ListParams<Account.FilterableFields> listParams = ListParams.ListParamsBuilder.<Account.FilterableFields>newInstance()
                .setFilters(
                        Collections.singletonList(Filter.FilterBuilder.<Account.FilterableFields>newInstance()
                                .setKey(Account.FilterableFields.PRODUCT_DURATION)
                                .setType(Filter.FilterType.EXACT_NOT)
                                .setValue(DurationType.DEMO.name())
                                .build()))
                .build();
        return spotlerAccountService.list(listParams).getItems();
    }

    public List<User> getUsersForAccount(int accountId) {
        SpotlerUserService spotlerUserService = AMSServiceFactory.buildSpotlerUserService();
        return spotlerUserService.getUsersForAccount(accountId);
    }
}
