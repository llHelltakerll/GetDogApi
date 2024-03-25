package com.example.getdog.repository;

import com.example.getdog.model.DogBreed;
import com.example.getdog.model.DogImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DogImageRepository extends JpaRepository<DogImage, Long> {

    @Query("SELECT di.imageUrl FROM DogImage di JOIN di.breed db WHERE db = :dogBreed")
    List<String> findImgUrlByBreed(DogBreed dogBreed);

    @Transactional
    void deleteByImageUrl(String imageUrl);

    @Transactional
    @Modifying
    @Query("UPDATE DogImage di SET di.imageUrl = :newImageUrl WHERE di.imageUrl = :oldImageUrl")
    void updateImageUrlByOldImageUrl(String oldImageUrl, String newImageUrl);

    boolean existsByImageUrl(String imageUrl);
}