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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
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

        //label welcome
        Label welcome = new Label("Welcome!");
        welcome.setFont(new Font("Arial", 55));
        welcome.setTextFill(Color.web("#89B3F5"));

        //label email si password
        Label labelEMail = new Label("Email Address: ");
        labelEMail.setFont(new Font("Arial", 24));

        Label labelPassword = new Label("Password: ");
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
        welcomeBox.setSpacing(40);
        welcomeBox.setPadding(new Insets(55, 0, 0, 320));
        welcomeBox.getChildren().add(welcome);

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
        totalBox.setSpacing(10);
        totalBox.setPadding(new Insets(20, 0, 0, 125));
        totalBox.getChildren().addAll(welcomeBox, logInBox, message);
        ((Group) logInScene.getRoot()).getChildren().add(totalBox);

        //main page students
        Button enrolButton = new Button("ENROL");
        enrolButton.setPrefSize(170, 45);
        enrolButton.setStyle("-fx-background-color: #89B3F5");
        enrolButton.setOnAction((event -> {
            Scene enrolScene = new Scene(new Group(), 900, 600); //TODO: SCENE MAI MIC
            primaryStage.setScene(enrolScene);
            primaryStage.show();

            //label mesaj
            Label messageLabel = new Label();
            messageLabel.setFont(new Font("Arial", 20));

            //label course name
            Label courseName = new Label("Course: ");
            courseName.setFont(new Font("Arial", 24));

            TextField inputCourse = new TextField();
            inputCourse.setMaxWidth(500);
            inputCourse.setFont(new Font("Arial", 20));
            inputCourse.setPromptText("Type the name of the course");
            //TODO: VF DC INPUT STRING

            Button enrolMeButton = new Button("ENROL ME!");
            enrolMeButton.setPrefSize(170, 45);
            enrolMeButton.setStyle("-fx-background-color: #89B3F5");
            enrolMeButton.setOnAction((event1 -> {
                switch (controller.registerStud(id[0], inputCourse.getText())) {
                    case "enrolled" -> messageLabel.setText("YOU ARE NOW ENROLLED IN THE COURSE!"); //daca a reusit sa se inscrie
                    case "credits" -> messageLabel.setText("YOU HAVE TOO MANY CREDITS!"); //daca are prea multe credite
                    case "already enrolled" -> messageLabel.setText("YOU ARE ALREADY ENROLLED IN THIS COURSE!"); //daca e deja inscris la curs
                    case "incorrect" -> messageLabel.setText("INVALID COURSE NAME. PLEASE TRY AGAIN!"); //daca cursul nu exista
                    default -> messageLabel.setText("THIS COURSE HAS NO FREE PLACES. PLEASE CHOOSE ANOTHER ONE!"); //daca cursul nu mai are locuri libere
                }
            }));

            //box label + input
            HBox courseBox = new HBox();
            courseBox.setSpacing(25);
            courseBox.setPadding(new Insets(60, 0, 0, 125));
            courseBox.getChildren().addAll(courseName, inputCourse);

            //box buton enroll me
            VBox buttonBox = new VBox();
            buttonBox.setSpacing(25);
            buttonBox.setPadding(new Insets(60, 0, 0, 320));
            buttonBox.getChildren().add(enrolMeButton);

            //box mesaj label
            HBox messageBox = new HBox();
            messageBox.setSpacing(25);
            messageBox.setPadding(new Insets(70, 0, 0, 150));
            messageBox.getChildren().add(messageLabel);

            //box complet pagina enrol
            VBox totalEnrolBox = new VBox();
            totalEnrolBox.setSpacing(25);
            totalEnrolBox.setPadding(new Insets(50, 0, 0, 125));
            totalEnrolBox.getChildren().addAll(courseBox, buttonBox, messageBox);
            ((Group) enrolScene.getRoot()).getChildren().add(totalEnrolBox);


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
        logOutButton.setOnAction((event2) -> {
            try {
                //revine la prima pagina
                start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

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
