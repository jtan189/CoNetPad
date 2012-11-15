package org.ndacm.acmgroup.cnp;



public class CNPPrivateSession extends CNPSession {
	
	private String sessionPassword; // hash
	private String sessionSalt;
	
	public CNPPrivateSession(int sessionID, String sessionName, CNPServer server, Account sessionLeader, String pass, String salt) {
		super(sessionID, sessionName, server, sessionLeader);
		sessionPassword = pass;
		sessionSalt = salt;
	}

	public boolean passwordIsCorrect(String attempt) {
		// TODO implement
		return false;
	}
}