package com.example.getdog.controller;

import com.example.getdog.model.DogImage;
import com.example.getdog.service.DogImageService;
import com.example.getdog.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class DogImageController extends DogApiBaseController {

    final DogImageService dogImageService;

    public DogImageController(DogImageService dogImageService) {
        this.dogImageService = dogImageService;
    }

    @GetMapping("/api/image/{breed}/{subBreed}/random")
    public ResponseEntity<Map<String, Object>> getImageByBreed(@PathVariable String breed, @PathVariable String subBreed) {
        String imageUrl = dogImageService.findRandomImageBySubBreed(breed, subBreed);
        return ApiResponse.buildResponse(HttpStatus.OK, imageUrl);
    }

    @GetMapping("/api/image/{breed}/random")
    public ResponseEntity<Map<String, Object>> getImageByBreed(@PathVariable String breed) {
        String imageUrl = dogImageService.findRandomImageByBreed(breed);
        return ApiResponse.buildResponse(HttpStatus.OK, imageUrl);
    }

    @PostMapping("/api/image/add")
    public ResponseEntity<Map<String, Object>> createNewImageUrl(@RequestParam String breedName,
                                                                 @RequestParam(required = false) String subBreedName,
                                                                 @RequestParam String imageUrl) {
        DogImage createdImageUrl = dogImageService.createImageUrl(breedName, subBreedName, imageUrl);
        return ApiResponse.buildResponse(HttpStatus.OK, createdImageUrl);
    }

    @DeleteMapping("/api/image/delete")
    public ResponseEntity<Map<String, Object>> deleteImageByImageUrl(@RequestParam String imageUrl) {
        dogImageService.deleteImageByImageUrl(imageUrl);
        return ApiResponse.buildResponse(HttpStatus.OK, "Image URL " + imageUrl + " deleted successfully.");
    }

    @PutMapping("/api/image/update")
    public ResponseEntity<Map<String, Object>> updateImageUrl(@RequestParam String oldName, @RequestParam String newName) {
        dogImageService.updateImageUrl(oldName, newName);
        return ApiResponse.buildResponse(HttpStatus.OK, "Image URL " + oldName + " updated successfully.");
    }
}
