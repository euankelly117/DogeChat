package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

public class ServerThread extends Thread {

    BufferedReader in = null;
    PrintWriter out = null;
    Socket s = null;
    DatabaseConnection c = new DatabaseConnection();
    MessageArray messages = new MessageArray();
    int MessageCount = 0;
    int newMessages = 0;


    public ServerThread(Socket s) throws SQLException {
        this.s = s;
        messages.messages = c.getMessages();
        int MessageCount = messages.messages.size();
        int newMessages = MessageCount;
    }

    public void run(){
        try{

            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            while(true) {
                //updates the list of messages by polling the database
                messages.checkMessages();
                //if there is a difference, then there are new messages
                newMessages = messages.messages.size() - MessageCount;
                MessageCount = messages.messages.size();
                if (newMessages > 0) {
                    out.println("You have " + newMessages + " New Messages");
                    System.out.println("Message Sent");
                } else {
                    out.println("nothing");
                }
                newMessages = 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        finally{
            try{
                System.out.println("Connection Closing..");
                if (in!=null){
                    in.close();
                    System.out.println(" Socket Input Stream Closed");
                }

                if(out!=null){
                    out.close();
                    System.out.println("Socket Out Closed");
                }
                if (s!=null){
                    s.close();
                    System.out.println("Socket Closed");
                }

            }
            catch(IOException ie){
                System.out.println("Socket Close Error");
            }
        }
    }
}
