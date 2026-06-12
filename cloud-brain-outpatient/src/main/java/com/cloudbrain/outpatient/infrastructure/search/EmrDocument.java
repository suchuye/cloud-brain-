package com.cloudbrain.outpatient.infrastructure.search;

public class EmrDocument {

    private String id;
    private String consultationId;
    private String doctorId;
    private String patientId;
    private String chiefComplaint;
    private String presentIllness;
    private String content;

    public EmrDocument() {}

    public EmrDocument(String id, String consultationId, String doctorId, String patientId,
                       String chiefComplaint, String presentIllness, String content) {
        this.id = id;
        this.consultationId = consultationId;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.chiefComplaint = chiefComplaint;
        this.presentIllness = presentIllness;
        this.content = content;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getConsultationId() { return consultationId; }
    public void setConsultationId(String consultationId) { this.consultationId = consultationId; }
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getChiefComplaint() { return chiefComplaint; }
    public void setChiefComplaint(String chiefComplaint) { this.chiefComplaint = chiefComplaint; }
    public String getPresentIllness() { return presentIllness; }
    public void setPresentIllness(String presentIllness) { this.presentIllness = presentIllness; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
