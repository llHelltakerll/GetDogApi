package com.example.dogapi.mappers;

import com.example.dogapi.dto.CharacteristicsDTO;
import com.example.dogapi.model.Characteristics;
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
public class CharacteristicMapperImpl implements CharacteristicMapper {

    @Override
    public CharacteristicsDTO charToDto(Characteristics characteristics) {
        if ( characteristics == null ) {
            return null;
        }

        int id = 0;
        String characteristicName = null;

        id = characteristics.getId();
        characteristicName = characteristics.getCharacteristicName();

        Set<String> breeds = getBreedNames(characteristics.getBreeds());

        CharacteristicsDTO characteristicsDTO = new CharacteristicsDTO( id, characteristicName, breeds );

        return characteristicsDTO;
    }

    @Override
    public Characteristics dtoToChar(CharacteristicsDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Characteristics.CharacteristicsBuilder characteristics = Characteristics.builder();

        characteristics.id( dto.id() );
        characteristics.characteristicName( dto.characteristicName() );

        return characteristics.build();
    }

    @Override
    public List<CharacteristicsDTO> listCharToListDto(List<Characteristics> characteristics) {
        if ( characteristics == null ) {
            return null;
        }

        List<CharacteristicsDTO> list = new ArrayList<CharacteristicsDTO>( characteristics.size() );
        for ( Characteristics characteristics1 : characteristics ) {
            list.add( charToDto( characteristics1 ) );
        }

        return list;
    }

    @Override
    public List<Characteristics> listDtoToCharList(List<CharacteristicsDTO> characteristics) {
        if ( characteristics == null ) {
            return null;
        }

        List<Characteristics> list = new ArrayList<Characteristics>( characteristics.size() );
        for ( CharacteristicsDTO characteristicsDTO : characteristics ) {
            list.add( dtoToChar( characteristicsDTO ) );
        }

        return list;
    }
}
