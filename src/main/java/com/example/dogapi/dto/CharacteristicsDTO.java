package com.example.dogapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.Set;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public
record CharacteristicsDTO(
        @JsonIgnore
        int id,

        @NotBlank
        String characteristicName,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        Set<String> breeds
) implements Serializable {
}

