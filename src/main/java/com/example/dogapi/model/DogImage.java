package com.example.dogapi.model;

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
    private Long id;

    @Column(name = "image_url", nullable = false, unique = true)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "breed_id")
    private DogBreed breed;

}