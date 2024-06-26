package com.example.dogapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dog_breed")
public class DogBreed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "breed_name", nullable = false, unique = true)
    private String breedName;

    @ManyToOne
    @JoinColumn(name = "parent_id")
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

    @OneToMany(mappedBy = "breed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DogImage> images = new ArrayList<>();
}
