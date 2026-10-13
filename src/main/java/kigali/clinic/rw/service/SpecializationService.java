package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.SpecializationRepository;

@Service
public class SpecializationService {

    @Autowired
    private SpecializationRepository specializationRepo;

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

    public List<Specialization> getUnused() {
        return specializationRepo.unused();
    }
}