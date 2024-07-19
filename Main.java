import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Calendar;

class RegistrationForm {
    private JFrame frame1, frame2;
    private JTextField nameField, contactField, addressField;
    private JComboBox<String> dayBox, monthBox, yearBox;
    private JRadioButton maleButton, femaleButton;
    private JLabel displayLabel;
    private String name, dob, gender, contact, address;
    private Connection connection;

    public RegistrationForm() {
        initializeDatabaseConnection();
        createFirstForm();
    }

    private void initializeDatabaseConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/registration";
            String username = "root";
            String password = "mbuva__17";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createFirstForm() {
        frame1 = new JFrame("Registration Form");
        frame1.setSize(300, 400);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setLayout(new GridLayout(8, 2));

        frame1.add(new JLabel("Name:"));
        nameField = new JTextField();
        frame1.add(nameField);

        frame1.add(new JLabel("Date of Birth:"));
        JPanel dobPanel = new JPanel();
        dayBox = new JComboBox<>(createNumberArray(1, 31));
        monthBox = new JComboBox<>(new String[]{"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"});
        yearBox = new JComboBox<>(createNumberArray(1950, 2024));
        dobPanel.add(dayBox);
        dobPanel.add(monthBox);
        dobPanel.add(yearBox);
        frame1.add(dobPanel);

        frame1.add(new JLabel("Gender:"));
        JPanel genderPanel = new JPanel();
        ButtonGroup genderGroup = new ButtonGroup();
        maleButton = new JRadioButton("Male");
        femaleButton = new JRadioButton("Female");
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);
        frame1.add(genderPanel);

        frame1.add(new JLabel("Contact:"));
        contactField = new JTextField();
        frame1.add(contactField);

        frame1.add(new JLabel("Address:"));
        addressField = new JTextField();
        frame1.add(addressField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitForm();
            }
        });
        frame1.add(submitButton);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetForm();
            }
        });
        frame1.add(resetButton);

        frame1.setVisible(true);
    }

    private String[] createNumberArray(int start, int end) {
        String[] numbers = new String[end - start + 1];
        for (int i = start; i <= end; i++) {
            numbers[i - start] = String.valueOf(i);
        }
        return numbers;
    }

    private void submitForm() {
        name = nameField.getText();
        dob = yearBox.getSelectedItem() + "-" + getMonthNumber((String) monthBox.getSelectedItem()) + "-" + dayBox.getSelectedItem();
        gender = maleButton.isSelected() ? "Male" : "Female";
        contact = contactField.getText();
        address = addressField.getText();

        frame1.setVisible(false);
        createSecondForm();
    }

    private int getMonthNumber(String monthName) {
        switch (monthName) {
            case "January":
                return 1;
            case "February":
                return 2;
            case "March":
                return 3;
            case "April":
                return 4;
            case "May":
                return 5;
            case "June":
                return 6;
            case "July":
                return 7;
            case "August":
                return 8;
            case "September":
                return 9;
            case "October":
                return 10;
            case "November":
                return 11;
            case "December":
                return 12;
            default:
                return 1; // default to January if not found (though should not happen with JComboBox)
        }
    }

    private void resetForm() {
        nameField.setText("");
        dayBox.setSelectedIndex(0);
        monthBox.setSelectedIndex(0);
        yearBox.setSelectedIndex(0);
        maleButton.setSelected(false);
        femaleButton.setSelected(false);
        contactField.setText("");
        addressField.setText("");
    }

    private void createSecondForm() {
        frame2 = new JFrame("Confirm Details");
        frame2.setSize(300, 400);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setLayout(new GridLayout(7, 1));

        displayLabel = new JLabel("<html>Name: " + name + "<br/>DOB: " + dob + "<br/>Gender: " + gender +
                "<br/>Contact: " + contact + "<br/>Address: " + address + "</html>");
        frame2.add(displayLabel);

        JCheckBox termsCheckBox = new JCheckBox("Accept terms and conditions");
        frame2.add(termsCheckBox);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (termsCheckBox.isSelected()) {
                    registerUser();
                } else {
                    JOptionPane.showMessageDialog(frame2, "Please accept the terms and conditions to register.");
                }
            }
        });
        frame2.add(registerButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        frame2.add(exitButton);

        frame2.setVisible(true);
    }

    private void registerUser() {
        try {
            String query = "INSERT INTO register (name, dob, gender, contact, address) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, dob);
            preparedStatement.setString(3, gender);
            preparedStatement.setString(4, contact);
            preparedStatement.setString(5, address);
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(frame2, "User registered successfully!");
            frame2.setVisible(false);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame2, "Error registering user.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RegistrationForm();
            }
        });
    }
}
