// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 * 
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient {
	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF clientUI;

	String userName;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 * 
	 * @param host
	 *            The server to connect to.
	 * @param port
	 *            The port number to connect on.
	 * @param clientUI
	 *            The interface type variable.
	 */

	public ChatClient(String host, int port, String userId, ChatIF clientUI)
			throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		openConnection();

		userName = userId;

		clientUI.display("LOGGING IN AS " + userId);

		sendToServer("#login " + userId);
	}

	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 * 
	 * @param msg
	 *            The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		if(msg.toString().charAt(0) == '#')
		{
			if(msg.toString().indexOf("#sendfile") >= 0)
			{
				
			}
		}
		else
		{
			clientUI.display(msg.toString());
		}
		
		 
	}

	/**
	 * This method handles all data coming from the UI
	 * 
	 * Good place to trap client commands
	 * 
	 * @param message
	 *            The message from the UI.
	 */
	public void handleMessageFromClientUI(String message)
	{
		try {
			if (message.toString().charAt(0) == '#') {
				if (message.indexOf("#quit") >= 0) {
					sendToServer(message);
					quit();
				} else if (message.indexOf("#logoff") >= 0) {
					sendToServer(message);
					closeConnection();
				} else if (message.indexOf("#sethost") >= 0) {
					if (isConnected()) {
						clientUI.display("Cannot set host while connected to a server.");
					} else {
						String newHost = message.toString();
						int start = newHost.indexOf("#sethost") + 9;
						int end = newHost.length();

						newHost = newHost.substring(start, end);

						setHost(newHost);
						clientUI.display("The host is now " + getHost());
					}
				} else if (message.indexOf("#setport") >= 0) {
					if (isConnected()) {
						clientUI.display("Cannot set ports while connected to a server.");
					} else {
						String newPortm = message.toString();
						int newPort = 0;
						int start = newPortm.indexOf("#setport") + 9;
						int end = newPortm.length();

						newPortm = newPortm.substring(start, end);
						newPort = Integer.parseInt(newPortm);

						setPort(newPort);
						clientUI.display("The port is now " + getPort());
					}
				} else if (message.indexOf("#gethost") >= 0) {
					clientUI.display("The current host is " + getHost());
				} else if (message.indexOf("#getport") >= 0) {
					String portS = Integer.toString(getPort());
					clientUI.display("The current port is " + portS);
				} else if (message.indexOf("#changeuser") >= 0) {
					String newUser = message.toString();
					int start = newUser.indexOf("#changeuser") + 12;
					int end = newUser.length();

					newUser = newUser.substring(start, end);

					userName = newUser;
					clientUI.display("Your selected username is now "
							+ userName);
				} else if (message.indexOf("#connect") >= 0) {
					if (isConnected()) {
						clientUI.display("Cannot connect while already connected!");
					} else {
						try {
							openConnection();
							sendToServer("#login " + userName);
						} catch (IOException IOe) {
							clientUI.display("Cannot connect while already connected!");
						}
					}
				} else if (message.indexOf("#sendfile") >= 0) {
					String[] split = message.split(" ");
					String fullFilePath = split[2];
					
					File f = new File(fullFilePath);
					int size = (int)f.length();

					message = message +" "+ getPort()+1 + " " + size;
					sendToServer(message);
					FileTransfer ft = new FileTransfer(fullFilePath, getPort()+1);
					ft.start();
				} else {
					sendToServer(message);
				}
			} else {
				sendToServer(message);
			}
		} catch (IOException e) {
			clientUI.display("Could not send message to server.  Terminating client.");
			quit();
		}
	}

	/*private void sendFileToUser(String message) {
		// starts as e.g. "#sendfile Bob C:\|file.jpg"
		String[] split = message.split(" ");
		String targetUser = split[1];
		String fullFilePath = split[2];
		
		String filename = fullFilePath.substring(fullFilePath.lastIndexOf('\\') + 1);
		
		System.out.println(targetUser);
		System.out.println(fullFilePath);
		System.out.println(filename);
		
		try {
			sendToServer(sf);
		} catch (IOException ioe) {
			clientUI.display("" + ioe);
		}
		// currently the sending client will immediatly wait for a connection,
		// in the future add a timeout or wait for confirmation
		FileTransfer ft = new FileTransfer(dir, filename, filesize,
				getInetAddress().toString(), getPort() + 1, userName,
				targetUser);
		ft.start();
	}*/

	/**
	 * Client Command Methods
	 * 
	 * 
	 */

	protected void connectionClosed() {
		clientUI.display("The connection to the server has been closed.");
		// clientUI.display("Client will shut down.");
		// System.exit(0);
	}

	protected void connectionException(Exception exception) {
		clientUI.display("The connection to the server has been lost.");
		clientUI.display("Client will shut down.");
		System.exit(0);
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
// End of ChatClient class
