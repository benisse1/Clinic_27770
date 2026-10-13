package kigali.clinic.rw.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.service.AppointmentService;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Appointment appointment) {
        String msg = appointmentService.saveAppointment(appointment);
        if (msg.equals("Appointment date cannot be in the past")) {
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Appointment>> all() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<Appointment>> byStatus(@RequestParam AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.getByStatus(status));
    }

    @GetMapping("/between")
    public ResponseEntity<List<Appointment>> between(@RequestParam String start, @RequestParam String end) {
        return ResponseEntity.ok(appointmentService.getBetween(LocalDate.parse(start), LocalDate.parse(end)));
    }

    @GetMapping("/status/by-status")
    public ResponseEntity<List<Object[]>> statusCount() {
        return ResponseEntity.ok(appointmentService.getStatusCount());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeStatus(@PathVariable UUID id, @RequestParam AppointmentStatus status) {
        String msg = appointmentService.changeStatus(id, status);
        if (msg.equals("The appointment with that id does not exist")) {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
        if (msg.startsWith("Cannot change status")) {
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(msg);
    }

    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<?> reschedule(@PathVariable UUID id, @RequestParam String newDate) {
        String msg = appointmentService.reschedule(id, LocalDate.parse(newDate));
        if (msg.equals("The appointment with that id does not exist")) {
            return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
        }
        if (msg.equals("Doctor is already booked on that date")) {
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        }
        if (msg.startsWith("A ") || msg.equals("Appointment date cannot be in the past")) {
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String msg = appointmentService.deleteAppointment(id);
        if (msg.equals("That appointment has prescriptions, delete them first")) {
            return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> byId(@PathVariable UUID id) {
        Optional<Appointment> a = appointmentService.getAppointmentById(id);
        if (a.isEmpty()) {
            return new ResponseEntity<>("Appointment not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(a.get());
    }
}