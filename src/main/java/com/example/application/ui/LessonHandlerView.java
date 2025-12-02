package com.example.application.ui;

import com.example.application.base.ui.component.ViewToolbar;
import com.example.application.model.Lesson;
import com.example.application.service.LessonService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.Duration;
import java.time.LocalDateTime;

@Route("lektionslista")
@PageTitle("Lektioner")
@Menu(title = "Lektionslista")
public class LessonHandlerView extends Div {
    private final LessonService lessonService;

    //TODO temporary teacherId
    private final Long teacherId = 1L;

    private final TextField subject;
    private final TextField classId;
    private final NumberField duration;
    private final DateTimePicker startDate;
    private final Button addButton;
    private final Grid<Lesson> lessonGrid;

    public LessonHandlerView(LessonService lessonService) {
        this.lessonService = lessonService;

        subject = new TextField();
        subject.setLabel("Ämne");
        subject.setPlaceholder("Ämne");

        classId = new TextField();
        classId.setLabel("Klass");
        classId.setPlaceholder("Klass");

        duration = new NumberField();
        duration.setLabel("Längd");
        duration.setPlaceholder("0");
        duration.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        Div minSuffix = new Div();
        minSuffix.setText("min");
        duration.setSuffixComponent(minSuffix);

        startDate = new DateTimePicker();
        startDate.setLabel("Datum och tid");
        startDate.setStep(Duration.ofMinutes(15));
        startDate.setMin(LocalDateTime.now());

        addButton = new Button("Lägg till", event -> addLesson());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        lessonGrid = new Grid<>(Lesson.class, false);
        lessonGrid.setItems(lessonService.findAll());
//        lessonGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        lessonGrid.addColumn(Lesson::getSubject)
                .setHeader("Ämne").setSortable(true);
        lessonGrid.addColumn(Lesson::getClassId)
                .setHeader("Klass").setSortable(true);
        lessonGrid.addColumn(Lesson::getDuration)
                .setHeader("Längd (min)").setSortable(true);
        lessonGrid.addColumn(Lesson::getStartTime)
                .setHeader("Datum och tid").setSortable(true);

        add(new ViewToolbar("Lektionslista"), ViewToolbar.group(subject, classId, duration, startDate, addButton));
        add(lessonGrid);
    }

    private void addLesson() {
        lessonService.add(teacherId, classId.getValue(), subject.getValue(), duration.getValue().intValue(), startDate.getValue());
        lessonGrid.setItems(lessonService.findAll());
        subject.clear();
        classId.clear();
        duration.clear();
        startDate.clear();
    }

}
