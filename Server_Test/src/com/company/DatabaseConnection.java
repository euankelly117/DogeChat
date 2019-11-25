package com.company;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DatabaseConnection {
	
	Statement stmt = null;
	Connection con = null;
	ResultSet rs = null;
	
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
		rs=stmt.executeQuery("select a_password from s_accounts where a_id = " + id);
		rs.next();
		String p = rs.getString("a_password");
		return p;
	}
	
	public ArrayList<Message> getMessages() throws SQLException {
		ArrayList<Message> result = new ArrayList<Message>();
		Message m = new Message();
		stmt=con.createStatement();  
		rs=stmt.executeQuery("select * from s_messages");
		while(rs.next()) {
			m= new Message(rs.getInt("m_id"), rs.getString("m_content"), rs.getString("m_sender"), rs.getTimestamp("m_date").toString());
			result.add(m);
		}
		
		return result;
	}
	
	public String getName(int id) throws SQLException {
		
		stmt=con.createStatement();
		rs=stmt.executeQuery("select a_forename, a_surname from s_accounts where a_id = " + id);
		rs.next();
		String name = rs.getString("a_forename") + " " + rs.getString("a_surname");
		return name;
	}
	
	public void sendMessage(String sender, String content) throws SQLException {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		
		stmt=con.createStatement();
		stmt.executeUpdate("insert into s_messages (m_content, m_sender, m_date) values ('" + content + "' , '" + sender + "' , '" + dtf.format(now) + "' ) ");
		System.out.println("message Sent");
		
	}
}
