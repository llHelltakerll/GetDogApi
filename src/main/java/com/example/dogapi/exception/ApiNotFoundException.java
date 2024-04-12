package com.example.dogapi.exception;

public class ApiNotFoundException extends RuntimeException {

    public ApiNotFoundException(String objectType, String name) {
        super(formatMessage(objectType, name));
    }

    private static String formatMessage(String objectType, String name) {
        return String.format("%s %s not found", objectType, name);
    }

    public static ApiNotFoundException subBreed(String subBreed) {
        return new ApiNotFoundException("sub-breed", subBreed);
    }

    public static ApiNotFoundException breed(String breedName) {
        return new ApiNotFoundException("breed", breedName);
    }

    public static ApiNotFoundException subBreedInBreed(String breedName, String subBreedName) {
        return new ApiNotFoundException("sub-breed", subBreedName + " in breed " + breedName);
    }

    public static ApiNotFoundException imageUrl(String imageUrl) {
        return new ApiNotFoundException("image URL", imageUrl);
    }

    public static ApiNotFoundException characteristic(String characteristic) {
        return new ApiNotFoundException("characteristic ", characteristic);
    }

}