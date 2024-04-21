package com.example.dogapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Characteristics {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "characteristic_name", nullable = false)
    private String characteristicName;

    @JsonIgnore
    @ManyToMany(mappedBy = "characteristics", fetch = FetchType.LAZY)
    private Set<DogBreed> breeds = new HashSet<>();
}
