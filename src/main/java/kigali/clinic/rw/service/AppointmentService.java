package kigali.clinic.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.PrescriptionRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PrescriptionRepository prescriptionRepo;

    // B1
    public String saveAppointment(Appointment appointment) {
        if (appointment.getAppointmentDate() == null) {
            return "Appointment date cannot be in the past";
        }
        if (appointment.getAppointmentDate().isBefore(LocalDate.now())) {
            return "Appointment date cannot be in the past";
        }
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepo.save(appointment);
        return "saved successfully";
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }

    public Optional<Appointment> getAppointmentById(UUID id) {
        return appointmentRepo.findById(id);
    }

    // C4 modification: block deleting an appointment that has prescriptions
    public String deleteAppointment(UUID id) {
        if (!appointmentRepo.existsById(id)) {
            return "Appointment not found";
        }
        if (prescriptionRepo.existsByAppointmentId(id)) {
            return "That appointment has prescriptions, delete them first";
        }
        appointmentRepo.deleteById(id);
        return "deleted successfully";
    }

    public List<Appointment> getByStatus(AppointmentStatus status) {
        return appointmentRepo.findByStatusOrderByAppointmentDateAsc(status);
    }

    public List<Appointment> getBetween(LocalDate start, LocalDate end) {
        return appointmentRepo.findByAppointmentDateBetweenOrderByAppointmentDateAsc(start, end);
    }

    public List<Object[]> getStatusCount() {
        return appointmentRepo.statusCount();
    }

    public List<Object[]> getBusiestOffice() {
        return appointmentRepo.busiestOffice();
    }

    // B2
    public String changeStatus(UUID id, AppointmentStatus newStatus) {
        Optional<Appointment> apptOpt = appointmentRepo.findById(id);
        if (apptOpt.isEmpty()) {
            return "The appointment with that id does not exist";
        }
        Appointment appointment = apptOpt.get();
        AppointmentStatus current = appointment.getStatus();

        boolean allowed = false;
        if (current == AppointmentStatus.SCHEDULED &&
                (newStatus == AppointmentStatus.CONFIRMED || newStatus == AppointmentStatus.CANCELLED)) {
            allowed = true;
        } else if (current == AppointmentStatus.CONFIRMED &&
                (newStatus == AppointmentStatus.COMPLETED || newStatus == AppointmentStatus.CANCELLED)) {
            allowed = true;
        }

        if (!allowed) {
            return "Cannot change status from " + current + " to " + newStatus;
        }

        appointment.setStatus(newStatus);
        appointmentRepo.save(appointment);
        return "Appointment status updated successfully";
    }

    // B3
    public String reschedule(UUID id, LocalDate newDate) {
        Optional<Appointment> apptOpt = appointmentRepo.findById(id);
        if (apptOpt.isEmpty()) {
            return "The appointment with that id does not exist";
        }
        Appointment appointment = apptOpt.get();

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            return "A COMPLETED appointment cannot be rescheduled";
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            return "A CANCELLED appointment cannot be rescheduled";
        }
        if (newDate == null || newDate.isBefore(LocalDate.now())) {
            return "Appointment date cannot be in the past";
        }
        if (appointment.getDoctor() != null &&
                appointmentRepo.existsByDoctorAndAppointmentDateAndStatusNot(
                        appointment.getDoctor(), newDate, AppointmentStatus.CANCELLED)) {
            return "Doctor is already booked on that date";
        }z

        appointment.setAppointmentDate(newDate);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepo.save(appointment);
        return "Appointment rescheduled successfully";
    }

    // Bonus
    public List<Appointment> getCancelledBefore(LocalDate date) {
        return appointmentRepo.findByStatusOrderByAppointmentDateAsc(AppointmentStatus.CANCELLED)
                .stream()
                .filter(a -> a.getAppointmentDate().isBefore(date))
                .toList();
    }
}