package com.example.dogapi.controller;

import com.example.dogapi.dto.DogImageDTO;
import com.example.dogapi.service.DogImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/images")
@AllArgsConstructor
public class DogImageController {

    final DogImageService dogImageService;

    @GetMapping("/{breed}/random")
    public ResponseEntity<Map<String, String>> getImageByBreed(@PathVariable String breed) {
        String imageUrl = dogImageService.findRandomImageByBreed(breed);
        Map<String, String> response = new HashMap<>();
        response.put("message", imageUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{breed}/{subBreed}/random")
    public ResponseEntity<Map<String, String>> getImageByBreedAndSubBreed(@PathVariable String breed,
                                                                          @PathVariable String subBreed) {
        String imageUrl = dogImageService.findRandomImageBySubBreed(breed, subBreed);
        Map<String, String> response = new HashMap<>();
        response.put("message", imageUrl);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<DogImageDTO> create(@RequestParam String breedName,
                                              @RequestParam(required = false)
                                              String subBreedName,
                                              @RequestParam String imageUrl) {
        DogImageDTO createdImageUrl = dogImageService.create(breedName, subBreedName, imageUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdImageUrl);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam String imageUrl) {
        dogImageService.delete(imageUrl);
        return ResponseEntity.ok("Image URL " + imageUrl + " deleted successfully.");
    }

    @PutMapping
    public ResponseEntity<DogImageDTO> update(@RequestParam String oldName,
                                              @RequestParam String newName) {
        DogImageDTO dogImage = dogImageService.update(oldName, newName);
        return ResponseEntity.status(HttpStatus.CREATED).body(dogImage);
    }
}
