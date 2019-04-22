/*
 * Copyright (C) 2010 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.flipkart.grpc.jexpress;

import com.flipkart.gjex.core.Application;
import com.flipkart.gjex.core.filter.Filter;
import com.flipkart.gjex.core.service.Service;
import com.flipkart.gjex.core.setup.Bootstrap;
import com.flipkart.gjex.core.setup.Environment;
import com.flipkart.gjex.core.tracing.TracingSampler;
import com.flipkart.gjex.guice.GuiceBundle;
import com.flipkart.gjex.hibernate.HibernateBundle;
import com.flipkart.gjex.hibernate.HibernateDataSourceFactory;
import com.flipkart.gjex.hibernate.config.HibernateModule;
import com.flipkart.grpc.jexpress.db.User;
import com.flipkart.grpc.jexpress.module.SampleModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SampleApplication extends Application<SampleConfiguration, Map> {

    private HibernateBundle<SampleConfiguration, Map> hibernateBundle =

            new HibernateBundle<SampleConfiguration, Map>(User.class) {

                @Override
                public HibernateDataSourceFactory getDataSourceFactory(SampleConfiguration configuration) {
                    return configuration.getDataSourceFactory();
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
