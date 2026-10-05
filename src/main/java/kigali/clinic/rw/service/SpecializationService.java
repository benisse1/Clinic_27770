package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.DoctorRepository;
import kigali.clinic.rw.repository.SpecializationRepository;

@Service
public class SpecializationService {

    @Autowired
    private SpecializationRepository specializationRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    public String saveSpecialization(Specialization spec) {
        if (spec.getName() == null || spec.getName().isBlank()) {
            return "Specialization name cannot be empty";
        }
        if (specializationRepo.existsByName(spec.getName())) {
            return "Specialization with this name already exists";
        }
        specializationRepo.save(spec);
        return "saved successfully";
    }

    public List<Specialization> getAllSpecializations() {
        return specializationRepo.findAll();
    }

    public Optional<Specialization> getSpecializationById(UUID id) {
        return specializationRepo.findById(id);
    }

    public String updateSpecialization(UUID id, Specialization newData) {
        Optional<Specialization> existing = specializationRepo.findById(id);
        if (existing.isEmpty()) {
            return "Specialization not found";
        }
        Specialization s = existing.get();
        s.setName(newData.getName());
        specializationRepo.save(s);
        return "updated successfully";
    }

    public String deleteSpecialization(UUID id) {
        if (!specializationRepo.existsById(id)) {
            return "Specialization not found";
        }
        specializationRepo.deleteById(id);
        return "deleted successfully";
    }

    public String addDoctorToSpecialization(UUID specId, UUID doctorId) {
        Optional<Specialization> specOpt = specializationRepo.findById(specId);
        Optional<Doctor> docOpt = doctorRepo.findById(doctorId);
        if (specOpt.isEmpty()) return "Specialization not found";
        if (docOpt.isEmpty()) return "Doctor not found";

        Specialization spec = specOpt.get();
        Doctor doctor = docOpt.get();
        if (spec.getDoctors().contains(doctor)) {
            return "This doctor already has this specialization";
        }
        spec.getDoctors().add(doctor);
        specializationRepo.save(spec);
        return "doctor linked to specialization successfully";
    }

    public String removeDoctorFromSpecialization(UUID specId, UUID doctorId) {
        Optional<Specialization> specOpt = specializationRepo.findById(specId);
        Optional<Doctor> docOpt = doctorRepo.findById(doctorId);
        if (specOpt.isEmpty() || docOpt.isEmpty()) {
            return "Not found";
        }
        Specialization spec = specOpt.get();
        spec.getDoctors().remove(docOpt.get());
        specializationRepo.save(spec);
        return "doctor removed from specialization successfully";
    }

    public List<Specialization> getUnused() {
        return specializationRepo.unused();
    }
}