# Attendance App README

## Project Structure

The sources of your Attendance App have the following structure:

```
src
├── main/frontend
│   └── themes
│       └── default
│           ├── styles.css
│           └── theme.json
├── main/java
│   └── [application package]
│       ├── base
│       │   └── ui
│       │       ├── component
│       │       │   └── ViewToolbar.java
│       │       └── MainLayout.java  
│       ├── dto    
│       │   ├── StatisticsDAO.java
│       │   └── StudentDAO.java   
│       ├── model  
│       │   ├── Attendance
│       │   ├── Lesson
│       │   └── Student
│       ├── repository  
│       │   ├── AttendanceRepository
│       │   ├── LessonRepository
│       │   └── StudentRepository
│       ├── service  
│       │   ├── AttendanceService
│       │   ├── LessonService
│       │   └── StudentService
│       ├── ui  
│       │   ├── AttendanceView
│       │   ├── LessonHandlerView
│       │   ├── MainView
│       │   ├── StatisticsView
│       │   └── StudentListView
│       └── Application.java       
└── test/java
    └── [application package]             
```

The main entry point into the application is `Application.java`. This class contains the `main()` method that start up 
the Spring Boot application.

* The `base` package contains classes meant for reuse across different features, either through composition or 
  inheritance. These have only been modified slightly from the standard ones generated when creating the project.
* The `dto` package contains classes meant for transferring data between ui and service. For example StudentDTO is used when 
  uploading a list of students in StudentListView.
* The `model`, `repository` and `service` packages contains the classes for the respective table in the database.
* The `ui` package contains classes that define all frontend views.


The `src/main/frontend` directory contains an empty theme called `default`, based on the Lumo theme. It is activated in
the `Application` class, using the `@Theme` annotation.

## Starting in Development Mode

To start the application in development mode, import it into your IDE and run the `Application` class. 

[//]: # (You can also start the application from the command line by running: )

[//]: # ()
[//]: # (```bash)

[//]: # (./mvnw)

[//]: # (```)

## Building for Production

[//]: # (To build the application in production mode, run:)

[//]: # ()
[//]: # (```bash)

[//]: # (./mvnw -Pproduction package)

[//]: # (```)

To build a Docker image, run:

```bash
docker build -t attendanceapp:latest .
```

To start Docker container, run:

```bash
docker run -p 8080:8080 attendanceapp:latest
```

(Irrelevant at the moment) If you use commercial components, pass the license key as a build secret:

```bash
docker build --secret id=proKey,src=$HOME/.vaadin/proKey .
```

## Future development

The code needs to be refactored. Suggestions include moving creating student-grids to its own class in the `base`
package. The next most important step is to implement a login feature with the separat user types `Teacher` and `Admin`,
with a future type also including `Parent`. A frontpage (`MainView` class) would also be a recommended next step in the
development.