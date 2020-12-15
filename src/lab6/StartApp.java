package lab6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lab6.controller.Controller;
import lab6.repository.LogFileRepository;

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

        //TODO: LABEL EMAIL SI PSWD + ARANJARE PAG

        //input email si password
        TextField inputEMail = new TextField();
        inputEMail.setMaxWidth(200);
        inputEMail.setFont(new Font("Arial", 18));

        PasswordField inputPassword = new PasswordField();
        inputPassword.setMaxWidth(200);
        inputPassword.setFont(new Font("Arial", 18));

        //buton LOG IN
        Button logInButton = new Button("LOG IN");
        logInButton.setPrefSize(170, 45);
        logInButton.setStyle("-fx-background-color: #89B3F5");
        logInButton.setOnAction((event -> {
            String eMail = inputEMail.getText();
            String pswd = inputPassword.getText();

            for (int i = 0; i < controller.logFileRepository.eMails.size(); i++) {
                if (i < controller.studEMails.size() && controller.studEMails.get(i).equals(eMail) && controller.studPasswords.get(i).equals(pswd)) {
                    id[0] = controller.pswdToId(pswd);
                    primaryStage.setScene(studScene);
                    primaryStage.show();
                    break;
                }
                if (i < controller.teacherEMails.size() && controller.teacherEMails.get(i).equals(eMail) && controller.teacherPasswords.get(i).equals(pswd)) {
                    id[0] = controller.pswdToId(pswd);
                    primaryStage.setScene(teacherScene);
                    primaryStage.show();
                    break;
                }
            }
            inputEMail.clear();
            inputPassword.clear();
            message.setText("E-Mail or password incorrect. Please try again!");
            message.setFont(new Font("Arial", 16));
            message.setTextFill(javafx.scene.paint.Color.web("#ED2A3F"));


        }));

        VBox loginBox = new VBox();
        loginBox.setSpacing(25);
        loginBox.setPadding(new Insets(50, 0, 0, 125));
        loginBox.getChildren().addAll(inputEMail, inputPassword, logInButton, message);
        ((Group) logInScene.getRoot()).getChildren().add(loginBox);


        //main page students
        Button enrolButton = new Button("ENROL");
        enrolButton.setPrefSize(170, 45);
        enrolButton.setStyle("-fx-background-color: #89B3F5");
        enrolButton.setOnAction((event -> {
            Scene enrolScene = new Scene(new Group(), 1100, 800); //TODO: SCENE MAI MIC
            primaryStage.setScene(enrolScene);
            primaryStage.show();

            Label messageLabel = new Label();

            TextField inputCourse = new TextField();
            inputCourse.setMaxWidth(200);
            inputCourse.setFont(new Font("Arial", 18));
            //TODO: VF DC INPUT STRING

            Button enrolMeButton = new Button("ENROL ME!");
            enrolMeButton.setPrefSize(170, 45);
            enrolMeButton.setStyle("-fx-background-color: #89B3F5");
            enrolMeButton.setOnAction((event1 -> {
                //succes
                switch (controller.registerStud(id[0], inputCourse.getText())) {
                    case "enrolled" -> messageLabel.setText("YES");
                    case "credits" -> messageLabel.setText("PREA MULTE CREDITE");
                    case "already enrolled" -> messageLabel.setText("ALREADY ENROLLED");
                    case "incorrect" -> messageLabel.setText("INVALID COURSE NAME");
                    default -> messageLabel.setText("FULL");
                }
            }));

            VBox enrolBox = new VBox();
            enrolBox.setSpacing(25);
            enrolBox.setPadding(new Insets(50, 0, 0, 125));
            enrolBox.getChildren().addAll(inputCourse, enrolMeButton, messageLabel);
            ((Group) enrolScene.getRoot()).getChildren().add(enrolBox);


        }));

        //TODO: ARANJARE PAG

        //butoane meniu student
        Button showAvailableButton = new Button("AVAILABLE COURSES");
        showAvailableButton.setPrefSize(170, 45);
        showAvailableButton.setStyle("-fx-background-color: #89B3F5");

        Button showCourseButton = new Button("COURSES");
        showCourseButton.setPrefSize(170, 45);
        showCourseButton.setStyle("-fx-background-color: #89B3F5");

        Button logOutButton = new Button("LOG OUT");
        logOutButton.setPrefSize(170, 45);
        logOutButton.setStyle("-fx-background-color: #89B3F5");

        HBox studentBox = new HBox();
        studentBox.setSpacing(25);
        studentBox.setPadding(new Insets(50, 0, 0, 125));
        studentBox.getChildren().addAll(enrolButton, showAvailableButton, showCourseButton);

        HBox logOutBox = new HBox();
        logOutBox.setSpacing(25);
        logOutBox.setPadding(new Insets(500, 0, 0, 500));
        logOutBox.getChildren().addAll(logOutButton);

        VBox studentTotalBox = new VBox();
        studentTotalBox.setSpacing(25);
        studentTotalBox.setPadding(new Insets(50, 0, 0, 125));
        studentTotalBox.getChildren().addAll(studentBox, logOutBox);
        ((Group) studScene.getRoot()).getChildren().addAll(studentTotalBox);



        primaryStage.setScene(logInScene);
        primaryStage.show();


    }


    public static void main(String[] args) throws FileNotFoundException {
        launch(args);
    }
}
