package com.example.dogapi.mappers;

import com.example.dogapi.dto.DogImageDTO;
import com.example.dogapi.model.DogBreed;
import com.example.dogapi.model.DogImage;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-22T01:46:38+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 18.0.2 (Amazon.com Inc.)"
)
@Component
public class DogImageMapperImpl implements DogImageMapper {

    @Override
    public DogImageDTO imageToDto(DogImage dogImage) {
        if ( dogImage == null ) {
            return null;
        }

        String breed = null;
        Long id = null;
        String imageUrl = null;

        breed = dogImageBreedBreedName( dogImage );
        id = dogImage.getId();
        imageUrl = dogImage.getImageUrl();

        DogImageDTO dogImageDTO = new DogImageDTO( id, imageUrl, breed );

        return dogImageDTO;
    }

    @Override
    public DogImage dtoToImage(DogImageDTO dogImageDTO) {
        if ( dogImageDTO == null ) {
            return null;
        }

        DogImage.DogImageBuilder dogImage = DogImage.builder();

        dogImage.id( dogImageDTO.id() );
        dogImage.imageUrl( dogImageDTO.imageUrl() );

        return dogImage.build();
    }

    private String dogImageBreedBreedName(DogImage dogImage) {
        if ( dogImage == null ) {
            return null;
        }
        DogBreed breed = dogImage.getBreed();
        if ( breed == null ) {
            return null;
        }
        String breedName = breed.getBreedName();
        if ( breedName == null ) {
            return null;
        }
        return breedName;
    }
}
