package com.example.dogapi.repository;

import com.example.dogapi.model.Characteristics;
import org.springframework.data.repository.CrudRepository;

public interface CharacteristicRepository extends CrudRepository<Characteristics, Long> {

    boolean existsByCharacteristicName(String characteristicName);

    Characteristics findCharacteristicsByCharacteristicName(String name);

}
