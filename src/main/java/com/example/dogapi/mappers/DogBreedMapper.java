package com.example.dogapi.mappers;

import com.example.dogapi.dto.DogBreedDTO;
import com.example.dogapi.model.Characteristics;
import com.example.dogapi.model.DogBreed;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface DogBreedMapper {

    @Mapping(target = "parentBreed", source = "parentBreed.breedName")
    @Mapping(target = "subBreeds", expression = "java(getSubBreedNames(dogBreed.getSubBreeds()))")
    @Mapping(target = "characteristics", expression = "java(getCharacteristicNames(dogBreed.getCharacteristics()))")
    DogBreedDTO dogBreedToDto(DogBreed dogBreed);

    @Mapping(target = "parentBreed", ignore = true)
    @Mapping(target = "subBreeds", ignore = true)
    @Mapping(target = "characteristics", ignore = true)
    DogBreed dtoToDogBreed(DogBreedDTO dto);

    List<DogBreedDTO> dogBreedListToDtoList(List<DogBreed> dogBreeds);

    List<DogBreed> dtoListToDogBreedList(List<DogBreedDTO> dogBreedDTOS);

    default List<String> getSubBreedNames(List<DogBreed> subBreeds) {
        return subBreeds.stream()
                .map(DogBreed::getBreedName)
                .toList();
    }

    default Set<String> getCharacteristicNames(Set<Characteristics> characteristics) {
        return characteristics.stream()
                .map(Characteristics::getCharacteristicName)
                .collect(Collectors.toSet());
    }
}
