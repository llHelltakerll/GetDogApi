package com.example.dogapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Set;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DogBreedDTO(
        @JsonIgnore
        Long id,

        @NotBlank
        String breedName,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String parentBreed,
        List<String> subBreeds,
        Set<String> characteristics
) {
}