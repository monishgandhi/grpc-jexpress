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

import com.flipkart.gjex.hibernate.SessionFactoryContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

/**
 * @author anand.pandey
 */
public class SessionFactoryContextImpl implements SessionFactoryContext {

    private final Map<String, SessionFactory> sessionFactories;
    private final ThreadLocal<Session> currentSession = new ThreadLocal<>();

    @Inject
    public SessionFactoryContextImpl(@Named("sessionFactories") Map<String, SessionFactory> sessionFactories) {
        this.sessionFactories = sessionFactories;
    }

    @Override
    public SessionFactory getSessionFactory(String name) {
        return sessionFactories.get(name);
    }

    @Override
    public Session getThreadLocalSession() {
        return currentSession.get();
    }

    @Override
    public void setThreadLocalSession(Session session) {
        currentSession.set(session);
    }

    @Override
    public void clear() {
        currentSession.remove();
    }

}
