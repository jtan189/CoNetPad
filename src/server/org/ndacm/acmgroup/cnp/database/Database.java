package server.org.ndacm.acmgroup.cnp.database;


import java.sql.Connection;

import org.ndacm.acmgroup.cnp.Account;
import org.ndacm.acmgroup.cnp.server.CNPPrivateSession;
import org.ndacm.acmgroup.cnp.server.CNPSession;
import java.sql.*;

public class Database implements IDatabase{
	
	private static final String driverClass = "org.sqlite.JDBC";
	private Connection dbConnection;
	private String dbFile = "jdbc:sqlite:src//sqllite//CoNetPad.db3";
	private Statement stmt;
	
	
	public Database() throws Exception{
		// load the sqlite-JDBC driver using the current class loader
	     Class.forName(driverClass);
	     dbConnection = DriverManager.getConnection(dbFile);
	     stmt = dbConnection.createStatement();
//	 	ResultSet rs = st.executeQuery(sql);
//		while(rs.next())
//		{
//			String output = rs.getString("name");
//			System.out.println(output);
	}
	public static void main(String[] args)
	{
		
	}
	public Account createAccount(String username, String email, String password) throws SQLException {
		// TODO implement
		// also store in DB
		String query = "SELECT * FROM UserAccount WHERE Username='" + username + "'";
		ResultSet result =stmt.executeQuery(query);
		while(result.next())
		{
			String name = result.getString("Username");
			String eml = result.getString("Email");
			return new Account(name, eml);
		}
		return new Account();
	}
	
//	public Account retrieveAccount(String username, String password) {
//		// TODO implement
//		return new Account();
//	}
//	
//	public CNPSession createSession(Account sessionLeader) {
//		// TODO implement
//		return new CNPSession();
//	}
//	
//	public CNPPrivateSession createSession(Account sessionLeader, String sessionPassword) {
//		// TODO implement
//		return new CNPPrivateSession();
//	}
//	
//	public CNPSession retrieveSession(String sessionName) {
//		// TODO implement
//		return new CNPSession();
//	}
//	
//	public CNPPrivateSession retrieveSession(String sessionName, String sessionPassword) {
//		// TODO implement
//		return new CNPPrivateSession();
//	}
//	
//	public boolean sessionIsPrivate(String sessionName) {
//		// TODO implement
//		return false;
//	}
//	
//	public boolean createSessionAccount(CNPSession session, Account account,
//			Account.FilePermissionLevel filePermission, Account.ChatPermissionLevel chatPermission) {
//		// TODO implement
//		return false;
//	}

}
