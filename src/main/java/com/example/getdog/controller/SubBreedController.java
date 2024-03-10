package com.example.getdog.controller;


import com.example.getdog.model.SubBreed;
import com.example.getdog.service.SubBreedService;
import com.example.getdog.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class SubBreedController extends DogApiBaseController {

    final SubBreedService subBreedService;

    public SubBreedController(SubBreedService subBreedService) {
        this.subBreedService = subBreedService;
    }

    @GetMapping("/api/sub_breed/list/all")
    public ResponseEntity<Map<String, Object>> getAllSubBreedNames() {
        List<SubBreed> subBreeds = subBreedService.findAllSubBreeds();
        Set<String> subBreedNames = new HashSet<>();
        for (SubBreed breed : subBreeds) {
            subBreedNames.add(breed.getSubBreedName());
        }
        String result = String.join(", ", subBreedNames);
        return ApiResponse.buildResponse(HttpStatus.OK, "success", result);
    }

    @GetMapping("/api/sub_breed/{breedName}/{subBreedName}")
    public ResponseEntity<Map<String, Object>> getBreedByName(@PathVariable String breedName, @PathVariable String subBreedName) {
        SubBreed subBreed = subBreedService.findSubBreedNameAndBreedName(subBreedName, breedName);
        return ApiResponse.buildResponse(HttpStatus.OK, "success", subBreed);
    }

    @GetMapping("/api/sub_breed/{subBreedName}")
    public ResponseEntity<Map<String, Object>> getSubBreedByName(@PathVariable String subBreedName) {
        List<SubBreed> subBreed = subBreedService.findSubBreedByName(subBreedName);
        return ApiResponse.buildResponse(HttpStatus.OK, "success", subBreed);
    }

    @PostMapping("/api/sub_breed/add")
    public ResponseEntity<Map<String, Object>> createNewSubBreed(@RequestParam String breedName, @RequestParam String subBreedName) {
        SubBreed createdSubBreed = subBreedService.createBreedSubBreed(breedName, subBreedName);
        return ApiResponse.buildResponse(HttpStatus.OK, "success", createdSubBreed);
    }

    @DeleteMapping("/api/sub_breed/delete")
    public ResponseEntity<Map<String, Object>> deleteSubBreedByName(@RequestParam String subBreedName) {
        subBreedService.deleteSubBreedByName(subBreedName);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/api/sub_breed/update")
    public ResponseEntity<Map<String, Object>> updateSubBreed(@RequestParam String oldName, @RequestParam String newName) {
        subBreedService.updateSubBreedName(oldName, newName);
        return ApiResponse.buildResponse(HttpStatus.OK, "success", "Sub-breed " + oldName + " updated successfully");
    }

}
