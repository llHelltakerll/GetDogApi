package com.example.dogapi.service;

import com.example.dogapi.dto.CharacteristicsDTO;
import com.example.dogapi.exception.ApiNotFoundException;
import com.example.dogapi.mappers.CharacteristicMapper;
import com.example.dogapi.model.Characteristics;
import com.example.dogapi.repository.CharacteristicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CharacteristicsServiceTest {

    @Mock
    private CharacteristicRepository characteristicRepository;

    @Mock
    private CharacteristicMapper characteristicMapper;

    @InjectMocks
    private CharacteristicsService characteristicsService;

    @Test
    void testDoesCharacteristicExist_whenCharacteristicExists_thenReturnTrue() {
        given(characteristicRepository.existsByCharacteristicName("characteristicName")).willReturn(true);

        boolean result = characteristicsService.doesCharacteristicExist("characteristicName");

        assertTrue(result);
    }

    @Test
    void testDoesCharacteristicExist_whenCharacteristicDoesNotExist_thenReturnFalse() {
        given(characteristicRepository.existsByCharacteristicName("characteristicName")).willReturn(false);

        boolean result = characteristicsService.doesCharacteristicExist("characteristicName");

        assertFalse(result);
    }

    @Test
    void testFindAll_whenCharacteristicsExist_thenReturnCharacteristicsDTOList() {
        given(characteristicRepository.findAll()).willReturn(List.of(new Characteristics(), new Characteristics()));

        List<CharacteristicsDTO> result = characteristicsService.findAll();

        assertNotNull(result);
    }

    @Test
    void testFindAll_whenCharacteristicsDoNotExist_thenReturnEmptyCharacteristicsDTOList() {
        given(characteristicRepository.findAll()).willReturn(new ArrayList<>());
        List<CharacteristicsDTO> result = characteristicsService.findAll();

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testFindCharByCharNameDTO_whenCharacteristicDoesNotExist_thenThrowApiNotFoundException() {
        assertThrows(ApiNotFoundException.class, () -> characteristicsService.findCharByCharNameDTO("nonExistentCharacteristicName"));
    }

    @Test
    void testCreateChars_whenCharacteristicsDoNotExist_thenSaveCharacteristics() {
        String charName1 = "charName1";
        String charName2 = "charName2";
        List<CharacteristicsDTO> characteristicsDTOList = List.of(new CharacteristicsDTO(
                        1, charName1, null),
                new CharacteristicsDTO(
                        2, charName2, null));
        List<Characteristics> characteristicsList = List.of(
                Characteristics.builder()
                        .characteristicName(charName1)
                        .build(),
                Characteristics.builder()
                        .characteristicName(charName2)
                        .build()
        );
        given(characteristicMapper.listDtoToCharList(characteristicsDTOList)).willReturn(characteristicsList);

        characteristicsService.createChars(characteristicsDTOList);

        verify(characteristicRepository, times(2)).save(any(Characteristics.class));
    }

    @Test
    void testDelete_whenCharacteristicExists_thenDeleteCharacteristic() {

        String charName = "charName";
        Characteristics characteristics = Characteristics.builder()
                .characteristicName(charName)
                .build();
        given(characteristicRepository
                .findCharacteristicsByCharacteristicName(charName)).willReturn(characteristics);

        characteristicsService.delete(charName);

        verify(characteristicRepository).delete(any(Characteristics.class));
    }

    @Test
    void testDelete_whenCharacteristicDoesNotExist_thenThrowApiNotFoundException() {
        assertThrows(ApiNotFoundException.class, () -> characteristicsService.delete("nonExistentCharacteristicName"));
    }

    @Test
    void testUpdate_whenCharacteristicExists_thenUpdateCharacteristic() {
        String charName = "charName";
        String newCharName = "newName";
        Characteristics characteristics = Characteristics.builder()
                .characteristicName(charName)
                .build();
        CharacteristicsDTO expectedDTO = new CharacteristicsDTO(1, newCharName, null);
        given(characteristicRepository
                .findCharacteristicsByCharacteristicName(charName)).willReturn(characteristics);
        given(characteristicRepository.save(characteristics)).willReturn(characteristics);
        given(characteristicMapper.charToDto(any())).willReturn(expectedDTO);

        CharacteristicsDTO result = characteristicsService.update(charName, newCharName);

        assertNotNull(result);
        assertEquals(newCharName, result.characteristicName());
        assertEquals(1L, result.id());
        verify(characteristicRepository).save(any(Characteristics.class));
    }

    @Test
    void testUpdate_whenCharacteristicDoesNotExist_thenThrowApiNotFoundException() {
        assertThrows(ApiNotFoundException.class, () -> characteristicsService.update("nonExistentCharacteristicName", "newCharacteristicName"));
    }
}

