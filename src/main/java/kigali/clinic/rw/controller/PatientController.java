package kigali.clinic.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.service.PatientService;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Patient patient) {
        String msg = patientService.savePatient(patient);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Patient>> all() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/by-last-name")
    public ResponseEntity<List<Patient>> byLastName(@RequestParam String lastName) {
        return ResponseEntity.ok(patientService.getByLastName(lastName));
    }

    @GetMapping("/frequent")
    public ResponseEntity<List<Patient>> frequent(@RequestParam long min) {
        return ResponseEntity.ok(patientService.getFrequent(min));
    }

    @GetMapping("/{ssNumber}/upcoming")
    public ResponseEntity<?> upcoming(@PathVariable String ssNumber) {
        if (patientService.getBySsNumber(ssNumber).isEmpty()) {
            return new ResponseEntity<>("That patient with that ssNumber does not exist", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(patientService.getUpcoming(ssNumber));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> byId(@PathVariable UUID id) {
        Optional<Patient> p = patientService.getPatientById(id);
        if (p.isEmpty()) {
            return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(p.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Patient p) {
        return ResponseEntity.ok(patientService.updatePatient(id, p));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(patientService.deletePatient(id));
    }
}