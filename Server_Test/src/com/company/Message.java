package com.company;

public class Message {

    int ID;
    String Content;
    String Sender;
    String Date;

    public Message(){
        ID = 0;
        Content = "ERROR";
        Sender = "ERROR";
        Date = null;
    }

    public Message(int id, String content, String sender, String date){
        ID = id;
        Content = content;
        Sender = sender;
        Date = date;
    }

    public String display(){
        String message = "";
        message += "\n";
        message += Date + " - ";
        message += Sender + " - ";
        message += Content;
        return message;
    }
}
