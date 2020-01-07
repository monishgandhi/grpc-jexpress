/*
 * Copyright (c) The original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.flipkart.gjex.hibernate.healthcheck;

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
