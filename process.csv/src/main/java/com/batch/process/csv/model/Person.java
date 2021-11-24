package com.batch.process.csv.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private Integer age;
    private String email;

    @ManyToOne
    @JoinColumn(name = "fk_companyId")
    private Company company;

    public Person() {
    }
}
