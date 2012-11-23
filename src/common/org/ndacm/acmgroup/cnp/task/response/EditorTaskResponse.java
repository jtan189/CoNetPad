package org.ndacm.acmgroup.cnp.task.response;

import javax.swing.text.BadLocationException;

public class EditorTaskResponse extends TaskResponse {

	private String username;
	private int keyPressed;
	private int editIndex;
	private int fileID;

	public EditorTaskResponse(String username, int keyPressed, int editIndex,
			int fileID) {
		this.username = username;
		this.keyPressed = keyPressed;
		this.editIndex = editIndex;
		this.fileID = fileID;
	}

	public int getKeyPressed() {
		return keyPressed;
	}

	public int getEditIndex() {
		return editIndex;
	}

	public String getUsername() {
		return username;
	}

	public int getFileID() {
		return fileID;
	}


	@Override
	public void run() {
		try {
			client.executeTask(this);
		} catch (BadLocationException e) {
			// do something
		}

	}

}
