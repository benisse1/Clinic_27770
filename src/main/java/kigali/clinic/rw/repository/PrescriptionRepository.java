package kigali.clinic.rw.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.clinic.rw.domain.Prescription;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {

    List<Prescription> findByAppointmentId(UUID appointmentId);

    boolean existsByAppointmentId(UUID appointmentId);

    @Query("SELECT p FROM Prescription p WHERE p.appointment.patient.ssNumber = :ss ORDER BY p.issuedDate DESC")
    List<Prescription> findByPatientSsNumber(@Param("ss") String ssNumber);

    @Query("SELECT p.medicineName, COUNT(p) FROM Prescription p GROUP BY p.medicineName ORDER BY COUNT(p) DESC")
    List<Object[]> topMedicines();
}