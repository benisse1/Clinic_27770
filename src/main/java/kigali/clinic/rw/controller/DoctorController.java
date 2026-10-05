package kigali.clinic.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.service.DoctorService;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Doctor doctor) {
        return new ResponseEntity<>(doctorService.saveDoctor(doctor), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Doctor>> all() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> byId(@PathVariable UUID id) {
        Optional<Doctor> d = doctorService.getDoctorById(id);
        if (d.isEmpty()) {
            return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(d.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Doctor d) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, d));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(doctorService.deleteDoctor(id));
    }

    @GetMapping("/by-specialization")
    public ResponseEntity<List<Doctor>> bySpecialization(@RequestParam String name) {
        return ResponseEntity.ok(doctorService.getBySpecialization(name));
    }

    @GetMapping("/without-office")
    public ResponseEntity<List<Doctor>> withoutOffice() {
        return ResponseEntity.ok(doctorService.getWithoutOffice());
    }
}