package com.example.getdog.util;

public class ApiIsExistException extends RuntimeException {

    public ApiIsExistException(String objectType, String name) {
        super(formatMessage(objectType, name));
    }

    private static String formatMessage(String objectType, String name) {
        return String.format("%s %s is exist", objectType, name);
    }

    public static ApiIsExistException subBreedWithBreed(String breedName, String subBreed) {
        return new ApiIsExistException("sub-breed", subBreed + " with breed " + breedName);
    }

    public static ApiIsExistException breed(String breedName) {
        return new ApiIsExistException("breed", breedName);
    }

    public static ApiIsExistException imageUrl(String imageUrl) {
        return new ApiIsExistException("image URL", imageUrl);
    }
}