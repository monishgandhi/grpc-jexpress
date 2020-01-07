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
package com.flipkart.gjex.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * @author anand.pandey
 */
public interface SessionFactoryContext {

    /**
     * Returns session factory associated with given hibernate bundle "name"
     *
     * @param name Hibernate Bundle name
     * @return SessionFactory associated with given hibernate bundle
     */
    SessionFactory getSessionFactory(String name);

    /**
     * Get the session for the current thread context to be used for the ongoing transaction.
     * @return
     */
    Session getThreadLocalSession();

    /**
     *  Sets session for the current thread context to be used for the ongoing transaction.
     * @param session
     */
    void setThreadLocalSession(Session session);

    /**
     * Removes session from thread local context.
     */
    void clear();

}
