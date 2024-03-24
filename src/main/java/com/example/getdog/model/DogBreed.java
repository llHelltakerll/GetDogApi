package com.example.getdog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "dog_breed")
public class DogBreed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "breed_name", nullable = false)
    private String breedName;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private DogBreed parentBreed;

    @ManyToMany
    @JoinTable(
            name = "breed_characteristics",
            joinColumns = {
                    @JoinColumn(name = "breed_id"),
            },
            inverseJoinColumns = @JoinColumn(name = "characteristic_id")
    )
    private Set<Characteristics> characteristics = new HashSet<>();
}
