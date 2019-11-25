package com.company;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void display() {
        Message m = new Message(1, "Content", "Sender", "Date");
        String expected = "\nDate - Sender - Content";
        String actual = m.display();
        assertEquals(expected, actual);
    }
}