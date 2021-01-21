package com.iodefaction.api.common.tasks;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.TimerTask;

@AllArgsConstructor
public class RunnableTimerTask extends TimerTask {
    @Getter
    private final Runnable runnable;

    @Override
    public void run() {
        runnable.run();
    }
}
