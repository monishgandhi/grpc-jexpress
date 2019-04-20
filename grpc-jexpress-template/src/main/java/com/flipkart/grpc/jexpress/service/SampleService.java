package com.flipkart.grpc.jexpress.service;

import com.flipkart.gjex.core.filter.MethodFilters;
import com.flipkart.gjex.core.logging.Logging;
import com.flipkart.grpc.jexpress.*;
import com.flipkart.grpc.jexpress.db.IUserDAO;
import com.flipkart.grpc.jexpress.db.User;
import com.flipkart.grpc.jexpress.filter.CreateLoggingFilter;
import com.flipkart.grpc.jexpress.filter.GetLoggingFilter;
import io.grpc.stub.StreamObserver;
import org.apache.commons.configuration.Configuration;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Named("SampleService")
public class SampleService extends UserServiceGrpc.UserServiceImplBase implements Logging {

    private ConcurrentHashMap<Integer, String> userIdToUserNameMap = new ConcurrentHashMap<>();
    private AtomicInteger lastId = new AtomicInteger(0);

    private final SampleConfiguration sampleConfiguration;
    private final Configuration flattenedConfig;
    private final Map mapConfig;
    private String driverClass;
    private final IUserDAO userDAO;

    @Inject
    public SampleService(SampleConfiguration sampleConfiguration,
                         @Named("GlobalFlattenedConfig") Configuration flattenedConfig,
                         @Named("GlobalMapConfig") Map mapConfig,
                         @Named("database.properties.hibernate.connection.driver_class") String driverClass,
                         IUserDAO userDAO)
    {
        this.sampleConfiguration = sampleConfiguration;
        this.flattenedConfig = flattenedConfig;
        this.mapConfig = mapConfig;
        this.driverClass = driverClass;
        this.userDAO = userDAO;
    }

    @Override
    @MethodFilters({GetLoggingFilter.class})
    public void getUser(GetRequest request, StreamObserver<GetResponse> responseObserver) {
        GetResponse response = GetResponse.newBuilder()
                .setId(request.getId())
                .setUserName(userIdToUserNameMap.getOrDefault(request.getId(), "Guest")).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        info(sampleConfiguration.toString());
        info(mapConfig.toString());

        info("\"database.driverClass\" class in @Named annotation =  " + driverClass);

        // Read values from Flattened config
        info("FlattenedConfig has \"Grpc.server.port\" = " + flattenedConfig.getInt("Grpc.server.port"));

        // Read values from plain map
        info("MapConfig of Dashboard = " + mapConfig.get("Dashboard").toString());
        info("MapConfig of employee = " + mapConfig.get("database").toString());
        Object properties = ((Map<String, Object>) mapConfig.get("database")).get("properties");
        info("MapConfig -> properties in database = " + properties);

        User user = userDAO.getUserByUserId((long)request.getId());
        info("Get user by id = " + user);
    }

    @Override
    @MethodFilters({CreateLoggingFilter.class})
    public void createUser(CreateRequest request, StreamObserver<CreateResponse> responseObserver) {
        int id = lastId.incrementAndGet();
        userIdToUserNameMap.put(id, request.getUserName());
        CreateResponse response = CreateResponse.newBuilder()
                .setId(id)
                .setIsCreated(true).
                        build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        userDAO.createUser(request.getUserName());
    }

}
