package com.example.dogapi.mappers;

import com.example.dogapi.dto.DogImageDTO;
import com.example.dogapi.model.DogImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DogImageMapper {
    @Mapping(target = "breed", source = "dogImage.breed.breedName")
    DogImageDTO imageToDto(DogImage dogImage);

    @Mapping(target = "breed", ignore = true)
    DogImage dtoToImage(DogImageDTO dogImageDTO);

}
