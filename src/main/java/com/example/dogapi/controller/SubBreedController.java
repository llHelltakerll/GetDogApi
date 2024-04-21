package com.example.dogapi.controller;

import com.example.dogapi.dto.DogBreedDTO;
import com.example.dogapi.service.DogBreedService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/sub-breeds")
public class SubBreedController {

    final DogBreedService dogBreedServiceImpl;

    @GetMapping("/{breedName}/{subBreedName}")
    public ResponseEntity<DogBreedDTO> findByBreed(@PathVariable String breedName,
                                                   @PathVariable String subBreedName) {
        DogBreedDTO subBreed = dogBreedServiceImpl.findBreedWithSubBreedDTO(breedName, subBreedName);
        return ResponseEntity.ok(subBreed);
    }

    @PostMapping
    public ResponseEntity<DogBreedDTO> create(@Valid @RequestBody DogBreedDTO subBreed,
                                              @RequestParam String breedName) {
        dogBreedServiceImpl.createBreedSubBreed(breedName, subBreed);
        return ResponseEntity.status(HttpStatus.CREATED).body(subBreed);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam String subBreedName) {
        dogBreedServiceImpl.deleteSubBreedByName(subBreedName);
        return ResponseEntity.ok("Sub-breed " + subBreedName + " deleted successfully");
    }

    @PutMapping
    public ResponseEntity<DogBreedDTO> update(@RequestParam String oldName,
                                              @RequestParam String newName) {
        DogBreedDTO subBreed = dogBreedServiceImpl.updateSubBreedName(oldName, newName);
        return ResponseEntity.status(HttpStatus.CREATED).body(subBreed);
    }

}
