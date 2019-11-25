package com.company;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class MessageArrayTest {

    @Test
    void checkMessages() throws SQLException {
        MessageArray ma = new MessageArray();
        int expected = ma.messages.size();
        int actual = ma.checkMessages();
        assertEquals(expected, actual);
    }
}