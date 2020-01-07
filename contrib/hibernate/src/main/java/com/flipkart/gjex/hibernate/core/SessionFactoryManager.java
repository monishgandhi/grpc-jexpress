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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.hibernate.SessionFactory;

import java.util.Map;

/**
 * @author anand.pandey
 */
public enum SessionFactoryManager {

    INSTANCE;

    private final Map<String, SessionFactory> sessionFactoryMap = Maps.newConcurrentMap();

    public void registerSessionFactory(String dbName, SessionFactory sessionFactory) {
        Preconditions.checkNotNull(dbName, "dbName cannot be null");
        Preconditions.checkNotNull(sessionFactory, "sessionFactory cannot be null");
        SessionFactory oldValue = sessionFactoryMap.putIfAbsent(dbName, sessionFactory);
        if (oldValue != null) {
            String errorMsg = "Duplicate HibernateBundle found with name " + dbName;
            throw new Error(errorMsg);
        }
    }

    public static SessionFactoryManager getInstance() {
        return INSTANCE;
    }

    public Map<String, SessionFactory> getAllSessionFactories() {
        return ImmutableMap.copyOf(sessionFactoryMap);
    }
}
