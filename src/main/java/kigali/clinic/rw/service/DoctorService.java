package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.repository.DoctorRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepo;

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
}