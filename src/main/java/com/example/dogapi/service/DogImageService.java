package com.example.dogapi.service;

import com.example.dogapi.exception.ApiIsExistException;
import com.example.dogapi.exception.ApiNotFoundException;
import com.example.dogapi.model.DogBreed;
import com.example.dogapi.model.DogImage;
import com.example.dogapi.repository.DogImageRepository;
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
        List<String> imageUrls = dogBreed.getImages().stream().map(DogImage::getImageUrl).toList();
        if (imageUrls.isEmpty()) {
            for (DogBreed subBreed : dogBreed.getSubBreeds()) {
                imageUrls.addAll(dogImageRepository.findImgUrlByBreed(subBreed));
            }
        }
        int randomIndex = rand.nextInt(imageUrls.size());
        return imageUrls.get(randomIndex);
    }

    public String findRandomImageBySubBreed(String breedName, String subBreedName)
            throws ApiIsExistException {
        DogBreed dogBreed = dogBreedService.findBreedWithSubBreed(breedName, subBreedName);
        List<String> imageUrls = dogBreed.getImages()
                .stream()
                .map(DogImage::getImageUrl)
                .toList();
        int randomIndex = rand.nextInt(imageUrls.size());
        return imageUrls.get(randomIndex);
    }

    public DogImage create(String breedName, String subBreedName, String imageUrl) {
        if (doesImageUrlExist(imageUrl)) {
            throw ApiIsExistException.imageUrl(imageUrl);
        }

        DogImage dogImage = new DogImage();
        dogImage.setImageUrl(imageUrl);

        if (!dogBreedService.doesBreedExist(breedName)) {
            throw ApiNotFoundException.breed(breedName);
        }
        if (subBreedName != null) {
            DogBreed dogBreed = dogBreedService.findBreedWithSubBreed(subBreedName, breedName);
            dogImage.setBreed(dogBreed.getParentBreed());
        } else {
            DogBreed dogBreed = dogBreedService.findBreedByName(breedName);
            dogImage.setBreed(dogBreed);
        }
        return dogImageRepository.save(dogImage);
    }

    public void delete(String imageUrl) {
        if (!doesImageUrlExist(imageUrl)) {
            throw ApiNotFoundException.imageUrl(imageUrl);
        }
        DogImage dogImage = dogImageRepository.findDogImageByImageUrl(imageUrl);
        dogImageRepository.delete(dogImage);
    }

    public DogImage update(String oldName, String newName) throws ApiNotFoundException {
        if (!doesImageUrlExist(oldName)) {
            throw ApiNotFoundException.imageUrl(oldName);
        }
        DogImage dogImage = dogImageRepository.findDogImageByImageUrl(oldName);
        dogImage.setImageUrl(newName);
        return dogImageRepository.save(dogImage);
    }
}