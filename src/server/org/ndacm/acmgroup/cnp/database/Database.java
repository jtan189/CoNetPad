package org.ndacm.acmgroup.cnp.database;


import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.ndacm.acmgroup.cnp.Account;
import org.ndacm.acmgroup.cnp.exceptions.FailedAccountException;

/**
 * Class:  Database<br>
 * Description:  This is a class for handling our database stuff.  
 * 
 */
public class Database implements IDatabase{

	private static final String DRIVER_CLASS = "org.sqlite.JDBC";
	private static final String ENCRYPTION_ALGORITHM = "PBKDF2WithHmacSHA1";
	private static final String DB_FILE = "jdbc:sqlite:src//sqllite//CoNetPad.db3";

	private Connection dbConnection;
	private Random random;

	/**
	 * Default Constructor
	 * @throws SQLException 
	 * @throws Exception
	 */
	public Database() throws ClassNotFoundException, SQLException
	{
		Class.forName(DRIVER_CLASS);
		dbConnection = DriverManager.getConnection(DB_FILE);
		random = new Random();

	}
	/**
	 * createAccount()
	 * This Creates a new user account and returns an object
	 * @param username - String The string username you wish to use to create new account
	 * @param email - String The password of the new account
	 * @param password - String The RAW password to be given.  Encrpytion is done for you.
	 * @return Returns an new Account Object or throws an FailedAccountException
	 * @throws FailedAccountException 
	 */
	public Account createAccount(String username, String email, String password) throws FailedAccountException {

		Account newAccount = null;

		// salt and hash password
		// http://stackoverflow.com/questions/2860943/suggestions-for-library-to-hash-passwords-in-java
		// http://stackoverflow.com/questions/5499924/convert-java-string-to-byte-array
		String hashString = null, saltString = null;
		byte[] salt = new byte[16];
		random.nextBytes(salt);

		try {
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 2048, 160);
			SecretKeyFactory f = SecretKeyFactory.getInstance(ENCRYPTION_ALGORITHM);
			hashString = new String(f.generateSecret(spec).getEncoded());
			saltString = new String(salt, "ISO-8859-1");

			//            // test if username/email already exists
			//            if (UserDAO.userExists(username, conn)){
			//                request.setAttribute("errorRegister", "Username already taken.");
			//                throw new Exception();
			//            } else if (UserDAO.emailExists(email, conn)){
			//                request.setAttribute("errorRegister", "An account registered to this email already exists.");
			//                throw new Exception();
			//            }

			// insert user into DB
			PreparedStatement registerUser = null;
			String insertion = "INSERT INTO UserAccount (Username, AccountPassword, AccountSalt, Email) "
					+ "VALUES (? , ?, ?, ?)";

			registerUser = dbConnection.prepareStatement(insertion);
			registerUser.setString(1, username);
			registerUser.setString(2, hashString);
			registerUser.setString(3, saltString);
			registerUser.setString(4, email);

			registerUser.executeUpdate();

			// return the account that was just inserted
			newAccount = retrieveAccount(username, password);

			registerUser.close();
			dbConnection.close();

		} catch (NoSuchAlgorithmException ex) {
			System.err.println("Invalid Encrpytion Algorithm: " + ENCRYPTION_ALGORITHM);
			throw new FailedAccountException("Error creating account for " + username);
		} catch (InvalidKeySpecException e) {
			System.err.println("Invalid key spec.");
			throw new FailedAccountException("Error creating account for " + username);
		} catch (UnsupportedEncodingException e) {
			System.err.println("Unsupported encoding.");
			throw new FailedAccountException("Error creating account for " + username);
		} catch (SQLException e) {
			System.err.println("SQL error.");
			throw new FailedAccountException("Error creating account for " + username);
		}

		if (newAccount != null) {
			return newAccount;
		} else {
			throw new FailedAccountException("Error creating account for " + username);
		}


	}
	/**
	 * retrieveAccount()
	 * Gets an existing account from the database and returns it into an Account Object
	 * @param username The username you wish to try and get
	 * @param password The password you wish to verify with.  Make sure its RAW and not encrypted.
	 * @return Account The account object of the the user account
	 * @throws FailedAccountException
	 */
	public Account retrieveAccount(String username, String password) throws FailedAccountException {

		// we should probably salt password too
		//		String query = null;
		//		try
		//		{
		//		    String encryptPass = sha1(password);
		//			query = "SELECT Username, Email FROM UserAccount WHERE Username='" + username + "' AND  AccountPassword='"
		//						+ encryptPass + "';";
		//			ResultSet rs = stmt.executeQuery(query);
		//			while(rs.next())
		//			{
		//				String uname = rs.getString("Username");
		//				String email = rs.getString("Email");
		//				return new Account(uname, email, 1); // TODO fix
		//			}
		//			throw new FailedAccountException("No Account Found");
		//			
		//		}
		//		catch(NoSuchAlgorithmException e)
		//		{
		//			throw new FailedAccountException("Encrpytion failed");
		//		}
		//		catch(SQLException e)
		//		{
		//			throw new FailedAccountException("SQL Error");
		//		}


		PreparedStatement retrieveAccount = null;
		ResultSet rset = null;
		Account accountRetrieved = null;
		String saltRetrieved = null;
		String hashRetrieved = null;

		String query = "SELECT * "
				+ "FROM UserAccount "
				+ "WHERE username = ?";

		try {
			// retrieve user with given username
			retrieveAccount = dbConnection.prepareStatement(query);
			retrieveAccount.setString(1, username);

			//run the query, return a result set        
			rset = retrieveAccount.executeQuery();

			int idRetrieved = rset.getInt("UserID");
			String nameRetrieved = rset.getString("UserName");
			String emailRetrieved = rset.getString("Email");
			accountRetrieved = new Account(idRetrieved, nameRetrieved, emailRetrieved);

			hashRetrieved = rset.getString("AccountPassword");
			saltRetrieved = rset.getString("AccountSalt");

			//clean up database classes
			retrieveAccount.close();
			rset.close();

		} catch (SQLException ex) {
			throw new FailedAccountException("Error retrieving account for " + username);
		}

		String hashSupplied = null;

		// generate hash for the password string supplied (using the salt from userRetrieved)
		try {
			byte[] salt = saltRetrieved.getBytes("ISO-8859-1");
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 2048, 160);
			SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			hashSupplied = new String(f.generateSecret(spec).getEncoded());
		} catch (NoSuchAlgorithmException ex) {
			System.err.println("Invalid Encrpytion Algorithm: " + ENCRYPTION_ALGORITHM);
			throw new FailedAccountException("Error retrieving account for " + username);
		} catch (UnsupportedEncodingException ex) {
			System.err.println("Unsupported encoding.");
			throw new FailedAccountException("Error retrieving account for " + username);
		} catch (InvalidKeySpecException ex) {
			System.err.println("Invalid key spec.");
			throw new FailedAccountException("Error retrieving account for " + username);
		}

		// check if hashes match. if so, return account.
		if (hashSupplied.equals(hashRetrieved)) {
			return accountRetrieved;
		} else {
			throw new FailedAccountException("Incorrect password supplied for " + username);
		}

	}

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

	// if we use this, we should salt the input before hashing
	//	/**
	//	 * sha1()
	//	 * This returns the string version of the SHA1 Encryption
	//	 * @param input This is the string you wish to get the SHA1 Hash
	//	 * @return The encrypted value
	//	 * @throws NoSuchAlgorithmException
	//	 */
	//   private static String sha1(String input) throws NoSuchAlgorithmException
	//   {
	//		MessageDigest md = MessageDigest.getInstance("SHA1");
	//		md.update(input.getBytes()); 
	//		byte[] b = md.digest();
	//		char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
	//		 '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	//		StringBuffer buf = new StringBuffer();
	//		for (int j=0; j<b.length; j++) 
	//		{
	//			buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
	//			buf.append(hexDigit[b[j] & 0x0f]);
	//		}
	//		  return buf.toString();
	//	}

}


