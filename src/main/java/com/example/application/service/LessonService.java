package com.example.application.service;

import com.example.application.model.Lesson;
import com.example.application.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;

    public void add(Long teacherId, String classId, String subject, Integer duration, LocalDateTime startTime) {
        Lesson lesson = Lesson.builder()
                .teacherId(teacherId)
                .classId(classId)
                .subject(subject)
                .duration(duration)
                .startTime(startTime)
                .build();
        lessonRepository.save(lesson);
    }

    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    public List<Lesson> findByTeacherId(Long teacherId) {
        return lessonRepository.findByTeacherId(teacherId);
    }

    public List<Lesson> findBySubject(String subject) {
        return lessonRepository.findBySubject(subject);
    }

    public List<Lesson> findByClassId(String classId) {
        return lessonRepository.findByClassId(classId);
    }

    public Lesson findByLessonId(Long lessonId) {
        return lessonRepository.findById(lessonId).orElse(null);
    }

    public void update(Lesson lesson) {
        lessonRepository.save(lesson);
    }

    @Transactional
    public void delete(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElse(null);
        if (lesson != null && lesson.getStartTime().isBefore(LocalDateTime.now())) {
            lessonRepository.deleteById(lessonId);
        }
    }
}
