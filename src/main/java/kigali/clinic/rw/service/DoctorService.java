package kigali.clinic.rw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.AppointmentRepository;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.OfficeRepository;
import kigali.clinic.rw.repository.SpecializationRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private OfficeRepository officeRepo;

    @Autowired
    private SpecializationRepository specializationRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    public String saveDoctor(Doctor doctor) {
        doctorRepo.save(doctor);
        return "saved successfully";
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepo.findAll();
    }

    public Optional<Doctor> getDoctorById(UUID id) {
        return doctorRepo.findById(id);
    }

    public String updateDoctor(UUID id, Doctor newData) {
        Optional<Doctor> existing = doctorRepo.findById(id);
        if (existing.isEmpty()) {
            return "Doctor not found";
        }
        Doctor d = existing.get();
        d.setFirstName(newData.getFirstName());
        d.setLastName(newData.getLastName());
        d.setDateOfBirth(newData.getDateOfBirth());
        d.setOffice(newData.getOffice());
        doctorRepo.save(d);
        return "updated successfully";
    }

    public String deleteDoctor(UUID id) {
        if (!doctorRepo.existsById(id)) {
            return "Doctor not found";
        }
        doctorRepo.deleteById(id);
        return "deleted successfully";
    }

    public List<Doctor> getBySpecialization(String name) {
        return doctorRepo.bySpecialization(name);
    }

    public List<Doctor> getWithoutOffice() {
        return doctorRepo.noOffice();
    }

    public List<Doctor> searchByName(String name) {
        return doctorRepo.searchByName(name);
    }

    // A1
    public String addSpecialization(UUID doctorId, UUID specializationId) {
        Optional<Doctor> doctorOpt = doctorRepo.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return "The doctor with that id does not exist";
        }
        Optional<Specialization> specOpt = specializationRepo.findById(specializationId);
        if (specOpt.isEmpty()) {
            return "That specialization does not exist";
        }
        Doctor doctor = doctorOpt.get();
        Specialization spec = specOpt.get();
        if (doctor.getSpecializations().contains(spec)) {
            return "The doctor already has that specialization";
        }
        doctor.getSpecializations().add(spec);
        doctorRepo.save(doctor);
        return "Specialization added to the doctor";
    }

    // A2
    public String removeSpecialization(UUID doctorId, UUID specializationId) {
        Optional<Doctor> doctorOpt = doctorRepo.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return "The doctor with that id does not exist";
        }
        Optional<Specialization> specOpt = specializationRepo.findById(specializationId);
        if (specOpt.isEmpty()) {
            return "That specialization does not exist";
        }
        Doctor doctor = doctorOpt.get();
        Specialization spec = specOpt.get();
        if (!doctor.getSpecializations().contains(spec)) {
            return "The doctor does not have that specialization";
        }
        doctor.getSpecializations().remove(spec);
        doctorRepo.save(doctor);
        return "Specialization removed from the doctor";
    }

    // A4
    public String moveToOffice(UUID doctorId, int officeNumber) {
        Optional<Doctor> doctorOpt = doctorRepo.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return "The doctor with that id does not exist";
        }
        Optional<Office> officeOpt = officeRepo.findByOfficeNumber(officeNumber);
        if (officeOpt.isEmpty()) {
            return "The office with that office number does not exist";
        }
        Doctor doctor = doctorOpt.get();
        Office newOffice = officeOpt.get();
        if (doctorRepo.existsByOfficeAndIdNot(newOffice, doctorId)) {
            return "That office is taken by other doctor";
        }
        doctor.setOffice(newOffice);
        doctorRepo.save(doctor);
        return "Office changed successfully";
    }

    // D1
    public Object getAgenda(UUID doctorId, LocalDate date) {
        if (!doctorRepo.existsById(doctorId)) {
            return "The doctor with that id does not exist";
        }
        return appointmentRepo.findByDoctorIdAndAppointmentDateAndStatusNot(
                doctorId, date, AppointmentStatus.CANCELLED);
    }

    // Bonus
    public List<Object[]> getWorkload(LocalDate start, LocalDate end) {
        return doctorRepo.workload(AppointmentStatus.COMPLETED, start, end);
    }
}