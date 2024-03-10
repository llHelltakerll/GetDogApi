package com.example.getdog.service;

import com.example.getdog.model.DogBreed;
import com.example.getdog.repository.DogBreedRepository;
import com.example.getdog.util.ApiIsExistException;
import com.example.getdog.util.ApiNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DogBreedService {
    private final DogBreedRepository dogBreedRepository;

    public DogBreedService(DogBreedRepository dogRepo) {
        this.dogBreedRepository = dogRepo;
    }

    public List<DogBreed> findAllBreeds() {
        return dogBreedRepository.findAll();
    }

    public boolean doesBreedExist(String breedName) {
        return dogBreedRepository.existsByBreedName(breedName);
    }

    public DogBreed findBreedByName(String breedName) throws ApiNotFoundException {
        if (!doesBreedExist(breedName)) {
            throw ApiNotFoundException.breed(breedName);
        }
        return dogBreedRepository.findByBreedName(breedName);
    }

    public DogBreed createDogBreed(String breedName) throws ApiIsExistException {
        if (doesBreedExist(breedName)) {
            throw ApiIsExistException.breed(breedName);
        }
        DogBreed newBreed = new DogBreed();
        newBreed.setBreedName(breedName);
        return dogBreedRepository.save(newBreed);
    }

    public void deleteBreedByName(String breedName) {
        if (!doesBreedExist(breedName)) {
            throw ApiIsExistException.breed(breedName);
        }
        dogBreedRepository.deleteByBreedName(breedName);
    }

    public void updateBreedName(String oldName, String newName) throws ApiIsExistException {
        if (!doesBreedExist(oldName)) {
            throw ApiIsExistException.breed(oldName);
        }
        dogBreedRepository.updateBreedName(oldName, newName);
    }
}
