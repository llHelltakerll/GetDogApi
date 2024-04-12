package com.example.dogapi.controller;

import com.example.dogapi.dto.DogBreedDTO;
import com.example.dogapi.mappers.DogBreedMapper;
import com.example.dogapi.model.DogBreed;
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

    final DogBreedService dogBreedService;

    final DogBreedMapper dogBreedMapper;

    @GetMapping
    public ResponseEntity<List<DogBreedDTO>> findAll() {
        List<DogBreed> breeds = dogBreedService.findAllBreeds();
        return ResponseEntity.ok(dogBreedMapper.dogBreedListToDtoList(breeds));
    }

    @GetMapping("/{breedName}")
    public ResponseEntity<DogBreedDTO> findByName(@PathVariable String breedName) {
        DogBreed dogBreed = dogBreedService.findBreedByName(breedName);
        return ResponseEntity.ok(dogBreedMapper.dogBreedToDto(dogBreed));
    }

    @PostMapping
    public ResponseEntity<List<DogBreedDTO>> create(@RequestBody List<DogBreedDTO> dogBreedDTO) {
        dogBreedService.createDogBreed(dogBreedMapper.dtoListToDogBreedList(dogBreedDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(dogBreedDTO);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam String breedName) {
        dogBreedService.deleteBreedByName(breedName);
        return ResponseEntity.ok("Breed deleted " + breedName + " successfully");
    }

    @PutMapping
    public ResponseEntity<DogBreedDTO> update(@RequestParam String oldName,
                                              @RequestParam String newName) {
        DogBreed dogBreed = dogBreedService.updateBreedName(oldName, newName);
        return ResponseEntity.ok(dogBreedMapper.dogBreedToDto(dogBreed));
    }

    @GetMapping("/error")
    public void error() {
        dogBreedService.internalErrorTest();
    }
}
