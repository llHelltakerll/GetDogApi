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

@RestController
public class DogBreedController extends DogApiBaseController {

    final DogBreedService dogBreedService;
    final SubBreedService subBreedService;

    public DogBreedController(DogBreedService dogBreedService, SubBreedService subBreedService) {
        this.dogBreedService = dogBreedService;
        this.subBreedService = subBreedService;
    }

    @GetMapping("/api/breeds/list/all")
    public ResponseEntity<?> getAllDogs() {
        List<DogBreed> breeds = dogBreedService.findAllBreeds();
        List<String> breedNames = new ArrayList<>();
        for (DogBreed breed : breeds) {
            breedNames.add(breed.getBreedName());
        }
        String result = String.join(", ", breedNames);
        return ApiResponse.buildResponse(HttpStatus.OK, "success", result);
    }

    @GetMapping("/api/breed/{breedName}")
    public ResponseEntity<?> getDogByName(@PathVariable String breedName) {
        DogBreed dogBreed = dogBreedService.findBreedByName(breedName);
        return ApiResponse.buildResponse(HttpStatus.OK, "success", dogBreed);
    }


    @GetMapping("/api/breed/{breedName}/sub_breeds/list")
    public ResponseEntity<?> getSubBreedsOfBreed(@PathVariable String breedName) {
        List<SubBreed> subBreedList = subBreedService.getSubBreedsOfBreed(breedName);
        return ApiResponse.buildResponse(HttpStatus.OK, "success", subBreedList);
    }

    @PostMapping("/api/breed/add")
    public ResponseEntity<?> createNewBreed(@RequestParam String breedName) {
        DogBreed createdBreed = dogBreedService.createDogBreed(breedName);
        return ApiResponse.buildResponse(HttpStatus.OK, "success", createdBreed);
    }

    @DeleteMapping("/api/breed/delete")
    public ResponseEntity<?> deleteBreedByName(@RequestParam String breedName) {
        dogBreedService.deleteBreedByName(breedName);
        return ApiResponse.buildResponse(HttpStatus.OK, "success", "deleted " + breedName + " successfully");
    }

    @PutMapping("/api/breed/update")
    public ResponseEntity<?> updateBreed(@RequestParam String oldName, @RequestParam String newName) {
        dogBreedService.updateBreedName(oldName, newName);
        return ApiResponse.buildResponse(HttpStatus.OK, "success", "Breed " + oldName + " updated successfully");
    }
}