import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ClientConsole extends JFrame implements ChatIF {
	private JButton sendB = new JButton("Send Message");
	private JButton quitB = new JButton("Quit");
	private JButton openB = new JButton("Open File");
	private JButton sendFileB = new JButton("Send File");
	private JTextField message = new JTextField();
	private JLabel messageLB = new JLabel("Message: ", Label.RIGHT);
	private JLabel filenameLB = new JLabel("Filename: ", Label.RIGHT);
	private JLabel targetuserLB = new JLabel("Send File To: ", Label.RIGHT);
	private JLabel dirLB = new JLabel("Directory: ", Label.RIGHT);
	private JTextArea messageList = new JTextArea();
	private JTextField filename = new JTextField(), dir = new JTextField();
	private JTextField targetuser = new JTextField();

	final public static int DEFAULT_PORT = 5555;

	ChatClient client;

	public ClientConsole(String host, int port, String userId) {
		JFrame frame = new JFrame("Simple Chat");
		setSize(400, 500);

		setLayout(new BorderLayout(5, 5));
		Panel bottom = new Panel();
		add("Center", new JScrollPane(messageList));
		add("South", bottom);

		bottom.setLayout(new GridLayout(0, 2, 5, 5));
		bottom.add(messageLB);
		bottom.add(message);
		bottom.add(filenameLB);
		bottom.add(filename);
		bottom.add(dirLB);
		bottom.add(dir);
		bottom.add(targetuserLB);
		bottom.add(targetuser);
		bottom.add(sendB);
		bottom.add(sendFileB);
		bottom.add(openB);
		bottom.add(quitB);

		sendB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send();
			}
		});

		sendFileB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!dir.getText().equals("") && !filename.getText().equals("")) {
					if (!targetuser.getText().equals("")) {
						String message = "#sendfile" + " "
								+ targetuser.getText() + " " + dir.getText()
								+ "|" + filename.getText();
						;
						client.handleMessageFromClientUI(message);
					} else {
						client.clientUI
								.display("You must select a user to send the file.");
					}
				} else {
					client.clientUI.display("You must select a file.");
				}

			}
		});

		openB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser c = new JFileChooser();
				int rVal = c.showOpenDialog(ClientConsole.this);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					filename.setText(c.getSelectedFile().getName());
					dir.setText(c.getCurrentDirectory().toString());
				}
				if (rVal == JFileChooser.CANCEL_OPTION) {
					filename.setText("You pressed cancel");
					dir.setText("");
				}
			}
		});

		quitB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.quit();
				System.exit(0);
			}
		});

		try {
			client = new ChatClient(host, port, userId, this);
		} catch (Exception exception) {
			System.out.println("Error: Can't setup connection!"
					+ " Terminating client.");
			System.exit(1);
		}

		setVisible(true);
	}

	public void display(String message) {
		messageList.append(message + "\n");
		messageList.scrollRectToVisible(new Rectangle(0, messageList
				.getHeight() - 2, 1, 1));
		// messageList.makeVisible(messageList.getItemCount()-1);
	}

	public void send() {
		try {
			client.handleMessageFromClientUI(message.getText());
		} catch (Exception ex) {
			messageList.append(ex.toString() + "\n");
			// messageList.makeVisible(messageList.getItemCount()-1);
			messageList.setBackground(Color.yellow);
		}
	}

	public static void main(String[] args) {

		String host = "";
		int port = 0; // The port number
		String userId = "";

		try {
			host = args[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			host = "localhost";
		}
		try {
			userId = args[1];
		} catch (ArrayIndexOutOfBoundsException e) {
			host = "anonymous";
		}
		try {
			port = Integer.parseInt(args[2]);
		} catch (ArrayIndexOutOfBoundsException e) {
			port = DEFAULT_PORT;
		}

		ClientConsole chat = new ClientConsole(host, port, userId);
	}
}