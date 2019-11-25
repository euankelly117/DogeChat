import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatWindow {

    private Container contentPane;
    private int user_id = 0;
    private String user_name = null;
    DatabaseConnection c = null;
    private JButton b_send;
    private JTextField tf_message;
    private JTextPane tp_messages;
    private JPanel content_pane;
    private JButton b_refresh;
    private JLabel l_currentUser;

    public ChatWindow(Container contentPane){

        this.contentPane = contentPane;



    }

    public ChatWindow(int user, Container newContentPane) throws SQLException, IOException {
        c = new DatabaseConnection();
        user_id = user;
        user_name = c.getName(user_id);
        this.contentPane = newContentPane;
        this.contentPane = content_pane;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();



        JFrame frame = new JFrame("Doge Chat - " + user_name);
        frame.setContentPane(new ChatWindow(contentPane).contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        refreshMessages();

        l_currentUser.setText("You are logged in as " + user_name + ", log-in time: " + dtf.format(now));

        b_refresh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                refreshMessages();
            }
        });

        b_send.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (tf_message.getText().length() > 0) {
                    try {
                        c.sendMessage(user_name ,tf_message.getText());
                    } catch (SQLException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    refreshMessages();
                    tf_message.setText("");
                }
            }
        });

        b_refresh.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                refreshMessages();
            }
        });

        Thread t = new Thread() {
            public void run(){
                Socket socket = null;
                Scanner in = null;
                try {
                    socket = new Socket("localhost", 9991);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    in = new Scanner(socket.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String line = null;
                Sound s = new Sound();


                while(true){
                    line = in.nextLine();
                    if (!line.equalsIgnoreCase("nothing")){
                        System.out.println(line);
                        System.out.println("message Received");
                        refreshMessages();
                        s.play("E:/Java Projects/DogeChat/src/sounds/gabe-bark.wav");
                    }
                }
            }
        };
        t.start();
    }


    public void refreshMessages(){
        ArrayList<String> messages = null;
        tp_messages.setText("");
        try {
            messages = c.getMessages();
        } catch (SQLException e1){
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String i : messages) {
            tp_messages.setText(tp_messages.getText() + "\n" + i);
        }
    }


}
