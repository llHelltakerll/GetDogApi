package com.example.dogapi.repository;

import com.example.dogapi.model.DogBreed;
import com.example.dogapi.model.DogImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DogImageRepository extends JpaRepository<DogImage, Long> {

    @Query("SELECT di.imageUrl FROM DogImage di JOIN di.breed db WHERE db = :dogBreed")
    List<String> findImgUrlByBreed(DogBreed dogBreed);

    boolean existsByImageUrl(String imageUrl);

    DogImage findDogImageByImageUrl(String imageUrl);
}