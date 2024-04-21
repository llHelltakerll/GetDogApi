package com.example.dogapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DogImageDTO(
        @JsonIgnore
        Long id,

        @NotBlank
        String imageUrl,

        @NotBlank
        @JsonIgnore
        String breed
) implements Serializable {
}
