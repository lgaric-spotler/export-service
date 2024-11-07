package com.spotler.core.apiclients.config;

public class MailPlusConfiguration {
    private String consumerKey;
    private String consumerSecret;
    private Integer maxParallelMTypeRuns;
    private int timeout;

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public Integer getMaxParallelMTypeRuns() {
        return maxParallelMTypeRuns;
    }

    public void setMaxParallelMTypeRuns(Integer maxParallelMTypeRuns) {
        this.maxParallelMTypeRuns = maxParallelMTypeRuns;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
