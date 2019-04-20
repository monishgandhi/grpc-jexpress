package com.flipkart.gjex.hibernate.internal;

import com.codahale.metrics.health.HealthCheck;
import com.flipkart.gjex.hibernate.internal.TimeBoundHealthCheck;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    protected Result check() throws Exception {
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
