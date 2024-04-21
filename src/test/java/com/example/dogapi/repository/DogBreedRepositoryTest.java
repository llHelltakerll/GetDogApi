package com.example.dogapi.repository;

import com.example.dogapi.model.DogBreed;
import com.example.dogapi.util.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DogBreedRepositoryTest {

    @Autowired
    DogBreedRepository dogBreedRepository;

    @BeforeEach
    void setUp() {
        dogBreedRepository.deleteAll();
    }

    @Test
    @DisplayName("Test save dog breed functionality")
    void givenDogBreedObject_whenSave_thanDogBreedIsCreated() {
        //given
        DogBreed dogBreed = DataUtils.getPoodleTransient();
        //when
        DogBreed savedDogBreed = dogBreedRepository.save(dogBreed);
        //then
        assertThat(savedDogBreed).isNotNull();
        assertThat(savedDogBreed.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test update dog breed functionality")
    void givenDogBreedToUpdate_whenSave_thenBreedNameIsChanged() {
        //given
        String updatedBreedName = "updatedBreedName";
        DogBreed dogBreedToCreate = DataUtils.getPoodleTransient();
        dogBreedRepository.save(dogBreedToCreate);
        //when
        DogBreed dogBreedToUpdate = dogBreedRepository.findById(Math.toIntExact(dogBreedToCreate.getId()))
                .orElse(null);
        assertThat(dogBreedToUpdate).isNotNull();
        dogBreedToUpdate.setBreedName(updatedBreedName);
        DogBreed updatedDogBreed = dogBreedRepository.save(dogBreedToUpdate);
        //then
        assertThat(updatedDogBreed).isNotNull();
        assertThat(updatedDogBreed.getBreedName()).isEqualTo(updatedBreedName);
    }

    @Test
    @DisplayName("Test get dog breed by id functionality")
    void givenDogBreedCreated_whenGetById_thenDeveloperIsReturned() {
        //given
        DogBreed dogBreedToCreate = DataUtils.getPoodleTransient();
        DogBreed dogBreedToFind = dogBreedRepository.save(dogBreedToCreate);
        Integer dogBreedToFindId = Math.toIntExact(dogBreedToFind.getId());
        //when
        DogBreed foundedDogBreed = dogBreedRepository.findById(dogBreedToFindId)
                .orElse(null);
        //then
        assertThat(foundedDogBreed).isEqualTo(dogBreedToFind);
    }

    @Test
    @DisplayName("Test dog breed not found functionality")
    void givenDogBreedIsNotCreated_whenGetById_thenOptionalIsEmpty() {
        //given

        //when
        DogBreed obtainedDogBreed = dogBreedRepository.findById(1).orElse(null);
        //then
        assertThat(obtainedDogBreed).isNull();
    }

    @Test
    @DisplayName("Test get all breeds functionality")
    void givenThreeDogBreedsAreStored_whenFoundAllDogBreeds_thenAllDogBreedsAreReturns() {
        //given
        DogBreed dogBreed1 = DataUtils.getPoodleTransient();
        DogBreed dogBreed2 = DataUtils.getLabradorTransient();
        DogBreed dogBreed3 = DataUtils.getBulldogTransient();
        dogBreedRepository.saveAll(List.of(dogBreed1, dogBreed2, dogBreed3));
        //when
        List<DogBreed> obtainedDogBreeds = dogBreedRepository.findAll();
        //then
        assertThat(obtainedDogBreeds).isNotEmpty();
    }
}