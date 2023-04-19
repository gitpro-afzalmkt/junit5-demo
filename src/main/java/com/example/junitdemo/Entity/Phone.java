package com.example.junitdemo.Entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "phones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String company;

    private String model;

    private Integer ram;

    private Integer storage;
}
