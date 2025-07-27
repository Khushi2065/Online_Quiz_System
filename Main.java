import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.*;

public class Main extends Application {

    private Connection conn;
    private Stage window;
    private Scene loginScene, quizScene, resultScene;
    private int currentUserId;
    private List<Question> questions;
    private int index = 0, score = 0;
    private Label questionLabel;
    private RadioButton optA, optB, optC, optD;
    private ToggleGroup optionsGroup;
    private Button nextBtn;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        connectDB();
        setupLoginScene();
        window.setScene(loginScene);
        window.setTitle("Online Quiz System");
        window.show();
    }

    private void connectDB() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz_db", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupLoginScene() {
        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        Button loginBtn = new Button("Login");

        Label message = new Label();

        loginBtn.setOnAction(e -> {
            String user = userField.getText();
            String pass = passField.getText();
            try {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
                ps.setString(1, user);
                ps.setString(2, pass);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    currentUserId = rs.getInt("id");
                    loadQuestions();
                    setupQuizScene();
                    window.setScene(quizScene);
                } else {
                    message.setText("Invalid credentials");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        VBox loginLayout = new VBox(10, userLabel, userField, passLabel, passField, loginBtn, message);
        loginLayout.setStyle("-fx-padding: 20");
        loginScene = new Scene(loginLayout, 300, 250);
    }

    private void loadQuestions() {
        questions = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM questions ORDER BY RAND() LIMIT 5");
            while (rs.next()) {
                questions.add(new Question(
                        rs.getInt("id"),
                        rs.getString("question_text"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d"),
                        rs.getString("correct_option")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupQuizScene() {
        questionLabel = new Label();
        optA = new RadioButton();
        optB = new RadioButton();
        optC = new RadioButton();
        optD = new RadioButton();
        optionsGroup = new ToggleGroup();

        optA.setToggleGroup(optionsGroup);
        optB.setToggleGroup(optionsGroup);
        optC.setToggleGroup(optionsGroup);
        optD.setToggleGroup(optionsGroup);

        nextBtn = new Button("Next");
        nextBtn.setOnAction(e -> handleNext());

        VBox quizLayout = new VBox(10, questionLabel, optA, optB, optC, optD, nextBtn);
        quizLayout.setStyle("-fx-padding: 20");
        quizScene = new Scene(quizLayout, 400, 300);

        showQuestion();
    }

    private void showQuestion() {
        if (index < questions.size()) {
            Question q = questions.get(index);
            questionLabel.setText((index + 1) + ". " + q.text);
            optA.setText("A. " + q.a);
            optB.setText("B. " + q.b);
            optC.setText("C. " + q.c);
            optD.setText("D. " + q.d);
            optionsGroup.selectToggle(null);
        } else {
            saveResult();
            showResultScene();
        }
    }

    private void handleNext() {
        if (optionsGroup.getSelectedToggle() == null) return;
        RadioButton selected = (RadioButton) optionsGroup.getSelectedToggle();
        String ans = selected.getText().substring(0, 1);
        if (ans.equalsIgnoreCase(questions.get(index).correct)) {
            score++;
        }
        index++;
        showQuestion();
    }

    private void saveResult() {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO results (user_id, quiz_id, score, total_questions) VALUES (?, ?, ?, ?)"
            );
            ps.setInt(1, currentUserId);
            ps.setInt(2, 1); // assuming one quiz
            ps.setInt(3, score);
            ps.setInt(4, questions.size());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showResultScene() {
        Label result = new Label("Quiz Complete! Score: " + score + "/" + questions.size());
        VBox layout = new VBox(20, result);
        layout.setStyle("-fx-padding: 20");
        resultScene = new Scene(layout, 300, 200);
        window.setScene(resultScene);
    }
}
