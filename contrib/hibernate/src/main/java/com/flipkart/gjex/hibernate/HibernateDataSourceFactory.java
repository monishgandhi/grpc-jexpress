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

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * This class specifies all configurations that are needed for setting up Hibernate
 *
 * @author anand.pandey
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
    @Min(1)
    private int validationQueryTimeoutInSeconds = 5;

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
