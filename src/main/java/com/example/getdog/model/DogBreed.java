package com.example.getdog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "dog_breed")
public class DogBreed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "breed_name", nullable = false, unique = true)
    private String breedName;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnore // Исключаем поле parentBreed из сериализации
    private DogBreed parentBreed;

    @OneToMany(mappedBy = "parentBreed", fetch = FetchType.LAZY)
    private List<DogBreed> subBreeds = new ArrayList<>();

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
