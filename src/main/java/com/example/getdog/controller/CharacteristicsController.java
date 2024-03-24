package com.example.getdog.controller;

import com.example.getdog.model.Characteristics;
import com.example.getdog.model.DogBreed;
import com.example.getdog.service.CharacteristicsService;
import com.example.getdog.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class CharacteristicsController extends DogApiBaseController {

    private final CharacteristicsService characteristicsService;

    public CharacteristicsController(CharacteristicsService characteristicsService) {
        this.characteristicsService = characteristicsService;
    }

    @GetMapping("/api/breed/characteristics/{breedName}")
    public ResponseEntity<Map<String, Object>> getCharacteristicsByBreed(@PathVariable String breedName) {
        Set<Characteristics> characteristicsSet = characteristicsService.getCharacteristicsByBreed(breedName);
        return ApiResponse.buildResponse(HttpStatus.OK, characteristicsSet);
    }

    @GetMapping("/api/sub_breed/characteristics/{breedName}/{subBreedName}")
    public ResponseEntity<Map<String, Object>> getCharacteristicsBySubBreed(@PathVariable String breedName, @PathVariable String subBreedName) {
        Set<Characteristics> characteristicsSet = characteristicsService.getCharacteristicsForBreedAndSubBreed(breedName, subBreedName);
        return ApiResponse.buildResponse(HttpStatus.OK, characteristicsSet);
    }

    @GetMapping("/api/breeds/characteristics/{characteristic}/list")
    public ResponseEntity<Map<String, Object>> getBreedsByCharacteristic(@PathVariable String characteristic) {
        List<DogBreed> dogBreedList = characteristicsService.findBreedsByCharacteristic(characteristic);
        return ApiResponse.buildResponse(HttpStatus.OK, dogBreedList);
    }

    @PostMapping("/api/characteristic/add")
    public ResponseEntity<Map<String, Object>> createNewCharacteristic(@RequestParam String characteristic) {
        Characteristics characteristics = characteristicsService.addCharacteristic(characteristic);
        return ApiResponse.buildResponse(HttpStatus.OK, characteristics);
    }

    @DeleteMapping("/api/characteristic/delete")
    public ResponseEntity<Map<String, Object>> deleteCharacteristic(@RequestParam String characteristic) {
        characteristicsService.deleteCharacteristicByName(characteristic);
        return ApiResponse.buildResponse(HttpStatus.OK, "characteristic " + characteristic + " deleted successfully.");
    }

    @PutMapping("/api/characteristic/update")
    public ResponseEntity<Map<String, Object>> updateCharacteristic(@RequestParam String oldName, @RequestParam String newName) {
        characteristicsService.updateByCharacteristic(oldName, newName);
        return ApiResponse.buildResponse(HttpStatus.OK, "characteristic " + oldName + " updated successfully.");
    }

}
