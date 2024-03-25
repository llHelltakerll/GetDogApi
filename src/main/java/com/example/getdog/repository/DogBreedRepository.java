package com.example.getdog.repository;

import com.example.getdog.model.DogBreed;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DogBreedRepository extends CrudRepository<DogBreed, Integer> {
    @Query("SELECT db FROM DogBreed db WHERE db.breedName = :breedName")
    DogBreed findByBreedName(String breedName);

    boolean existsByBreedNameAndParentBreedIsNull(String breedName);

    @Query("SELECT db FROM DogBreed db WHERE db.parentBreed IS NULL")
    List<DogBreed> findAllBreedsWhereParentIdIsNull();

    @Transactional
    void deleteByBreedName(String breedName);

    @Transactional
    @Modifying
    @Query("UPDATE DogBreed db SET db.breedName = :newName WHERE db.breedName = :oldName")
    void updateBreedName(String oldName, String newName);

    @Query("SELECT CASE WHEN COUNT(db) > 0 THEN true ELSE false END FROM DogBreed db WHERE db.breedName = :subBreedName AND db.parentBreed IS NOT NULL")
    boolean existsSubBreedBySubBreedName(String subBreedName);

    @Query("SELECT CASE WHEN COUNT(db) > 0 THEN true ELSE false END FROM DogBreed db WHERE db.breedName = :subBreedName AND db.parentBreed.breedName = :parentBreedName")
    boolean existsByBreedNameAndParentBreedName(String subBreedName, String parentBreedName);

    @Query("SELECT db FROM DogBreed db JOIN db.parentBreed parent WHERE parent.breedName = :breedName")
    List<DogBreed> findSubBreedsByParentBreedName(String breedName);

    @Query("SELECT db FROM DogBreed db WHERE db.breedName = :breedName AND db.parentBreed.breedName = :subBreedName")
    DogBreed findBreedWithSubBreed(String breedName, String subBreedName);

    DogBreed findByBreedNameAndParentBreed_BreedName(String subBreedName, String breedName);

    DogBreed findByBreedNameAndParentBreedIsNull(String breedName);

}
