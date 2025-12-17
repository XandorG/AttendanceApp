package com.example.application.repository;

import com.example.application.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentId(Long studentId);
    List<Attendance> findByLessonId(Long lessonId);
    List<Attendance> findByStudentIdIn(Collection<Long> student_ids);
}
