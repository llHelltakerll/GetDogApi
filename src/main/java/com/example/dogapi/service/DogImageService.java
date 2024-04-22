package com.example.dogapi.service;

import com.example.dogapi.dto.DogImageDTO;
import com.example.dogapi.exception.ApiIsExistException;
import com.example.dogapi.exception.ApiNotFoundException;
import com.example.dogapi.mappers.DogImageMapper;
import com.example.dogapi.model.DogBreed;
import com.example.dogapi.model.DogImage;
import com.example.dogapi.repository.DogImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
@AllArgsConstructor
public class DogImageService {
    final DogImageMapper dogImageMapper;
    private final DogImageRepository dogImageRepository;
    private final DogBreedService dogBreedServiceImpl;
    private final SecureRandom rand = new SecureRandom();

    public boolean doesImageUrlExist(String imageUrl) {
        return dogImageRepository.existsByImageUrl(imageUrl);
    }

    public String findRandomImageByBreed(String breedName) {
        DogBreed dogBreed = dogBreedServiceImpl.findBreedByName(breedName);
        List<String> imageUrls = new java.util.ArrayList<>(dogBreed.getImages()
                .stream()
                .map(DogImage::getImageUrl)
                .toList());
        for (DogBreed subBreed : dogBreed.getSubBreeds()) {
            imageUrls.addAll(dogImageRepository.findImgUrlByBreed(subBreed));
        }
        if (imageUrls.isEmpty()) {
            return "no images";
        }
        int randomIndex = rand.nextInt(imageUrls.size());
        return imageUrls.get(randomIndex);
    }

    public String findRandomImageBySubBreed(String breedName, String subBreedName)
            throws ApiIsExistException {
        DogBreed dogBreed = dogBreedServiceImpl.findBreedWithSubBreed(breedName, subBreedName);
        List<String> imageUrls = new java.util.ArrayList<>(dogBreed.getImages().stream()
                .map(DogImage::getImageUrl)
                .toList());
        if (imageUrls.isEmpty()) {
            return "no images";
        }
        int randomIndex = rand.nextInt(imageUrls.size());
        return imageUrls.get(randomIndex);
    }

    public DogImageDTO create(String breedName, String subBreedName, String imageUrl) {
        if (dogImageRepository.existsByImageUrl(imageUrl)) {
            throw ApiIsExistException.imageUrl(imageUrl);
        }

        DogImage dogImage = new DogImage();
        dogImage.setImageUrl(imageUrl);

        if (subBreedName != null) {
            DogBreed dogBreed = dogBreedServiceImpl.findBreedWithSubBreed(subBreedName, breedName);
            dogImage.setBreed(dogBreed.getParentBreed());
        } else {
            DogBreed dogBreed = dogBreedServiceImpl.findBreedByName(breedName);
            dogImage.setBreed(dogBreed);
        }
        return dogImageMapper.imageToDto(dogImageRepository.save(dogImage));
    }

    public void delete(String imageUrl) {
        if (dogImageRepository.existsByImageUrl(imageUrl)) {
            throw ApiNotFoundException.imageUrl(imageUrl);
        }
        DogImage dogImage = dogImageRepository.findDogImageByImageUrl(imageUrl);
        dogImageRepository.delete(dogImage);
    }

    public DogImageDTO update(String oldName, String newName) throws ApiNotFoundException {
        if (!dogImageRepository.existsByImageUrl(oldName)) {
            throw ApiNotFoundException.imageUrl(oldName);
        }
        DogImage dogImage = dogImageRepository.findDogImageByImageUrl(oldName);
        dogImage.setImageUrl(newName);
        DogImage savedImage = dogImageRepository.save(dogImage);
        return dogImageMapper.imageToDto(savedImage);
    }
}