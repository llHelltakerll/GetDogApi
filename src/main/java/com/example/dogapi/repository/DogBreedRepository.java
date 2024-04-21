package com.example.dogapi.repository;

import com.example.dogapi.model.DogBreed;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DogBreedRepository extends JpaRepository<DogBreed, Integer> {

    Optional<DogBreed> findByBreedName(String breedName);

    boolean existsByBreedNameAndParentBreedIsNull(String breedName);

    @Query("SELECT db FROM DogBreed db WHERE db.parentBreed IS NULL")
    List<DogBreed> findAllBreedsWhereParentIdIsNull();

    @Transactional
    void deleteByBreedName(String breedName);

}
