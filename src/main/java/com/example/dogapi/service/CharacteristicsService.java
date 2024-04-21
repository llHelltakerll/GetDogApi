package com.example.dogapi.service;

import com.example.dogapi.dto.CharacteristicsDTO;
import com.example.dogapi.exception.ApiIsExistException;
import com.example.dogapi.exception.ApiNotFoundException;
import com.example.dogapi.mappers.CharacteristicMapper;
import com.example.dogapi.model.Characteristics;
import com.example.dogapi.repository.CharacteristicRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CharacteristicsService {

    final CharacteristicMapper characteristicMapper;
    private final CharacteristicRepository characteristicRepository;

    public boolean doesCharacteristicExist(String characteristicName) {
        return characteristicRepository.existsByCharacteristicName(characteristicName);
    }

    public List<CharacteristicsDTO> findAll() {
        return characteristicMapper.listCharToListDto(characteristicRepository.findAll());
    }

    public CharacteristicsDTO findCharByCharNameDTO(String charName) {
        return characteristicMapper.charToDto(findCharByCharName(charName));
    }

    public Characteristics findCharByCharName(String charName) {
        Characteristics characteristic = characteristicRepository
                .findCharacteristicsByCharacteristicName(charName);
        if (characteristic == null) {
            throw ApiNotFoundException.characteristic(charName);
        }
        return characteristic;
    }

    public void createChars(List<CharacteristicsDTO> characteristicsDTO) {
        List<Characteristics> characteristics = characteristicMapper.listDtoToCharList(characteristicsDTO);
        characteristics.forEach(characteristic -> {
                    if (doesCharacteristicExist(characteristic.getCharacteristicName())) {
                        throw ApiIsExistException.characteristic(characteristic.getCharacteristicName());
                    }
                    characteristicRepository.save(characteristic);
                }
        );
    }

    public void delete(String characteristicName) {
        Characteristics characteristics = findCharByCharName(characteristicName);
        characteristicRepository.delete(characteristics);
    }

    public CharacteristicsDTO update(String oldName, String newName) {
        Characteristics characteristics = findCharByCharName(oldName);
        characteristics.setCharacteristicName(newName);
        Characteristics updatedChar = characteristicRepository.save(characteristics);
        return characteristicMapper.charToDto(updatedChar);
    }
}
