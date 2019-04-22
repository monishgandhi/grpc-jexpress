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
package com.flipkart.grpc.jexpress.db;

import com.flipkart.gjex.hibernate.SelectedDataSource;
import com.flipkart.gjex.hibernate.SessionFactoryContext;
import com.flipkart.gjex.hibernate.Transactional;
import com.flipkart.gjex.hibernate.core.AbstractDAO;

import javax.inject.Inject;
import javax.persistence.Query;
import java.util.List;


/**
 * @Transactional and @SelectedDataSource together can be added at both either method level OR class level OR both.
 * When added at class level, it means ALL methods inside that class will support @Transactional semantics.
 * When added at method level, it means @Transactional semantics will start at the beginning of this method.
 *
 */
//@Transactional
//@SelectedDataSource
public class UserDAO extends AbstractDAO<User> implements IUserDAO {

    @Inject
    public UserDAO(SessionFactoryContext sessionFactoryContext) {
        super(sessionFactoryContext);
    }

    @Override
    public List<User> getAllUsers() {
        Query query = namedQuery("User.findAll");
        List<User> result = (List<User>) query.getResultList();
        System.out.println(result);
        return result;
    }

    @Override
    @Transactional
    @SelectedDataSource
    public void createUser(String userName) {
        User user = new User();
        user.setName(userName);
        System.out.println("Saving user in database.");
        persist(user);
    }

    @Override
    @Transactional
    @SelectedDataSource
    public User getUserByUserId(Long userId) {
        Query query = namedQuery("User.findById").setParameter("id", userId);
        try {
           return (User) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
