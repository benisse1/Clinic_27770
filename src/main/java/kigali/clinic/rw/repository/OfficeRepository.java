package kigali.clinic.rw.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kigali.clinic.rw.domain.Office;

@Repository
public interface OfficeRepository extends JpaRepository<Office, UUID> {

    Optional<Office> findByOfficeNumber(int officeNumber);

    @Query("SELECT o FROM Office o WHERE NOT EXISTS (SELECT d FROM Doctor d WHERE d.office = o) ORDER BY o.officeNumber")
    List<Office> findFreeOffices();
}