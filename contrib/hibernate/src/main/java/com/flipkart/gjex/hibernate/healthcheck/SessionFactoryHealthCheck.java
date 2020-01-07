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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author anand.pandey
 */
public class SessionFactoryHealthCheck extends HealthCheck {

    private final String validationQuery;
    private final SessionFactory sessionFactory;
    private final TimeBoundHealthCheck timeBoundHealthCheck;

    public SessionFactoryHealthCheck(SessionFactory sessionFactory,
                                     String validationQuery) {
        this(sessionFactory, validationQuery,5);
    }

    public SessionFactoryHealthCheck(SessionFactory sessionFactory,
                                     String validationQuery,
                                     int validationQueryTimeoutInSeconds) {
        this(Executors.newFixedThreadPool(1), sessionFactory, validationQuery, validationQueryTimeoutInSeconds);
    }

    public SessionFactoryHealthCheck(ExecutorService executorService,
                                     SessionFactory sessionFactory,
                                     String validationQuery,
                                     int validationQueryTimeoutInSeconds) {
        this.sessionFactory = sessionFactory;
        this.validationQuery = validationQuery;
        this.timeBoundHealthCheck = new TimeBoundHealthCheck(executorService, Duration.ofSeconds(validationQueryTimeoutInSeconds));
    }

    @Override
    protected Result check() {
        return timeBoundHealthCheck.check(() -> {
            try (Session session = sessionFactory.openSession()) {
                final Transaction txn = session.beginTransaction();
                try {
                    session.createNativeQuery(validationQuery).list();
                    txn.commit();
                } catch (Exception e) {
                    if (txn.getStatus().canRollback()) {
                        txn.rollback();
                    }
                    throw e;
                }
            }
            return Result.healthy();
        });
    }
}
