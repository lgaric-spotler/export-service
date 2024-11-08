package com.spotler.model.datalake;

import java.time.LocalDate;

public class Usage {
    private String id;
    private LocalDate date;
    private Integer accountId;
    private String name;
    private String type;
    private Integer sentCount;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSentCount() {
        return sentCount;
    }

    public void setSentCount(Integer sentCount) {
        this.sentCount = sentCount;
    }
}
