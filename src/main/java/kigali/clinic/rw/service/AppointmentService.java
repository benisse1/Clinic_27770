package kigali.clinic.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.repository.AppointmentRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepo;

    public String saveAppointment(Appointment appointment) {
        if (appointment.getDoctor() != null && appointment.getAppointmentDate() != null) {
            boolean booked = appointmentRepo.existsByDoctorAndAppointmentDateAndStatusNot(
                    appointment.getDoctor(),
                    appointment.getAppointmentDate(),
                    AppointmentStatus.CANCELLED);

            if (booked) {
                return "Doctor is already booked on that date";
            }
        }
        appointmentRepo.save(appointment);
        return "saved successfully";
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }

    public Optional<Appointment> getAppointmentById(UUID id) {
        return appointmentRepo.findById(id);
    }

    public String deleteAppointment(UUID id) {
        if (!appointmentRepo.existsById(id)) {
            return "Appointment not found";
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

    public List<Patient> getPatientsOf(UUID doctorId) {
        return appointmentRepo.patientsOf(doctorId);
    }

    public List<Object[]> getStatusCount() {
        return appointmentRepo.statusCount();
    }

    public List<Object[]> getBusiestOffice() {
        return appointmentRepo.busiestOffice();
    }

    @Transactional
    public String cancelDay(UUID doctorId, LocalDate date) {
        int n = appointmentRepo.cancelDay(doctorId, date, AppointmentStatus.CANCELLED, AppointmentStatus.COMPLETED);
        return n + " appointments cancelled";
    }

    public Page<Appointment> getPage(Pageable pageable) {
        return appointmentRepo.findAll(pageable);
    }

    @Transactional
    public String deleteCancelledBefore(LocalDate date) {
        int n = appointmentRepo.deleteCancelledBefore(AppointmentStatus.CANCELLED, date);
        return n + " appointments deleted";
    }
}