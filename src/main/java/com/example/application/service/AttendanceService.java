package com.example.application.service;

import com.example.application.model.Attendance;
import com.example.application.model.Lesson;
import com.example.application.model.Student;
import com.example.application.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    Logger logger = LoggerFactory.getLogger(AttendanceService.class);

    public void addAttendance(Student student, Lesson lesson, int minutesAbsent) {
        Attendance attendance = Attendance.builder()
                .student(student)
                .lesson(lesson)
                .minutesAbsent(minutesAbsent)
                .present(false)
                .build();
        attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendanceByStudent(Student student) {
        return attendanceRepository.findByStudentId(student.getId());
    }

    public List<Attendance> getAttendanceByLesson(Lesson lesson) {
        return attendanceRepository.findByLessonId(lesson.getId());
    }

    public void updateAttendance(Attendance attendance) {
        attendanceRepository.save(attendance);
        logger.info("Attendance updated: {}", attendance.isPresent());
    }
}
