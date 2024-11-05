package com.spotler.core.apiclients;

import com.google.inject.Inject;
import com.spotler.core.business.service.AccountServiceFactory;
import com.spotler.core.model.account.Account;
import com.spotler.core.model.account.DurationType;
import com.spotler.model.search.Filter;
import com.spotler.model.search.ListParams;
import com.spotler.service.SpotlerAccountService;

import java.util.Collections;
import java.util.List;

public class AMSClient {

    private final AccountServiceFactory accountServiceFactory;

    @Inject
    public AMSClient(AccountServiceFactory accountServiceFactory) {
        this.accountServiceFactory = accountServiceFactory;
    }

    public List<Account> getAllAccounts() {
        SpotlerAccountService spotlerAccountService = accountServiceFactory.build();
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
}
