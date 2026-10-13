package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.repository.OfficeRepository;

@Service
public class OfficeService {

    @Autowired
    private OfficeRepository officeRepo;

    public String saveOffice(Office office) {
        Optional<Office> existingOffice = officeRepo.findByOfficeNumber(office.getOfficeNumber());
        if (existingOffice.isPresent()) {
            return "Office with this number already exists";
        }
        officeRepo.save(office);
        return "saved successfully";
    }

    public List<Office> getAllOffices() {
        return officeRepo.findAll();
    }

    public Optional<Office> getOfficeById(UUID id) {
        return officeRepo.findById(id);
    }

    public String updateOffice(UUID id, Office newData) {
        Optional<Office> existing = officeRepo.findById(id);
        if (existing.isEmpty()) {
            return "Office not found";
        }
        Office office = existing.get();
        office.setName(newData.getName());
        office.setOfficeNumber(newData.getOfficeNumber());
        officeRepo.save(office);
        return "updated successfully";
    }

    public String deleteOffice(UUID id) {
        if (!officeRepo.existsById(id)) {
            return "Office not found";
        }
        officeRepo.deleteById(id);
        return "deleted successfully";
    }

    public List<Office> getFreeOffices() {
        return officeRepo.findFreeOffices();
    }
}