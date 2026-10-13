package kigali.clinic.rw.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Prescription;
import kigali.clinic.rw.service.PrescriptionService;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam UUID appointmentId, @RequestBody Prescription prescription) {
        String msg = prescriptionService.savePrescription(appointmentId, prescription);
        if (msg.equals("The appointment with that id does not exist")) {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
        if (msg.equals("Prescriptions are only for COMPLETED appointments")) {
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> byAppointment(@PathVariable UUID appointmentId) {
        Object result = prescriptionService.getByAppointment(appointmentId);
        if (result instanceof String) {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/patient/{ssNumber}")
    public ResponseEntity<List<Prescription>> byPatient(@PathVariable String ssNumber) {
        return ResponseEntity.ok(prescriptionService.getByPatient(ssNumber));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Prescription p) {
        String msg = prescriptionService.updatePrescription(id, p);
        if (msg.equals("The prescription with that id does not exist")) {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String msg = prescriptionService.deletePrescription(id);
        if (msg.equals("The prescription with that id does not exist")) {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/stats/top-medicines")
    public ResponseEntity<List<Object[]>> topMedicines() {
        return ResponseEntity.ok(prescriptionService.getTopMedicines());
    }
}