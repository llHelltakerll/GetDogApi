package com.example.dogapi.service;

import com.example.dogapi.dto.DogBreedDTO;
import com.example.dogapi.exception.ApiIsExistException;
import com.example.dogapi.exception.ApiNotFoundException;
import com.example.dogapi.mappers.DogBreedMapper;
import com.example.dogapi.model.DogBreed;
import com.example.dogapi.repository.DogBreedRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class DogBreedService {
    final DogBreedMapper dogBreedMapper;
    private final DogBreedRepository dogBreedRepository;

    public List<DogBreedDTO> findAllBreeds() {
        return dogBreedMapper.dogBreedListToDtoList(dogBreedRepository.findAllBreedsWhereParentIdIsNull());
    }

    public boolean doesBreedExist(String breedName) {
        return dogBreedRepository.existsByBreedNameAndParentBreedIsNull(breedName);
    }

    public DogBreed findBreedByName(String breedName) {
        return dogBreedRepository.findByBreedName(breedName)
                .orElseThrow(() -> ApiNotFoundException.breed(breedName));
    }

    @Cacheable(value = "dogBreed", key = "#breedName")
    public DogBreedDTO findBreedDTOByName(String breedName) {
        return dogBreedMapper.dogBreedToDto(findBreedByName(breedName));
    }

    public void saveDogBreed(DogBreedDTO breedDTO) {
        DogBreed breed = dogBreedMapper.dtoToDogBreed(breedDTO);
        if (doesBreedExist(breed.getBreedName())) {
            throw ApiIsExistException.breed(breed.getBreedName());
        }
        dogBreedRepository.save(breed);
    }

    @CacheEvict(value = "dogBreed", key = "#breedName")
    public void deleteBreedByName(String breedName) {
        if (dogBreedRepository.existsByBreedNameAndParentBreedIsNull(breedName)) {
            throw ApiNotFoundException.breed(breedName);
        }
        dogBreedRepository.deleteByBreedName(breedName);
    }

    @CacheEvict(value = "dogBreed", key = "#oldName")
    @CachePut(value = "dogBreed", key = "#newName")
    public DogBreedDTO updateBreedName(String oldName, String newName) {
        DogBreed dogBreed = findBreedByName(oldName);
        if (dogBreedRepository.existsByBreedNameAndParentBreedIsNull(newName)) {
            throw ApiIsExistException.breed(newName);
        }
        dogBreed.setBreedName(newName);
        DogBreed updatedBreed = dogBreedRepository.save(dogBreed);
        return dogBreedMapper.dogBreedToDto(updatedBreed);
    }

    public DogBreed findBreedWithSubBreed(String breedName, String subBreedName)
            throws ApiIsExistException, ApiNotFoundException {
        DogBreed dogBreed = findBreedByName(breedName);
        return dogBreed.getSubBreeds()
                .stream()
                .filter(breed -> breed.getBreedName().equals(subBreedName))
                .findFirst().orElseThrow(() -> ApiNotFoundException.subBreedInBreed(breedName, subBreedName));
    }

    public DogBreedDTO findBreedWithSubBreedDTO(String breedName, String subBreedName) {
        return dogBreedMapper.dogBreedToDto(findBreedWithSubBreed(breedName, subBreedName));
    }

    public void createBreedSubBreed(String breedName, DogBreedDTO newSubBreedDTO) {
        DogBreed newSubBreed = dogBreedMapper.dtoToDogBreed(newSubBreedDTO);
        DogBreed dogBreed = findBreedByName(breedName);
        if (dogBreed.getSubBreeds()
                .stream()
                .anyMatch(breed -> breed.equals(newSubBreed))) {
            throw ApiIsExistException.subBreedWithBreed(breedName, newSubBreed.getBreedName());
        }
        newSubBreed.setParentBreed(dogBreed);
        dogBreedRepository.save(newSubBreed);
    }

    @CacheEvict(value = "subBreed", key = "#subBreedName")
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

    @CacheEvict(value = "dogBreed", key = "#oldName")
    @CachePut(value = "dogBreed", key = "#newName")
    public DogBreedDTO updateSubBreedName(String oldName, String newName) {
        if (findSubBreedByName(newName) != null) {
            throw ApiIsExistException.subBreed(newName);
        }
        DogBreed subBreed = findSubBreedByName(oldName);
        subBreed.setBreedName(newName);
        return dogBreedMapper.dogBreedToDto(dogBreedRepository.save(subBreed));
    }

}
