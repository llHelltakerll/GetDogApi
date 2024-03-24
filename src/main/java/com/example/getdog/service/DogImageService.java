package com.example.getdog.service;

import com.example.getdog.model.DogBreed;
import com.example.getdog.model.DogImage;
import com.example.getdog.repository.DogImageRepository;
import com.example.getdog.util.ApiIsExistException;
import com.example.getdog.util.ApiNotFoundException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
public class DogImageService {
    private final DogImageRepository dogImageRepository;
    private final DogBreedService dogBreedService;

    private final SecureRandom rand = new SecureRandom();

    public DogImageService(DogImageRepository dogImageRepository, DogBreedService dogBreedService) {
        this.dogImageRepository = dogImageRepository;
        this.dogBreedService = dogBreedService;
    }

    public boolean doesImageUrlExist(String imageUrl) {
        return dogImageRepository.existsByImageUrl(imageUrl);
    }

    public String findRandomImageByBreed(String breed) throws ApiNotFoundException {
        if (!dogBreedService.doesBreedExist(breed)) {
            throw ApiNotFoundException.breed(breed);
        }
        List<String> imageUrls = dogImageRepository.findImgUrlByBreed(breed);
        int randomIndex = rand.nextInt(imageUrls.size());
        return imageUrls.get(randomIndex);
    }

    public String findRandomImageBySubBreed(String breedName, String subBreedName) throws ApiIsExistException {
        if (!dogBreedService.doesBreedExist(breedName)) {
            throw ApiNotFoundException.breed(breedName);
        }
        if (!dogBreedService.doesSubBreedExists(subBreedName)) {
            throw ApiNotFoundException.subBreed(subBreedName);
        }
        if (!dogBreedService.doesSubBreedInBreedExists(breedName, subBreedName)) {
            throw ApiNotFoundException.subBreedInBreed(breedName, subBreedName);
        }
        List<String> imageUrls = dogImageRepository.findImageUrlsByBreedAndSubBreed(breedName, subBreedName);
        int randomIndex = rand.nextInt(imageUrls.size());
        return imageUrls.get(randomIndex);
    }

    public DogImage createImageUrl(String breedName, String subBreedName, String imageUrl) {
        if (doesImageUrlExist(imageUrl)) {
            throw ApiIsExistException.imageUrl(imageUrl);
        }

        DogImage dogImage = new DogImage();
        dogImage.setImageUrl(imageUrl);

        if (!dogBreedService.doesBreedExist(breedName)) {
            throw ApiNotFoundException.breed(breedName);
        }
        if (subBreedName != null) {
            if (!dogBreedService.doesSubBreedExists(subBreedName)) {
                throw ApiNotFoundException.subBreed(subBreedName);
            }
            if (!dogBreedService.doesSubBreedInBreedExists(breedName, subBreedName)) {
                throw ApiNotFoundException.subBreedInBreed(breedName, subBreedName);
            }
            DogBreed dogBreed = dogBreedService.findSubBreedNameAndBreedName(subBreedName, breedName);
            dogImage.setSubBreed(dogBreed);
            dogImage.setBreed(dogBreed.getParentBreed());
        } else {
            DogBreed dogBreed = dogBreedService.findBreedByName(breedName);
            dogImage.setBreed(dogBreed);
        }
        return dogImageRepository.save(dogImage);
    }

    public void deleteImageByImageUrl(String imageUrl) {
        if (!doesImageUrlExist(imageUrl)) {
            throw ApiIsExistException.imageUrl(imageUrl);
        }
        dogImageRepository.deleteByImageUrl(imageUrl);
    }

    public void updateImageUrl(String oldName, String newName) throws ApiNotFoundException {
        if (!doesImageUrlExist(oldName)) {
            throw ApiNotFoundException.imageUrl(oldName);
        }
        dogImageRepository.updateImageUrlByOldImageUrl(oldName, newName);
    }
}
