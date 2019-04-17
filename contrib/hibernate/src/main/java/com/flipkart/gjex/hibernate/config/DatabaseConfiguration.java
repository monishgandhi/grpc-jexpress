package com.flipkart.gjex.hibernate.config;

import com.flipkart.gjex.core.GJEXConfiguration;

import java.util.Map;

public interface DatabaseConfiguration<T extends GJEXConfiguration> {

    Map<String, Object> getHibernateProperties(T configuration);

}
