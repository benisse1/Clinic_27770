package kigali.clinic.rw.controller;

import java.time.LocalDate;
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
        String msg = doctorService.saveDoctor(doctor);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Doctor>> all() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/by-specialization")
    public ResponseEntity<List<Doctor>> bySpecialization(@RequestParam String name) {
        return ResponseEntity.ok(doctorService.getBySpecialization(name));
    }

    @GetMapping("/without-office")
    public ResponseEntity<List<Doctor>> withoutOffice() {
        return ResponseEntity.ok(doctorService.getWithoutOffice());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Doctor>> search(@RequestParam String name) {
        return ResponseEntity.ok(doctorService.searchByName(name));
    }

    @GetMapping("/workload")
    public ResponseEntity<List<Object[]>> workload(@RequestParam String start, @RequestParam String end) {
        return ResponseEntity.ok(doctorService.getWorkload(LocalDate.parse(start), LocalDate.parse(end)));
    }

    @GetMapping("/{doctorId}/agenda")
    public ResponseEntity<?> agenda(@PathVariable UUID doctorId, @RequestParam String date) {
        LocalDate d = LocalDate.parse(date);
        Object result = doctorService.getAgenda(doctorId, d);
        if (result instanceof String) {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{doctorId}/specializations/{specializationId}")
    public ResponseEntity<?> addSpec(@PathVariable UUID doctorId, @PathVariable UUID specializationId) {
        String msg = doctorService.addSpecialization(doctorId, specializationId);
        if (msg.equals("The doctor with that id does not exist") || msg.equals("That specialization does not exist")) {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
        if (msg.equals("The doctor already has that specialization")) {
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("/{doctorId}/specializations/{specializationId}")
    public ResponseEntity<?> removeSpec(@PathVariable UUID doctorId, @PathVariable UUID specializationId) {
        String msg = doctorService.removeSpecialization(doctorId, specializationId);
        if (msg.equals("The doctor with that id does not exist") || msg.equals("That specialization does not exist")) {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
        if (msg.equals("The doctor does not have that specialization")) {
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(msg);
    }

    @PatchMapping("/{doctorId}/office")
    public ResponseEntity<?> changeOffice(@PathVariable UUID doctorId, @RequestParam int officeNumber) {
        String msg = doctorService.moveToOffice(doctorId, officeNumber);
        if (msg.equals("The doctor with that id does not exist") || msg.equals("The office with that office number does not exist")) {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
        if (msg.equals("That office is taken by other doctor")) {
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(msg);
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
}