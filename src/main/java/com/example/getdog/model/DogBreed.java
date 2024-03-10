package com.example.getdog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "dog_breed")
public class DogBreed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "breed_name", nullable = false, unique = true)
    private String breedName;


}
