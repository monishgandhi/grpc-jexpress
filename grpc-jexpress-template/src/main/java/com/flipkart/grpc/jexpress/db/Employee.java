package com.flipkart.grpc.jexpress.db;

import javax.persistence.*;

@Entity
@Table(name = "employee")
@NamedQuery(name = "Employee.findAll", query = "select x from Employee x")
public class Employee {

    @Id
    private String id;

    @Column
    private String name;

}
