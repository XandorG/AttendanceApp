package com.example.application.ui;

import com.example.application.base.ui.component.ViewToolbar;
import com.example.application.dto.StudentDTO;
import com.example.application.model.Student;
import com.example.application.service.StudentService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.InMemoryUploadHandler;
import com.vaadin.flow.server.streams.UploadHandler;

import java.util.List;
import java.util.Set;

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
    private final Button deleteButton;

    public StudentListView(StudentService studentService) {
        this.studentService = studentService;

        name = new TextField();
        name.setPlaceholder("Fullständigt namn");

        personalIdNumber = new TextField();
        personalIdNumber.setPlaceholder("ÅÅMMDD-XXXX");

        classId = new TextField();
        classId.setPlaceholder("Klass");

        addButton = new Button("Lägg till", event -> addStudent());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        //TODO put behind Tabs to make more sense
        Upload upload = getUpload();

        studentGrid = new Grid<>(Student.class, false);
        studentGrid.setItems(studentService.findAllStudents());
        studentGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        studentGrid.addColumn(Student::getName)
                .setHeader("Namn").setSortable(true);
        studentGrid.addColumn(Student::getPersonalIdNumber)
                .setHeader("Personnummer").setSortable(true);
        studentGrid.addColumn(Student::getClassId)
                .setHeader("Klass").setSortable(true);

        deleteButton = new Button("Ta bort", event -> deleteStudent());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        add(new ViewToolbar("Elevlista"), ViewToolbar.group(name, personalIdNumber, classId, addButton, upload));
        add(studentGrid, deleteButton);
    }

    private Upload getUpload() {
        InMemoryUploadHandler inMemoryHandler = UploadHandler.inMemory(
                (metadata, uploadedFile) -> {
                    String fileName = metadata.fileName();
                    String contentType = metadata.contentType();
                    long size = metadata.contentLength();

                    if (contentType.equals("application/json")) {
                        ObjectMapper mapper = new ObjectMapper();
                        List<StudentDTO> studentDTOList = mapper.readValue(uploadedFile, new TypeReference<List<StudentDTO>>() {});
                        studentService.add(studentDTOList);
                        studentGrid.setItems(studentService.findAllStudents());
                    }
                }
        );
        Upload upload = new Upload(inMemoryHandler);
        return upload;
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
        studentGrid.setItems(studentService.findAllStudents());
//        studentGrid.getDataProvider().refreshAll();
        name.clear();
        personalIdNumber.clear();
        classId.clear();
    }

    private void deleteStudent() {
        Set<Student> students = studentGrid.getSelectedItems();
        studentService.deleteAll(students);
//        studentGrid.getDataProvider().refreshAll();
        studentGrid.setItems(studentService.findAllStudents());
    }
}
