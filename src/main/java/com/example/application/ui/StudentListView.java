package com.example.application.ui;

import com.example.application.base.ui.component.ViewToolbar;
import com.example.application.model.Student;
import com.example.application.service.StudentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import static com.vaadin.flow.spring.data.VaadinSpringDataHelpers.toSpringPageRequest;

@Route("elevlista")
@PageTitle("Elever")
@Menu(title = "Elevlista")
public class StudentListView extends Div {
    private final StudentService studentService;

    private final TextField name;
    private final TextField personalIdNumber;
    private final TextField classId;
    private final Button addButton;
    private final Grid<Student> studentGrid;
//    private final Button deleteButton;

    public StudentListView(StudentService studentService) {
        this.studentService = studentService;

        name = new TextField();
        name.setPlaceholder("Fullständigt namn");

        personalIdNumber = new TextField();
        personalIdNumber.setPlaceholder("ÅÅMMDD-XXXX");

        classId = new TextField();
        classId.setPlaceholder("Klass");

        addButton = new Button("Add", event -> addStudent());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        studentGrid = new Grid<>(Student.class);
        studentGrid.setItems(query -> studentService.findAllStudents(toSpringPageRequest(query)).stream());



        add(new ViewToolbar("Elevlista"), ViewToolbar.group(name, personalIdNumber, classId, addButton));
        add(studentGrid);
    }

    private void addStudent() {
        //TODO move creation of student to service
        //TODO add check for valid id number (not necessary)
        Student student = Student.builder()
                .name(name.getValue())
                .personalIdNumber(personalIdNumber.getValue())
                .classId(classId.getValue())
                .build();
        studentService.add(student);
        studentGrid.getDataProvider().refreshAll();
        name.clear();
        personalIdNumber.clear();
        classId.clear();
    }
}
