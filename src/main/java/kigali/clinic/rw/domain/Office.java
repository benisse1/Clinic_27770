package kigali.clinic.rw.domain;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "office")
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "office_number")
    private Integer officeNumber;

    @JsonIgnore
    @OneToOne(mappedBy = "office")
    private Doctor doctor;

    public UUID getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getOfficeNumber() { return officeNumber; }
    public void setOfficeNumber(Integer officeNumber) { this.officeNumber = officeNumber; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
}