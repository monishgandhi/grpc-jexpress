package com.flipkart.grpc.jexpress;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.flipkart.gjex.core.GJEXConfiguration;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SampleConfiguration extends GJEXConfiguration {

    private Map<String, Object> hibernateProperties;

//    @JsonProperty("database")
//    private DataSourceFactory dataSourceFactory;
}
