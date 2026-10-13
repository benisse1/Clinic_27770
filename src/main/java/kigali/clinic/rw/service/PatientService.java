package kigali.clinic.rw.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Patient;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    public String savePatient(Patient patient) {
        patientRepo.save(patient);
        return "saved successfully";
    }

    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    public Optional<Patient> getPatientById(UUID id) {
        return patientRepo.findById(id);
    }

    public Optional<Patient> getBySsNumber(String ssNumber) {
        return patientRepo.findBySsNumber(ssNumber);
    }

    public String updatePatient(UUID id, Patient newData) {
        Optional<Patient> existing = patientRepo.findById(id);
        if (existing.isEmpty()) {
            return "Patient not found";
        }
        Patient p = existing.get();
        p.setSsNumber(newData.getSsNumber());
        p.setFirstName(newData.getFirstName());
        p.setLastName(newData.getLastName());
        p.setDateOfBirth(newData.getDateOfBirth());
        patientRepo.save(p);
        return "updated successfully";
    }

    public String deletePatient(UUID id) {
        if (!patientRepo.existsById(id)) {
            return "Patient not found";
        }
        patientRepo.deleteById(id);
        return "deleted successfully";
    }

    public List<Patient> getByLastName(String lastName) {
        return patientRepo.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public List<Patient> getFrequent(long min) {
        return patientRepo.findFrequent(min);
    }

    public List<Appointment> getUpcoming(String ssNumber) {
        return appointmentRepo.findByPatientSsNumberAndAppointmentDateGreaterThanEqualAndStatusInOrderByAppointmentDateAsc(
                ssNumber,
                LocalDate.now(),
                Arrays.asList(AppointmentStatus.SCHEDULED, AppointmentStatus.CONFIRMED));
    }
}