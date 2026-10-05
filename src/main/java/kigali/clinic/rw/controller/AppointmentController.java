package kigali.clinic.rw.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        String message = appointmentService.saveAppointment(appointment);
        if (message.equals("Doctor is already booked on that date")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Appointment>> all() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> byId(@PathVariable UUID id) {
        Optional<Appointment> a = appointmentService.getAppointmentById(id);
        if (a.isEmpty()) {
            return new ResponseEntity<>("Appointment not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(a.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(appointmentService.deleteAppointment(id));
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<Appointment>> byStatus(@RequestParam AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.getByStatus(status));
    }

    @GetMapping("/between")
    public ResponseEntity<List<Appointment>> between(@RequestParam String start,
                                                     @RequestParam String end) {
        LocalDate s = LocalDate.parse(start);
        LocalDate e = LocalDate.parse(end);
        return ResponseEntity.ok(appointmentService.getBetween(s, e));
    }

    @GetMapping("/status/by-status")
    public ResponseEntity<List<Object[]>> statusCount() {
        return ResponseEntity.ok(appointmentService.getStatusCount());
    }

    @PatchMapping("/cancel-day")
    public ResponseEntity<?> cancelDay(@RequestParam UUID doctorId,
                                       @RequestParam String date) {
        LocalDate d = LocalDate.parse(date);
        return ResponseEntity.ok(appointmentService.cancelDay(doctorId, d));
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Appointment>> page(@RequestParam int page,
                                                   @RequestParam int size,
                                                   @RequestParam String sort) {
        String[] parts = sort.split(",");
        Sort.Direction dir = "desc".equalsIgnoreCase(parts[1]) ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageable = PageRequest.of(page, size, Sort.by(dir, parts[0]));
        return ResponseEntity.ok(appointmentService.getPage(pageable));
    }

    @DeleteMapping("/cancelled-before")
    public ResponseEntity<?> deleteCancelledBefore(@RequestParam String date) {
        LocalDate d = LocalDate.parse(date);
        return ResponseEntity.ok(appointmentService.deleteCancelledBefore(d));
    }
}