package com.company;

import java.sql.SQLException;
import java.util.ArrayList;

public class MessageArray {

    DatabaseConnection c = new DatabaseConnection();
    public ArrayList<Message> messages = new ArrayList<Message>();

    MessageArray() throws SQLException {
        messages = c.getMessages();
    }

    public int checkMessages() throws SQLException {
        messages = c.getMessages();
        return messages.size();
    }
}
