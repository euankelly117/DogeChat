package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        //connectToServer();
        Socket s = null;
        ServerSocket ss = null;
        System.out.println("Waiting for Clients");
        try{
            ss = new ServerSocket(9991);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server Error");
        }

        while (true){
            try{
                s = ss.accept();
                System.out.println("Connection Established");
                ServerThread st = new ServerThread(s);
                st.start();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

	//This code is no longer used. It was the original server connection code, but is now reduntant since
	//the ServerSocket class is used to allow multiple clients simultaneously
    public static void connectToServer() {
        try (ServerSocket serverSocket = new ServerSocket(9991)) {
            Socket connectionSocket = serverSocket.accept();

            DatabaseConnection c = new DatabaseConnection();
            MessageArray messages = new MessageArray();
            messages.messages = c.getMessages();
            int MessageCount = messages.messages.size();
            int newMessages = MessageCount;

            InputStream inputToServer = connectionSocket.getInputStream();
            OutputStream outputFromServer = connectionSocket.getOutputStream();

            Scanner scanner = new Scanner(inputToServer, "UTF-8");
            PrintWriter serverPrintOut = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);

            serverPrintOut.println("Waiting for Messages");

            boolean done = false;

            while(true) {
                //updates the list of messages by polling the database
                messages.checkMessages();
                //if there is a difference, then there are new messages
                newMessages = messages.messages.size() - MessageCount;
                MessageCount = messages.messages.size();

                if (newMessages > 0) {
                    serverPrintOut.println("You have " + newMessages + " New Messages");
                    System.out.println("Message Sent");
                }
                newMessages = 0;
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
}
