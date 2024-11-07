package com.spotler.core.business.service;

import com.google.inject.Inject;
import com.spotler.ExportServiceConfiguration;
import com.spotler.core.apiclients.AMSClient;
import com.spotler.core.apiclients.MailPlusClient;
import com.spotler.core.model.account.Account;
import com.spotler.model.datalake.Environment;
import com.spotler.model.datalake.Usage;
import com.spotler.model.datalake.User;
import com.spotler.model.mailplus.Mailing;
import com.spotler.util.ProgressLoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class DataLakeService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final AMSClient amsClient;
    private final MailPlusClient mailPlusClient;
    private final int maxParallelMTypeRuns;

    @Inject
    public DataLakeService(AMSClient amsClient, MailPlusClient mailPlusClient, ExportServiceConfiguration exportServiceConfiguration) {
        this.amsClient = amsClient;
        this.mailPlusClient = mailPlusClient;

        Integer maxParallelMTypeRuns = exportServiceConfiguration.getMailPlus().getMaxParallelMTypeRuns();
        this.maxParallelMTypeRuns = maxParallelMTypeRuns != null ? maxParallelMTypeRuns : 1;
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
        List<Account> accounts = amsClient.getAllAccounts();
        Map<String, List<Account>> accountsByServer = accounts.stream().collect(Collectors.groupingBy(Account::getServer));

        // See if we can get all usage data from the rest api

        ExecutorService executorService = Executors.newFixedThreadPool(maxParallelMTypeRuns);
        List<Future<List<Usage>>> futures = new ArrayList<>();
        LocalDateTime startDate = LocalDateTime.of(LocalDateTime.now().toLocalDate().minusDays(30), LocalTime.MIDNIGHT);

        for (List<Account> serverAccounts : accountsByServer.values()) {
            futures.add(executorService.submit(() -> {
                List<Usage> usage = new ArrayList<>();
                int totalAccounts = serverAccounts.size();
                int lastLoggedProgress = 0;
                String server = serverAccounts.get(0).getServer();

                for (int i = 0; i < serverAccounts.size(); i++) {
                    Account account = serverAccounts.get(i);
                    List<Usage> accountUsage = mailPlusClient.getMailingStats(account, startDate, LocalDateTime.now()).stream().map(this::mapToUsage).collect(Collectors.toList());
                    accountUsage.forEach(u -> u.setAccountId(account.getId()));
                    usage.addAll(accountUsage);

                    lastLoggedProgress = ProgressLoggerUtil.logProgress(String.format("%s-GetUserData", server), i, totalAccounts, lastLoggedProgress);
                }
                return usage;
            }));
        }

        List<Usage> allMailingUsages = new ArrayList<>();
        for (Future<List<Usage>> future : futures) {
            try {
                allMailingUsages.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error("Error processing mailings", e);
                Thread.currentThread().interrupt();
            }
        }

        LOGGER.info("Enriching automation and campaign data...");
        for (Usage usage : allMailingUsages) {
            if (usage.getType().contains("arp")) {
                // TODO enrich campaigns
            } else if (usage.getType().contains("automation")) {
                // TODO enrich automations
            }

            usage.setType(usage.getType().contains("sms") ? "SMS" : "Email");
        }

        executorService.shutdown();

        return allMailingUsages;
    }

    private Usage mapToUsage(Mailing mailing) {
        Usage usage = new Usage();
        usage.setId(mailing.getEncryptedId());
        //usage.setDate(mailing.getScheduledStartDate());
        usage.setName(mailing.getName());
        usage.setType(mailing.getType());
        usage.setSentCount(mailing.getSentCount());
        return usage;
    }
}
