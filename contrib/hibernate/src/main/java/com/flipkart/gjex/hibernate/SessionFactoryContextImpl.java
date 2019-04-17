package com.flipkart.gjex.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Map;

public class SessionFactoryContextImpl implements SessionFactoryContext {

    private Map<String, SessionFactory> sessionFactories;
    private final ThreadLocal<Session> currentSessionFactoryContext = new ThreadLocal<>();

    public SessionFactoryContextImpl(Map<String, SessionFactory> sessionFactories) {
        this.sessionFactories = sessionFactories;
    }

    @Override
    public SessionFactory getSessionFactory(String name) {
        return sessionFactories.get(name);
    }

    @Override
    public Session getThreadLocalSession() {
        return currentSessionFactoryContext.get();
    }

    @Override
    public void setThreadLocalSession(Session session) {
        currentSessionFactoryContext.set(session);
    }

    @Override
    public void clear() {
        currentSessionFactoryContext.remove();
    }

}
