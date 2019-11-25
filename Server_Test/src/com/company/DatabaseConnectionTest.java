package com.company;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {

    @org.junit.jupiter.api.Test
    void getPassword() throws SQLException {
        DatabaseConnection c = new DatabaseConnection();
        String expected = "5f4dcc3b5aa765d61d8327deb882cf99";
        String actual = c.getPassword(1);
        assertEquals(expected, actual);
    }

    @org.junit.jupiter.api.Test
    void getMessages() {
        DatabaseConnection c = new DatabaseConnection();
        ArrayList<Message> actual = new ArrayList<Message>();
        assertNotNull(actual);
    }

    @org.junit.jupiter.api.Test
    void getName() throws SQLException {
        DatabaseConnection c = new DatabaseConnection();
        String expected = "Euan Kelly";
        String actual = c.getName(1);
        assertEquals(expected, actual);
    }

    @org.junit.jupiter.api.Test
    void sendMessage() throws SQLException {
        DatabaseConnection c = new DatabaseConnection();
        int originalCount = c.getMessages().size();
        int expectedNewCount = c.getMessages().size() + 1;
        c.sendMessage("AutomatedMessage", "This is part of a unit test, please ignore");
        int actualNewCount = c.getMessages().size();
        assertEquals(expectedNewCount, actualNewCount);
    }
}