package com.example.dogapi.service;

import com.example.dogapi.dto.DogImageDTO;
import com.example.dogapi.exception.ApiIsExistException;
import com.example.dogapi.exception.ApiNotFoundException;
import com.example.dogapi.mappers.DogImageMapper;
import com.example.dogapi.model.DogBreed;
import com.example.dogapi.model.DogImage;
import com.example.dogapi.repository.DogImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DogImageServiceTest {

    @Mock
    private DogImageRepository dogImageRepository;

    @Mock
    private DogBreedService dogBreedServiceImpl;

    @Mock
    private DogImageMapper dogImageMapper;

    @InjectMocks
    private DogImageService dogImageService;

    private DogBreed dogBreed;
    private DogImage dogImage;
    private DogImageDTO dogImageDTO;

    @BeforeEach
    void setup() {
        String breedName = "breedName";
        String subBreedName = "subBreedName";
        String imageUrl = "imageUrl";
        dogBreed = DogBreed.builder()
                .breedName(breedName)
                .subBreeds(List.of(DogBreed.builder().breedName(subBreedName).build()))
                .build();
        dogImage = DogImage.builder()
                .imageUrl(imageUrl)
                .breed(dogBreed)
                .build();
        dogImageDTO = dogImageMapper.imageToDto(dogImage);
    }

    @Test
    void testDoesImageUrlExist_True() {
        String imageUrl = "imageUrl";
        given(dogImageRepository.existsByImageUrl(imageUrl)).willReturn(true);

        assertTrue(dogImageService.doesImageUrlExist(imageUrl));
    }

    @Test
    void testDoesImageUrlExist_False() {
        String imageUrl = "imageUrl";
        given(dogImageRepository.existsByImageUrl(imageUrl)).willReturn(false);

        assertFalse(dogImageService.doesImageUrlExist(imageUrl));
    }

    @Test
    void testFindRandomImageByBreed() {
        String breedName = "breedName";
        String imageUrl = "imageUrl";
        dogBreed.setImages(List.of(DogImage.builder().imageUrl(imageUrl).build()));
        given(dogBreedServiceImpl.findBreedByName(breedName)).willReturn(dogBreed);

        assertEquals(imageUrl, dogImageService.findRandomImageByBreed(breedName));
    }

    @Test
    void testFindRandomImageByBreed_whenBreedImageUrlListIsEmpty_returnNoImages() {
        String breedName = "breedName";
        dogBreed.setImages(Collections.emptyList());
        given(dogBreedServiceImpl.findBreedByName(breedName)).willReturn(dogBreed);

        assertEquals("no images", dogImageService.findRandomImageByBreed(breedName));
    }

    @Test
    void testFindRandomImageBySubBreed() throws ApiIsExistException {
        String breedName = "breedName";
        String subBreedName = "subBreedName";
        String imageUrl = "imageUrl";
        dogBreed.setImages(List.of(DogImage.builder().imageUrl(imageUrl).build()));
        given(dogBreedServiceImpl.findBreedWithSubBreed(breedName, subBreedName)).willReturn(dogBreed);

        assertEquals(imageUrl, dogImageService.findRandomImageBySubBreed(breedName, subBreedName));
    }

    @Test
    void testCreate() {
        String breedName = "breedName";
        String imageUrl = "imageUrl";
        given(dogBreedServiceImpl.findBreedByName(breedName)).willReturn(dogBreed);
        given(dogImageMapper.imageToDto(dogImage)).willReturn(dogImageDTO);
        given(dogImageRepository.save(dogImage)).willReturn(dogImage);

        assertEquals(dogImageDTO, dogImageService.create(breedName, null, imageUrl));
    }

    @Test
    void testDelete() {
        String imageUrl = "imageUrl";
        given(dogImageRepository.findDogImageByImageUrl(imageUrl)).willReturn(dogImage);

        dogImageService.delete(imageUrl);

        verify(dogImageRepository, times(1)).delete(dogImage);
    }

    @Test
    void testUpdate() throws ApiNotFoundException {
        String oldName = dogImage.getImageUrl();
        String newName = "newName";

        given(dogImageRepository.existsByImageUrl(oldName)).willReturn(true);
        given(dogImageRepository.findDogImageByImageUrl(oldName)).willReturn(dogImage);
        given(dogImageRepository.save(argThat(dogImage -> dogImage.getImageUrl().equals(newName)))).willReturn(dogImage);
        given(dogImageMapper.imageToDto(dogImage)).willReturn(dogImageDTO);

        DogImageDTO actualDogImageDTO = dogImageService.update(oldName, newName);

        assertThat(actualDogImageDTO).isEqualTo(dogImageDTO);
        verify(dogImageRepository, times(1)).findDogImageByImageUrl(oldName);
        verify(dogImageRepository, times(1)).save(argThat(dogImage -> dogImage.getImageUrl().equals(newName)));
        verifyNoMoreInteractions(dogImageRepository);
    }

}
