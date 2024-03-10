package com.example.getdog.repository;

import com.example.getdog.model.DogBreed;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DogBreedRepository extends JpaRepository<DogBreed, Integer> {
    DogBreed findByBreedName(String breedName);

    boolean existsByBreedName(String breedName);

    @Transactional
    void deleteByBreedName(String breedName);

    @Transactional
    @Modifying
    @Query("UPDATE DogBreed db SET db.breedName = :newName WHERE db.breedName = :oldName")
    void updateBreedName(String oldName, String newName);

}
