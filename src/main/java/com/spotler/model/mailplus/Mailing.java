package com.spotler.model.mailplus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Mailing {
    private String name;
    private String encryptedId;
    private Date scheduledStartDate;
    private String type;
    private Date sentCountFrom;
    private Date sentCountTo;
    private int sentCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getScheduledStartDate() {
        return scheduledStartDate;
    }

    public void setScheduledStartDate(Date scheduledStartDate) {
        this.scheduledStartDate = scheduledStartDate;
    }

    public Date getSentCountFrom() {
        return sentCountFrom;
    }

    public void setSentCountFrom(Date sentCountFrom) {
        this.sentCountFrom = sentCountFrom;
    }

    public Date getSentCountTo() {
        return sentCountTo;
    }

    public void setSentCountTo(Date sentCountTo) {
        this.sentCountTo = sentCountTo;
    }

    public String getEncryptedId() {
        return encryptedId;
    }

    public void setEncryptedId(String encryptedId) {
        this.encryptedId = encryptedId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSentCount() {
        return sentCount;
    }

    public void setSentCount(int sentCount) {
        this.sentCount = sentCount;
    }
}
