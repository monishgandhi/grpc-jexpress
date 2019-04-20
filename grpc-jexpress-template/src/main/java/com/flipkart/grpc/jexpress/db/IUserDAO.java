package com.flipkart.grpc.jexpress.db;

import java.util.List;

public interface IUserDAO {

    List<User> getAllUsers();

    void createUser(String userName);

    User getUserByUserId(Long userId);

}
