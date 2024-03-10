package com.example.getdog.repository;

import com.example.getdog.model.DogImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DogImageRepository extends JpaRepository<DogImage, Long> {
    @Query("SELECT di.imageUrl FROM DogImage di JOIN di.breed db WHERE db.breedName = ?1")
    List<String> findImgUrlByBreed(String breedName);

    @Query("SELECT di.imageUrl FROM DogImage di JOIN di.breed db JOIN di.subBreed sb WHERE db.breedName = ?1 AND sb.subBreedName = ?2")
    List<String> findImgUrlBySubBreed(String breedName, String subBreedName);

    @Transactional
    void deleteByImageUrl(String imageUrl);

    @Transactional
    @Modifying
    @Query("UPDATE DogImage di SET di.imageUrl = :newImageUrl WHERE di.imageUrl = :oldImageUrl")
    void updateImageUrlByOldImageUrl(String oldImageUrl, String newImageUrl);

    boolean existsByImageUrl(String imageUrl);
}