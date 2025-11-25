package com.example.application.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "lessons")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long teacherId;

    @Column(nullable = false)
    private String classId;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<Attendance> attendances;

}
