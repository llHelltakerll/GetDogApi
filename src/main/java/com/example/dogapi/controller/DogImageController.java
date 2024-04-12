package com.example.dogapi.controller;

import com.example.dogapi.dto.DogImageDTO;
import com.example.dogapi.mappers.DogImageMapper;
import com.example.dogapi.model.DogImage;
import com.example.dogapi.service.DogImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/images")
@AllArgsConstructor
public class DogImageController {

    final DogImageService dogImageService;
    final DogImageMapper dogImageMapper;

    @GetMapping("{breed}/random")
    public ResponseEntity<String> getImageByBreed(@PathVariable String breed) {
        String imageUrl = dogImageService.findRandomImageByBreed(breed);
        return ResponseEntity.ok(imageUrl);
    }

    @GetMapping("{breed}/{subBreed}/random")
    public ResponseEntity<String> getImageByBreedAndSubBreed(@PathVariable String breed,
                                                             @PathVariable String subBreed) {
        String imageUrl = dogImageService.findRandomImageBySubBreed(breed, subBreed);
        return ResponseEntity.ok(imageUrl);
    }

    @PostMapping
    public ResponseEntity<DogImageDTO> create(@RequestParam String breedName,
                                              @RequestParam(required = false)
                                              String subBreedName,
                                              @RequestParam String imageUrl) {
        DogImage createdImageUrl = dogImageService.create(breedName, subBreedName, imageUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(dogImageMapper.imageToDto(createdImageUrl));
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam String imageUrl) {
        dogImageService.delete(imageUrl);
        return ResponseEntity.ok("Image URL " + imageUrl + " deleted successfully.");
    }

    @PutMapping
    public ResponseEntity<DogImageDTO> update(@RequestParam String oldName,
                                              @RequestParam String newName) {
        DogImage dogImage = dogImageService.update(oldName, newName);
        return ResponseEntity.status(HttpStatus.CREATED).body(dogImageMapper.imageToDto(dogImage));
    }
}
