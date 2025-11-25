package com.example.application.ui;

import com.example.application.base.ui.component.ViewToolbar;
import com.example.application.model.Attendance;
import com.example.application.model.Lesson;
import com.example.application.model.Student;
import com.example.application.service.AttendanceService;
import com.example.application.service.LessonService;
import com.example.application.service.StudentService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Route("närvaro")
@PageTitle("Närvaro")
@Menu(title = "Närvarohantering")
public class AttendanceView extends Div {
    private final AttendanceService attendanceService;
    private final StudentService studentService;
    private final LessonService lessonService;

    Text text;
    Grid<Lesson> lessonGrid;
//    Grid<Student> studentGrid;
    Grid<Attendance> studentGrid;
    Button nextButton;
    Button presentButton;
    Button absentButton;

    public AttendanceView(AttendanceService attendanceService, StudentService studentService, LessonService lessonService) {
        this.attendanceService = attendanceService;
        this.studentService = studentService;
        this.lessonService = lessonService;

        lessonGrid = new Grid<>(Lesson.class, false);
        lessonGrid.setItems(lessonService.findAll());
        lessonGrid.addColumn(Lesson::getSubject)
                .setHeader("Ämne").setSortable(true);
        lessonGrid.addColumn(Lesson::getClassId)
                .setHeader("Klass").setSortable(true);
        lessonGrid.addColumn(Lesson::getDuration)
                .setHeader("Längd (min)").setSortable(true);
        lessonGrid.addColumn(Lesson::getStartTime)
                .setHeader("Datum och tid").setSortable(true);

        nextButton = new Button("Nästa", event -> switchToStudentGrid());
        nextButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

//        studentGrid = new Grid<>(Student.class, false);
//        studentGrid.setSelectionMode(Grid.SelectionMode.MULTI);
//        studentGrid.addColumn(Student::getName)
//                .setHeader("Namn").setSortable(true);
//        studentGrid.addColumn(Student)

//        attendanceService.

        studentGrid = new Grid<>(Attendance.class, false);
        studentGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        studentGrid.addColumn(Attendance::getStudentName)
                .setHeader("Elev").setSortable(true);
        studentGrid.addColumn(new ComponentRenderer<>(
                attendance -> {
                    Checkbox checkbox = new Checkbox();
                    checkbox.setValue(attendance.isPresent());
                    checkbox.addValueChangeListener(event -> {
                        attendance.setPresent(checkbox.getValue());
                        attendanceService.updateAttendance(attendance);
                        studentGrid.getDataProvider().refreshItem(attendance);
                    });
                    return checkbox;
                }
        )).setHeader("Närvarande");
        studentGrid.addColumn(Attendance::isPresent)
                .setHeader("Present").setSortable(true);

        presentButton = new Button("Närvarande");
        presentButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        absentButton = new Button("Frånvarande");
        absentButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(new ViewToolbar("Närvarohantering"));
        text = new Text("Välj lektion");
        add(text);
        add(lessonGrid, nextButton);
    }

    private void switchToStudentGrid() {
        Lesson lesson = lessonGrid.getSelectedItems().iterator().next();
        remove(lessonGrid, nextButton);

//        studentGrid.setItems(studentService.findByClassID(lesson.getClassId()));
        studentGrid.setItems(attendanceService.getAttendanceByLesson(lesson));




        text.setText(lesson.getSubject() + ": " + lesson.getClassId());
        add(studentGrid);
    }
}
