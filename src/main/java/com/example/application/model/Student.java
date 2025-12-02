package com.example.application.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "students")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String personalIdNumber;

    @Column(nullable = false)
    private String classId;

    @Override
    public boolean equals(Object o) {
        if (o instanceof Student student) {
            return Objects.equals(id, student.getId());
        }
        return false;
    }
}
