package com.example.dogapi.service;

import com.example.dogapi.exception.ApiIsExistException;
import com.example.dogapi.exception.ApiNotFoundException;
import com.example.dogapi.model.DogBreed;
import com.example.dogapi.repository.DogBreedRepository;
import com.example.dogapi.util.LRUCache;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DogBreedService {
    private final DogBreedRepository dogBreedRepository;
    private final LRUCache<String, DogBreed> breedCache;

    public DogBreedService(DogBreedRepository dogRepo, LRUCache<String, DogBreed> breedCache) {
        this.dogBreedRepository = dogRepo;
        this.breedCache = breedCache;
    }

    public List<DogBreed> findAllBreeds() {
        return dogBreedRepository.findAllBreedsWhereParentIdIsNull();
    }

    public boolean doesBreedExist(String breedName) {
        if (breedCache.containsKey(breedName)) {
            return true;
        }
        return dogBreedRepository.existsByBreedNameAndParentBreedIsNull(breedName);
    }

    public DogBreed findBreedByName(String breedName) throws ApiNotFoundException {
        if (breedCache.containsKey(breedName)) {
            return breedCache.get(breedName);
        }
        DogBreed dogBreed = dogBreedRepository.findByBreedName(breedName)
                .orElseThrow(() -> ApiNotFoundException.breed(breedName));
        breedCache.put(breedName, dogBreed);
        return dogBreed;
    }

    public void createDogBreed(List<DogBreed> breeds) throws ApiIsExistException {
        breeds.forEach(breed -> {
                    if (doesBreedExist(breed.getBreedName())) {
                        throw ApiIsExistException.breed(breed.getBreedName());
                    }
                    breedCache.put(breed.getBreedName(), breed);
                    dogBreedRepository.save(breed);
                }
        );
    }

    public void deleteBreedByName(String breedName) {
        if (!doesBreedExist(breedName)) {
            throw ApiNotFoundException.breed(breedName);
        }
        if (breedCache.containsKey(breedName)) {
            breedCache.remove(breedName);
        }
        dogBreedRepository.deleteByBreedName(breedName);
    }

    public DogBreed updateBreedName(String oldName, String newName) throws ApiIsExistException {
        DogBreed dogBreed = findBreedByName(oldName);
        if (findBreedByName(newName) != null) {
            throw ApiIsExistException.breed(newName);
        }
        if (breedCache.containsKey(oldName)) {
            breedCache.remove(oldName);
        }
        dogBreed.setBreedName(newName);
        return dogBreedRepository.save(dogBreed);
    }

    public DogBreed findBreedWithSubBreed(String breedName, String subBreedName)
            throws ApiIsExistException, ApiNotFoundException {
        DogBreed dogBreed = findBreedByName(breedName);
        if (dogBreed.getSubBreeds()
                .stream()
                .noneMatch(breed -> breed.getBreedName().equals(subBreedName))) {
            throw ApiNotFoundException.subBreedInBreed(breedName, subBreedName);
        }
        return dogBreed;
    }

    public void createBreedSubBreed(String breedName, DogBreed newSubBreed) {
        DogBreed dogBreed = findBreedByName(breedName);
        if (dogBreed.getSubBreeds()
                .stream()
                .anyMatch(breed -> breed.equals(newSubBreed))) {
            //TODO change with to in
            throw ApiIsExistException.subBreedWithBreed(breedName, newSubBreed.getBreedName());
        }
        newSubBreed.setParentBreed(dogBreed);
        dogBreedRepository.save(newSubBreed);
    }

    public void deleteSubBreedByName(String subBreedName) {
        findSubBreedByName(subBreedName);
        dogBreedRepository.deleteByBreedName(subBreedName);
    }

    public DogBreed findSubBreedByName(String subBreedName) {
        DogBreed subBreed = findBreedByName(subBreedName);
        if (subBreed.getParentBreed() == null) {
            throw ApiNotFoundException.subBreed(subBreedName);
        }
        return subBreed;
    }

    public DogBreed updateSubBreedName(String oldName, String newName) {
        if (findSubBreedByName(newName) != null) {
            throw ApiIsExistException.subBreed(newName);
        }
        DogBreed subBreed = findSubBreedByName(oldName);
        subBreed.setBreedName(newName);
        return dogBreedRepository.save(subBreed);
    }

    public DogBreed save(DogBreed breed) {
        return dogBreedRepository.save(breed);
    }

    public void internalErrorTest() {
        throw new NoSuchElementException("Internal server error");
    }
}
