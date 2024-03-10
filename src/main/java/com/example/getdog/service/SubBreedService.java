package com.example.getdog.service;

import com.example.getdog.model.DogBreed;
import com.example.getdog.model.SubBreed;
import com.example.getdog.repository.SubBreedRepository;
import com.example.getdog.util.ApiIsExistException;
import com.example.getdog.util.ApiNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubBreedService {
    private final SubBreedRepository subBreedRepository;
    private final DogBreedService dogBreedService;

    public SubBreedService(SubBreedRepository subBreedRepository, DogBreedService dogBreedService) {
        this.subBreedRepository = subBreedRepository;
        this.dogBreedService = dogBreedService;
    }

    public boolean doesSubBreedExist(String subBreedName) {
        return subBreedRepository.existsBySubBreedName(subBreedName);
    }

    public List<SubBreed> findSubBreedByName(String subBreedName) throws ApiNotFoundException {
        if (!doesSubBreedExist(subBreedName)) {
            throw ApiNotFoundException.subBreed(subBreedName);
        }
        return subBreedRepository.findBySubBreedName(subBreedName);
    }

    public SubBreed findSubBreedNameAndBreedName(String subBreedName, String breedName) throws ApiIsExistException, ApiNotFoundException {
        if (!dogBreedService.doesBreedExist(breedName)) {
            throw ApiNotFoundException.breed(breedName);
        }
        if (!doesSubBreedExist(subBreedName)) {
            throw ApiNotFoundException.subBreed(subBreedName);
        }
        if (!doesSubBreadWithBreedExist(breedName, subBreedName)) {
            throw ApiNotFoundException.subBreedInBreed(breedName, subBreedName);
        }
        return subBreedRepository.findBySubBreedNameAndBreedName(subBreedName, breedName);
    }

    public List<SubBreed> getSubBreedsOfBreed(String breedName) {
        if (!dogBreedService.doesBreedExist(breedName)) {
            throw ApiNotFoundException.breed(breedName);
        }
        return subBreedRepository.findByBreedName(breedName);
    }

    public boolean doesSubBreadWithBreedExist(String breedName, String subBreedName) {
        return subBreedRepository.existsByBreedNameAndSubBreedName(breedName, subBreedName);
    }

    public SubBreed createBreedSubBreed(String breedName, String subBreedName) throws ApiIsExistException {
        DogBreed dogBreed = dogBreedService.findBreedByName(breedName);
        if (doesSubBreadWithBreedExist(breedName, subBreedName)) {
            throw ApiIsExistException.subBreedWithBreed(breedName, subBreedName);
        }
        SubBreed subBreed = new SubBreed();
        subBreed.setBreed(dogBreed);
        subBreed.setSubBreedName(subBreedName);
        return subBreedRepository.save(subBreed);
    }

    public void deleteSubBreedByName(String subBreedName) {
        if (!doesSubBreedExist(subBreedName)) {
            throw ApiNotFoundException.subBreed(subBreedName);
        }
        subBreedRepository.deleteBySubBreedName(subBreedName);
    }

    public void updateSubBreedName(String oldName, String newName) throws ApiNotFoundException {
        if (!doesSubBreedExist(oldName)) {
            throw ApiNotFoundException.subBreed(oldName);
        }
        subBreedRepository.updateSubBreedName(oldName, newName);
    }

    public List<SubBreed> findAllSubBreeds() {
        return subBreedRepository.findAll();
    }
}
