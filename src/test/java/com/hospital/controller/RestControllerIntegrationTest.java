package com.hospital.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class RestControllerIntegrationTest {
 @Autowired MockMvc mvc;
 @Test void smokeTestAllRestModulesAndBusinessRules() throws Exception {
  String department=mvc.perform(post("/api/v1/departments").contentType(MediaType.APPLICATION_JSON).content("{\"departmentName\":\"Integration Cardiology\",\"description\":\"test\",\"departmentContact\":\"test\"}"))
   .andExpect(status().isCreated()).andExpect(jsonPath("$.departmentName").value("Integration Cardiology")).andReturn().getResponse().getContentAsString();
  int departmentId=new com.fasterxml.jackson.databind.ObjectMapper().readTree(department).get("departmentId").asInt();
  String doctor=mvc.perform(post("/api/v1/doctors").contentType(MediaType.APPLICATION_JSON).content("{\"doctorName\":\"Integration Doctor\",\"specialization\":\"Cardiology\",\"departmentId\":"+departmentId+",\"email\":\"integration.doctor@example.test\",\"availabilityStatus\":\"Available\"}"))
   .andExpect(status().isCreated()).andExpect(jsonPath("$.doctorName").value("Integration Doctor")).andReturn().getResponse().getContentAsString();
  int doctorId=new com.fasterxml.jackson.databind.ObjectMapper().readTree(doctor).get("doctorId").asInt();
  String patient=mvc.perform(post("/api/v1/patients").contentType(MediaType.APPLICATION_JSON).content("{\"patientName\":\"Integration Patient\",\"age\":\"30\",\"gender\":\"Other\",\"admissionDate\":\"2026-11-21\",\"ailment\":\"Testing\",\"assignedDoctor\":\"Integration Doctor\"}"))
   .andExpect(status().isCreated()).andExpect(jsonPath("$.patientName").value("Integration Patient")).andReturn().getResponse().getContentAsString();
  int patientId=new com.fasterxml.jackson.databind.ObjectMapper().readTree(patient).get("patientId").asInt();
  mvc.perform(get("/api/v1/patients/{id}",patientId)).andExpect(status().isOk()).andExpect(jsonPath("$.patientId").value(patientId));
  mvc.perform(get("/api/v1/patients")).andExpect(status().isOk());
  mvc.perform(get("/api/v1/patients/reports/admissions").param("start","2026-01-01").param("end","2026-12-31")).andExpect(status().isOk());
  mvc.perform(get("/api/v1/patients/reports/doctor").param("doctor","Integration Doctor")).andExpect(status().isOk());
  mvc.perform(get("/api/v1/patients/reports/doctor-count")).andExpect(status().isOk());
  mvc.perform(get("/api/v1/patients/reports/ailment").param("ailment","Testing")).andExpect(status().isOk()).andExpect(jsonPath("$[0].patientName").value("Integration Patient"));
  mvc.perform(get("/api/v1/doctors")).andExpect(status().isOk());
  mvc.perform(get("/api/v1/doctors/{id}",doctorId)).andExpect(status().isOk());
  mvc.perform(get("/api/v1/doctors/specialization").param("name","Cardiology")).andExpect(status().isOk()).andExpect(jsonPath("$[0].doctorName").value("Integration Doctor"));
  mvc.perform(get("/api/v1/doctors/department/{id}",departmentId)).andExpect(status().isOk());
  mvc.perform(get("/api/v1/departments")).andExpect(status().isOk());
  mvc.perform(get("/api/v1/departments/{id}",departmentId)).andExpect(status().isOk());
  String record="{\"patientId\":" + patientId + ",\"doctorId\":" + doctorId + ",\"diagnosis\":\"test\",\"treatment\":\"test\",\"medicalNotes\":\"test note\",\"recordDate\":\"2026-11-21\"}";
  String recordResult=mvc.perform(post("/api/v1/medical-records").contentType(MediaType.APPLICATION_JSON).content(record)).andExpect(status().isCreated()).andExpect(jsonPath("$.patientId").value(patientId)).andReturn().getResponse().getContentAsString();
  int recordId=new com.fasterxml.jackson.databind.ObjectMapper().readTree(recordResult).get("recordId").asInt();
  mvc.perform(get("/api/v1/medical-records")).andExpect(status().isOk());
  mvc.perform(get("/api/v1/medical-records/{id}",recordId)).andExpect(status().isOk());
  mvc.perform(get("/api/v1/medical-records/patient/{id}",patientId)).andExpect(status().isOk());
  mvc.perform(get("/api/v1/medical-records/doctor/{id}",doctorId)).andExpect(status().isOk());
  String appointment="{\"patientId\":" + patientId + ",\"doctorId\":" + doctorId + ",\"appointmentDate\":\"2026-12-10\",\"appointmentTime\":\"10:30:00\",\"reason\":\"integration test\",\"status\":\"SCHEDULED\"}";
  String appointmentResult=mvc.perform(post("/api/v1/appointments").contentType(MediaType.APPLICATION_JSON).content(appointment)).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
  int appointmentId=new com.fasterxml.jackson.databind.ObjectMapper().readTree(appointmentResult).get("appointmentId").asInt();
  mvc.perform(get("/api/v1/appointments")).andExpect(status().isOk());
  mvc.perform(get("/api/v1/appointments/{id}",appointmentId)).andExpect(status().isOk());
  mvc.perform(post("/api/v1/appointments").contentType(MediaType.APPLICATION_JSON).content(appointment)).andExpect(status().isBadRequest());
  mvc.perform(get("/api/v1/appointments/doctor/{id}",doctorId)).andExpect(status().isOk());
  mvc.perform(get("/api/v1/appointments/patient/{id}",patientId)).andExpect(status().isOk());
  mvc.perform(get("/api/v1/appointments/date").param("date","2026-12-10")).andExpect(status().isOk());
  String room=mvc.perform(post("/api/v1/rooms").contentType(MediaType.APPLICATION_JSON).content("{\"roomNumber\":\"IT-101\",\"roomType\":\"General Ward\",\"totalBeds\":2,\"availableBeds\":2,\"roomStatus\":\"AVAILABLE\"}"))
   .andExpect(status().isCreated()).andExpect(jsonPath("$.availableBeds").value(2)).andReturn().getResponse().getContentAsString();
  int roomId=new com.fasterxml.jackson.databind.ObjectMapper().readTree(room).get("roomId").asInt();
  mvc.perform(get("/api/v1/rooms")).andExpect(status().isOk());
  mvc.perform(get("/api/v1/rooms/{id}",roomId)).andExpect(status().isOk());
  mvc.perform(get("/api/v1/rooms/available")).andExpect(status().isOk());
  String allocation="{\"patientId\":" + patientId + ",\"roomId\":" + roomId + ",\"bedNumber\":1,\"admissionDate\":\"2026-11-21\",\"releaseDate\":null}";
  String allocationResult=mvc.perform(post("/api/v1/room-allocations").contentType(MediaType.APPLICATION_JSON).content(allocation)).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
  int allocationId=new com.fasterxml.jackson.databind.ObjectMapper().readTree(allocationResult).get("allocationId").asInt();
  mvc.perform(get("/api/v1/room-allocations")).andExpect(status().isOk());
  mvc.perform(get("/api/v1/room-allocations/{id}",allocationId)).andExpect(status().isOk());
  mvc.perform(post("/api/v1/room-allocations").contentType(MediaType.APPLICATION_JSON).content(allocation)).andExpect(status().isBadRequest());
  mvc.perform(put("/api/v1/room-allocations/{id}",allocationId).contentType(MediaType.APPLICATION_JSON).content("{\"patientId\":" + patientId + ",\"roomId\":" + roomId + ",\"bedNumber\":1,\"admissionDate\":\"2026-11-21\",\"releaseDate\":\"2026-11-22\"}"))
   .andExpect(status().isOk()).andExpect(jsonPath("$.releaseDate").value("2026-11-22"));
  mvc.perform(get("/api/v1/reports/dashboard")).andExpect(status().isOk()).andExpect(jsonPath("$.totalPatients").isNumber());
  mvc.perform(get("/api/v1/reports/doctor-statistics")).andExpect(status().isOk());
  mvc.perform(get("/api/v1/reports/department-statistics")).andExpect(status().isOk());
 }
}
