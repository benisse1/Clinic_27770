package kigali.clinic.rw.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kigali.clinic.rw.domain.Appointment;
import kigali.clinic.rw.domain.AppointmentStatus;
import kigali.clinic.rw.domain.Doctor;
import kigali.clinic.rw.domain.Patient;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    List<Appointment> findByStatusOrderByAppointmentDateAsc(AppointmentStatus status);

    List<Appointment> findByAppointmentDateBetweenOrderByAppointmentDateAsc(LocalDate start, LocalDate end);

    boolean existsByDoctorAndAppointmentDateAndStatusNot(Doctor doctor, LocalDate date, AppointmentStatus status);

    @Query("SELECT DISTINCT a.patient FROM Appointment a WHERE a.doctor.id = :doctorId")
    List<Patient> patientsOf(@Param("doctorId") UUID doctorId);

    @Query("SELECT a.status, COUNT(a) FROM Appointment a GROUP BY a.status")
    List<Object[]> statusCount();

    @Query("SELECT o.name, o.officeNumber, COUNT(a) FROM Appointment a JOIN a.doctor d JOIN d.office o GROUP BY o.name, o.officeNumber ORDER BY COUNT(a) DESC")
    List<Object[]> busiestOffice();

    @Modifying
    @Query("UPDATE Appointment a SET a.status = :newStatus WHERE a.doctor.id = :doctorId AND a.appointmentDate = :date AND a.status <> :excluded")
    int cancelDay(@Param("doctorId") UUID doctorId, @Param("date") LocalDate date, @Param("newStatus") AppointmentStatus newStatus, @Param("excluded") AppointmentStatus excluded);

    @Modifying
    @Query("DELETE FROM Appointment a WHERE a.status = :status AND a.appointmentDate < :date")
    int deleteCancelledBefore(@Param("status") AppointmentStatus status, @Param("date") LocalDate date);
}