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
package com.flipkart.gjex.hibernate.core;

import com.flipkart.gjex.hibernate.SelectedDataSource;
import com.flipkart.gjex.hibernate.SessionFactoryContext;
import com.flipkart.gjex.hibernate.Transactional;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.reflect.Method;

/**
 * @author anand.pandey
 */
public class TransactionInterceptor implements MethodInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionInterceptor.class);

    @Inject
    private Provider<SessionFactoryContext> sessionFactoryContextProvider;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        SessionFactoryContext context = sessionFactoryContextProvider.get();
        Session session = context.getThreadLocalSession();
        String method = invocation.getMethod().getName();
        Class<?> klass = invocation.getMethod().getDeclaringClass();
        if (session == null) {
            SelectedDataSource selectedDataSource = readSelectedDataSourceMetadata(invocation);
            Transactional transactional = readTransactionalMetadata(invocation);
            if (selectedDataSource == null) {
                String errorFormat = String.format("Current transactional method doesn't have annotation @SelectedDataSource either " +
                        "at @method_name %s OR @class_name %s ", invocation.getMethod().getName(), invocation.getThis().getClass());
                LOG.info(errorFormat);
                return new Error(errorFormat);
            }
            String name = selectedDataSource.name();
            // open a new session and set it in local thread context.
            SessionFactory sessionFactory = context.getSessionFactory(name);
            if (sessionFactory == null) {
                String errorMsg = "No session factory found for hibernate bundle " + name;
                LOG.error(errorMsg);
                throw new Error(errorMsg);
            }
            session = sessionFactory.openSession();
            configureSession(session, selectedDataSource);
            context.setThreadLocalSession(session);
            LOG.info("Open new session for the thread transaction started, using it: {} {}", method, klass);
            Transaction txn = session.beginTransaction();
            try {
                Object result = invocation.proceed();
                txn.commit();
                return result;
            } catch (Exception e) {
                //commit transaction only if rollback didn't occur
                if (rollbackIfNecessary(transactional, e, txn)) {
                    txn.commit();
                }
                //propagate whatever exception is thrown anyway
                throw e;
            } finally {
                LOG.info("Transaction completed for method : {} {}", method, klass);
                if (session != null) {
                    session.close();
                }
                context.clear();
                LOG.debug("Clearing session from ThreadLocal Context : {} {}", method, klass);
            }
        } else {
            Object result = invocation.proceed();
            LOG.debug("Use old session for the thread, reusing it: {} {}", method, klass);
            return result;
        }
    }

    private void configureSession(Session session, SelectedDataSource selectedDataSource) {
        session.setCacheMode(selectedDataSource.cacheMode());
        session.setHibernateFlushMode(selectedDataSource.flushMode());
    }

    /**
     * Returns True if rollback DID NOT HAPPEN (i.e. if commit should continue).
     *
     * @param transactional The metadata annotaiton of the method
     * @param e             The exception to test for rollback
     * @param txn           A Hibernate Transaction to issue rollbacks on
     */
    private boolean rollbackIfNecessary(Transactional transactional, Exception e, Transaction txn) {
        boolean commit = true;
        //check rollback clauses
        for (Class<? extends Exception> rollBackOn : transactional.rollbackOn()) {
            //if one matched, try to perform a rollback
            if (rollBackOn.isInstance(e)) {
                commit = false;
                //check ignore clauses (supercedes rollback clause)
                for (Class<? extends Exception> exceptOn : transactional.ignore()) {
                    //An exception to the rollback clause was found, DON'T rollback
                    // (i.e. commit and throw anyway)
                    if (exceptOn.isInstance(e)) {
                        commit = true;
                        break;
                    }
                }
                //rollback only if nothing matched the ignore check
                if (!commit) {
                    txn.rollback();
                }
                //otherwise continue to commit
                break;
            }
        }

        return commit;
    }

    private SelectedDataSource readSelectedDataSourceMetadata(MethodInvocation methodInvocation) {
        // First try to find SelectedDataSource name at method level.
        // If not able to find, then try at Class level.
        SelectedDataSource selectedDataSource;
        Method method = methodInvocation.getMethod();
        selectedDataSource = method.getAnnotation(SelectedDataSource.class);
        if (null == selectedDataSource) {
            Class<?> targetClass = methodInvocation.getThis().getClass();
            selectedDataSource = targetClass.getAnnotation(SelectedDataSource.class);
        }
        return selectedDataSource;
    }

    private Transactional readTransactionalMetadata(MethodInvocation methodInvocation) {
        Transactional transactional;
        Method method = methodInvocation.getMethod();
        transactional = method.getAnnotation(Transactional.class);
        if (null == transactional) {
            Class<?> targetClass = methodInvocation.getThis().getClass();
            transactional = targetClass.getAnnotation(Transactional.class);
        }
        return transactional;
    }
}
