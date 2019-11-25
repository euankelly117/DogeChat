import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Driver;

public class LogIn {
    private JPanel panel1;
    private JTextField tf_user;
    private JButton b_send;
    private JPasswordField tf_password;
    private JLabel l_user;
    private JLabel l_password;
    private JLabel l_dogeChat;
    private JPanel p_doge;
    private DatabaseConnection c = new DatabaseConnection();

    public LogIn() {
        //When the Log in button is clicked
        b_send.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent f) {

                if (!tf_user.getText().equals("") && !tf_password.getText().equals("")) {
                    super.mouseClicked(f);
                    //retrieve username and password, then hash the password
                    int user = Integer.parseInt(tf_user.getText());
                    String pass = tf_password.getText();
                    pass = hashPassword(pass);
                    try {
                        String actual_password = c.getPassword(user);
                        //compares passwords
                        if (pass.equals(actual_password)) {
                            System.out.println("Opening DogeChat...");
                            ChatWindow w = new ChatWindow(user, new Container());

                            //For Testing purposes, the login window must remain visible to open multiple chat windows. Before
                            //Final deployment, this line will be feature to close the login window.

                            //panel1.setVisible(false);
                        }
                    } catch (SQLException | IOException e) {
                        System.out.println(e);
                    }
                }
            }
        });
    }

    public String hashPassword(String oldPassword) {
        String passwordToHash = oldPassword;
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //Convert from Decimal to Hexidecimal
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LogIn");
        frame.setContentPane(new LogIn().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
