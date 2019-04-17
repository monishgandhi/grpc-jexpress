package com.flipkart.gjex.hibernate.internal;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.hibernate.SessionFactory;

import java.util.Map;

public enum SessionFactoryManager {

    INSTANCE;

    private final Map<String, SessionFactory> sessionFactoryMap = Maps.newConcurrentMap();

    public void registerSessionFactory(String dbName, SessionFactory sessionFactory) {
        Preconditions.checkNotNull(dbName, "dbName cannot be null");
        Preconditions.checkNotNull(sessionFactory, "sessionFactory cannot be null");
        sessionFactoryMap.putIfAbsent(dbName, sessionFactory);
    }

    public static SessionFactoryManager getInstance() {
        return INSTANCE;
    }

    public Map<String, SessionFactory> getAllSessionFactories() {
        return ImmutableMap.copyOf(sessionFactoryMap);
    }
}
