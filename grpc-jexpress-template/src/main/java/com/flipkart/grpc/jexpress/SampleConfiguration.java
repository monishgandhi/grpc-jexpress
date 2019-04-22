package com.flipkart.grpc.jexpress;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flipkart.gjex.core.GJEXConfiguration;
import com.flipkart.gjex.hibernate.HibernateDataSource;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SampleConfiguration extends GJEXConfiguration {

    @JsonProperty("database")
    private HibernateDataSource hibernateDataSource;

}