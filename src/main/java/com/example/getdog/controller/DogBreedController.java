package com.example.getdog.controller;


import com.example.getdog.model.DogBreed;
import com.example.getdog.model.SubBreed;
import com.example.getdog.service.DogBreedService;
import com.example.getdog.service.SubBreedService;
import com.example.getdog.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class DogBreedController extends DogApiBaseController {

    final DogBreedService dogBreedService;
    final SubBreedService subBreedService;

    public DogBreedController(DogBreedService dogBreedService, SubBreedService subBreedService) {
        this.dogBreedService = dogBreedService;
        this.subBreedService = subBreedService;
    }

    @GetMapping("/api/breeds/list/all")
    public ResponseEntity<Map<String, Object>> getAllDogs() {
        List<DogBreed> breeds = dogBreedService.findAllBreeds();
        List<String> breedNames = new ArrayList<>();
        for (DogBreed breed : breeds) {
            breedNames.add(breed.getBreedName());
        }
        String result = String.join(", ", breedNames);
        return ApiResponse.buildResponse(HttpStatus.OK, result);
    }

    @GetMapping("/api/breed/{breedName}")
    public ResponseEntity<Map<String, Object>> getDogByName(@PathVariable String breedName) {
        DogBreed dogBreed = dogBreedService.findBreedByName(breedName);
        return ApiResponse.buildResponse(HttpStatus.OK, dogBreed);
    }


    @GetMapping("/api/breed/{breedName}/sub_breeds/list")
    public ResponseEntity<Map<String, Object>> getSubBreedsOfBreed(@PathVariable String breedName) {
        List<SubBreed> subBreedList = subBreedService.getSubBreedsOfBreed(breedName);
        return ApiResponse.buildResponse(HttpStatus.OK, subBreedList);
    }

    @PostMapping("/api/breed/add")
    public ResponseEntity<Map<String, Object>> createNewBreed(@RequestParam String breedName) {
        DogBreed createdBreed = dogBreedService.createDogBreed(breedName);
        return ApiResponse.buildResponse(HttpStatus.OK, createdBreed);
    }

    @DeleteMapping("/api/breed/delete")
    public ResponseEntity<Map<String, Object>> deleteBreedByName(@RequestParam String breedName) {
        dogBreedService.deleteBreedByName(breedName);
        return ApiResponse.buildResponse(HttpStatus.OK, "deleted " + breedName + " successfully");
    }

    @PutMapping("/api/breed/update")
    public ResponseEntity<Map<String, Object>> updateBreed(@RequestParam String oldName, @RequestParam String newName) {
        dogBreedService.updateBreedName(oldName, newName);
        return ApiResponse.buildResponse(HttpStatus.OK, "Breed " + oldName + " updated successfully");
    }
}