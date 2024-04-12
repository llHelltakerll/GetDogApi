package com.example.dogapi.controller;

import com.example.dogapi.dto.DogBreedDTO;
import com.example.dogapi.mappers.DogBreedMapper;
import com.example.dogapi.model.DogBreed;
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

    final DogBreedService dogBreedService;
    final DogBreedMapper dogBreedMapper;

    @GetMapping("/{breedName}/{subBreedName}")
    public ResponseEntity<DogBreedDTO> findByBreed(@PathVariable String breedName,
                                                   @PathVariable String subBreedName) {
        DogBreed subBreed = dogBreedService.findBreedWithSubBreed(subBreedName, breedName);
        return ResponseEntity.ok(dogBreedMapper.dogBreedToDto(subBreed));
    }

    @PostMapping
    public ResponseEntity<DogBreedDTO> create(@Valid @RequestBody DogBreedDTO subBreed,
                                              @RequestParam String breedName) {
        dogBreedService.createBreedSubBreed(breedName, dogBreedMapper.dtoToDogBreed(subBreed));
        return ResponseEntity.status(HttpStatus.CREATED).body(subBreed);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam String subBreedName) {
        dogBreedService.deleteSubBreedByName(subBreedName);
        return ResponseEntity.ok("Sub-breed " + subBreedName + " deleted successfully");
    }

    @PutMapping
    public ResponseEntity<DogBreedDTO> update(@RequestParam String oldName,
                                              @RequestParam String newName) {
        DogBreed subBreed = dogBreedService.updateSubBreedName(oldName, newName);
        return ResponseEntity.status(HttpStatus.CREATED).body(dogBreedMapper.dogBreedToDto(subBreed));
    }

}
