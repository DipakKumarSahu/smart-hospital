package com.hospital.smart_hospital.appointment.service.impl;



import com.hospital.smart_hospital.appointment.dto.request.CreateAppointmentRequest;
import com.hospital.smart_hospital.appointment.dto.request.UpdateAppointmentRequest;
import com.hospital.smart_hospital.appointment.dto.response.AppointmentResponse;
import com.hospital.smart_hospital.appointment.entity.Appointment;

import com.hospital.smart_hospital.appointment.mapper.AppointmentMapper;
import com.hospital.smart_hospital.appointment.repository.AppointmentRepository;

import com.hospital.smart_hospital.appointment.service.AppointmentService;
import com.hospital.smart_hospital.appointment_slot.entity.AppointmentSlot;
import com.hospital.smart_hospital.appointment_slot.repository.AppointmentSlotRepository;
import com.hospital.smart_hospital.common.enums.AppointmentStatus;
import com.hospital.smart_hospital.common.enums.PaymentStatus;
import com.hospital.smart_hospital.common.enums.SlotStatus;
import com.hospital.smart_hospital.doctor.entity.Doctor;
import com.hospital.smart_hospital.doctor.repository.DoctorRepository;
import com.hospital.smart_hospital.exception.BadRequestException;
import com.hospital.smart_hospital.exception.ResourceNotFoundException;
import com.hospital.smart_hospital.patient.entity.Patient;
import com.hospital.smart_hospital.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentSlotRepository slotRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentMapper mapper;

    @Override
    public AppointmentResponse createAppointment(CreateAppointmentRequest request) {

        Patient patient = getPatient(request.patientId());

        Doctor doctor = getDoctor(request.doctorId());

        AppointmentSlot slot = getAvailableSlot(request.appointmentSlotId());

        validateDoctor(slot, doctor);

        Appointment appointment = mapper.toEntity(request);

        appointment.setAppointmentCode(generateAppointmentCode());

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentSlot(slot);

        appointment.setAppointmentDate(slot.getSlotDate());
        appointment.setAppointmentTime(slot.getStartTime());

        appointment.setStatus(AppointmentStatus.BOOKED);
        appointment.setPaymentStatus(PaymentStatus.PENDING);
        appointment.setActive(true);

        slot.setStatus(SlotStatus.BOOKED);

        appointmentRepository.save(appointment);

        return mapper.toResponse(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(Long id) {

        return mapper.toResponse(getAppointment(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentResponse> getAppointments(String keyword,
                                                     Pageable pageable) {

        if (keyword == null || keyword.isBlank()) {

            return appointmentRepository
                    .findByActiveTrue(pageable)
                    .map(mapper::toResponse);
        }

        return appointmentRepository
                .findByPatientUserFullNameContainingIgnoreCaseAndActiveTrue(
                        keyword,
                        pageable
                )
                .map(mapper::toResponse);
    }

    @Override
    public AppointmentResponse updateAppointment(Long id,
                                                 UpdateAppointmentRequest request) {

        Appointment appointment = getAppointment(id);

        mapper.updateAppointment(request, appointment);

        return mapper.toResponse(
                appointmentRepository.save(appointment)
        );
    }

    @Override
    public void deleteAppointment(Long id) {

        Appointment appointment = getAppointment(id);

        appointment.setActive(false);

        appointmentRepository.save(appointment);
    }

    // =========================================================
    // Helper Methods
    // =========================================================

    private Appointment getAppointment(Long id) {

        return appointmentRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found."
                        ));
    }

    private Patient getPatient(Long id) {

        return patientRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found."
                        ));
    }

    private Doctor getDoctor(Long id) {

        return doctorRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found."
                        ));
    }

    private AppointmentSlot getAvailableSlot(Long id) {

        AppointmentSlot slot = slotRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment slot not found."
                        ));

        if (slot.getStatus() != SlotStatus.AVAILABLE) {

            throw new BadRequestException(
                    "Selected slot is not available."
            );
        }

        return slot;
    }

    private void validateDoctor(AppointmentSlot slot,
                                Doctor doctor) {

        if (!slot.getDoctor().getId().equals(doctor.getId())) {

            throw new BadRequestException(
                    "Selected slot does not belong to the doctor."
            );
        }
    }

    private String generateAppointmentCode() {

        long count = appointmentRepository.count() + 1;

        return String.format("APT-%06d", count);
    }
}
