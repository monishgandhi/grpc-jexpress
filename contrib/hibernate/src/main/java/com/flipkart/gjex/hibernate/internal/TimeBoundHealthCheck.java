package com.flipkart.gjex.hibernate.internal;

import com.codahale.metrics.health.HealthCheck;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class TimeBoundHealthCheck {
    private final ExecutorService executorService;
    private final Duration duration;

    public TimeBoundHealthCheck(ExecutorService executorService, Duration duration) {
        this.executorService = executorService;
        this.duration = duration;
    }

    public HealthCheck.Result check(Callable<HealthCheck.Result> c) {
        HealthCheck.Result result;
        try {
            result = executorService.submit(c).get(duration.getSeconds(), TimeUnit.SECONDS);
        } catch (Exception e) {
            result = HealthCheck.Result.unhealthy("Unable to successfully check in %s seconds", duration);
        }
        return result;
    }
}
