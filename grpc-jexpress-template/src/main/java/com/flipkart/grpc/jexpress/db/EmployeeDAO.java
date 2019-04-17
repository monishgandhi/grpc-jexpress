package com.flipkart.grpc.jexpress.db;

import com.flipkart.gjex.hibernate.SelectedDataSource;
import com.flipkart.gjex.hibernate.SessionFactoryContext;
import com.flipkart.gjex.hibernate.internal.AbstractDAO;

import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@SelectedDataSource
public class EmployeeDAO extends AbstractDAO<Employee> implements IEmployeeDAO {

    @Inject
    public EmployeeDAO(SessionFactoryContext sessionFactoryContext) {
        super(sessionFactoryContext);
    }

    @Override
    public List<Employee> getAllEmployees() {
        Query query = namedQuery("Employee.findAll");
        List<Employee> result = query.getResultList();
        System.out.println(result);
        return result;
    }

}
