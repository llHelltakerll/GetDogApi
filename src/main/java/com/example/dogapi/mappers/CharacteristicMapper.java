package com.example.dogapi.mappers;

import com.example.dogapi.dto.CharacteristicsDTO;
import com.example.dogapi.model.Characteristics;
import com.example.dogapi.model.DogBreed;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface CharacteristicMapper {

    @Mapping(target = "breeds", expression = "java(getBreedNames(characteristics.getBreeds()))")
    CharacteristicsDTO charToDto(Characteristics characteristics);

    @Mapping(target = "breeds", ignore = true)
    Characteristics dtoToChar(CharacteristicsDTO dto);

    List<CharacteristicsDTO> listCharToListDto(List<Characteristics> characteristics);

    List<Characteristics> listDtoToCharList(List<CharacteristicsDTO> characteristics);

    default Set<String> getBreedNames(Set<DogBreed> breeds) {
        return breeds.stream()
                .map(DogBreed::getBreedName)
                .collect(Collectors.toSet());
    }

}
