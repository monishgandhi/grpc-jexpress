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
package com.flipkart.gjex.hibernate.internal;

import com.flipkart.gjex.hibernate.SessionFactoryContext;
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
