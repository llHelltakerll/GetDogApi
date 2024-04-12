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
//    private final DogBreedService dogBreedService;

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
        characteristicRepository.delete(characteristics); //TODO to all
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

//    public Characteristics addCharacteristicToBreed(String breedName, String subBreedName,
//                                                    String characteristicName) {
//        DogBreed dogBreed = dogBreedService.findBreedWithSubBreed(breedName, subBreedName);
//        Characteristics characteristics = new Characteristics();
//        characteristics.setCharacteristicName(characteristicName);
//        dogBreed.getCharacteristics().add(characteristics);
//        dogBreedService.save(dogBreed);
//        return characteristics;
//    }
//
//    public void delete(String breedName, String subBreedName,
//                       String characteristicName) {
//        DogBreed dogBreed = dogBreedService.findBreedWithSubBreed(breedName, subBreedName);
//        Characteristics characteristics = findCharByCharName(characteristicName);
//        dogBreed.getCharacteristics().remove(characteristics);
//        dogBreedService.save(dogBreed);
//    }
//
//    public DogBreed update(String breedName, String oldCharacteristicName,
//                           String subBreedName,
//                           String newCharacteristicName) {
//        DogBreed dogBreed = dogBreedService.findBreedWithSubBreed(breedName, subBreedName);
//        Characteristics oldCharacteristics = findCharByCharName(oldCharacteristicName);
//        dogBreed.getCharacteristics().remove(oldCharacteristics);
//
//        Characteristics newCharacteristics = findCharByCharName(newCharacteristicName);
//        dogBreed.getCharacteristics().add(newCharacteristics);
//        return dogBreedService.save(dogBreed);
//    }
}
