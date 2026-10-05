package kigali.clinic.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.service.AppointmentService;
import kigali.clinic.rw.service.OfficeService;

@RestController
@RequestMapping("/api/offices")
public class OfficeController {

    @Autowired
    private OfficeService officeService;

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Office office) {
        return new ResponseEntity<>(officeService.saveOffice(office), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Office>> all() {
        return ResponseEntity.ok(officeService.getAllOffices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> byId(@PathVariable UUID id) {
        Optional<Office> o = officeService.getOfficeById(id);
        if (o.isEmpty()) {
            return new ResponseEntity<>("Office not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(o.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Office o) {
        return ResponseEntity.ok(officeService.updateOffice(id, o));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(officeService.deleteOffice(id));
    }

    @GetMapping("/busiest")
    public ResponseEntity<?> busiest() {
        List<Object[]> rows = appointmentService.getBusiestOffice();
        if (rows.isEmpty()) {
            return ResponseEntity.ok("No appointments yet");
        }
        return ResponseEntity.ok(rows.get(0));
    }
}