package com.example.dogapi.service;

import com.example.dogapi.dto.DogBreedDTO;
import com.example.dogapi.exception.ApiIsExistException;
import com.example.dogapi.exception.ApiNotFoundException;
import com.example.dogapi.mappers.DogBreedMapper;
import com.example.dogapi.model.DogBreed;
import com.example.dogapi.repository.DogBreedRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DogBreedServiceImplTest {

    @Mock
    private DogBreedRepository dogBreedRepository;

    @Mock
    private DogBreedMapper dogBreedMapper;

    @InjectMocks
    private DogBreedService dogBreedService;

    @Test
    void testFindAllBreeds() {
        // given
        List<DogBreed> dogBreeds = List.of(
                DogBreed.builder().breedName("Poodle").build(),
                DogBreed.builder().breedName("Labrador").build(),
                DogBreed.builder().breedName("Bulldog").build()
        );
        List<DogBreedDTO> dogBreedDTOs = dogBreeds.stream()
                .map(dogBreedMapper::dogBreedToDto)
                .collect(Collectors.toList());

        given(dogBreedRepository.findAllBreedsWhereParentIdIsNull()).willReturn(dogBreeds);

        given(dogBreedMapper.dogBreedListToDtoList(dogBreeds)).willReturn(dogBreedDTOs);

        // when
        List<DogBreedDTO> result = dogBreedService.findAllBreeds();

        // then
        assertThat(result).isEqualTo(dogBreedDTOs);
    }

    @Test
    void testDoesBreedExist() {
        // given
        String breedName = "Poodle";
        given(dogBreedRepository.existsByBreedNameAndParentBreedIsNull(breedName)).willReturn(true);

        // when
        boolean result = dogBreedService.doesBreedExist(breedName);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void testFindBreedByName() {
        // given
        String breedName = "Poodle";
        DogBreed dogBreed = DogBreed.builder().breedName(breedName).build();
        given(dogBreedRepository.findByBreedName(breedName)).willReturn(Optional.of(dogBreed));

        // when
        DogBreed result = dogBreedService.findBreedByName(breedName);

        // then
        assertThat(result).isEqualTo(dogBreed);
    }

    @Test
    void testFindBreedDTOByName() {
        // given
        String breedName = "Poodle";
        DogBreed dogBreed = DogBreed.builder().breedName(breedName).build();
        DogBreedDTO dogBreedDTO = dogBreedMapper.dogBreedToDto(dogBreed);
        given(dogBreedRepository.findByBreedName(breedName)).willReturn(Optional.of(dogBreed));
        given(dogBreedMapper.dogBreedToDto(dogBreed)).willReturn(dogBreedDTO);

        // when
        DogBreedDTO result = dogBreedService.findBreedDTOByName(breedName);

        // then
        assertThat(result).isEqualTo(dogBreedDTO);
    }

    @Test
    void testDeleteBreedByName() {
        // given
        String breedName = "Poodle";
        given(dogBreedRepository.existsByBreedNameAndParentBreedIsNull(breedName)).willReturn(false);

        // when
        dogBreedService.deleteBreedByName(breedName);

        // then
        verify(dogBreedRepository).deleteByBreedName(breedName);
    }

    @Test
    void testUpdateBreedName_Success() {
        String oldName = "oldName";
        String newName = "newName";
        DogBreed dogBreed = new DogBreed();
        dogBreed.setBreedName(oldName);
        dogBreed.setId(1L);
        dogBreedRepository.save(dogBreed);
        DogBreed newDogBreed = DogBreed.builder()
                .breedName(dogBreed.getBreedName())
                .id(dogBreed.getId())
                .build();
        DogBreedDTO newDogBreedDTO = dogBreedMapper.dogBreedToDto(newDogBreed);

        given(dogBreedRepository.findByBreedName(oldName)).willReturn(Optional.of(dogBreed));
        given(dogBreedRepository.existsByBreedNameAndParentBreedIsNull(newName)).willReturn(false);
        given(dogBreedRepository.save(any())).willReturn(any());
        given(dogBreedMapper.dogBreedToDto(newDogBreed)).willReturn(newDogBreedDTO);

        DogBreedDTO result = dogBreedService.updateBreedName(oldName, newName);

        assertEquals(newDogBreedDTO, result);
    }

    @Test
    void testFindBreedWithSubBreed() {
        // given
        String breedName = "Poodle";
        String subBreedName = "Toy Poodle";
        DogBreed parentBreed = DogBreed.builder().breedName(breedName).build();
        DogBreed subBreed = DogBreed.builder().breedName(subBreedName).parentBreed(parentBreed).build();
        parentBreed.setSubBreeds(List.of(subBreed));
        given(dogBreedRepository.findByBreedName(breedName)).willReturn(Optional.of(parentBreed));

        // when
        DogBreed result = dogBreedService.findBreedWithSubBreed(breedName, subBreedName);

        // then
        assertThat(result).isEqualTo(subBreed);
    }

    @Test
    void testFindBreedWithSubBreedDTO() {
        // given
        String breedName = "Poodle";
        String subBreedName = "Toy Poodle";
        DogBreed parentBreed = DogBreed.builder().breedName(breedName).build();
        DogBreed subBreed = DogBreed.builder().breedName(subBreedName).parentBreed(parentBreed).build();
        parentBreed.setSubBreeds(List.of(subBreed));
        DogBreedDTO subBreedDTO = dogBreedMapper.dogBreedToDto(subBreed);
        given(dogBreedRepository.findByBreedName(breedName)).willReturn(Optional.of(parentBreed));
        given(dogBreedMapper.dogBreedToDto(subBreed)).willReturn(subBreedDTO);

        // when
        DogBreedDTO result = dogBreedService.findBreedWithSubBreedDTO(breedName, subBreedName);

        // then
        assertThat(result).isEqualTo(subBreedDTO);
    }

    @Test
    void testSaveDogBreed() throws ApiIsExistException {
        // given
        DogBreed breed = DogBreed.builder()
                .breedName("testBreed")
                .build();
        DogBreedDTO breedDTO = dogBreedMapper.dogBreedToDto(breed);
        given(dogBreedMapper.dtoToDogBreed(breedDTO)).willReturn(breed);
        given(dogBreedRepository.existsByBreedNameAndParentBreedIsNull("testBreed")).willReturn(false);

        // when
        dogBreedService.saveDogBreed(breedDTO);

        // then
        verify(dogBreedRepository).save(breed);
    }

    @Test
    void testSaveDogBreed_whenBreedExists_thenThrowException() throws ApiIsExistException {
        // given
        DogBreed breed = DogBreed.builder()
                .breedName("testBreed")
                .build();
        DogBreedDTO breedDTO = dogBreedMapper.dogBreedToDto(breed);
        given(dogBreedMapper.dtoToDogBreed(breedDTO)).willReturn(breed);
        given(dogBreedRepository.existsByBreedNameAndParentBreedIsNull("testBreed")).willReturn(true);

        // when and then
        assertThrows(ApiIsExistException.class, () -> dogBreedService.saveDogBreed(breedDTO));
    }

    @Test
    void testDeleteBreedByName_whenBreedNotExists_thenThrowException() {
        // given
        String breedName = "testBreed";
        given(dogBreedRepository.existsByBreedNameAndParentBreedIsNull((breedName))).willReturn(true);

        // when and then
        assertThrows(ApiNotFoundException.class, () -> dogBreedService.deleteBreedByName(breedName));
    }

    @Test
    void updateBreedName_whenNewBreedNameExists_thenThrowException() {
        // given
        String oldName = "testBreedOld";
        String newName = "testBreedNew";
        DogBreed breed = new DogBreed();
        breed.setBreedName(oldName);
        given(dogBreedRepository.findByBreedName(oldName)).willReturn(Optional.of(breed));
        given(dogBreedRepository.existsByBreedNameAndParentBreedIsNull(newName)).willReturn(true);

        // when and then
        assertThrows(ApiIsExistException.class, () -> dogBreedService.updateBreedName(oldName, newName));
    }

    @Test
    void updateBreedName_whenBreedNotExists_thenThrowException() {
        // given
        String oldName = "testBreedOld";
        String newName = "testBreedNew";
        given(dogBreedRepository.findByBreedName(oldName)).willReturn(Optional.empty());

        // when and then
        assertThrows(ApiNotFoundException.class, () -> dogBreedService.updateBreedName(oldName, newName));
    }

}