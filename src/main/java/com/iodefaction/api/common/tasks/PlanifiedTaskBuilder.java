package com.iodefaction.api.common.tasks;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Builder
public class PlanifiedTaskBuilder {
    private @Setter boolean scheduledInFixedRate;
    private @Getter int hourOfTheDay, minutes, seconds;
    private @Getter long time;
    private @Getter DayOfWeek day;

    /*public PlanifiedTaskBuilder() {
        this.hourOfTheDay = 0;
        this.minutes = 0;
        this.seconds = 0;
        this.day = DayOfWeek.MONDAY;
        this.time = 0;
    }*/

    public PlanifiedTaskBuilder scheduledInFixedRate() {
        this.scheduledInFixedRate = true;
        return this;
    }

    public void setHourOfTheDay(int hourOfTheDay) {
        this.hourOfTheDay = hourOfTheDay;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public PlanifiedTaskBuilder hourOfTheDay(int hourOfTheDay) {
        this.setHourOfTheDay(hourOfTheDay);
        return this;
    }

    public PlanifiedTaskBuilder minutes(int hourOfTheDay) {
        this.setMinutes(hourOfTheDay);
        return this;
    }

    public PlanifiedTaskBuilder day(DayOfWeek dayOfWeek) {
        this.setDay(dayOfWeek);
        return this;
    }

    public PlanifiedTaskBuilder seconds(int hourOfTheDay) {
        this.setSeconds(hourOfTheDay);
        return this;
    }

    public PlanifiedTaskBuilder time(long time) {
        this.setTime(time);
        return this;
    }

    public void build(Runnable runnable) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_WEEK, this.day.getValue() + 1);

        calendar.set(Calendar.HOUR_OF_DAY, this.hourOfTheDay);
        calendar.set(Calendar.MINUTE, this.minutes);
        calendar.set(Calendar.SECOND, this.seconds);

        if(new Date().after(calendar.getTime())) {
            calendar.add(Calendar.MILLISECOND, Math.toIntExact(this.getTime()));
        }

        if(this.scheduledInFixedRate) {
            MultiThreading.schedule(runnable, calendar.getTime().getTime() - System.currentTimeMillis(), TimeUnit.DAYS.toMillis(7), TimeUnit.MILLISECONDS);
        } else {
            MultiThreading.schedule(runnable, calendar.getTime().getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }
    }
}
