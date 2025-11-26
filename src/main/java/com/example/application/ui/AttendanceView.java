package com.example.application.ui;

import com.example.application.base.ui.component.ViewToolbar;
import com.example.application.model.Attendance;
import com.example.application.model.Lesson;
import com.example.application.service.AttendanceService;
import com.example.application.service.LessonService;
import com.example.application.service.StudentService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("närvaro")
@PageTitle("Närvaro")
@Menu(title = "Närvarohantering")
public class AttendanceView extends Div {
    private final AttendanceService attendanceService;
    private final StudentService studentService;
    private final LessonService lessonService;

    Text text;
    Grid<Lesson> lessonGrid;
    Grid<Attendance> attendanceGrid;
    Editor<Attendance> attendanceEditor;
    Grid.Column<Attendance> absenseColumn;
    Grid.Column<Attendance> editColumn;
    Button nextButton;

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

        attendanceGrid = new Grid<>(Attendance.class, false);
        attendanceEditor = attendanceGrid.getEditor();
//        attendanceGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        attendanceGrid.addColumn(Attendance::getStudentName)
                .setHeader("Elev").setSortable(true);
        attendanceGrid.addColumn(new ComponentRenderer<>(
                attendance -> {
                    Checkbox checkbox = new Checkbox();
                    checkbox.setValue(attendance.isPresent());
                    checkbox.addValueChangeListener(event -> {
                        attendance.setPresent(checkbox.getValue());
                        attendanceService.updateAttendance(attendance);
                        attendanceGrid.getDataProvider().refreshItem(attendance);
                    });
                    return checkbox;
                }
        )).setHeader("Närvarande");
        absenseColumn = attendanceGrid.addColumn(Attendance::getMinutesAbsent)
                .setHeader("Ogiltig Frånvaro (min)").setSortable(false);
        editColumn = attendanceGrid.addComponentColumn(attendance -> {
            Button editButton = new Button("Ändra");
            editButton.addClickListener(event -> {
                if (attendanceEditor.isOpen()) {
                    attendanceEditor.cancel();
                }
                attendanceGrid.getEditor().editItem(attendance);
            });
            return editButton;
        });

        Binder<Attendance> binder = new Binder<>(Attendance.class);
        attendanceEditor.setBinder(binder);
        attendanceEditor.setBuffered(true);

        IntegerField absenceField = new IntegerField();
        absenceField.setMin(0);
        //TODO change max to lesson length
        absenceField.setMax(60);
        binder.forField(absenceField)
                .withValidator(new IntegerRangeValidator("Ogilitg tid, minimum 0, max " + 60, absenceField.getMin(), absenceField.getMax()))
                .bind(Attendance::getMinutesAbsent, Attendance::setMinutesAbsent);
        absenseColumn.setEditorComponent(absenceField);

        Button saveButton = new Button("Spara", event -> {
            Attendance attendance = attendanceEditor.getItem();
            attendanceEditor.save();
            attendanceService.updateAttendance(attendance);
        });
        Button cancelButton = new Button(VaadinIcon.CLOSE.create(),
                event -> attendanceEditor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton);
        editColumn.setEditorComponent(actions);

        add(new ViewToolbar("Närvarohantering"));
        text = new Text("Välj lektion");
        add(text);
        add(lessonGrid, nextButton);
    }

    private void switchToStudentGrid() {
        Lesson lesson = lessonGrid.getSelectedItems().iterator().next();
        remove(lessonGrid, nextButton);

        attendanceGrid.setItems(attendanceService.getAttendanceByLesson(lesson));

        text.setText(lesson.getSubject() + ": " + lesson.getClassId() + " Lektionstid: " + lesson.getDuration());
        add(attendanceGrid);
    }
}
