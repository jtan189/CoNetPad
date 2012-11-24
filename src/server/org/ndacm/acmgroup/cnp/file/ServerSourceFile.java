package org.ndacm.acmgroup.cnp.file;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ndacm.acmgroup.cnp.CNPServer;
import org.ndacm.acmgroup.cnp.network.CNPConnection;
import org.ndacm.acmgroup.cnp.task.DownloadFileTask;
import org.ndacm.acmgroup.cnp.task.EditorTask;
import org.ndacm.acmgroup.cnp.task.FileTask;
import org.ndacm.acmgroup.cnp.task.FileTaskExecutor;
import org.ndacm.acmgroup.cnp.task.SendResponseTask;
import org.ndacm.acmgroup.cnp.task.response.EditorTaskResponse;
import org.ndacm.acmgroup.cnp.task.response.TaskResponse;

/**
 * This is the class that deals with source files at the server end
 * @author Cesar Ramirez, Josh Tan
 * @version 1.5
 */
public class ServerSourceFile extends SourceFile implements FileTaskExecutor {

	private ExecutorService fileTaskCourier;				//This is used to store tasks to be process in queue
	private ExecutorService fileTaskQueue;					//This is the task queue
	private Map<Integer, CNPConnection> clientConnections; // clients that have this specific file open
	private CNPServer server;

	/**
	 * This is the default Constructor
	 * @param fileID			The unique ID for the file
	 * @param filename			The the unique file name w/o file type 
	 * @param type				The file type of the file
	 * @param initialText		The initial text or context of the file.
	 */
	public ServerSourceFile(int fileID, String filename, SourceType type, String initialText, CNPServer server) {
		super(fileID, filename, type, initialText);

		fileTaskCourier = Executors.newCachedThreadPool();
		fileTaskQueue = Executors.newSingleThreadExecutor();
		clientConnections = new ConcurrentHashMap<Integer, CNPConnection>();
		this.server = server;
	}

	/**
	 * This second constructor, this only creates a blank file
	 * @param fileID		The unique ID of the file
	 * @param filename		The name of the file w/o the type
	 * @param type			The type of file
	 */
	public ServerSourceFile(int fileID, String filename, SourceType type) {
		super(fileID, filename, type, "");
	}
	
	/**
	 * This adds a new fileTask to the queue	
	 * @param task		The FileTask to add to the queue
	 */
	public void submitTask(FileTask task) {
		fileTaskQueue.submit(task);
	}
	
	@Override
	public void executeTask(FileTask task) {
		executeTask(task);
		
	}	

	/**
	 * This executes an editorTask
	 * @param task		The editor task to execute
	 */
	public void executeTask(EditorTask task) {

		TaskResponse response = null;
		if (server.userIsAuth(task.getUserID(), task.getUserAuthToken())){
			editSource(task);

			// notify clients of edit
			response = new EditorTaskResponse(task.getUsername(), task.getKeyPressed(),
					task.getEditIndex(), task.getFileID(), true);
		} else {
			// user authentication failed
			response = new EditorTaskResponse("", -1, -1, -1, false);
		}
		distributeTask(response);
		
	}

	/**
	 * This executes a DownloadFileTask		[Not Implemented]
	 * @param task		The DownloadFile Task to execute
	 * @return			True if successful, false otherwise
	 */
	public boolean executeTask(DownloadFileTask task) {
		// TODO implement
		return false;
	}

	
	/**
	 * This edits the source code of the file
	 * @param task		The EditorTask that determines what needs to be edited
	 */
	public void editSource(EditorTask task) {
		editSource(task.getKeyPressed(), task.getEditIndex());
	}

	/**
	 * This distrubutes a response task between the different clients
	 * @param response		The task response to distribute to the clients
	 */
	public void distributeTask(TaskResponse response) {
		for (CNPConnection client : clientConnections.values()) {
			SendResponseTask fileTask = new SendResponseTask(response, client);
			fileTaskCourier.submit(fileTask);
		}
	}

	public void addFileTaskEventListener(int userID, CNPConnection connection) {
		clientConnections.put(userID, connection);
	}

	public void removeFileTaskEventListener(int userID) {
		clientConnections.remove(userID);
	}






}
