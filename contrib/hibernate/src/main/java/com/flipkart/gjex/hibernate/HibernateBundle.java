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

import com.codahale.metrics.health.HealthCheck;
import com.flipkart.gjex.core.Bundle;
import com.flipkart.gjex.core.GJEXConfiguration;
import com.flipkart.gjex.core.setup.Bootstrap;
import com.flipkart.gjex.core.setup.Environment;
import com.flipkart.gjex.hibernate.core.SessionFactoryFactory;
import com.flipkart.gjex.hibernate.core.SessionFactoryManager;
import com.flipkart.gjex.hibernate.healthcheck.SessionFactoryHealthCheck;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Map;

/**
 * When using {@link HibernateBundle}, please make sure it is added in {@link Bootstrap} before {@link com.flipkart.gjex.guice.GuiceBundle}
 * in {@link com.flipkart.gjex.core.Application} class
 * @author anand.pandey
 */
public abstract class HibernateBundle<T extends GJEXConfiguration, U extends Map> implements Bundle<T, U>, DatabaseConfiguration<T> {

    public static final String DEFAULT_NAME = "hibernate";
    private final List<HealthCheck> healthChecks = Lists.newLinkedList();
    private final ImmutableList<Class<?>> entities;
    private final SessionFactoryFactory sessionFactoryFactory;

    private SessionFactory sessionFactory;
    private T configuration;
    private U configMap;

    protected HibernateBundle(Class<?> entity, Class<?>... entities) {
        this(ImmutableList.<Class<?>>builder().add(entity).add(entities).build(), new SessionFactoryFactory());
    }

    protected HibernateBundle(ImmutableList<Class<?>> entities, SessionFactoryFactory sessionFactoryFactory) {
        this.entities = entities;
        this.sessionFactoryFactory = sessionFactoryFactory;
    }

    /**
     * Override to configure the name of the bundle
     */
    protected String name() {
        return DEFAULT_NAME;
    }


    @Override
    public void initialize(Bootstrap<?, ?> bootstrap) {

    }

    @Override
    public void run(T configuration, U configMap, Environment environment) {
        this.configuration = configuration;
        this.configMap = configMap;
        HibernateDataSource dataSource = getDataSource(configuration);
        final Map<String, String> hibernateConfig = dataSource.getProperties();
        this.sessionFactory = sessionFactoryFactory.build(this, hibernateConfig, entities);
        SessionFactoryManager.getInstance().registerSessionFactory(name(), sessionFactory);
        SessionFactoryHealthCheck healthCheck = new SessionFactoryHealthCheck(sessionFactory, dataSource.getValidationQuery(),
                dataSource.getValidationQueryTimeoutInSeconds());
        healthChecks.add(healthCheck);
    }

    public void configure(org.hibernate.cfg.Configuration configuration) {
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public T getConfiguration() {
        return configuration;
    }

    public U getConfigMap() {
        return configMap;
    }

    @Override
    public List<HealthCheck> getHealthChecks() {
        return healthChecks;
    }

}
