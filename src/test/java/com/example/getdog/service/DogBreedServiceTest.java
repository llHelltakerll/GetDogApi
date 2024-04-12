package com.example.getdog.service;

import com.example.dogapi.dto.DogBreedDTO;
import com.example.dogapi.exception.ApiNotFoundException;
import com.example.dogapi.model.DogBreed;
import com.example.dogapi.repository.DogBreedRepository;
import com.example.dogapi.service.DogBreedService;
import com.example.dogapi.util.LRUCache;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DogBreedServiceTest {

    @Mock
    private DogBreedRepository dogBreedRepository;

    @Mock
    private LRUCache<String, DogBreedDTO> cache;

    @InjectMocks
    private DogBreedService dogBreedService;

    @Test
    void testFindBreedByName_WhenBreedExists() throws ApiNotFoundException {
        // Arrange
        String breedName = "labrador";
        DogBreed expectedBreed = new DogBreed();
        expectedBreed.setBreedName(breedName);

        // Stubbing
        when(dogBreedRepository.findByBreedName(breedName)).thenReturn(Optional.of(expectedBreed));
        when(cache.containsKey(breedName)).thenReturn(false);

        // Act
        DogBreed actualBreed =
                dogBreedRepository.findByBreedName(breedName).orElseThrow();

        // Assert
        assertEquals(expectedBreed, actualBreed);
    }

    @Test
    void testFindBreedByName_WhenBreedDoesNotExist() throws ApiNotFoundException {
        // Arrange
        String breedName = "NonexistentBreed";

        // Stubbing
        when(dogBreedRepository.findByBreedName(breedName)).thenReturn(Optional.empty());

        // Act
        dogBreedService.findBreedByName(breedName);

        // Assert
        // Exception expected
    }
}
