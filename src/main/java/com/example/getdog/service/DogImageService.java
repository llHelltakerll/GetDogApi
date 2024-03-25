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

    public String findRandomImageByBreed(String breedName) throws ApiNotFoundException {
        DogBreed dogBreed = dogBreedService.findBreedByName(breedName);
        List<String> imageUrls = dogImageRepository.findImgUrlByBreed(dogBreed);
        if (imageUrls.isEmpty()) {
            for (DogBreed subBreed : dogBreed.getSubBreeds()) {
                imageUrls.addAll(dogImageRepository.findImgUrlByBreed(subBreed));
            }
        }
        int randomIndex = rand.nextInt(imageUrls.size());
        return imageUrls.get(randomIndex);
    }

    public String findRandomImageBySubBreed(String breedName, String subBreedName) throws ApiIsExistException {
        DogBreed dogBreed = dogBreedService.findSubBreedNameAndBreedName(subBreedName, breedName);
        List<String> imageUrls = dogImageRepository.findImgUrlByBreed(dogBreed);
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
            dogImage.setBreed(dogBreed.getParentBreed());
        } else {
            DogBreed dogBreed = dogBreedService.findBreedByName(breedName);
            dogImage.setBreed(dogBreed);
        }
        return dogImageRepository.save(dogImage);
    }

    public void deleteImageByImageUrl(String imageUrl) {
        if (!doesImageUrlExist(imageUrl)) {
            throw ApiNotFoundException.imageUrl(imageUrl);
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