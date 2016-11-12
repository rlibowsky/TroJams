package frames;

import resources.AppearanceConstants;
import resources.AppearanceSettings;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class LoginScreenWindow extends JFrame {

    private JPanel mainPanel, northPanel, centerPanel, southPanel, usernamePanel,
            passwordPanel, alertPanel, instructionsPanel, trojamsPanel;
    private JLabel headerLabel, alertLabel1, alertLabel2, instructionsLabel, trojamsLabel;
    private JTextField usernameTextField, passwordTextField;
    private JButton loginButton, guestButton, createAccountButton;

    public LoginScreenWindow() {
        super("Login Screen");

        initializeComponents();
        createGUI();
        addListeners();
    }

    private void initializeComponents() {
        mainPanel = new JPanel();
        loginButton = new JButton("Login");
        guestButton = new JButton("Guest User");
        createAccountButton = new JButton("Create Account");
    }

    private void createGUI() {
        createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
        setSize(600, 600);
        setLocation(300, 100);
    }

    private JPanel createNorthPanel() {
        instructionsLabel = new JLabel("login or create an account to play");
        trojamsLabel = new JLabel("Jeopardy!");

        northPanel = new JPanel();
        instructionsPanel = new JPanel();
        trojamsPanel = new JPanel();

        AppearanceSettings.setBackground(AppearanceConstants.trojamPurple, instructionsPanel, trojamsPanel);
        AppearanceSettings.setFont(AppearanceConstants.fontSmall, instructionsLabel);
        AppearanceSettings.setTextAlignment(instructionsLabel, trojamsLabel);
        AppearanceSettings.setSize(600, 50, instructionsPanel, trojamsPanel);
        trojamsLabel.setFont(AppearanceConstants.fontLarge);

        instructionsPanel.add(instructionsLabel);
        trojamsPanel.add(trojamsLabel);
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.add(instructionsPanel);
        northPanel.add(trojamsPanel);

        return northPanel;
        /*headerLabel = new JLabel("Trojams");
        //AppearanceSettings.setSize(600, 50, northPanel);
        northPanel = new JPanel();
        northPanel.add(headerLabel);

        return northPanel;*/
    }

    private JPanel createCenterPanel() {
        centerPanel = new JPanel();
        AppearanceSettings.setSize(600, 200, centerPanel);

        alertPanel = new JPanel();
        alertPanel.setPreferredSize(new Dimension(600, 100));
        alertPanel.setLayout(new BoxLayout(alertPanel, BoxLayout.Y_AXIS));

        JPanel alertPanel1 = new JPanel();
        alertPanel1.setPreferredSize(new Dimension(300, 50));
        alertLabel1 = new JLabel("this password and user combination does not exist");
        alertLabel1.setHorizontalAlignment(JLabel.CENTER);
        alertPanel1.add(alertLabel1);
        JPanel alertPanel2 = new JPanel();
        alertPanel2.setPreferredSize(new Dimension(300, 50));
        alertLabel2 = new JLabel("this username already exists");
        alertLabel2.setHorizontalAlignment(JLabel.CENTER);
        //alertLabel2.setPreferredSize(new Dimension(300, 50));
        alertPanel2.add(alertLabel2);
        AppearanceSettings.setBackground(AppearanceConstants.trojamPurple, alertPanel1, alertPanel2);
        AppearanceSettings.setFont(AppearanceConstants.fontSmallest, alertLabel1, alertLabel2);
        alertLabel1.setVisible(false);
        alertLabel2.setVisible(false);

        alertPanel.add(alertPanel1);
        alertPanel.add(alertPanel2);

        usernamePanel = new JPanel();
        usernamePanel.setPreferredSize(new Dimension(400, 70));
        //usernameTextField = new JTextField("username");
        usernameTextField = new JTextField();
        usernameTextField.setPreferredSize(new Dimension(400, 70));
        usernamePanel.add(usernameTextField);

        passwordPanel = new JPanel();
        passwordPanel.setPreferredSize(new Dimension(400, 70));
        //passwordTextField = new JTextField("password");
        passwordTextField = new JTextField();
        passwordTextField.setPreferredSize(new Dimension(400, 70));
        passwordPanel.add(passwordTextField);

        AppearanceSettings.setBackground(AppearanceConstants.trojamPurple, alertPanel, centerPanel,
                usernamePanel, passwordPanel);
        centerPanel.add(alertPanel);
        centerPanel.add(usernamePanel);
        centerPanel.add(passwordPanel);

        return centerPanel;
    }

    private JPanel createSouthPanel() {
        southPanel = new JPanel();
        southPanel.setBackground(AppearanceConstants.trojamPurple);
        southPanel.setPreferredSize(new Dimension(600, 100));
        loginButton.setEnabled(false);
        createAccountButton.setEnabled(false);

        southPanel.add(loginButton);
        southPanel.add(guestButton);
        southPanel.add(createAccountButton);

        return southPanel;
    }

    private void createMainPanel() {
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(createNorthPanel(), BorderLayout.NORTH);
        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);
        mainPanel.add(createSouthPanel(), BorderLayout.SOUTH);
    }

    private void addListeners() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //terminates program

        usernameTextField.getDocument().addDocumentListener(new MyDocumentListener());
        passwordTextField.getDocument().addDocumentListener(new MyDocumentListener());
    }

    private class MyDocumentListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {

        }

        @Override
        public void removeUpdate(DocumentEvent e) {

        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }
    }

    public static void main(String [] args) {
        LoginScreenWindow lsw = new LoginScreenWindow();
        lsw.setVisible(true);
    }
}