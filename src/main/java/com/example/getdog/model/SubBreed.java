package com.example.getdog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "sub_breed")
public class SubBreed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "breed_id", nullable = false)
    private DogBreed breed;

    @Column(name = "sub_breed_name", nullable = false)
    private String subBreedName;


}