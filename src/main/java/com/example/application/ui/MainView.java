package com.example.application.ui;

import com.example.application.base.ui.component.ViewToolbar;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@PageTitle("Huvudmeny")
@Menu(order = 0, icon = "", title = "Huvudmeny")
public class MainView extends Main {

    MainView() {
        add(new ViewToolbar("Huvudmeny", ViewToolbar.group()));
    }
}
