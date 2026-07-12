package com.hospital.smart_hospital.schedule.repository;




import com.hospital.smart_hospital.schedule.enitiy.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DoctorScheduleRepository
        extends JpaRepository<DoctorSchedule, Long> {

    List<DoctorSchedule> findByDoctorId(Long doctorId);

    List<DoctorSchedule> findByDoctorIdAndActiveTrue(Long doctorId);

//    List<DoctorSchedule> findByDoctorIdAndDayOfWeekAndActiveTrue(
//            Long doctorId,
//            DayOfWeek dayOfWeek
//    ); for multiple shift after mvp we can think

    Optional<DoctorSchedule> findByDoctorIdAndDayOfWeekAndActiveTrue(
            Long doctorId,
            DayOfWeek dayOfWeek
    );




}
