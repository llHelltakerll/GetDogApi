package com.example.getdog.service;

import com.example.getdog.model.DogBreed;
import com.example.getdog.repository.DogBreedRepository;
import com.example.getdog.util.ApiIsExistException;
import com.example.getdog.util.ApiNotFoundException;
import com.example.getdog.util.LRUCache;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DogBreedService {
    private final DogBreedRepository dogBreedRepository;
    private final LRUCache<String, DogBreed> breedCache;
    private final LRUCache<String, DogBreed> subBreedCache;
    private final LRUCache<String, DogBreed> breedWithSubBreedCache;

    public DogBreedService(DogBreedRepository dogRepo, LRUCache<String, DogBreed> breedCache, LRUCache<String, DogBreed> subBreedCache, LRUCache<String, DogBreed> breedWithSubBreedCache) {
        this.dogBreedRepository = dogRepo;
        this.breedCache = breedCache;
        this.subBreedCache = subBreedCache;
        this.breedWithSubBreedCache = breedWithSubBreedCache;
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

    public boolean doesSubBreedExists(String subBreedName) {
        if (subBreedCache.containsKey(subBreedName)) {
            return true;
        }
        return dogBreedRepository.existsSubBreedBySubBreedName(subBreedName);
    }

    public boolean doesSubBreedInBreedExists(String breedName, String subBreedName) {
        if (breedWithSubBreedCache.containsKey(breedName + "_" + subBreedName)) {
            return true;
        }
        return dogBreedRepository.existsByBreedNameAndParentBreedName(subBreedName, breedName);
    }

    public DogBreed findBreedByName(String breedName) throws ApiNotFoundException {
        if (!doesBreedExist(breedName)) {
            throw ApiNotFoundException.breed(breedName);
        }
        if (breedCache.containsKey(breedName)) {
            return breedCache.get(breedName);
        }
        DogBreed dogBreed = dogBreedRepository.findByBreedName(breedName);
        breedCache.put(breedName, dogBreed);
        return dogBreed;
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
            throw ApiNotFoundException.breed(breedName);
        }
        if (breedCache.containsKey(breedName)) {
            breedCache.remove(breedName);
            breedWithSubBreedCache.removeKeysByPrefix(breedName + "_");
        }
        dogBreedRepository.deleteByBreedName(breedName);
    }

    public void updateBreedName(String oldName, String newName) throws ApiIsExistException {
        if (!doesBreedExist(oldName)) {
            throw ApiIsExistException.breed(oldName);
        }
        if (doesBreedExist(newName)) {
            throw ApiIsExistException.breed(newName);
        }
        if (breedCache.containsKey(oldName)) {
            breedCache.remove(oldName);
            breedWithSubBreedCache.removeKeysByPrefix(oldName + "_");
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
        String cacheKey = breedName + "_" + subBreedName;
        if (breedWithSubBreedCache.containsKey(cacheKey)) {
            return breedWithSubBreedCache.get(cacheKey);
        }
        DogBreed breed = dogBreedRepository.findBreedWithSubBreed(subBreedName, breedName);
        breedWithSubBreedCache.put(cacheKey, breed);
        breedCache.put(breedName, breed.getParentBreed());
        subBreedCache.put(subBreedName, breed);
        return breed;
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
        if (subBreedCache.containsKey(subBreedName)) {
            subBreedCache.remove(subBreedName);
            breedWithSubBreedCache.removeKeysBySuffix("_" + subBreedName);
        }
        dogBreedRepository.deleteByBreedName(subBreedName);
    }

    public void updateSubBreedName(String oldName, String newName) {
        if (!doesSubBreedExists(oldName)) {
            throw ApiNotFoundException.subBreed(oldName);
        }
        if (doesSubBreedExists(newName)) {
            throw ApiIsExistException.breed(newName);
        }
        if (subBreedCache.containsKey(oldName)) {
            subBreedCache.remove(oldName);
            breedWithSubBreedCache.removeKeysBySuffix("_" + oldName);
        }
        dogBreedRepository.updateBreedName(oldName, newName);
    }

    public void save(DogBreed breed) {
        dogBreedRepository.save(breed);
    }

}
