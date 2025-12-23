package com.example.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticsDTO {
    int absentTimeSum;
    int lessonTimeSum;
    double percentage;

    public void addAbsentTime(int minutes) {
        absentTimeSum += minutes;
    }

    public void addLessonTime(int minutes) {
        lessonTimeSum += minutes;
    }

    public double getPercentage() {
        return percentage;
    }
}
