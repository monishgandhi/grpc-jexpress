package com.flipkart.gjex.hibernate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * This class specifies all configurations that are needed for setting up Hibernate
 */
public class HibernateDataSourceFactory {

    /**
     * Returns the configuration properties for hibernate
     *
     * @return configuration properties as a map
     */
    private Map<String, String> properties;

    /**
     * Returns the SQL query, which is being used for the database
     * connection health check.
     *
     * @return the SQL query as a string
     */
    @NotNull
    private String validationQuery = "select 1";

    /**
     * Returns the timeout for awaiting a response from the database
     * during connection health checks.
     */
    @Min(3)
    private int validationQueryTimeoutInSeconds = 3;

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public int getValidationQueryTimeoutInSeconds() {
        return validationQueryTimeoutInSeconds;
    }

    public void setValidationQueryTimeoutInSeconds(int validationQueryTimeoutInSeconds) {
        this.validationQueryTimeoutInSeconds = validationQueryTimeoutInSeconds;
    }

    @Override
    public String toString() {
        return "HibernateDataSourceFactory{" +
                "properties=" + properties +
                ", validationQuery='" + validationQuery + '\'' +
                ", validationQueryTimeoutInSeconds=" + validationQueryTimeoutInSeconds +
                '}';
    }
}
