package com.example.application.ui;

import com.example.application.base.ui.component.ViewToolbar;
import com.example.application.dto.StatisticsDAO;
import com.example.application.model.Student;
import com.example.application.service.AttendanceService;
import com.example.application.service.StudentService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route("statistik")
@PageTitle("Närvarostatistik")
@Menu(title = "Närvarostatistik")
public class StatisticsView extends Div {
    private final AttendanceService attendanceService;
    private final StudentService studentService;

    public StatisticsView(AttendanceService attendanceService, StudentService studentService) {
        this.attendanceService = attendanceService;
        this.studentService = studentService;

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.addToPrimary(createSelectionGrid());
        splitLayout.addToSecondary(createStatistics());

        add(new ViewToolbar("Närvarostatistik"), ViewToolbar.group(splitLayout));
    }

    private Grid<Student> createSelectionGrid() {
        Grid<Student> grid = new Grid<>(Student.class, false);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addSelectionListener(event -> updateStatistics(event.getFirstSelectedItem().orElse(null)));
        grid.addColumn(Student::getName)
                .setHeader("Namn").setSortable(true);
        grid.addColumn(Student::getPersonalIdNumber)
                .setHeader("Personnummer").setSortable(true);
        grid.addColumn(Student::getClassId)
                .setHeader("Klass").setSortable(true);
        grid.setItems(studentService.findAllStudents());
        return grid;
    }

    private TextArea textArea;

    private void updateStatistics(Student student) {
        if (student == null) {
            textArea.setValue("Välj en elev");
            return;
        }

        StatisticsDAO statistics = attendanceService.getStatisticsByStudents(List.of(student));

        textArea.setValue(String.format("Procent frånvaro: %.1f%%", statistics.getPercentage()));
    }

    //TODO make better looking
    private Component createStatistics() {
        textArea = new TextArea();
        textArea.setValue("Välj en elev");

        return textArea;
    }
}
