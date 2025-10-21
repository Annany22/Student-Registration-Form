import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentRegistrationForm extends JFrame implements ActionListener {
    JTextField nameField, ageField, emailField;
    JComboBox<String> genderBox, courseBox;
    JButton submitButton, clearButton;

    Connection con;
    PreparedStatement pst;

    StudentRegistrationForm() {
        setTitle("Student Registration Form");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 2, 10, 10));
        setLocationRelativeTo(null);

        JLabel nameLabel = new JLabel("Full Name:");
        JLabel ageLabel = new JLabel("Age:");
        JLabel genderLabel = new JLabel("Gender:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel courseLabel = new JLabel("Course:");

        nameField = new JTextField();
        ageField = new JTextField();
        emailField = new JTextField();

        String[] genders = {"Select", "Male", "Female", "Other"};
        genderBox = new JComboBox<>(genders);

        String[] courses = {"Select", "CSE", "ECE", "ME", "IT", "CIVIL"};
        courseBox = new JComboBox<>(courses);

        submitButton = new JButton("Submit");
        clearButton = new JButton("Clear");

        submitButton.addActionListener(this);
        clearButton.addActionListener(this);

        add(nameLabel); add(nameField);
        add(ageLabel); add(ageField);
        add(genderLabel); add(genderBox);
        add(emailLabel); add(emailField);
        add(courseLabel); add(courseBox);
        add(submitButton); add(clearButton);

        connectDB();
        setVisible(true);
    }

    void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/studentdb", "root", "your_password_here");
            System.out.println("✅ Database Connected Successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Connection Failed: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            registerStudent();
        } else if (e.getSource() == clearButton) {
            clearForm();
        }
    }

    void registerStudent() {
        String name = nameField.getText();
        String ageText = ageField.getText();
        String gender = genderBox.getSelectedItem().toString();
        String email = emailField.getText();
        String course = courseBox.getSelectedItem().toString();

        // Basic input validation
        if (name.isEmpty() || ageText.isEmpty() || gender.equals("Select") || course.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields!");
            return;
        }

        try {
            int age = Integer.parseInt(ageText);

            pst = con.prepareStatement("INSERT INTO students(name, age, gender, email, course) VALUES(?, ?, ?, ?, ?)");
            pst.setString(1, name);
            pst.setInt(2, age);
            pst.setString(3, gender);
            pst.setString(4, email);
            pst.setString(5, course);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student Registered Successfully!");

            clearForm();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a number!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    void clearForm() {
        nameField.setText("");
        ageField.setText("");
        emailField.setText("");
        genderBox.setSelectedIndex(0);
        courseBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        new StudentRegistrationForm();
    }
}
