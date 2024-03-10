package com.example.getdog.repository;

import com.example.getdog.model.SubBreed;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubBreedRepository extends JpaRepository<SubBreed, Integer> {

    boolean existsBySubBreedName(String subBreedName);

    List<SubBreed> findBySubBreedName(String subBreedName);

    @Query("SELECT COUNT(sb) > 0 FROM SubBreed sb WHERE sb.breed.breedName = :breedName AND sb.subBreedName = :subBreedName")
    boolean existsByBreedNameAndSubBreedName(String breedName, String subBreedName);

    @Query("SELECT sb FROM SubBreed sb JOIN sb.breed b WHERE b.breedName = :breedName")
    List<SubBreed> findByBreedName(String breedName);

    @Query("SELECT sb FROM SubBreed sb JOIN sb.breed b WHERE sb.subBreedName = :subBreedName AND b.breedName = :breedName")
    SubBreed findBySubBreedNameAndBreedName(String subBreedName, String breedName);

    @Transactional
    void deleteBySubBreedName(String breedName);

    @Transactional
    @Modifying
    @Query("UPDATE SubBreed sb SET sb.subBreedName = :newSubBreedName WHERE sb.subBreedName = :oldSubBreedName")
    void updateSubBreedName(String oldSubBreedName, String newSubBreedName);
}
