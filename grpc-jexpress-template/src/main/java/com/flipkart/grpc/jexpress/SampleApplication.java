package com.flipkart.grpc.jexpress;

import com.codahale.metrics.health.HealthCheck;
import com.flipkart.gjex.core.Application;
import com.flipkart.gjex.core.filter.Filter;
import com.flipkart.gjex.core.service.Service;
import com.flipkart.gjex.core.setup.Bootstrap;
import com.flipkart.gjex.core.setup.Environment;
import com.flipkart.gjex.core.tracing.TracingSampler;
import com.flipkart.gjex.guice.GuiceBundle;
import com.flipkart.gjex.hibernate.HibernateBundle;
import com.flipkart.gjex.hibernate.config.HibernateModule;
import com.flipkart.grpc.jexpress.db.Employee;
import com.flipkart.grpc.jexpress.module.SampleModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SampleApplication extends Application<SampleConfiguration, Map> {

    private HibernateBundle<SampleConfiguration, Map> hibernateBundle =

            new HibernateBundle<SampleConfiguration, Map>(Employee.class) {

                @Override
                public Map<String, Object> getHibernateProperties(SampleConfiguration configuration) {
                    return configuration.getHibernateProperties();
                }

                @Override
                public List<Service> getServices() {
                    return new ArrayList<>();
                }

                @Override
                public List<Filter> getFilters() {
                    return new ArrayList<>();
                }

                @Override
                public List<HealthCheck> getHealthChecks() {
                    return new ArrayList<>();
                }

                @Override
                public List<TracingSampler> getTracingSamplers() {
                    return new ArrayList<>();
                }

            };


    @Override
    public String getName() {
        return "Sample JExpress Application";
    }

    @Override
    public void run(SampleConfiguration configuration, Map configMap, Environment environment) throws Exception {

    }

    @Override
    public void initialize(Bootstrap<SampleConfiguration, Map> bootstrap) {
        bootstrap.addBundle(hibernateBundle);

        GuiceBundle<SampleConfiguration, Map> guiceBundle = new GuiceBundle.Builder<SampleConfiguration, Map>()
                .setConfigClass(SampleConfiguration.class)
                .addModules(new SampleModule(), new HibernateModule())
                .build();
        bootstrap.addBundle(guiceBundle);

    }

    public static void main(String[] args) throws Exception {
        SampleApplication app = new SampleApplication();
        app.run(args);
    }
}
