package com.flipkart.grpc.jexpress.db;

import com.flipkart.gjex.hibernate.SelectedDataSource;
import com.flipkart.gjex.hibernate.SessionFactoryContext;
import com.flipkart.gjex.hibernate.Transactional;
import com.flipkart.gjex.hibernate.internal.AbstractDAO;

import javax.inject.Inject;
import javax.persistence.Query;
import java.util.List;


public class EmployeeDAO extends AbstractDAO<Employee> implements IEmployeeDAO {

    @Inject
    public EmployeeDAO(SessionFactoryContext sessionFactoryContext) {
        super(sessionFactoryContext);
    }

    @Override
    @Transactional
    @SelectedDataSource
    public List<Employee> getAllEmployees() {
        return getAllEmployees1();
    }

    public List<Employee> getAllEmployees1() {
        Query query = namedQuery("Employee.findAll");
        List<Employee> result = query.getResultList();
        System.out.println(result);
        return result;
    }



}
