package com.example.dogapi.util;

import com.example.dogapi.model.DogBreed;

public class DataUtils {

    public static DogBreed getPoodlePersisted() {
        return DogBreed.builder()
                .id(1L)
                .breedName("poodle")
                .build();
    }

    public static DogBreed getPoodleTransient() {
        return DogBreed.builder()
                .breedName("poodle")
                .build();
    }

    public static DogBreed getLabradorTransient() {
        return DogBreed.builder()
                .breedName("labrador")
                .build();
    }

    public static DogBreed getBulldogTransient() {
        return DogBreed.builder()
                .breedName("bulldog")
                .build();
    }
}
