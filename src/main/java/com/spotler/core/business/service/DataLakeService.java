package com.spotler.core.business.service;

import com.google.inject.Inject;
import com.spotler.api.mailing.Mailing;
import com.spotler.core.apiclients.AMSClient;
import com.spotler.core.apiclients.MailPlusClient;
import com.spotler.core.model.account.Account;
import com.spotler.model.datalake.Environment;
import com.spotler.model.datalake.Usage;
import com.spotler.model.datalake.User;
import com.spotler.util.ProgressLoggerUtil;
import com.spotler.util.RetryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataLakeService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final AMSClient amsClient;
    private final MailPlusClient mailPlusClient;

    @Inject
    public DataLakeService(AMSClient amsClient, MailPlusClient mailPlusClient) {
        this.amsClient = amsClient;
        this.mailPlusClient = mailPlusClient;
    }

    public List<Environment> getEnvironmentData() {
        LOGGER.info("Getting environment data...");
        List<Account> accounts = amsClient.getAllAccounts();
        int totalAccounts = accounts.size();
        int lastLoggedProgress = 0;

        List<Environment> environmentData = new ArrayList<>();
        for (int i = 0; i < totalAccounts; i++) {
            Account account = accounts.get(i);
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

            lastLoggedProgress = ProgressLoggerUtil.logProgress("GetEnvironmentData", i, totalAccounts, lastLoggedProgress);
        }

        return environmentData;
    }

    public List<User> getUserData() {
        LOGGER.info("Getting user data...");
        List<Account> accounts = amsClient.getAllAccounts();
        int totalAccounts = accounts.size();
        int lastLoggedProgress = 0;

        List<User> userData = new ArrayList<>();
        for (int i = 0; i < totalAccounts; i++) {
            Account account = accounts.get(i);
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

            lastLoggedProgress = ProgressLoggerUtil.logProgress("GetUserData", i, totalAccounts, lastLoggedProgress);
        }

        return userData;
    }

    public List<Usage> getUsageData() {
        LOGGER.info("Getting usage data...");
        // mTypes run in parallel?
        List<Account> accounts = amsClient.getAllAccounts();

        LocalDateTime startDate = LocalDateTime.now().minusDays(30);

        for (Account account : accounts) {
            List<Mailing> mailings = mailPlusClient.getMailings(account, startDate, LocalDateTime.now());
        }

        List<Usage> usageData = new ArrayList<>();
        for (Usage usage : usageData) {

        }

        return usageData;
    }
}
