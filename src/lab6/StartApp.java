package lab6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import lab6.controller.Controller;
import lab6.model.Student;
import lab6.model.Teacher;
import lab6.repository.LogFileRepository;
import lab6.repository.StudentRepository;

import java.io.FileNotFoundException;
import java.util.List;

public class StartApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Controller controller = new Controller();
        controller.declareData();
        Parent root = FXMLLoader.load(getClass().getResource("app.fxml"));
        primaryStage.setTitle("Moodle 2.0");
        primaryStage.setScene(new Scene(root, 1100, 800));
        final int[] id = {0};

        Scene logInScene = new Scene(new Group(), 1100, 800);
        Scene studScene = new Scene(new Group(), 1100, 800);
        Scene teacherScene = new Scene(new Group(), 1100, 800);

        Label message = new Label(); //mesaj eroare

        //label welcome
        Label welcome = new Label("Welcome");
        welcome.setFont(new Font("Arial", 65));
        welcome.setTextFill(Color.web("#89B3F5"));

        //label to moodle
        Label toMoodle = new Label("     to Moodle 2.0!");
        toMoodle.setFont(new Font("Arial", 30));
        toMoodle.setTextFill(Color.web("#89B3F5"));

        //label email si password
        Label labelEMail = new Label("Email Address: ");
        labelEMail.setFont(new Font("Arial", 24));

        Label labelPassword = new Label("       Password: ");
        labelPassword.setFont(new Font("Arial", 24));

        //input email si password
        TextField inputEMail = new TextField();
        inputEMail.setMaxWidth(500);
        inputEMail.setFont(new Font("Arial", 20));

        PasswordField inputPassword = new PasswordField();
        inputPassword.setMaxWidth(500);
        inputPassword.setFont(new Font("Arial", 20));

        //buton LOG IN
        Button logInButton = new Button("LOG IN");
        logInButton.setPrefSize(120, 35);
        logInButton.setFont(new Font("Arial", 14));
        logInButton.setStyle("-fx-background-color: #89B3F5");
        logInButton.setOnAction((event -> {
            String eMail = inputEMail.getText();
            String pswd = inputPassword.getText();

            //cautam in lista de eMail-uri
            for (int i = 0; i < controller.logFileRepository.eMails.size(); i++) {
                //daca gaseste eMail-ul si parola date ca input in lista cu eMail-uri si parole ale studentilor => e student
                if (i < controller.studEMails.size() && controller.studEMails.get(i).equals(eMail) && controller.studPasswords.get(i).equals(pswd)) {
                    id[0] = controller.pswdToId(pswd);
                    primaryStage.setScene(studScene); //deschide pagina destinata studentilor
                    primaryStage.show();
                    break;
                }
                //daca gaseste eMail-ul si parola date ca input in lista cu eMail-uri si parole ale profesorilor => e profesor
                if (i < controller.teacherEMails.size() && controller.teacherEMails.get(i).equals(eMail) && controller.teacherPasswords.get(i).equals(pswd)) {
                    id[0] = controller.pswdToId(pswd);
                    primaryStage.setScene(teacherScene); //deschide pagina destinata profesorilor
                    primaryStage.show();
                    break;
                }
            }
            //daca introduce gresit eMail-ul/parola
            inputEMail.clear(); //stergem ce a scris in text field-uri
            inputPassword.clear();
            message.setText("Email or password incorrect. Please try again!");
            message.setFont(new Font("Arial", 20));
            message.setTextFill(javafx.scene.paint.Color.web("#ED2A3F"));


        }));

        //box mesaj welcome
        VBox welcomeBox = new VBox();
        welcomeBox.setSpacing(30);
        welcomeBox.setPadding(new Insets(55, 0, 0, 320));
        welcomeBox.getChildren().addAll(welcome, toMoodle);

        //box label email + label password
        VBox labelBox = new VBox();
        labelBox.setSpacing(40);
        labelBox.setPadding(new Insets(55, 0, 0, 155));
        labelBox.getChildren().addAll(labelEMail, labelPassword);

        //box log in buton
        HBox logButtonBox = new HBox();
        logButtonBox.setSpacing(25);
        logButtonBox.setPadding(new Insets(50, 0, 0, 155));
        logButtonBox.getChildren().add(logInButton);

        //box input-uri
        VBox inputBox = new VBox();
        inputBox.setSpacing(25);
        inputBox.setPadding(new Insets(50, 0, 0, 125));
        inputBox.getChildren().addAll(inputEMail, inputPassword, logButtonBox);

        //box label + input
        HBox logInBox = new HBox();
        logInBox.setSpacing(0);
        logInBox.setPadding(new Insets(100, 0, 0, 30));
        logInBox.getChildren().addAll(labelBox, inputBox);

        //total box pentru pagina de log in
        VBox totalBox = new VBox();
        totalBox.setSpacing(5);
        totalBox.setPadding(new Insets(20, 0, 0, 77));
        totalBox.getChildren().addAll(welcomeBox, logInBox, message);
        ((Group) logInScene.getRoot()).getChildren().add(totalBox);

        //main page students


        //label hi student

        Label hiLabel = new Label("\n              Hi, what do you want to do? :D");
        hiLabel.setFont(new Font("Arial", 40));
        hiLabel.setTextFill(Color.web("#89B3F5"));


        //button enroll me
        Button enrolButton = new Button("ENROL");
        enrolButton.setPrefSize(170, 50);
        enrolButton.setFont(new Font("Arial", 14));
        enrolButton.setStyle("-fx-background-color: #89B3F5");
        enrolButton.setOnAction((event -> {
            Scene enrolScene = new Scene(new Group(), 900, 600);
            primaryStage.setScene(enrolScene);
            primaryStage.show();

            //label mesaj
            Label messageLabel = new Label();
            messageLabel.setFont(new Font("Arial", 20));


            //label course name
            Label courseName = new Label("Course: ");
            courseName.setFont(new Font("Arial", 30));

            //input course name
            TextField inputCourse = new TextField();
            inputCourse.setMaxWidth(500);
            inputCourse.setFont(new Font("Arial", 25));

            //button enroll me
            Button enrolMeButton = new Button("ENROL ME!");
            enrolMeButton.setPrefSize(150, 45);
            enrolMeButton.setFont(new Font("Arial", 14));
            enrolMeButton.setStyle("-fx-background-color: #89F68D");
            enrolMeButton.setOnAction((event1 -> {
                if (controller.registerStud(id[0], inputCourse.getText()).equals("enrolled")) {
                    messageLabel.setText("YOU ARE NOW ENROLLED IN THE COURSE!"); //daca a reusit sa se inscrie
                    messageLabel.setTextFill(Color.web("#36BF2B"));
                }
                else {
                    messageLabel.setTextFill(Color.web("#ED2A3F"));
                    switch (controller.registerStud(id[0], inputCourse.getText())) {
                        case "credits" -> messageLabel.setText("YOU HAVE TOO MANY CREDITS!"); //daca are prea multe credite
                        case "already enrolled" -> messageLabel.setText("YOU ARE ALREADY ENROLLED IN THIS COURSE!"); //daca e deja inscris la curs
                        case "incorrect" -> messageLabel.setText("INVALID COURSE NAME. PLEASE TRY AGAIN!"); //daca cursul nu exista
                        default -> messageLabel.setText("THIS COURSE HAS NO FREE PLACES. :( \n     PLEASE CHOOSE ANOTHER ONE!"); //daca cursul nu mai are locuri libere
                    }
                }
            }));

            //back button
            Button backButton = new Button("BACK");
            backButton.setPrefSize(170, 45);
            backButton.setFont(new Font("Arial", 14));
            backButton.setStyle("-fx-background-color: #89B3F5");
            backButton.setOnAction((event1 -> {
                primaryStage.setScene(studScene);
                primaryStage.show();
            }));

            //box label + input + enrol me button
            HBox courseBox = new HBox();
            courseBox.setSpacing(25);
            courseBox.setPadding(new Insets(165, 0, 0, 40));
            courseBox.getChildren().addAll(courseName, inputCourse, enrolMeButton);

            //box button back
            VBox buttonBox = new VBox();
            buttonBox.setSpacing(25);
            buttonBox.setPadding(new Insets(130, 0, 0, 575));
            buttonBox.getChildren().add(backButton);

            //box mesaj label
            HBox messageBox = new HBox();
            messageBox.setSpacing(25);
            messageBox.setPadding(new Insets(60, 0, 0, 135));
            messageBox.getChildren().add(messageLabel);

            //box complet pagina enrol
            VBox totalEnrolBox = new VBox();
            totalEnrolBox.setSpacing(25);
            totalEnrolBox.setPadding(new Insets(50, 0, 0, 125));
            totalEnrolBox.getChildren().addAll(courseBox, messageBox, buttonBox);
            ((Group) enrolScene.getRoot()).getChildren().add(totalEnrolBox);


        }));


        //button available courses
        Button showAvailableButton = new Button("AVAILABLE COURSES");
        showAvailableButton.setPrefSize(170, 50);
        showAvailableButton.setFont(new Font("Arial", 14));
        showAvailableButton.setStyle("-fx-background-color: #89B3F5");
        showAvailableButton.setOnAction((event -> {
            Scene showAvScene = new Scene(new Group(), 900, 600);
            primaryStage.setScene(showAvScene);
            primaryStage.show();

            Label showAvLabel = new Label(controller.showAvailableCourses());
            showAvLabel.setFont(new Font("Arial", 24));

            //back button
            Button backButton = new Button("BACK");
            backButton.setPrefSize(170, 45);
            backButton.setFont(new Font("Arial", 14));
            backButton.setStyle("-fx-background-color: #89B3F5");
            backButton.setOnAction((event1 -> {
                primaryStage.setScene(studScene);
                primaryStage.show();
            }));

            //box button back
            VBox buttonBox = new VBox();
            buttonBox.setSpacing(25);
            buttonBox.setPadding(new Insets(150, 0, 0, 575));
            buttonBox.getChildren().add(backButton);


            //box complet pagina show available courses
            VBox totalShowAvBox = new VBox();
            totalShowAvBox.setSpacing(25);
            totalShowAvBox.setPadding(new Insets(150, 0, 0, 125));
            totalShowAvBox.getChildren().addAll(showAvLabel, buttonBox);
            ((Group) showAvScene.getRoot()).getChildren().add(totalShowAvBox);

        }));

        //button show all courses
        Button showCourseButton = new Button("COURSES");
        showCourseButton.setPrefSize(170, 50);
        showCourseButton.setFont(new Font("Arial", 14));
        showCourseButton.setStyle("-fx-background-color: #89B3F5");
        showCourseButton.setOnAction((event -> {
            Scene showCoursesScene = new Scene(new Group(), 900, 600);
            primaryStage.setScene(showCoursesScene);
            primaryStage.show();

            Label showCoursesLabel = new Label(controller.showAllCourses());
            showCoursesLabel.setFont(new Font("Arial", 24));

            //back button
            Button backButton = new Button("BACK");
            backButton.setPrefSize(170, 45);
            backButton.setFont(new Font("Arial", 14));
            backButton.setStyle("-fx-background-color: #89B3F5");
            backButton.setOnAction((event1 -> {
                primaryStage.setScene(studScene);
                primaryStage.show();
            }));

            //box button back
            VBox buttonBox = new VBox();
            buttonBox.setSpacing(25);
            buttonBox.setPadding(new Insets(130, 0, 0, 575));
            buttonBox.getChildren().add(backButton);

            //box complet pagina show available courses
            VBox totalShowCoursesBox = new VBox();
            totalShowCoursesBox.setSpacing(25);
            totalShowCoursesBox.setPadding(new Insets(100, 0, 0, 125));
            totalShowCoursesBox.getChildren().addAll(showCoursesLabel, buttonBox);
            ((Group) showCoursesScene.getRoot()).getChildren().add(totalShowCoursesBox);
        }));

        //button profil
        Button profileButton = new Button("PROFILE");
        profileButton.setPrefSize(170, 50);
        profileButton.setFont(new Font("Arial", 14));
        profileButton.setStyle("-fx-background-color: #89B3F5");
        profileButton.setOnAction((event -> {
            Scene profileScene = new Scene(new Group(), 900, 600);
            primaryStage.setScene(profileScene);
            primaryStage.show();

            Label titleLabel = new Label("             MY PROFILE");
            titleLabel.setFont(new Font("Arial", 50));
            titleLabel.setTextFill(Color.web("#89B3F5"));

            Label profileLabel = new Label(controller.studProfile(id[0]));
            profileLabel.setFont(new Font("Arial", 24));

            //back button
            Button backButton = new Button("BACK");
            backButton.setPrefSize(170, 45);
            backButton.setFont(new Font("Arial", 14));
            backButton.setStyle("-fx-background-color: #89B3F5");
            backButton.setOnAction((event1 -> {
                primaryStage.setScene(studScene);
                primaryStage.show();
            }));

            //box button back
            VBox buttonBox = new VBox();
            buttonBox.setSpacing(25);
            buttonBox.setPadding(new Insets(60, 0, 0, 575));
            buttonBox.getChildren().add(backButton);

            //box complet pagina profile
            VBox profileBox = new VBox();
            profileBox.setSpacing(130);
            profileBox.setPadding(new Insets(50, 0, 0, 125));
            profileBox.getChildren().addAll(titleLabel, profileLabel, buttonBox);
            ((Group) profileScene.getRoot()).getChildren().add(profileBox);
        }));

        //log out button
        Button logOutButton = new Button("LOG OUT");
        logOutButton.setPrefSize(170, 45);
        logOutButton.setFont(new Font("Arial", 14));
        logOutButton.setStyle("-fx-background-color: #89B3F5");
        logOutButton.setOnAction((event2) -> {
            primaryStage.setScene(logInScene);
            primaryStage.show();
            message.setTextFill(Color.web("#FFFFFF"));
        });


        //box enroll me button + my courses button
        VBox student1Box = new VBox();
        student1Box.setSpacing(150);
        student1Box.setPadding(new Insets(0, 0, 0, 125));
        student1Box.getChildren().addAll(enrolButton, profileButton);

        //box available courses button + all courses button
        VBox student2Box = new VBox();
        student2Box.setSpacing(150);
        student2Box.setPadding(new Insets(0, 0, 0, 125));
        student2Box.getChildren().addAll(showAvailableButton, showCourseButton);

        //box log out button
        HBox logOutBox = new HBox();
        logOutBox.setSpacing(25);
        logOutBox.setPadding(new Insets(185, 0, 0, 750));
        logOutBox.getChildren().addAll(logOutButton);

        //box student buttons
        HBox studentBox = new HBox();
        studentBox.setSpacing(55);
        studentBox.setPadding(new Insets(150, 0, 0, 45));
        studentBox.getChildren().addAll(student1Box, student2Box);

        //box complet pagina student
        VBox studentTotalBox = new VBox();
        studentTotalBox.setSpacing(25);
        studentTotalBox.setPadding(new Insets(0, 0, 0, 125));
        studentTotalBox.getChildren().addAll(hiLabel, studentBox, logOutBox);
        ((Group) studScene.getRoot()).getChildren().addAll(studentTotalBox);


        // main page teacher

        Label hiTLabel = new Label("\n         Good evening, what do you want to do?");
        hiTLabel.setFont(new Font("Arial", 40));
        hiTLabel.setTextFill(Color.web("#89B3F5"));

        //button delete curs predat de teacher
        Button deleteCourseButton = new Button("DELETE COURSE");
        deleteCourseButton.setPrefSize(170, 50);
        deleteCourseButton.setFont(new Font("Arial", 14));
        deleteCourseButton.setStyle("-fx-background-color: #89B3F5");
        deleteCourseButton.setOnAction((event -> {
            Scene deleteCourseScene = new Scene(new Group(), 900, 600);
            primaryStage.setScene(deleteCourseScene);
            primaryStage.show();

            //label mesaj
            Label messageLabel = new Label();
            messageLabel.setFont(new Font("Arial", 20));


            //label course name
            Label courseName = new Label("Course: ");
            courseName.setFont(new Font("Arial", 34));

            //input course name
            TextField inputCourse = new TextField();
            inputCourse.setMaxWidth(500);
            inputCourse.setFont(new Font("Arial", 25));

            Button deleteButton = new Button("DELETE");
            deleteButton.setPrefSize(150, 45);
            deleteButton.setFont(new Font("Arial", 14));
            deleteButton.setStyle("-fx-background-color: #89F68D");
            deleteButton.setOnAction((event1 -> {
                String course = inputCourse.getText();
                if (controller.deleteCourse(course, id[0]).equals("incorrect")) {
                    messageLabel.setTextFill(Color.web("#ED2A3F"));
                    messageLabel.setText("Invalid course name");
                }
                else {
                    messageLabel.setTextFill(Color.web("#36BF2B"));
                    messageLabel.setText("Your course was successfully deleted!");
                }
                inputCourse.clear();
            }));

            //back button
            Button backButton = new Button("BACK");
            backButton.setPrefSize(170, 45);
            backButton.setFont(new Font("Arial", 14));
            backButton.setStyle("-fx-background-color: #89B3F5");
            backButton.setOnAction((event1 -> {
                primaryStage.setScene(teacherScene);
                primaryStage.show();
            }));

            //box label + input + delete button
            HBox courseBox = new HBox();
            courseBox.setSpacing(25);
            courseBox.setPadding(new Insets(165, 0, 0, 40));
            courseBox.getChildren().addAll(courseName, inputCourse, deleteButton);

            //box button back
            VBox buttonBox = new VBox();
            buttonBox.setSpacing(25);
            buttonBox.setPadding(new Insets(130, 0, 0, 575));
            buttonBox.getChildren().add(backButton);

            //box mesaj label
            HBox messageBox = new HBox();
            messageBox.setSpacing(25);
            messageBox.setPadding(new Insets(60, 0, 0, 135));
            messageBox.getChildren().add(messageLabel);

            //box complet pagina delete course
            VBox totalDeleteBox = new VBox();
            totalDeleteBox.setSpacing(25);
            totalDeleteBox.setPadding(new Insets(50, 0, 0, 125));
            totalDeleteBox.getChildren().addAll(courseBox, messageBox, buttonBox);
            ((Group) deleteCourseScene.getRoot()).getChildren().add(totalDeleteBox);
        }));

        //button show all students from a specific course
        Button showStudFromCourseButton = new Button("STUDENTS FROM COURSE...");
        showStudFromCourseButton.setPrefSize(175, 50);
        showStudFromCourseButton.setFont(new Font("Arial", 11));
        showStudFromCourseButton.setStyle("-fx-background-color: #89B3F5");
        showStudFromCourseButton.setOnAction((event -> {
            Scene showStudFromCourseScene = new Scene(new Group(), 900, 600);
            primaryStage.setScene(showStudFromCourseScene);
            primaryStage.show();

            //label mesaj
            Label messageLabel = new Label();
            messageLabel.setFont(new Font("Arial", 20));


            //label course name
            Label courseName = new Label("Course: ");
            courseName.setFont(new Font("Arial", 34));

            //input course name
            TextField inputCourse = new TextField();
            inputCourse.setMaxWidth(500);
            inputCourse.setFont(new Font("Arial", 25));

            Button showStudButton = new Button("SEARCH");
            showStudButton.setPrefSize(170, 45);
            showStudButton.setFont(new Font("Arial", 14));
            showStudButton.setStyle("-fx-background-color: #89F68D");
            showStudButton.setOnAction((event1 -> {
                String course = inputCourse.getText();
                if (controller.showStudFromCourse(course).equals("incorrect")) {
                    messageLabel.setTextFill(Color.web("#ED2A3F"));
                    messageLabel.setText("Invalid course name");
                }
                else {
                    messageLabel.setTextFill(Color.web("#000000"));
                    messageLabel.setText(controller.showStudFromCourse(course));
                }
                inputCourse.clear();
            }));

            //back button
            Button backButton = new Button("BACK");
            backButton.setPrefSize(170, 50);
            backButton.setFont(new Font("Arial", 14));
            backButton.setStyle("-fx-background-color: #89B3F5");
            backButton.setOnAction((event1 -> {
                primaryStage.setScene(teacherScene);
                primaryStage.show();
            }));

            //box label + input + show button
            HBox courseBox = new HBox();
            courseBox.setSpacing(25);
            courseBox.setPadding(new Insets(50, 0, 0, 40));
            courseBox.getChildren().addAll(courseName, inputCourse, showStudButton);

            //box button back
            VBox buttonBox = new VBox();
            buttonBox.setSpacing(25);
            buttonBox.setPadding(new Insets(130, 0, 0, 610));
            buttonBox.getChildren().add(backButton);

            //box mesaj label
            HBox messageBox = new HBox();
            messageBox.setSpacing(25);
            messageBox.setPadding(new Insets(60, 0, 0, 125));
            messageBox.getChildren().add(messageLabel);

            //box complet pagina show stud from course
            VBox totalShowBox = new VBox();
            totalShowBox.setSpacing(25);
            totalShowBox.setPadding(new Insets(50, 0, 0, 100));
            totalShowBox.getChildren().addAll(courseBox, messageBox, buttonBox);
            ((Group) showStudFromCourseScene.getRoot()).getChildren().add(totalShowBox);

        }));

        //button show my courses
        Button showTeachCoursesButton = new Button("MY COURSES");
        showTeachCoursesButton.setPrefSize(175, 50);
        showTeachCoursesButton.setFont(new Font("Arial", 14));
        showTeachCoursesButton.setStyle("-fx-background-color: #89B3F5");
        showTeachCoursesButton.setOnAction((event ->  {
            Scene showTeachCoursesScene = new Scene(new Group(), 900, 600);
            primaryStage.setScene(showTeachCoursesScene);
            primaryStage.show();


            Label titleLabel = new Label("             MY COURSES");
            titleLabel.setFont(new Font("Arial", 50));
            titleLabel.setTextFill(Color.web("#89B3F5"));

            Label showTeachCoursesLabel = new Label(controller.showTeacherCourses(id[0]));
            showTeachCoursesLabel.setFont(new Font("Arial", 18));


            //back button
            Button backButton = new Button("BACK");
            backButton.setPrefSize(170, 45);
            backButton.setFont(new Font("Arial", 14));
            backButton.setStyle("-fx-background-color: #89B3F5");
            backButton.setOnAction((event1 -> {
                primaryStage.setScene(teacherScene);
                primaryStage.show();
            }));

            VBox buttonBox = new VBox();
            buttonBox.setSpacing(25);
            buttonBox.setPadding(new Insets(10, 0, 0, 575));
            buttonBox.getChildren().add(backButton);

            //box complet pagina show available courses
            VBox totalShowTeachCoursesBox = new VBox();
            totalShowTeachCoursesBox.setSpacing(25);
            totalShowTeachCoursesBox.setPadding(new Insets(10, 0, 0, 125));
            totalShowTeachCoursesBox.getChildren().addAll(titleLabel, showTeachCoursesLabel, buttonBox);
            ((Group) showTeachCoursesScene.getRoot()).getChildren().add(totalShowTeachCoursesBox);
        }));

        //button show all students
        Button showAllStudButton = new Button("STUDENTS");
        showAllStudButton.setPrefSize(175, 50);
        showAllStudButton.setFont(new Font("Arial", 14));
        showAllStudButton.setStyle("-fx-background-color: #89B3F5");
        showAllStudButton.setOnAction((event -> {
            Scene showStudentsScene = new Scene(new Group(), 900, 600);
            primaryStage.setScene(showStudentsScene);
            primaryStage.show();

            Label showStudentsLabel = new Label(controller.showAllStudents());
            showStudentsLabel.setFont(new Font("Arial", 20));

            //back button
            Button backButton = new Button("BACK");
            backButton.setPrefSize(170, 45);
            backButton.setFont(new Font("Arial", 14));
            backButton.setStyle("-fx-background-color: #89B3F5");
            backButton.setOnAction((event1 -> {
                primaryStage.setScene(teacherScene);
                primaryStage.show();
            }));

            VBox buttonBox = new VBox();
            buttonBox.setSpacing(25);
            buttonBox.setPadding(new Insets(150, 0, 0, 590));
            buttonBox.getChildren().addAll(backButton);

            //box complet pagina show all courses
            VBox totalShowStudentsBox = new VBox();
            totalShowStudentsBox.setSpacing(25);
            totalShowStudentsBox.setPadding(new Insets(150, 0, 0, 80));
            totalShowStudentsBox.getChildren().addAll(showStudentsLabel, buttonBox);
            ((Group) showStudentsScene.getRoot()).getChildren().add(totalShowStudentsBox);

        }));

        //button show all courses
        Button showAllCoursesButton = new Button("COURSES");
        showAllCoursesButton.setPrefSize(175, 50);
        showAllCoursesButton.setFont(new Font("Arial", 14));
        showAllCoursesButton.setStyle("-fx-background-color: #89B3F5");
        showAllCoursesButton.setOnAction((event -> {
            Scene showCoursesScene = new Scene(new Group(), 900, 600);
            primaryStage.setScene(showCoursesScene);
            primaryStage.show();

            Label showCoursesLabel = new Label(controller.showAllCourses());
            showCoursesLabel.setFont(new Font("Arial", 24));

            //back button
            Button backButton = new Button("BACK");
            backButton.setPrefSize(170, 45);
            backButton.setFont(new Font("Arial", 14));
            backButton.setStyle("-fx-background-color: #89B3F5");
            backButton.setOnAction((event1 -> {
                primaryStage.setScene(teacherScene);
                primaryStage.show();
            }));

            VBox buttonBox = new VBox();
            buttonBox.setSpacing(25);
            buttonBox.setPadding(new Insets(130, 0, 0, 575));
            buttonBox.getChildren().addAll(backButton);

            //box complet pagina show all courses
            VBox totalShowCoursesBox = new VBox();
            totalShowCoursesBox.setSpacing(25);
            totalShowCoursesBox.setPadding(new Insets(100, 0, 0, 125));
            totalShowCoursesBox.getChildren().addAll(showCoursesLabel, buttonBox);
            ((Group) showCoursesScene.getRoot()).getChildren().add(totalShowCoursesBox);

            }));

        //button show all teachers
        Button showAllTeachersButton = new Button("TEACHERS");
        showAllTeachersButton.setPrefSize(175, 50);
        showAllTeachersButton.setFont(new Font("Arial", 14));
        showAllTeachersButton.setStyle("-fx-background-color: #89B3F5");
        showAllTeachersButton.setOnAction((event ->  {
            Scene showTeachersScene = new Scene(new Group(), 900, 600);
            primaryStage.setScene(showTeachersScene);
            primaryStage.show();

            Label showTeachersLabel = new Label(controller.showAllTeachers());
            showTeachersLabel.setFont(new Font("Arial", 24));

            //back button
            Button backButton = new Button("BACK");
            backButton.setPrefSize(170, 45);
            backButton.setFont(new Font("Arial", 14));
            backButton.setStyle("-fx-background-color: #89B3F5");
            backButton.setOnAction((event1 -> {
                primaryStage.setScene(teacherScene);
                primaryStage.show();
            }));


            VBox buttonBox = new VBox();
            buttonBox.setSpacing(25);
            buttonBox.setPadding(new Insets(170, 0, 0, 575));
            buttonBox.getChildren().addAll(backButton);

            //box complet pagina show all courses
            VBox totalShowTeachersBox = new VBox();
            totalShowTeachersBox.setSpacing(25);
            totalShowTeachersBox.setPadding(new Insets(150, 0, 0, 125));
            totalShowTeachersBox.getChildren().addAll(showTeachersLabel, buttonBox);
            ((Group) showTeachersScene.getRoot()).getChildren().add(totalShowTeachersBox);
        }));


        //log out button
        Button logOutTeachButton = new Button("LOG OUT");
        logOutTeachButton.setPrefSize(170, 45);
        logOutButton.setFont(new Font("Arial", 14));
        logOutTeachButton.setStyle("-fx-background-color: #89B3F5");
        logOutTeachButton.setOnAction((event2) -> {
            primaryStage.setScene(logInScene);
            primaryStage.show();
            message.setTextFill(Color.web("#FFFFFF"));
        });

        //box buttons delete + show stud from course + my courses
        VBox teacher1Box = new VBox();
        teacher1Box.setSpacing(80);
        teacher1Box.setPadding(new Insets(0, 0, 0, 125));
        teacher1Box.getChildren().addAll(deleteCourseButton, showStudFromCourseButton, showTeachCoursesButton);

        //box buttons show stud, teacher, course
        VBox teacher2Box = new VBox();
        teacher2Box.setSpacing(80);
        teacher2Box.setPadding(new Insets(0, 0, 0, 125));
        teacher2Box.getChildren().addAll(showAllStudButton, showAllCoursesButton, showAllTeachersButton);


        //box teacher buttons
        HBox teacherBox = new HBox();
        teacherBox.setSpacing(55);
        teacherBox.setPadding(new Insets(130, 0, 0, 45));
        teacherBox.getChildren().addAll(teacher1Box, teacher2Box);


        //box log out button
        HBox logOutTeachBox = new HBox();
        logOutTeachBox.setSpacing(25);
        logOutTeachBox.setPadding(new Insets(140, 0, 0, 750));
        logOutTeachBox.getChildren().addAll(logOutTeachButton);

        //box complet pagina teacher
        VBox teacherTotalBox = new VBox();
        teacherTotalBox.setSpacing(25);
        teacherTotalBox.setPadding(new Insets(0, 0, 0, 125));
        teacherTotalBox.getChildren().addAll(hiTLabel, teacherBox, logOutTeachBox);
        ((Group) teacherScene.getRoot()).getChildren().addAll(teacherTotalBox);


        primaryStage.setScene(logInScene);
        primaryStage.show();


    }


    public static void main(String[] args) throws FileNotFoundException {
        launch(args);
    }
}
