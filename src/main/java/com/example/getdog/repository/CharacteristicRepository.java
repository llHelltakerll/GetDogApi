package com.example.getdog.repository;

import com.example.getdog.model.Characteristics;
import com.example.getdog.model.DogBreed;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CharacteristicRepository extends CrudRepository<Characteristics, Long> {
    boolean existsByCharacteristicName(String characteristicName);

    @Transactional
    @Modifying
    @Query("DELETE FROM Characteristics c WHERE c.characteristicName = :characteristicName")
    void deleteByCharacteristicName(String characteristicName);

    @Modifying
    @Transactional
    @Query("UPDATE Characteristics c SET c.characteristicName = :newName WHERE c.characteristicName = :oldName")
    void updateByCharacteristicName(String oldName, String newName);

    @Query("SELECT db FROM DogBreed db JOIN db.characteristics ch WHERE ch.characteristicName = :characteristicName")
    List<DogBreed> findByCharacteristicName(String characteristicName);

    @Query("SELECT c FROM Characteristics c WHERE c.characteristicName = :name")
    Characteristics getCharacteristicsByCharacteristicName(@Param("name") String name);
}
