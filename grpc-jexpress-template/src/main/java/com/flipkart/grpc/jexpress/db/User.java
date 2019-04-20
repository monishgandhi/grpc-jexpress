package com.flipkart.grpc.jexpress.db;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
@NamedQueries({
        @NamedQuery(name = "User.findAll", query = "select x from User x"),
        @NamedQuery(name = "User.findById", query = "select x from User x where x.id = :id")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

}
