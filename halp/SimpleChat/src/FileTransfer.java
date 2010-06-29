import java.net.*;
import java.io.*;

public class FileTransfer extends Thread
{
	String fileDir;
	String fileName;
	String hostIP;
	String sendingUserName;
	String targetUserName;
	int fileSize;
	int port;
	ServerSocket fileHostSocket;
	Socket clientSocket;
	File transferFile;
	
	
	public FileTransfer()
	{
		
	}
	
	public FileTransfer(String fileDir, String fileName, int fileSize, String hostIP, int port, String sendingUserName, String targetUserName)
	{
		this.fileDir = fileDir;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.hostIP = hostIP;
		this.port = port;
		this.sendingUserName = sendingUserName;
		this.targetUserName = targetUserName;
	}
	
	public void run()
	{
		try
		{
			fileHostSocket = new ServerSocket(port);
			clientSocket = fileHostSocket.accept();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		transferFile =  new File(fileDir + fileName);
		byte [] transferArray  = new byte [(int)transferFile.length()];
		try
		{
			FileInputStream fis = new FileInputStream(transferFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			bis.read(transferArray,0,transferArray.length);
			fis.close();
			bis.close();

		}
		catch(FileNotFoundException fne)
		{
			System.out.println(fne);
		}
		catch(IOException ioe)
		{
			System.out.println(ioe);
		}
		
		try
		{
			OutputStream os = clientSocket.getOutputStream();
			os.write(transferArray,0,transferArray.length);
			os.flush();
			clientSocket.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}



	}
}
