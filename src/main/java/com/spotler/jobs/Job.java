package com.spotler.jobs;

import java.time.ZonedDateTime;

public abstract class Job implements Runnable {

    private final ZonedDateTime created = ZonedDateTime.now();
    private ZonedDateTime lastStarted = null;
    private ZonedDateTime lastFinished = null;

    @Override
    public void run() {
        lastStarted = ZonedDateTime.now();
        doRun();
        lastFinished = ZonedDateTime.now();
    }

    protected abstract void doRun();

    public ZonedDateTime getCreated() {
        return created;
    }

    public ZonedDateTime getLastStarted() {
        return lastStarted;
    }

    public ZonedDateTime getLastFinished() {
        return lastFinished;
    }
}
