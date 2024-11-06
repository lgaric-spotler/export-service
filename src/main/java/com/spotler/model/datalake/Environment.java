package com.spotler.model.datalake;

import java.time.LocalDateTime;

public class Environment {
    private Integer accountId;
    private String accountName;
    private String accountType;
    private LocalDateTime creationDate;
    private LocalDateTime lastLoginDate;
    private LocalDateTime expiryDate;
    private Boolean testAccount;
    private Boolean SSOLicense;
    private Boolean B2BLicense;
    private Boolean IntegrationLicense;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean getTestAccount() {
        return testAccount;
    }

    public void setTestAccount(Boolean testAccount) {
        this.testAccount = testAccount;
    }

    public Boolean getSSOLicense() {
        return SSOLicense;
    }

    public void setSSOLicense(Boolean SSOLicense) {
        this.SSOLicense = SSOLicense;
    }

    public Boolean getB2BLicense() {
        return B2BLicense;
    }

    public void setB2BLicense(Boolean b2BLicense) {
        B2BLicense = b2BLicense;
    }

    public Boolean getIntegrationLicense() {
        return IntegrationLicense;
    }

    public void setIntegrationLicense(Boolean integrationLicense) {
        IntegrationLicense = integrationLicense;
    }
}
