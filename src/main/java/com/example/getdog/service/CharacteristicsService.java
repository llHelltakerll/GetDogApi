package com.example.getdog.service;

import com.example.getdog.model.Characteristics;
import com.example.getdog.model.DogBreed;
import com.example.getdog.repository.CharacteristicRepository;
import com.example.getdog.util.ApiIsExistException;
import com.example.getdog.util.ApiNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CharacteristicsService {
    private final CharacteristicRepository characteristicRepository;
    private final DogBreedService dogBreedService;

    public CharacteristicsService(CharacteristicRepository characteristicRepository, DogBreedService dogBreedService) {
        this.characteristicRepository = characteristicRepository;
        this.dogBreedService = dogBreedService;
    }

    public Set<Characteristics> getCharacteristicsByBreed(String breedName) {
        DogBreed breed = dogBreedService.findBreedByName(breedName);
        if (breed.getParentBreed() != null) {
            throw ApiNotFoundException.breed(breedName);
        }
        return breed.getCharacteristics();
    }

    public Set<Characteristics> getCharacteristicsForBreedAndSubBreed(String breedName, String subBreedName) {
        DogBreed breed = dogBreedService.findSubBreedNameAndBreedName(subBreedName, breedName);
        return breed.getCharacteristics();
    }

    public Characteristics addCharacteristic(String characteristicName) {
        if (characteristicRepository.existsByCharacteristicName(characteristicName)) {
            throw ApiIsExistException.characteristic(characteristicName);
        }
        Characteristics characteristics = new Characteristics();
        characteristics.setCharacteristicName(characteristicName);
        return characteristicRepository.save(characteristics);
    }

    public void deleteCharacteristicByName(String characteristicName) {
        if (!characteristicRepository.existsByCharacteristicName(characteristicName)) {
            throw ApiNotFoundException.characteristic(characteristicName);
        }
        characteristicRepository.deleteByCharacteristicName(characteristicName);
    }

    public void updateByCharacteristic(String oldName, String newName) {
        if (!characteristicRepository.existsByCharacteristicName(oldName)) {
            throw ApiNotFoundException.characteristic(oldName);
        }
        characteristicRepository.updateByCharacteristicName(oldName, newName);
    }

    public List<DogBreed> findBreedsByCharacteristic(String characteristicName) {
        if (!characteristicRepository.existsByCharacteristicName(characteristicName)) {
            throw ApiNotFoundException.characteristic(characteristicName);
        }
        return characteristicRepository.findByCharacteristicName(characteristicName);
    }

    public Characteristics getCharacteristicByName(String characteristicName) {
        if (!characteristicRepository.existsByCharacteristicName(characteristicName)) {
            throw ApiNotFoundException.characteristic(characteristicName);
        }
        return characteristicRepository.getCharacteristicsByCharacteristicName(characteristicName);
    }

    public Characteristics addCharacteristicToBreed(String breedName, String subBreedName, String characteristicName) {
        DogBreed dogBreed;
        if (subBreedName != null) {
            dogBreed = dogBreedService.findSubBreedNameAndBreedName(subBreedName, breedName);
        } else {
            dogBreed = dogBreedService.findBreedByName(breedName);
        }

        Characteristics characteristics = getCharacteristicByName(characteristicName);
        dogBreed.getCharacteristics().add(characteristics);
        dogBreedService.save(dogBreed);
        return characteristics;
    }

    public void deleteCharacteristicWithBreed(String breedName, String subBreedName, String characteristicName) {
        DogBreed dogBreed;
        if (subBreedName != null) {
            dogBreed = dogBreedService.findSubBreedNameAndBreedName(subBreedName, breedName);
        } else {
            dogBreed = dogBreedService.findBreedByName(breedName);
        }
        Characteristics characteristics = getCharacteristicByName(characteristicName);
        dogBreed.getCharacteristics().remove(characteristics);
        dogBreedService.save(dogBreed);
    }

    public void updateCharacteristicWithBreed(String breedName, String oldCharacteristicName,
                                              String subBreedName,
                                              String newCharacteristicName) {
        DogBreed dogBreed;
        if (subBreedName != null) {
            dogBreed = dogBreedService.findSubBreedNameAndBreedName(subBreedName, breedName);
        } else {
            dogBreed = dogBreedService.findBreedByName(breedName);
        }
        Characteristics oldCharacteristics = getCharacteristicByName(oldCharacteristicName);
        dogBreed.getCharacteristics().remove(oldCharacteristics);

        Characteristics newCharacteristics = getCharacteristicByName(newCharacteristicName);
        if (newCharacteristics == null) {
            throw ApiIsExistException.characteristic(newCharacteristicName);
        }

        dogBreed.getCharacteristics().add(newCharacteristics);
        dogBreedService.save(dogBreed);
    }

}
