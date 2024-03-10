package com.example.getdog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dog_image")
public class DogImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "breed_id", nullable = false)
    private DogBreed breed;

    @ManyToOne
    @JoinColumn(name = "sub_breed_id")
    private SubBreed subBreed;

    @Column(name = "image_url", nullable = false, unique = true)
    private String imageUrl;

}