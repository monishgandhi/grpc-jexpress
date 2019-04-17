package com.flipkart.gjex.hibernate;

import com.flipkart.gjex.core.GJEXConfiguration;

import java.util.Map;

public interface DatabaseConfiguration<T extends GJEXConfiguration> {

    Map<String, Object> getHibernateProperties(T configuration);

}
