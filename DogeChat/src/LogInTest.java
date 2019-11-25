import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogInTest {

    @Test
    void hashPassword() {
        LogIn l = new LogIn();
        String oldPassword = "password";
        String expected = "5f4dcc3b5aa765d61d8327deb882cf99";
        String actual = l.hashPassword(oldPassword);
        assertEquals(expected, actual);
    }
}