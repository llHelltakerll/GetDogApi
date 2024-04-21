package com.example.dogapi.controller;

import com.example.dogapi.dto.CharacteristicsDTO;
import com.example.dogapi.service.CharacteristicsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/chars")
@AllArgsConstructor
public class CharacteristicsController {

    final CharacteristicsService characteristicsService;

    @GetMapping
    public ResponseEntity<List<String>> findByAllChars() {
        List<CharacteristicsDTO> characteristics = characteristicsService.findAll();
        return ResponseEntity.ok(characteristics.stream()
                .map(CharacteristicsDTO::characteristicName)
                .toList());
    }

    @GetMapping("/{characteristicName}/breeds")
    public ResponseEntity<CharacteristicsDTO> findByName(@PathVariable String characteristicName) {
        CharacteristicsDTO characteristics = characteristicsService.findCharByCharNameDTO(characteristicName);
        return ResponseEntity.ok(characteristics);
    }

    @PostMapping
    public ResponseEntity<List<CharacteristicsDTO>> create(@RequestBody List<CharacteristicsDTO> newCharacteristics) {
        characteristicsService.createChars(newCharacteristics);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCharacteristics);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam String characteristic) {
        characteristicsService.delete(characteristic);
        return ResponseEntity.ok("characteristic " + characteristic + " deleted successfully.");
    }

    @PutMapping
    public ResponseEntity<CharacteristicsDTO> updateCharacteristic(@RequestParam String oldName,
                                                                   @RequestParam String newName) {
        CharacteristicsDTO characteristics = characteristicsService.update(oldName, newName);
        return ResponseEntity.status(HttpStatus.CREATED).body(characteristics);
    }
}
