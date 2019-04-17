package com.flipkart.gjex.hibernate;

import com.flipkart.gjex.core.Bundle;
import com.flipkart.gjex.core.GJEXConfiguration;
import com.flipkart.gjex.core.setup.Bootstrap;
import com.flipkart.gjex.core.setup.Environment;
import com.google.common.collect.ImmutableList;
import org.hibernate.SessionFactory;

import java.util.Map;

public abstract class HibernateBundle<T extends GJEXConfiguration, U extends Map> implements Bundle<T, U>, DatabaseConfiguration<T> {

    public static final String DEFAULT_NAME = "hibernate";

    private SessionFactory sessionFactory;

    private final ImmutableList<Class<?>> entities;
    private final SessionFactoryFactory sessionFactoryFactory;

    protected HibernateBundle(Class<?> entity, Class<?>... entities) {
        this(ImmutableList.<Class<?>>builder().add(entity).add(entities).build(),
                new SessionFactoryFactory());
    }

    protected HibernateBundle(ImmutableList<Class<?>> entities,
                              SessionFactoryFactory sessionFactoryFactory) {
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
        final Map<String, Object> hibernateConfig = getHibernateProperties(configuration);
        this.sessionFactory = sessionFactoryFactory.build(this, hibernateConfig, entities, name());
        SessionFactoryManager.getInstance().registerSessionFactory(name(), sessionFactory);
    }

    protected void configure(org.hibernate.cfg.Configuration configuration) {
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
