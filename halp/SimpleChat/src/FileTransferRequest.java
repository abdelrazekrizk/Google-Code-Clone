import java.net.*;
import java.io.*;
import java.io.Serializable;

public class FileTransferRequest implements Serializable {
	String fileName;
	String hostIP;
	String sendingUserName;
	String targetUserName;
	int fileSize;
	int port;
	Socket clientSocket;
	File transferFile;

	public FileTransferRequest() {

	}

	public FileTransferRequest(String fileName, int fileSize, String hostIP,
			int port, String sendingUserName, String targetUserName) {
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.hostIP = hostIP;
		this.port = port;
		this.sendingUserName = sendingUserName;
		this.targetUserName = targetUserName;
	}

	public void acceptRequest() {
		FileReceive fr = new FileReceive(fileName, fileSize, hostIP, port);
		fr.start();
	}

	public String getTargetUser() {
		return targetUserName;
	}
}
