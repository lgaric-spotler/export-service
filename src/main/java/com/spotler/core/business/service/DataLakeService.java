package com.spotler.core.business.service;

import com.google.inject.Inject;
import com.spotler.core.apiclients.AMSClient;
import com.spotler.core.model.account.Account;
import com.spotler.model.datalake.Environment;
import com.spotler.model.datalake.User;

import java.util.ArrayList;
import java.util.List;

public class DataLakeService {

    private final AMSClient amsClient;

    @Inject
    public DataLakeService(AMSClient amsClient) {
        this.amsClient = amsClient;
    }

    public List<Environment> getEnvironmentData() {
        List<Account> accounts = amsClient.getAllAccounts();

        List<Environment> environmentData = new ArrayList<>();
        for (Account account : accounts) {
            Environment environment = new Environment();
            environment.setAccountId(account.getId());
            environment.setAccountName(account.getName());
            environment.setAccountType(account.getProductType().toString());
            environment.setCreationDate(account.getCreatedAt());
            environment.setLastLoginDate(account.getLastLoginAt());
            environment.setExpiryDate(account.getExpiresAt());
            //environment.setTestAccount(account.isTestAccount());
            //environment.setSSOLicense(account.isSSOLicense());
            //environment.setB2BLicense(account.isB2BLicense());
            //environment.setIntegrationLicense(account.isIntegrationLicense());
            environmentData.add(environment);
        }

        return environmentData;
    }

    public List<User> getUserData() {
        List<Account> accounts = amsClient.getAllAccounts();

        List<User> userData = new ArrayList<>();
        for (Account account : accounts) {
            List<com.spotler.core.model.user.User> amsAccountUsers = amsClient.getUsersForAccount(account.getId());

            for (com.spotler.core.model.user.User amsAccountUser : amsAccountUsers) {
                User user = new User();
                user.setUserId(amsAccountUser.getId());
                user.setEmailAddress(amsAccountUser.getEmail());
                user.setFirstName(amsAccountUser.getFirstName());
                user.setLastName(amsAccountUser.getLastName());
                user.setMainAccountId(amsAccountUser.getMainAccountId());
                userData.add(user);
            }
        }

        return userData;
    }
}
