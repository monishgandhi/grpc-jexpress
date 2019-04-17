package com.flipkart.gjex.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public interface SessionFactoryContext {

    SessionFactory getSessionFactory(String name);

    Session getThreadLocalSession();

    void setThreadLocalSession(Session session);

    void clear();

}
