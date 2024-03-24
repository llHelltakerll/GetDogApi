package com.example.getdog.controller;

import com.example.getdog.model.Characteristics;
import com.example.getdog.model.DogBreed;
import com.example.getdog.service.CharacteristicsService;
import com.example.getdog.service.DogBreedService;
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
    final CharacteristicsService characteristicsService;

    public DogBreedController(DogBreedService dogBreedService, CharacteristicsService characteristicsService) {
        this.dogBreedService = dogBreedService;
        this.characteristicsService = characteristicsService;
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

    @GetMapping("/api/breed/{breedName}/sub_breeds/list")
    public ResponseEntity<Map<String, Object>> getSubBreedByName(@PathVariable String breedName) {
        List<DogBreed> subBreed = dogBreedService.findAllSubBreedsByBreed(breedName);
        return ApiResponse.buildResponse(HttpStatus.OK, subBreed);
    }

    @GetMapping("/api/sub_breed/{breedName}/{subBreedName}")
    public ResponseEntity<Map<String, Object>> getBreedByName(@PathVariable String breedName, @PathVariable String subBreedName) {
        DogBreed subBreed = dogBreedService.findSubBreedNameAndBreedName(subBreedName, breedName);
        return ApiResponse.buildResponse(HttpStatus.OK, subBreed);
    }

    @PostMapping("/api/sub_breed/add")
    public ResponseEntity<Map<String, Object>> createNewSubBreed(@RequestParam String breedName, @RequestParam String subBreedName) {
        DogBreed createdSubBreed = dogBreedService.createBreedSubBreed(breedName, subBreedName);
        return ApiResponse.buildResponse(HttpStatus.OK, createdSubBreed);
    }

    @DeleteMapping("/api/sub_breed/delete")
    public ResponseEntity<Map<String, Object>> deleteSubBreedByName(@RequestParam String subBreedName) {
        dogBreedService.deleteSubBreedByName(subBreedName);
        return ApiResponse.buildResponse(HttpStatus.OK, "Sub-breed " + subBreedName + " deleted successfully");
    }

    @PutMapping("/api/sub_breed/update")
    public ResponseEntity<Map<String, Object>> updateSubBreed(@RequestParam String oldName, @RequestParam String newName) {
        dogBreedService.updateSubBreedName(oldName, newName);
        return ApiResponse.buildResponse(HttpStatus.OK, "Sub-breed " + oldName + " updated successfully");
    }

    @PostMapping("api/breed_characteristic/add")
    public ResponseEntity<Map<String, Object>> addCharacteristicToBreed(@RequestParam String breedName,
                                                                        @RequestParam(required = false) String subBreedName,
                                                                        @RequestParam String characteristicName) {
        Characteristics characteristics = characteristicsService.addCharacteristicToBreed(breedName, subBreedName, characteristicName);
        return ApiResponse.buildResponse(HttpStatus.OK, characteristics);
    }

    @DeleteMapping("api/breed_characteristic/delete")
    public ResponseEntity<Map<String, Object>> deleteCharacteristicToBreed(@RequestParam String breedName,
                                                                           @RequestParam(required = false) String subBreedName,
                                                                           @RequestParam String characteristicName) {
        characteristicsService.deleteCharacteristicWithBreed(breedName, subBreedName, characteristicName);
        return ApiResponse.buildResponse(HttpStatus.OK, "characteristic " + characteristicName
                + " and breed " + breedName + " in breed characteristic deleted successfully.");
    }

    @PutMapping("/api/breed_characteristic/update")
    public ResponseEntity<Map<String, Object>> updateBreedCharacteristic(@RequestParam String breedName, @RequestParam String oldName,
                                                                         @RequestParam(required = false) String subBreedName,
                                                                         @RequestParam String newName) {
        characteristicsService.updateCharacteristicWithBreed(breedName, subBreedName, oldName, newName);
        return ApiResponse.buildResponse(HttpStatus.OK, "characteristic " + oldName
                + " and breed " + breedName + " in breed characteristic updated successfully.");
    }
}