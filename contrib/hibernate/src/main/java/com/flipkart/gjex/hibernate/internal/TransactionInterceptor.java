package com.flipkart.gjex.hibernate.internal;

import com.flipkart.gjex.core.logging.Logging;
import com.flipkart.gjex.hibernate.SelectedDataSource;
import com.flipkart.gjex.hibernate.SessionFactoryContext;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;

public class TransactionInterceptor implements MethodInterceptor, Logging {

    private static final Logger logger = LoggerFactory.getLogger(TransactionInterceptor.class);

    @Inject
    private Provider<SessionFactoryContext> sessionFactoryContextProvider;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Transaction transaction = null;
        Session session = null;
        SessionFactoryContext context = sessionFactoryContextProvider.get();
        try {
            session = context.getThreadLocalSession();
        } catch (HibernateException e) {
        }
        if (session == null) {
            SessionFactory sessionFactory;
            try {
                String name = invocation.getMethod().getAnnotation(SelectedDataSource.class).name();
                sessionFactory = sessionFactoryContextProvider.get().getSessionFactory(name);
            } catch (Exception ex) {
                logger.error("Current transactional method doesn't have annotation @SelectDataSource method_name:{} {}"
                        , invocation.getMethod().getName(), ex.getStackTrace());
                return new Error("Something wrong with Method's annotations " + ex.getMessage());
            }
            // open a new session and set it in local thread context.

            session = sessionFactory.openSession();
            context.setThreadLocalSession(session);
            debug("Open new session for the thread transaction started, using it: " + invocation.getMethod().getName()
                    + " " + invocation.getMethod().getDeclaringClass());
            transaction = session.getTransaction();
            transaction.begin();
            try {
                Object result = invocation.proceed();
                transaction.commit();
                return result;
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            } finally {
                debug("Transaction completed for method : " + invocation.getMethod().getName() + " " +
                        invocation.getMethod().getDeclaringClass());
                session.close();
                context.clear();
                debug("Clearing session from ThreadLocal Context : " + invocation.getMethod().getName() + " " +
                        invocation.getMethod().getDeclaringClass());
            }
        } else {
            Object result = invocation.proceed();
            debug("Use old session for the thread, reusing it: " + invocation.getMethod().getName() + " " +
                    invocation.getMethod().getDeclaringClass());
            return result;
        }
    }
}
