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
        return (List<DogBreed>) dogBreedRepository.findAll();
    }

    public boolean doesBreedExist(String breedName) {
        return dogBreedRepository.existsByBreedNameAndParentBreedIsNull(breedName);
    }

    public boolean doesSubBreedExists(String subBreedName) {
        return dogBreedRepository.existsSubBreedBySubBreedName(subBreedName);
    }

    public boolean doesSubBreedInBreedExists(String breedName, String subBreedName) {
        return dogBreedRepository.existsByBreedNameAndParentBreedName(subBreedName, breedName);
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

    public List<DogBreed> findAllSubBreedsByBreed(String breedName) {
        if (!doesBreedExist(breedName)) {
            throw ApiNotFoundException.breed(breedName);
        }
        return dogBreedRepository.findSubBreedsByParentBreedName(breedName);
    }

    public DogBreed findSubBreedNameAndBreedName(String subBreedName, String breedName) throws ApiIsExistException, ApiNotFoundException {
        if (!doesBreedExist(breedName)) {
            throw ApiNotFoundException.breed(breedName);
        }
        if (!doesSubBreedExists(subBreedName)) {
            throw ApiNotFoundException.subBreed(subBreedName);
        }
        if (!doesSubBreedInBreedExists(breedName, subBreedName)) {
            throw ApiNotFoundException.subBreedInBreed(breedName, subBreedName);
        }
        return dogBreedRepository.findBreedWithSubBreed(subBreedName, breedName);
    }

    public DogBreed createBreedSubBreed(String breedName, String subBreedName) {
        if (!doesBreedExist(breedName)) {
            throw ApiNotFoundException.breed(breedName);
        }
        if (doesSubBreedInBreedExists(breedName, subBreedName)) {
            throw ApiIsExistException.subBreedWithBreed(breedName, subBreedName);
        }
        DogBreed dogBreed = findBreedByName(breedName);
        DogBreed newDogBreed = new DogBreed();
        newDogBreed.setBreedName(subBreedName);
        newDogBreed.setParentBreed(dogBreed);
        return dogBreedRepository.save(newDogBreed);
    }

    public void deleteSubBreedByName(String subBreedName) {
        if (!doesSubBreedExists(subBreedName)) {
            throw ApiNotFoundException.subBreed(subBreedName);
        }
        dogBreedRepository.deleteByBreedName(subBreedName);
    }

    public void updateSubBreedName(String oldName, String newName) {
        if (!doesSubBreedExists(oldName)) {
            throw ApiNotFoundException.subBreed(oldName);
        }
        dogBreedRepository.updateBreedName(oldName, newName);
    }

    public DogBreed save(DogBreed breed) {
        return dogBreedRepository.save(breed);
    }

}
