import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

public class code extends Frame {

    static class User {
        private final String username;
        private final String password;
        private final String role;

        User(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }

        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public String getRole() { return role; }
    }

    static class WelcomePage extends Frame {
        WelcomePage(User user) {
            super("Welcome");
            setLayout(new GridBagLayout());

            Panel content = new Panel(new GridLayout(2, 1, 0, 10));

            Label hello = new Label("Hello, " + user.getUsername(), Label.CENTER);
            hello.setFont(new Font("SansSerif", Font.BOLD, 20));

            Label roleLabel = new Label("Role: " + user.getRole(), Label.CENTER);
            roleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

            content.add(hello);
            content.add(roleLabel);
            add(content);

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    dispose();
                    System.exit(0);
                }
            });

            setSize(320, 180);
            setLocationRelativeTo(null);
        }
    }

    private static final int MAX_ATTEMPTS = 3;
    private static final int LOCK_SECONDS = 30;

    // list of users to seed
    private final List<User> users = new ArrayList<>();
    private int failedAttempts = 0;

    // two screens (login / sign up) inside the same window
    private final CardLayout cards = new CardLayout();
    private final Panel root = new Panel(cards);

    // login components
    private final TextField loginUser = new TextField(18);
    private final TextField loginPass = new TextField(18);
    private final Choice loginRole = new Choice();
    private final Label loginMessage = new Label("", Label.CENTER);
    private final Button loginBtn = new Button("Login");
    private final Button toSignUp = new Button("New here? Sign up");

    // sign up components
    private final TextField signUser = new TextField(18);
    private final TextField signPass = new TextField(18);
    private final TextField signPass2 = new TextField(18);
    private final Choice signRole = new Choice();
    private final Label signMessage = new Label("", Label.CENTER);
    private final Button signBtn = new Button("Sign up");
    private final Button toLogin = new Button("Already have an account? Login");

    // countdown
    private Timer lockTimer;
    private int secondsLeft;

    public code() {
        super("Login");
        loadUsers();

        setLayout(new GridBagLayout());
        root.add(buildLoginPanel(), "login");
        root.add(buildSignUpPanel(), "signup");
        add(root);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (lockTimer != null) lockTimer.stop();
                dispose();
                System.exit(0);
            }
        });
        setSize(460, 440);
        setLocationRelativeTo(null);
    }

    // ---------------- login screen ----------------
    private Panel buildLoginPanel() {
        Panel content = new Panel(new BorderLayout(10, 15));

        Label title = new Label("Welcome back", Label.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        content.add(title, BorderLayout.NORTH);

        Panel form = new Panel(new GridLayout(3, 2, 8, 8));
        form.add(new Label("Username:", Label.RIGHT));
        form.add(loginUser);

        form.add(new Label("Password:", Label.RIGHT));
        loginPass.setEchoChar('*');
        form.add(loginPass);

        form.add(new Label("Role:", Label.RIGHT));
        loginRole.add("Student");
        loginRole.add("TA");
        loginRole.add("Professor");
        form.add(loginRole);

        content.add(form, BorderLayout.CENTER);

        loginMessage.setForeground(Color.RED);
        Panel buttons = new Panel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        Button cancel = new Button("Cancel");
        buttons.add(cancel);
        buttons.add(loginBtn);

        Panel south = new Panel(new GridLayout(3, 1, 0, 8));
        south.add(loginMessage);
        south.add(buttons);
        south.add(toSignUp);
        content.add(south, BorderLayout.SOUTH);

        loginBtn.addActionListener(e -> doLogin());
        cancel.addActionListener(e -> {
            if (lockTimer != null) lockTimer.stop();
            dispose();
            System.exit(0);
        });
        toSignUp.addActionListener(e -> {
            clearSignUp();
            cards.show(root, "signup");
        });

        return content;
    }

    private void doLogin() {
        String name = loginUser.getText().trim();
        String pass = loginPass.getText();
        String selectedRole = loginRole.getSelectedItem();

        // 1. Validate empty fields
        if (name.isEmpty() && pass.isEmpty()) {
            showMessage(loginMessage, "Please enter your username and password.", Color.RED);
            return;
        }
        if (name.isEmpty()) {
            showMessage(loginMessage, "Please enter your username.", Color.RED);
            return;
        }
        if (pass.isEmpty()) {
            showMessage(loginMessage, "Please enter your password.", Color.RED);
            return;
        }

        // 2. Validate role and user existence
        User user = findUser(name);
        if (user == null || !user.getRole().equalsIgnoreCase(selectedRole)) {
            registerFailure("No " + selectedRole + " with this name.");
            return;
        }

        // 3. Validate password
        if (!user.getPassword().equals(pass)) {
            registerFailure("Wrong password.");
            return;
        }

        // Success
        failedAttempts = 0;
        new WelcomePage(user).setVisible(true);
        dispose();
    }

    // ---------------- sign up screen ----------------
    private Panel buildSignUpPanel() {
        Panel content = new Panel(new BorderLayout(10, 15));

        Label title = new Label("Create account", Label.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        content.add(title, BorderLayout.NORTH);

        Panel form = new Panel(new GridLayout(4, 2, 8, 8));
        form.add(new Label("Username:", Label.RIGHT));
        form.add(signUser);

        form.add(new Label("Password:", Label.RIGHT));
        signPass.setEchoChar('*');
        form.add(signPass);

        form.add(new Label("Confirm:", Label.RIGHT));
        signPass2.setEchoChar('*');
        form.add(signPass2);

        form.add(new Label("Role:", Label.RIGHT));
        signRole.add("Student");
        signRole.add("TA");
        signRole.add("Professor");
        form.add(signRole);

        content.add(form, BorderLayout.CENTER);

        signMessage.setForeground(Color.RED);
        Panel buttons = new Panel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        Button cancel = new Button("Cancel");
        buttons.add(cancel);
        buttons.add(signBtn);

        Panel south = new Panel(new GridLayout(3, 1, 0, 8));
        south.add(signMessage);
        south.add(buttons);
        south.add(toLogin);
        content.add(south, BorderLayout.SOUTH);

        signBtn.addActionListener(e -> doSignUp());
        cancel.addActionListener(e -> {
            clearSignUp();
            cards.show(root, "login");
        });
        toLogin.addActionListener(e -> {
            clearSignUp();
            cards.show(root, "login");
        });

        return content;
    }

    private void doSignUp() {
        String name = signUser.getText().trim();
        String pass = signPass.getText();
        String pass2 = signPass2.getText();

        if (name.isEmpty() || pass.isEmpty() || pass2.isEmpty()) {
            showMessage(signMessage, "Please fill in all fields.", Color.RED);
            return;
        }
        if (name.length() < 3) {
            showMessage(signMessage, "Username must be at least 3 characters.", Color.RED);
            return;
        }
        if (findUser(name) != null) {
            showMessage(signMessage, "Username '" + name + "' is already taken.", Color.RED);
            return;
        }
        if (pass.length() < 4) {
            showMessage(signMessage, "Password must be at least 4 characters.", Color.RED);
            return;
        }
        if (!pass.equals(pass2)) {
            showMessage(signMessage, "Passwords do not match.", Color.RED);
            return;
        }

        users.add(new User(name, pass, signRole.getSelectedItem()));

        clearSignUp();
        loginUser.setText("");
        loginPass.setText("");
        cards.show(root, "login");
        showMessage(loginMessage, "Account created for " + name + ". You can login now.", new Color(0, 128, 0));
    }

    private void clearSignUp() {
        signUser.setText("");
        signPass.setText("");
        signPass2.setText("");
        signRole.select(0);
        showMessage(signMessage, "", Color.RED);
    }

    // ---------------- lockout with countdown ----------------
    private void registerFailure(String reason) {
        failedAttempts++;
        if (failedAttempts >= MAX_ATTEMPTS) {
            startLock();
        } else {
            showMessage(loginMessage,
                    reason + " Attempts left: " + (MAX_ATTEMPTS - failedAttempts), Color.RED);
        }
    }

    private void startLock() {
        secondsLeft = LOCK_SECONDS;
        setLocked(true);
        showLockMessage();

        lockTimer = new Timer(1000, e -> {
            secondsLeft--;
            if (secondsLeft <= 0) {
                endLock();
            } else {
                showLockMessage();
            }
        });
        lockTimer.start();
    }

    private void endLock() {
        lockTimer.stop();
        failedAttempts = 0;
        setLocked(false);
        showMessage(loginMessage, "", Color.RED);
        showMessage(signMessage, "", Color.RED);
    }

    private void setLocked(boolean locked) {
        loginBtn.setEnabled(!locked);
        signBtn.setEnabled(!locked);
        toSignUp.setEnabled(!locked);
        toLogin.setEnabled(!locked);
    }

    private void showLockMessage() {
        String text = "Too many failed attempts. Try again in " + secondsLeft + "s";
        showMessage(loginMessage, text, Color.RED);
        showMessage(signMessage, text, Color.RED);
    }

    private void showMessage(Label label, String text, Color color) {
        label.setForeground(color);
        label.setText(text);
    }

    // ---------------- users ----------------
    private void loadUsers() {
        users.add(new User("ahmed", "1234", "Student"));
        users.add(new User("sara", "abcd", "TA"));
        users.add(new User("drmohamed", "pass123", "Professor"));
    }

    private User findUser(String name) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(name)) {
                return u;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        new code().setVisible(true);
    }
}
