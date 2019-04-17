package com.flipkart.gjex.hibernate;

import java.util.Map;

public class HibernateConfiguration {

    private String databaseName;
    private Map<String, Object> properties;

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
