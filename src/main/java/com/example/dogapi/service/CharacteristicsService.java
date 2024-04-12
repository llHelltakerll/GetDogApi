package com.example.dogapi.service;

import com.example.dogapi.exception.ApiIsExistException;
import com.example.dogapi.exception.ApiNotFoundException;
import com.example.dogapi.model.Characteristics;
import com.example.dogapi.model.DogBreed;
import com.example.dogapi.repository.CharacteristicRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CharacteristicsService {
    private final CharacteristicRepository characteristicRepository;

    public CharacteristicsService(CharacteristicRepository characteristicRepository) {
        this.characteristicRepository = characteristicRepository;
    }

    public boolean doesCharacteristicExist(String characteristicName) {
        return characteristicRepository.existsByCharacteristicName(characteristicName);
    }

    public Characteristics findCharByCharName(String charName) {
        Characteristics characteristic = characteristicRepository
                .findCharacteristicsByCharacteristicName(charName);
        if (characteristic == null) {
            throw ApiNotFoundException.characteristic(charName);
        }
        return characteristic;
    }

    public Characteristics create(Characteristics characteristic) {
        if (doesCharacteristicExist(characteristic.getCharacteristicName())) {
            throw ApiIsExistException.characteristic(characteristic.getCharacteristicName());
        }
        return characteristicRepository.save(characteristic);
    }

    public void delete(String characteristicName) {
        Characteristics characteristics = findCharByCharName(characteristicName);
        characteristicRepository.delete(characteristics);
    }

    public Characteristics update(String oldName, String newName) {
        Characteristics characteristics = findCharByCharName(oldName);
        characteristics.setCharacteristicName(newName);
        return characteristicRepository.save(characteristics);
    }

    public Set<DogBreed> findBreedsByCharacteristic(String characteristicName) {
        Characteristics characteristics = findCharByCharName(characteristicName);
        return characteristics.getBreeds();
    }

}
