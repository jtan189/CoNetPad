package org.ndacm.acmgroup.cnp.database;

import java.sql.SQLException;

import org.ndacm.acmgroup.cnp.Account;
import org.ndacm.acmgroup.cnp.CNPServer;
import org.ndacm.acmgroup.cnp.CNPSession;
import org.ndacm.acmgroup.cnp.exceptions.FailedAccountException;
import org.ndacm.acmgroup.cnp.exceptions.FailedSessionException;

/**
 * Interface for the Database.
 *
 */
public interface IDatabase {

	/**
	 * This creates a new account.
	 * 
	 * @param username		The username of the new account
	 * @param email			The email address of the new account
	 * @param password		The un-encrypted password
	 * @return				The newly created account object
	 * @throws SQLException
	 * @throws FailedAccountException
	 */
	Account createAccount(String username, String email, String password)  throws SQLException, FailedAccountException;
	/**
	 * This retrieves an account.
	 * 
	 * @param username		The username of the account
	 * @param password		The un-encrpyted password
	 * @return				The account object
	 * @throws SQLException
	 * @throws FailedAccountException
	 */
	Account retrieveAccount(String username, String password) throws SQLException, FailedAccountException;

	/**
	 * This creates a public session.
	 * 
	 * @param sessionLeader			The database ID of the session leader
	 * @param server				The CNPServer
	 * @return						The Session object
	 * @throws SQLException
	 * @throws FailedSessionException
	 */
	CNPSession createSession(int sessionLeader, CNPServer server) throws SQLException, FailedSessionException;

	/**
	 * This creates a private Session.
	 * 
	 * @param sessionLeader			The database ID of the leader
	 * @param server				The CNPServer
	 * @param sessionPassword		The un-encrypted password of the session
	 * @return						The session object
	 * @throws SQLException
	 * @throws FailedSessionException
	 */
	CNPSession createSession(int sessionLeader, CNPServer server, String sessionPassword) throws SQLException, FailedSessionException;

	/**
	 * This retrieves a public session.
	 * 
	 * @param sessionName		The unique name of the session
	 * @param server			The CNPServer 
	 * @return					The public session object
	 * @throws SQLException
	 * @throws FailedSessionException
	 * @throws FailedAccountException
	 */
	CNPSession retrieveSession(String sessionName, CNPServer server)throws SQLException, FailedSessionException, FailedAccountException;

	/**
	 * This retrieves a private session.
	 * 
	 * @param sessionName			The unique session name
	 * @param server				The CNPServer 
	 * @param sessionPassword		The Un-encrypted password of the Session
	 * @return						The Private sesion object
	 * @throws SQLException
	 * @throws FailedSessionException
	 * @throws FailedAccountException
	 */
	CNPSession retrieveSession(String sessionName, CNPServer server, String sessionPassword)throws SQLException, FailedSessionException, FailedAccountException;

	/**
	 * This determines if a given session is private or not.
	 * 
	 * @param sessionName	The unique session name
	 * @return				True if it is private, false if its not
	 * @throws SQLException
	 */
	boolean sessionIsPrivate(String sessionName) throws SQLException ;

	/**
	 * This attaches a user to a session.
	 * 
	 * @param session			The session object
	 * @param account			The account object
	 * @param filePermission	The file permission of the user
	 * @param chatPermission	The chat permission of the user
	 */
	void createSessionAccount(CNPSession session, Account account,
			Account.FilePermissionLevel filePermission, Account.ChatPermissionLevel chatPermission) throws SQLException;

	/**
	 * This attaches a user to a private session.
	 * 
	 * @param session				The private session for the user to join
	 * @param account				The account of the user
	 * @param password				The password of the session.  Un-Encrypted.
	 * @param filePermission		The file permission of the user
	 * @param chatPermission		The chat permission of the user
	 * @throws SQLException
	 * @throws FailedSessionException 
	 */
	void createSessionAccount(CNPSession session, Account account, String password,
			Account.FilePermissionLevel filePermission, Account.ChatPermissionLevel chatPermission) throws SQLException, FailedSessionException;

	/**
	 * This deletes a public session.
	 * 
	 * @param session		The public session
	 * @throws SQLException
	 */
	void deleteSession(CNPSession session)throws SQLException;

	/**
	 * This deletes the given account.
	 * 
	 * @param account				The account to delete
	 * @throws SQLException
	 * @throws FailedAccountException
	 */
	void deleteAccount(Account account) throws SQLException, FailedAccountException;

}
