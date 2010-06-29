import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ServerConsole extends JFrame implements ChatIF {
	// Interface Variables
	private JButton sendB = new JButton("Send");
	private JButton quitB = new JButton("Quit");
	private JTextField message = new JTextField();
	private JLabel messageLB = new JLabel("Message: ", Label.RIGHT);
	private JTextArea messageList = new JTextArea();
	// Class variables *************************************************

	/**
	 * The default port to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;

	// Instance variables **********************************************

	/**
	 * The instance of the client that created this ConsoleChat.
	 */
	EchoServer server;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the ClientConsole UI.
	 * 
	 * @param host
	 *            The host to connect to.
	 * @param port
	 *            The port to connect on.
	 */
	public ServerConsole(int port) {
		JFrame frame = new JFrame("Simple Chat");
		setSize(600, 500);
		setLayout(new BorderLayout(5, 5));
		Panel bottom = new Panel();
		add("Center", new JScrollPane(messageList));
		add("South", bottom);

		bottom.setLayout(new GridLayout(5, 2, 5, 5));

		bottom.add(messageLB);
		bottom.add(message);
		bottom.add(sendB);
		bottom.add(quitB);

		sendB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send();
			}
		});

		quitB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				server.handleMessageFromServerUI("#quit");
				System.exit(0);
			}
		});

		try {
			server = new EchoServer(port, this);
		} catch (Exception exception) {
			System.out.println("Error: Can't setup connection!"
					+ " Terminating server.");
			System.exit(1);
		}

		setVisible(true);
	}

	// Instance methods ************************************************

	/**
	 * This method waits for input from the console. Once it is received, it
	 * sends it to the client's message handler.
	 */

	/*
	 * public void accept() { try { BufferedReader fromConsole = new
	 * BufferedReader(new InputStreamReader(System.in)); String message;
	 * 
	 * while (true) { message = fromConsole.readLine();
	 * server.handleMessageFromServerUI(message); } } catch (Exception ex) {
	 * System.out.println ("Unexpected error while reading from console!"); } }
	 */

	public void send() {
		try {
			server.handleMessageFromServerUI(message.getText());
		} catch (Exception ex) {
			messageList.append(ex.toString() + "\n");
			// messageList.makeVisible(messageList.getItemCount()-1);
			messageList.setBackground(Color.yellow);
		}
	}

	public void display(String message) {
		messageList.append(message + "\n");
		messageList.scrollRectToVisible(new Rectangle(0, messageList
				.getHeight() - 2, 1, 1));
		// messageList.makeVisible(messageList.getItemCount()-1);
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the Client UI.
	 * 
	 * @param args
	 *            [0] The host to connect to.
	 */
	public static void main(String[] args) {
		String host = "";
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}
		String userId = "";

		ServerConsole serverUI = new ServerConsole(port);
	}
}
