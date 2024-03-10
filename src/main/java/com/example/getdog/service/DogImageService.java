package com.example.getdog.service;

import com.example.getdog.model.DogBreed;
import com.example.getdog.model.DogImage;
import com.example.getdog.model.SubBreed;
import com.example.getdog.repository.DogImageRepository;
import com.example.getdog.util.ApiIsExistException;
import com.example.getdog.util.ApiNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class DogImageService {
    final private DogImageRepository dogImageRepository;
    final private DogBreedService dogBreedService;
    final private SubBreedService subBreedService;


    public DogImageService(DogImageRepository dogImageRepository, DogBreedService dogBreedService, SubBreedService subBreedService) {
        this.dogImageRepository = dogImageRepository;
        this.dogBreedService = dogBreedService;
        this.subBreedService = subBreedService;
    }

    public boolean doesImageUrlExist(String imageUrl) {
        return dogImageRepository.existsByImageUrl(imageUrl);
    }

    public String findRandomImageByBreed(String breed) throws ApiNotFoundException {
        if (!dogBreedService.doesBreedExist(breed)) {
            throw ApiNotFoundException.breed(breed);
        }
        List<String> imageUrls = dogImageRepository.findImgUrlByBreed(breed);
        Random rand = new Random();
        int randomIndex = rand.nextInt(imageUrls.size());
        return imageUrls.get(randomIndex);
    }

    public String findRandomImageBySubBreed(String breedName, String subBreedName) throws ApiIsExistException {
        if (!dogBreedService.doesBreedExist(breedName)) {
            throw ApiNotFoundException.breed(breedName);
        }
        if (!subBreedService.doesSubBreedExist(subBreedName)) {
            throw ApiNotFoundException.subBreed(subBreedName);
        }
        if (!subBreedService.doesSubBreadWithBreedExist(breedName, subBreedName)) {
            throw ApiNotFoundException.subBreedInBreed(breedName, subBreedName);
        }
        List<String> imageUrls = dogImageRepository.findImgUrlBySubBreed(breedName, subBreedName);
        Random rand = new Random();
        int randomIndex = rand.nextInt(imageUrls.size());
        return imageUrls.get(randomIndex);
    }

    public DogImage createImageUrl(String breedName, String subBreedName, String imageUrl) {
        if (doesImageUrlExist(imageUrl)) {
            throw ApiIsExistException.imageUrl(imageUrl);
        }
        DogImage dogImage = new DogImage();
        DogBreed dogBreed = dogBreedService.findBreedByName(breedName);
        dogImage.setBreed(dogBreed);
        dogImage.setImageUrl(imageUrl);

        if (subBreedName != null) {
            SubBreed subBreed = subBreedService.findSubBreedNameAndBreedName(subBreedName, breedName);
            dogImage.setSubBreed(subBreed);
        }
        return dogImageRepository.save(dogImage);
    }

    public void deleteImageByImageUrl(String imageUrl) {
        dogImageRepository.deleteByImageUrl(imageUrl);
    }

    public void updateImageUrl(String oldName, String newName) throws ApiNotFoundException {
        if (!doesImageUrlExist(oldName)) {
            throw ApiNotFoundException.imageUrl(oldName);
        }
        dogImageRepository.updateImageUrlByOldImageUrl(oldName, newName);
    }
}
