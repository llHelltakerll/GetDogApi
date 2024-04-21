package com.example.dogapi.repository;

import com.example.dogapi.model.Characteristics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacteristicRepository extends JpaRepository<Characteristics, Long> {

    boolean existsByCharacteristicName(String characteristicName);

    Characteristics findCharacteristicsByCharacteristicName(String name);

}
