package com.example.application.repository;

import com.example.application.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    public List<Lesson> findByClassId(String classId);
    public List<Lesson> findByTeacherId(Long teacherId);
    public List<Lesson> findBySubject(String subject);
}
