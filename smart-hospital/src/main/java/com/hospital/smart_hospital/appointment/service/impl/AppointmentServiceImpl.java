package com.hospital.smart_hospital.appointment.service.impl;

import com.hospital.smart_hospital.appointment.dto.request.CreateAppointmentRequest;
import com.hospital.smart_hospital.appointment.dto.request.PatientBookAppointmentRequest;
import com.hospital.smart_hospital.appointment.dto.request.UpdateAppointmentRequest;
import com.hospital.smart_hospital.appointment.dto.response.AppointmentResponse;
import com.hospital.smart_hospital.appointment.entity.Appointment;
import com.hospital.smart_hospital.appointment.mapper.AppointmentMapper;
import com.hospital.smart_hospital.appointment.repository.AppointmentRepository;
import com.hospital.smart_hospital.appointment.service.AppointmentService;
import com.hospital.smart_hospital.appointment_slot.entity.AppointmentSlot;
import com.hospital.smart_hospital.appointment_slot.repository.AppointmentSlotRepository;
import com.hospital.smart_hospital.common.enums.AppointmentStatus;
import com.hospital.smart_hospital.common.enums.BookingSource;
import com.hospital.smart_hospital.common.enums.PaymentStatus;
import com.hospital.smart_hospital.common.enums.SlotStatus;
import com.hospital.smart_hospital.doctor.entity.Doctor;
import com.hospital.smart_hospital.doctor.repository.DoctorRepository;
import com.hospital.smart_hospital.exception.BadRequestException;
import com.hospital.smart_hospital.exception.ResourceNotFoundException;
import com.hospital.smart_hospital.patient.entity.Patient;
import com.hospital.smart_hospital.patient.repository.PatientRepository;
import com.hospital.smart_hospital.security.service.CurrentUserService;
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
    private final CurrentUserService currentUserService;

    // =========================================================
    // Existing / Administrative APIs
    // =========================================================

    @Override
    public AppointmentResponse createAppointment(
            CreateAppointmentRequest request) {

        Patient patient = getPatient(request.patientId());

        Doctor doctor = getDoctor(request.doctorId());

        /*
         * Lock the slot while booking.
         * This prevents two requests from booking
         * the same slot simultaneously.
         */
        AppointmentSlot slot =
                getAvailableSlotForBooking(
                        request.appointmentSlotId()
                );

        validateDoctor(slot, doctor);

        Appointment appointment = mapper.toEntity(request);

        appointment.setAppointmentCode(
                generateAppointmentCode()
        );

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentSlot(slot);

        appointment.setAppointmentDate(
                slot.getSlotDate()
        );

        appointment.setAppointmentTime(
                slot.getStartTime()
        );

        appointment.setStatus(
                AppointmentStatus.BOOKED
        );

        appointment.setPaymentStatus(
                PaymentStatus.PENDING
        );

        appointment.setActive(true);

        slot.setStatus(
                SlotStatus.BOOKED
        );

        appointmentRepository.save(appointment);

        return mapper.toResponse(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(Long id) {

        return mapper.toResponse(
                getAppointment(id)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentResponse> getAppointments(
            String keyword,
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
    public AppointmentResponse updateAppointment(
            Long id,
            UpdateAppointmentRequest request) {

        Appointment appointment = getAppointment(id);

        mapper.updateAppointment(
                request,
                appointment
        );

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
    // Patient Self-Service APIs
    // =========================================================

    @Override
    public AppointmentResponse bookAppointmentAsPatient(
            PatientBookAppointmentRequest request) {

        /*
         * Patient comes from JWT.
         * The patient does NOT send patientId.
         */
        Patient patient =
                currentUserService.getCurrentPatient();

        Doctor doctor =
                getDoctor(request.doctorId());

        /*
         * IMPORTANT:
         *
         * This method obtains the appointment slot using
         * PESSIMISTIC_WRITE locking.
         */
        AppointmentSlot slot =
                getAvailableSlotForBooking(
                        request.appointmentSlotId()
                );

        validateDoctor(slot, doctor);

        Appointment appointment = new Appointment();

        appointment.setAppointmentCode(
                generateAppointmentCode()
        );

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentSlot(slot);

        appointment.setAppointmentDate(
                slot.getSlotDate()
        );

        appointment.setAppointmentTime(
                slot.getStartTime()
        );

        appointment.setVisitType(
                request.visitType()
        );

        appointment.setBookingSource(
                BookingSource.PATIENT
        );

        appointment.setStatus(
                AppointmentStatus.BOOKED
        );

        appointment.setPaymentStatus(
                PaymentStatus.PENDING
        );

        appointment.setReasonForVisit(
                request.reasonForVisit()
        );

        appointment.setActive(true);

        /*
         * Because the slot is locked inside the transaction,
         * only one transaction can change it at a time.
         */
        slot.setStatus(
                SlotStatus.BOOKED
        );

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        return mapper.toResponse(savedAppointment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentResponse> getMyAppointments(
            Pageable pageable) {

        Patient patient =
                currentUserService.getCurrentPatient();

        return appointmentRepository
                .findByPatientIdAndActiveTrue(
                        patient.getId(),
                        pageable
                )
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getMyAppointmentById(
            Long id) {

        Patient patient =
                currentUserService.getCurrentPatient();

        Appointment appointment =
                appointmentRepository
                        .findByIdAndActiveTrue(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment not found."
                                ));

        validateAppointmentOwnership(
                appointment,
                patient
        );

        return mapper.toResponse(appointment);
    }

    @Override
    public void cancelMyAppointment(Long id) {

        Patient patient =
                currentUserService.getCurrentPatient();

        Appointment appointment =
                appointmentRepository
                        .findByIdAndActiveTrue(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment not found."
                                ));

        validateAppointmentOwnership(
                appointment,
                patient
        );

        validateCancellation(appointment);

        appointment.setStatus(
                AppointmentStatus.CANCELLED
        );

        appointment.setActive(false);

        AppointmentSlot slot =
                appointment.getAppointmentSlot();

        slot.setStatus(
                SlotStatus.AVAILABLE
        );

        appointmentRepository.save(appointment);
    }

    // =========================================================
    // Helper Methods
    // =========================================================

    private Appointment getAppointment(Long id) {

        return appointmentRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found."
                        ));
    }

    private Patient getPatient(Long id) {

        return patientRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found."
                        ));
    }

    private Doctor getDoctor(Long id) {

        return doctorRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found."
                        ));
    }

    /*
     * Normal slot lookup.
     *
     * Use this when you don't need a database lock.
     */
    private AppointmentSlot getAvailableSlot(Long id) {

        AppointmentSlot slot =
                slotRepository
                        .findByIdAndActiveTrue(id)
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

    /*
     * Booking-specific slot lookup.
     *
     * AppointmentSlotRepository uses:
     *
     * @Lock(LockModeType.PESSIMISTIC_WRITE)
     *
     * This locks the database row until the current
     * transaction completes.
     */
    private AppointmentSlot getAvailableSlotForBooking(
            Long id) {

        AppointmentSlot slot =
                slotRepository
                        .findByIdAndActiveTrueForUpdate(id)
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

    private void validateDoctor(
            AppointmentSlot slot,
            Doctor doctor) {

        if (!slot.getDoctor()
                .getId()
                .equals(doctor.getId())) {

            throw new BadRequestException(
                    "Selected slot does not belong to the doctor."
            );
        }
    }

    private void validateAppointmentOwnership(
            Appointment appointment,
            Patient patient) {

        if (!appointment.getPatient()
                .getId()
                .equals(patient.getId())) {

            throw new BadRequestException(
                    "You are not allowed to access this appointment."
            );
        }
    }

    private void validateCancellation(
            Appointment appointment) {

        if (appointment.getStatus()
                == AppointmentStatus.COMPLETED) {

            throw new BadRequestException(
                    "Completed appointment cannot be cancelled."
            );
        }

        if (appointment.getStatus()
                == AppointmentStatus.CANCELLED) {

            throw new BadRequestException(
                    "Appointment is already cancelled."
            );
        }

        if (appointment.getStatus()
                == AppointmentStatus.NO_SHOW) {

            throw new BadRequestException(
                    "No-show appointment cannot be cancelled."
            );
        }

        if (appointment.getStatus()
                == AppointmentStatus.EXPIRED) {

            throw new BadRequestException(
                    "Expired appointment cannot be cancelled."
            );
        }
    }

    private String generateAppointmentCode() {

        long count =
                appointmentRepository.count() + 1;

        return String.format(
                "APT-%06d",
                count
        );
    }
}