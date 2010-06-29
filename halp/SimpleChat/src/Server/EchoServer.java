// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 * 
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer {
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;

	ChatIF serverUI;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 * 
	 * @param port
	 *            The port number to connect on.
	 */
	public EchoServer(int port, ChatIF serverUI) {
		super(port);
		this.serverUI = serverUI;

		try {
			this.listen(); // Start listening for connections
		} catch (Exception ex) {
			serverUI.display("ERROR - Could not listen for clients!");
		}
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 * 
	 * @param msg
	 *            The message received from the client.
	 * @param client
	 *            The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if (msg.toString().charAt(0) == '#') {
			performClientCommand(msg, client);
		} else {

			if (client.getInfo("group") == null) {

				serverUI.display("Message received: " + msg + " from " + client);
				this.sendToAllClients(client.getInfo("userId") + ": " + msg);

			} else {
				serverUI.display("Message received: " + msg + " from " + client
						+ "in group " + client.getInfo("group"));
				this.sendToAllClientsInMyGroup(client.getInfo("group") + ">> "
						+ client.getInfo("userId") + ": " + msg, client
						.getInfo("group").toString());
			}

		}
	}

	public void handleMessageFromServerUI(String message) {
		// HANDLE LOCAL SERVER COMMANDS HERE

		if (message.toString().charAt(0) == '#') {
			if (message.toString().indexOf("#quit") >= 0) {
				try {
					sendToAllClients("SERVER MSG > The server is shutting down.");
				} catch (Exception e) {
					serverUI.display("Could not send message to server:"
							+ e.toString());
				}
				try {
					close();
					System.exit(0);
				} catch (IOException e) {
					serverUI.display("" + e);
				}

			} else if (message.toString().indexOf("#stop") >= 0) {
				serverUI.display("The server is no longer accepting new clients.");
				stopListening();
			} else if (message.toString().indexOf("#close") >= 0) {
				try {
					sendToAllClients("SERVER MSG > The server is shutting down.");
				} catch (Exception e) {
					serverUI.display("Could not send message to server:"
							+ e.toString());
				}
				try {
					close();
				} catch (IOException e) {
					serverUI.display("" + e);
				}
			} else if (message.toString().indexOf("#setport") >= 0) {
				if (!isListening()) {
					String newPortm = message.toString();
					int newPort = 0;
					int start = newPortm.indexOf("#setport") + 9;
					int end = newPortm.length();

					newPortm = newPortm.substring(start, end);
					newPort = Integer.parseInt(newPortm);

					setPort(newPort);
					serverUI.display("The port is now " + getPort());
				} else {
					serverUI.display("Server is already started.");
				}

			} else if (message.toString().indexOf("#start") >= 0) {
				if (!isListening()) {
					try {
						listen();
					} catch (IOException e) {
						serverUI.display("" + e);
					}

				} else {
					serverUI.display("Server is already started.");
				}
			} else if (message.toString().indexOf("#getport") >= 0) {
				String portS = Integer.toString(getPort());
				serverUI.display("The current port is " + portS);
			} else if (message.toString().indexOf("#setyell") >= 0) {
				String messageBody = message.toString();
				int start = messageBody.indexOf("#setyell") + 9;
				int end = messageBody.length();

				messageBody = messageBody.substring(start, end);

				int start2 = messageBody.indexOf(" ");
				String targetUser = messageBody.substring(0, start2);
				messageBody = messageBody.substring(start2 + 1,
						messageBody.length());

				if (messageBody.equals("on") || messageBody.equals("off")) {

					Thread[] clientThreadList = getClientConnections();
					for (int i = 0; i < clientThreadList.length; i++) {
						if (((ConnectionToClient) clientThreadList[i])
								.getInfo("userId") != null
								&& ((ConnectionToClient) clientThreadList[i])
										.getInfo("userId").equals(targetUser))
							try {
								((ConnectionToClient) clientThreadList[i])
										.sendToClient("Your ability to yell is now "
												+ messageBody);
								((ConnectionToClient) clientThreadList[i])
										.setInfo("canYell", messageBody);
							} catch (Exception ex) {
							}
					}

				}
			}
		} else {
			try {
				sendToAllClients("SERVER MSG > " + message);
				serverUI.display("SERVER MSG > " + message);
			} catch (Exception e) {
				serverUI.display("Could not send message to server:"
						+ e.toString());

			}
		}

	}

	/**************** Perform Command ***************************/

	private void performClientCommand(Object msg, ConnectionToClient client) {
		if (msg.toString().indexOf("#login") >= 0) {
			clientCommandLogin(msg, client);
		}
		if (msg.toString().indexOf("#quit") >= 0) {
			clientCommandQuit(msg, client);
		}
		if (msg.toString().indexOf("#logoff") >= 0) {
			clientCommandLogoff(msg, client);
		}
		if (msg.toString().indexOf("#join") >= 0) {
			clientCommandJoin(msg, client);
		}
		if (msg.toString().indexOf("#whisper") >= 0) {
			clientCommandWhisper(msg, client);
		}
		if (msg.toString().indexOf("#yell") >= 0) {
			clientCommandYell(msg, client);
		}
		if (msg.toString().indexOf("#isHere") >= 0) {
			clientCommandIsHere(msg, client);
		}
		if (msg.toString().indexOf("sendfile") >= 0) {
			clientCommandIsHere(msg, client);
		}

	}

	/**************** Start Client Commands *******************/
	private void clientCommandLogin(Object msg, ConnectionToClient client) {
		String userId = msg.toString();
		int start = userId.indexOf("#login") + 7;
		int end = userId.length();

		userId = userId.substring(start, end);

		if (client.getInfo("userId") == null) {
			client.setInfo("userId", userId);

			this.sendToAllClients(client.getInfo("userId")
					+ " has just logged in");
		} else {
			try {
				client.sendToClient("Cannot perform login while already logged in, please logout to change your username.");
			} catch (IOException IOe) {
				serverUI.display("" + IOe);
			}
		}

	}

	private void clientCommandQuit(Object msg, ConnectionToClient client) {
		if (client.getInfo("userId") != null) {
			this.sendToAllClients(client.getInfo("userId")
					+ " has disconnected.");
		} else {
			this.sendToAllClients(client + " has disconnected.");
		}
	}

	private void clientCommandLogoff(Object msg, ConnectionToClient client) {
		if (client.getInfo("userId") != null) {
			this.sendToAllClients(client.getInfo("userId") + " has logged off.");
		} else {
			this.sendToAllClients(client + " has logged off.");
		}
	}

	private void clientCommandJoin(Object msg, ConnectionToClient client) {

		String group = msg.toString();
		int start = group.indexOf("#join") + 6;
		int end = group.length();

		group = group.substring(start, end);

		client.setInfo("group", group);

		this.sendToAllClients(client.getInfo("userId")
				+ " has just joined group " + group);

	}

	private void clientCommandWhisper(Object msg, ConnectionToClient client) {

		String targetUser = msg.toString();
		int start = targetUser.indexOf("#whisper") + 9;
		int end = targetUser.length();
		targetUser = targetUser.substring(start, end);

		// grab the string after #whisper is removed
		String tempMsg = targetUser;
		// find the space after the first word, which will be the target
		// username
		int start2 = targetUser.indexOf(" ");
		// Grab the message text after the user name
		tempMsg = tempMsg.substring(start2, tempMsg.length());
		// grab the username from the start of the string up to the first space
		targetUser = targetUser.substring(0, start2);

		this.sendToAClient(client.getInfo("userId") + " whispers to you: "
				+ tempMsg, targetUser);

	}

	private void clientCommandYell(Object msg, ConnectionToClient client) {

		if (client.getInfo("canYell") != null
				&& client.getInfo("canYell").equals("on")) {
			String yell = msg.toString();
			int start = yell.indexOf("#yell") + 6;
			int end = yell.length();

			yell = yell.substring(start, end);

			this.sendToAllClients(client.getInfo("userId") + " yells: " + yell);
		} else {
			serverUI.display(">>" + client.getInfo("canYell") + "<<");
			try {
				client.sendToClient("You are not allowed to yell, please use your inside voice.");
			} catch (IOException IOe) {
				serverUI.display("" + IOe);
			}

		}

	}
	
	private void clientSendFile(Object msg, ConnectionToClient client)
	{
		String senderIP = client.getInetAddress().toString();
		String message = msg.toString();
		String[] split = message.split(" ");
		String targetUser = split[1];
		String fullFilePath = split[2];
		String filename = fullFilePath.substring(fullFilePath.lastIndexOf('\\') + 1);
		
		System.out.println(targetUser);
		System.out.println(fullFilePath);
		System.out.println(filename);
	}

	private void clientCommandIsHere(Object msg, ConnectionToClient client) {
		if (client.getInfo("group") != null) {
			Thread[] clientThreadList = getClientConnections();
			String targetUser = msg.toString();
			int start = targetUser.indexOf("#isHere") + 8;
			int end = targetUser.length();
			targetUser = targetUser.substring(start, end);

			for (int i = 0; i < clientThreadList.length; i++) {
				if (((ConnectionToClient) clientThreadList[i])
						.getInfo("userId") != null
						&& ((ConnectionToClient) clientThreadList[i]).getInfo(
								"userId").equals(targetUser))
					try {
						if (((ConnectionToClient) clientThreadList[i])
								.getInfo("group") != null
								&& ((ConnectionToClient) clientThreadList[i])
										.getInfo("group").equals(
												client.getInfo("group"))) {
							((ConnectionToClient) clientThreadList[i])
									.sendToClient(client.getInfo("userId")
											+ " is looking for you, run and hide.");
							try {
								client.sendToClient(targetUser + " is here.");
							} catch (IOException IOe) {
								serverUI.display("" + IOe);
							}
						}

					} catch (Exception ex) {
					}

			}
		} else {
			try {
				client.sendToClient("You are not in a group.");
			} catch (IOException IOe) {
				serverUI.display("" + IOe);
			}

		}

	}

	// ///////////////////////////////////////////////////////////

	public void sendToAllClientsInMyGroup(Object msg, String group) {
		Thread[] clientThreadList = getClientConnections();

		for (int i = 0; i < clientThreadList.length; i++) {

			if (((ConnectionToClient) clientThreadList[i]).getInfo("group") != null
					&& ((ConnectionToClient) clientThreadList[i]).getInfo(
							"group").equals(group))
				try {
					((ConnectionToClient) clientThreadList[i])
							.sendToClient(msg);
				} catch (Exception ex) {
				}
		}
	}

	public void sendToAClient(String msg, String targetUser) {
		Thread[] clientThreadList = getClientConnections();

		for (int i = 0; i < clientThreadList.length; i++) {
			if (((ConnectionToClient) clientThreadList[i]).getInfo("userId") != null
					&& ((ConnectionToClient) clientThreadList[i]).getInfo(
							"userId").equals(targetUser))
				try {
					((ConnectionToClient) clientThreadList[i])
							.sendToClient(msg);
				} catch (Exception ex) {
				}
		}
	}

	/*public void sendFileToAClient(FileTransferRequest req) {
		String targetUser = req.getTargetUser();

		Thread[] clientThreadList = getClientConnections();

		for (int i = 0; i < clientThreadList.length; i++) {
			if (((ConnectionToClient) clientThreadList[i]).getInfo("userId") != null
					&& ((ConnectionToClient) clientThreadList[i]).getInfo(
							"userId").equals(targetUser))
				try {
					((ConnectionToClient) clientThreadList[i])
							.sendToClient(req);
				} catch (Exception ex) {
				}
		}
	}*/

	/**************** End Client Commands *********************/

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		serverUI.display("Server listening for connections on port "
				+ getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * stops listening for connections.
	 */
	protected void serverStopped() {
		serverUI.display("Server has stopped listening for connections.");
	}

	protected void clientConnected(ConnectionToClient client) {
		serverUI.display(client + " connected.");
	}

	synchronized protected void clientDisconnected(ConnectionToClient client) {
		serverUI.display(client + " disconnected.");
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there
	 * is no UI in this phase).
	 * 
	 * @param args
	 *            [0] The port number to listen on. Defaults to 5555 if no
	 *            argument is entered.
	 */
	/*
	 * public static void main(String[] args) { int port = 0; //Port to listen
	 * on
	 * 
	 * try { port = Integer.parseInt(args[0]); //Get port from command line }
	 * catch(Throwable t) { port = DEFAULT_PORT; //Set port to 5555 }
	 * 
	 * EchoServer sv = new EchoServer(port, this);
	 * 
	 * try { sv.listen(); //Start listening for connections } catch (Exception
	 * ex) { System.out.println("ERROR - Could not listen for clients!"); } }
	 */
}
// End of EchoServer class
