import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class DatabaseConnection {

    Statement stmt = null;
    Connection con = null;
    ResultSet rs = null;
    private static SecretKeySpec secretKey;
    private static byte[] key;

    public DatabaseConnection() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://lochnagar.abertay.ac.uk:3306/sql1505050","sql1505050","BtHm9qit9Hck");
            System.out.println("Connection Successful");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public String getPassword(int id) throws SQLException {
        stmt=con.createStatement();
        rs=stmt.executeQuery("select a_password from s_accounts where a_id = " + id + ";");
        rs.next();
        String p = rs.getString("a_password");
        rs.close();
        return p;
    }

    public ArrayList<String> getMessages() throws Exception {
        ArrayList<String> result = new ArrayList<String>();
        stmt=con.createStatement();
        rs=stmt.executeQuery("select * from s_messages order by m_date ASC;");
        while(rs.next()) {
            result.add(rs.getString("m_date") + " - " + rs.getString("m_sender") + " : " + decrypt(rs.getString("m_content"), "VeryEncryption"));
        }
        rs.close();
        return result;
    }

    public String getName(int id) throws SQLException {

        stmt=con.createStatement();
        rs=stmt.executeQuery("select a_forename, a_surname from s_accounts where a_id = " + id + ";");
        rs.next();
        String name = rs.getString("a_forename") + " " + rs.getString("a_surname");
        rs.close();
        return name;
    }

    public void sendMessage(String sender, String content) throws Exception {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        stmt=con.createStatement();
        stmt.executeUpdate("insert into s_messages (m_content, m_sender, m_date) values ('" + encrypt(content, "VeryEncryption") + "' , '" + sender + "' , '" + dtf.format(now) + "');");
        System.out.println("message Sent");

    }

    public static void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String strToEncrypt, String secret) throws Exception {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public String decrypt(String strToDecrypt, String secret) throws Exception {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}
