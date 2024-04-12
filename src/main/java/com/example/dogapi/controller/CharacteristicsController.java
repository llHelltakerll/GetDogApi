package com.example.dogapi.controller;

import com.example.dogapi.dto.CharacteristicsDTO;
import com.example.dogapi.mappers.CharacteristicMapper;
import com.example.dogapi.model.Characteristics;
import com.example.dogapi.service.CharacteristicsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/chars")
@AllArgsConstructor
public class CharacteristicsController {

    final CharacteristicsService characteristicsService;
    final CharacteristicMapper characteristicMapper;

    @GetMapping("/{characteristicName}/breeds")
    public ResponseEntity<CharacteristicsDTO> findByName(@PathVariable String characteristicName) {
        Characteristics characteristics = characteristicsService.findCharByCharName(characteristicName);
        return ResponseEntity.ok(characteristicMapper.charToDto(characteristics));
    }

    @PostMapping
    public ResponseEntity<CharacteristicsDTO> create(@Valid @RequestBody CharacteristicsDTO newCharacteristic) {
        characteristicsService.create(characteristicMapper.dtoToChar(newCharacteristic));
        return ResponseEntity.status(HttpStatus.CREATED).body(newCharacteristic);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam String characteristic) {
        characteristicsService.delete(characteristic);
        return ResponseEntity.ok("characteristic " + characteristic + " deleted successfully.");
    }

    @PutMapping
    public ResponseEntity<CharacteristicsDTO> updateCharacteristic(@RequestParam String oldName,
                                                                   @RequestParam String newName) {
        Characteristics characteristics = characteristicsService.update(oldName, newName);
        return ResponseEntity.status(HttpStatus.CREATED).body(characteristicMapper.charToDto(characteristics));
    }
}
