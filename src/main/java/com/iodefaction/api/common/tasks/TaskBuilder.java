package com.iodefaction.api.common.tasks;

import lombok.SneakyThrows;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TaskBuilder {
    private boolean scheduledInFixedRate;
    private long time;

    public TaskBuilder() {
        this.scheduledInFixedRate = false;
        this.time = 0;
    }

    public void setScheduledInFixedRate(boolean scheduledInFixedRate) {
        this.scheduledInFixedRate = scheduledInFixedRate;
    }

    public TaskBuilder scheduleInFixedRate() {
        this.setScheduledInFixedRate(true);
        return this;
    }

    public TaskBuilder time(long time) {
        this.time = time;
        return this;
    }

    @SneakyThrows
    public void build(TimerTask timerTask) {
        if(this.scheduledInFixedRate) {
            MultiThreading.schedule(timerTask, this.time, this.time, TimeUnit.MILLISECONDS);
        } else {
            MultiThreading.schedule(timerTask, this.time, TimeUnit.MILLISECONDS);
        }
    }
}
