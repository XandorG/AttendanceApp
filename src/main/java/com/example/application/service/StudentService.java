package com.example.application.service;

import com.example.application.model.Student;
import com.example.application.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public void add(Student student) {
        studentRepository.save(student);
    }

    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    public List<Student> findAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable).toList();
    }

    public List<Student> findByClassID(String classID) {
        return studentRepository.findByClassId(classID);
    }

    public Student findById(long id) {
        //TODO add proper handling of optional
        return studentRepository.findById(id).get();
    }

    public void delete(long id) {
        studentRepository.deleteById(id);
    }

    public void deleteAll(Set<Student> students) {
        studentRepository.deleteAll(students);
    }

    public void update(Student student) {
        studentRepository.save(student);
    }
}
