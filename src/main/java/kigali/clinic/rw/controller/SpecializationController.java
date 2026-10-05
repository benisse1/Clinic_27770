package kigali.clinic.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.service.SpecializationService;

@RestController
@RequestMapping("/api/specializations")
public class SpecializationController {

    @Autowired
    private SpecializationService specializationService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Specialization s) {
        return new ResponseEntity<>(specializationService.saveSpecialization(s), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Specialization>> all() {
        return ResponseEntity.ok(specializationService.getAllSpecializations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> byId(@PathVariable UUID id) {
        Optional<Specialization> s = specializationService.getSpecializationById(id);
        if (s.isEmpty()) {
            return new ResponseEntity<>("Specialization not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(s.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Specialization s) {
        return ResponseEntity.ok(specializationService.updateSpecialization(id, s));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(specializationService.deleteSpecialization(id));
    }

    @PostMapping("/{specId}/add-doctor/{doctorId}")
    public ResponseEntity<?> addDoctor(@PathVariable UUID specId, @PathVariable UUID doctorId) {
        return ResponseEntity.ok(specializationService.addDoctorToSpecialization(specId, doctorId));
    }

    @DeleteMapping("/{specId}/remove-doctor/{doctorId}")
    public ResponseEntity<?> removeDoctor(@PathVariable UUID specId, @PathVariable UUID doctorId) {
        return ResponseEntity.ok(specializationService.removeDoctorFromSpecialization(specId, doctorId));
    }

    @GetMapping("/unused")
    public ResponseEntity<List<Specialization>> unused() {
        return ResponseEntity.ok(specializationService.getUnused());
    }
}