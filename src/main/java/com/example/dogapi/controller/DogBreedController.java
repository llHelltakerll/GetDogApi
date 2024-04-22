package com.example.dogapi.controller;

import com.example.dogapi.dto.DogBreedDTO;
import com.example.dogapi.service.DogBreedService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/breeds")
public class DogBreedController {

    final DogBreedService dogBreedServiceImpl;

    @GetMapping
    public ResponseEntity<List<DogBreedDTO>> findAll() {
        List<DogBreedDTO> breeds = dogBreedServiceImpl.findAllBreeds();
        return ResponseEntity.ok(breeds);
    }

    @GetMapping("/{breedName}")
    public ResponseEntity<DogBreedDTO> findByName(@PathVariable String breedName) {
        DogBreedDTO dogBreed = dogBreedServiceImpl.findBreedDTOByName(breedName);
        return ResponseEntity.ok(dogBreed);
    }

    @PostMapping
    public ResponseEntity<DogBreedDTO> create(@RequestBody DogBreedDTO dogBreedDTO) {
        dogBreedServiceImpl.saveDogBreed(dogBreedDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dogBreedDTO);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam String breedName) {
        dogBreedServiceImpl.deleteBreedByName(breedName);
        return ResponseEntity.ok("Breed deleted " + breedName + " successfully");
    }

    @PutMapping
    public ResponseEntity<DogBreedDTO> update(@RequestParam String oldName,
                                              @RequestParam String newName) {
        DogBreedDTO dogBreed = dogBreedServiceImpl.updateBreedName(oldName, newName);
        return ResponseEntity.ok(dogBreed);
    }

}
