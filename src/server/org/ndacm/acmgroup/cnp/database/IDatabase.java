package org.ndacm.acmgroup.cnp.database;

import org.ndacm.acmgroup.cnp.Account;
import org.ndacm.acmgroup.cnp.server.CNPPrivateSession;
import org.ndacm.acmgroup.cnp.server.CNPSession;

public interface IDatabase {

	Account createAccount(String username, String email, String password);
	
	Account retrieveAccount(String username, String password);
	
	CNPSession createSession(Account sessionLeader);
	
	CNPPrivateSession createSession(Account sessionLeader, String sessionPassword);
	
	CNPSession retrieveSession(String sessionName);
	
	CNPPrivateSession retrieveSession(String sessionName, String sessionPassword);
	
	boolean sessionIsPrivate(String sessionName);
	
	boolean createSessionAccount(CNPSession session, Account account,
			Account.FilePermissionLevel filePermission, Account.ChatPermissionLevel chatPermission);
}
