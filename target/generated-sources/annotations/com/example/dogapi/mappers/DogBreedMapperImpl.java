package com.example.dogapi.mappers;

import com.example.dogapi.dto.DogBreedDTO;
import com.example.dogapi.model.DogBreed;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-22T01:46:39+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 18.0.2 (Amazon.com Inc.)"
)
@Component
public class DogBreedMapperImpl implements DogBreedMapper {

    @Override
    public DogBreedDTO dogBreedToDto(DogBreed dogBreed) {
        if ( dogBreed == null ) {
            return null;
        }

        String parentBreed = null;
        Long id = null;
        String breedName = null;

        parentBreed = dogBreedParentBreedBreedName( dogBreed );
        id = dogBreed.getId();
        breedName = dogBreed.getBreedName();

        List<String> subBreeds = getSubBreedNames(dogBreed.getSubBreeds());
        Set<String> characteristics = getCharacteristicNames(dogBreed.getCharacteristics());

        DogBreedDTO dogBreedDTO = new DogBreedDTO( id, breedName, parentBreed, subBreeds, characteristics );

        return dogBreedDTO;
    }

    @Override
    public DogBreed dtoToDogBreed(DogBreedDTO dto) {
        if ( dto == null ) {
            return null;
        }

        DogBreed.DogBreedBuilder dogBreed = DogBreed.builder();

        dogBreed.id( dto.id() );
        dogBreed.breedName( dto.breedName() );

        return dogBreed.build();
    }

    @Override
    public List<DogBreedDTO> dogBreedListToDtoList(List<DogBreed> dogBreeds) {
        if ( dogBreeds == null ) {
            return null;
        }

        List<DogBreedDTO> list = new ArrayList<DogBreedDTO>( dogBreeds.size() );
        for ( DogBreed dogBreed : dogBreeds ) {
            list.add( dogBreedToDto( dogBreed ) );
        }

        return list;
    }

    @Override
    public List<DogBreed> dtoListToDogBreedList(List<DogBreedDTO> dogBreedDTOS) {
        if ( dogBreedDTOS == null ) {
            return null;
        }

        List<DogBreed> list = new ArrayList<DogBreed>( dogBreedDTOS.size() );
        for ( DogBreedDTO dogBreedDTO : dogBreedDTOS ) {
            list.add( dtoToDogBreed( dogBreedDTO ) );
        }

        return list;
    }

    private String dogBreedParentBreedBreedName(DogBreed dogBreed) {
        if ( dogBreed == null ) {
            return null;
        }
        DogBreed parentBreed = dogBreed.getParentBreed();
        if ( parentBreed == null ) {
            return null;
        }
        String breedName = parentBreed.getBreedName();
        if ( breedName == null ) {
            return null;
        }
        return breedName;
    }
}
