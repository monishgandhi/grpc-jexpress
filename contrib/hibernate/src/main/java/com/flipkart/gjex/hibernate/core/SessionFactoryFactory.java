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

import com.flipkart.gjex.hibernate.HibernateBundle;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author anand.pandey
 */
public class SessionFactoryFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionFactoryFactory.class);

    public SessionFactory build(HibernateBundle<?, ?> bundle, Map<String, String> hibernateProperties,
                                List<Class<?>> entities) {
        final Configuration configuration = getDefaultConfiguration();
        Iterator<String> propertyKeys = hibernateProperties.keySet().iterator();
        Properties configProperties = new Properties();
        while (propertyKeys.hasNext()) {
            String propertyKey = propertyKeys.next();
            Object propertyValue = hibernateProperties.get(propertyKey);
            configProperties.put(propertyKey, propertyValue);
        }
        configuration.addProperties(configProperties);
        addAnnotatedClasses(configuration, entities);
        bundle.configure(configuration);
        return configuration.buildSessionFactory();
    }

    private Configuration getDefaultConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setProperty(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "managed");
        configuration.setProperty(AvailableSettings.USE_GET_GENERATED_KEYS, "true");
        configuration.setProperty(AvailableSettings.USE_REFLECTION_OPTIMIZER, "true");
        configuration.setProperty(AvailableSettings.ORDER_UPDATES, "true");
        configuration.setProperty(AvailableSettings.ORDER_INSERTS, "true");
        configuration.setProperty(AvailableSettings.USE_NEW_ID_GENERATOR_MAPPINGS, "true");
        configuration.setProperty("jadira.usertype.autoRegisterUserTypes", "true");
        return configuration;
    }

    private void addAnnotatedClasses(Configuration configuration,
                                     Iterable<Class<?>> entities) {
        final SortedSet<String> entityClasses = new TreeSet<>();
        for (Class<?> klass : entities) {
            configuration.addAnnotatedClass(klass);
            entityClasses.add(klass.getCanonicalName());
        }
        LOGGER.info("Entity classes: {}", entityClasses);
    }
}
