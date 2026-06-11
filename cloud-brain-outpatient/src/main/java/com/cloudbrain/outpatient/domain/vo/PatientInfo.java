package com.cloudbrain.outpatient.domain.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public class PatientInfo {

    private String patientId;
    private String patientName;
    private String gender;
    private int age;

    public PatientInfo() {}

    public PatientInfo(String patientId, String patientName, String gender, int age) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.gender = gender;
        this.age = age;
    }

    public String getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public String getGender() { return gender; }
    public int getAge() { return age; }
}
