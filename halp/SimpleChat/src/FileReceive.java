import java.net.*;
import java.io.*;

public class FileReceive extends Thread
{
	String fileName;
	String saveDir;
	String hostIP;
	int fileSize;
	int port;
	Socket clientSocket;
	File transferFile;
	
	public FileReceive()
	{
		
	}
	
	public FileReceive(String fileName, int fileSize, String hostIP, int port)
	{
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.hostIP = hostIP;
		this.port = port;
	}
	
	public void run()
	{
		saveDir = "C:\\";
		int bytesRead;
	    int current = 0;
	    try
	    {
	    	clientSocket = new Socket(hostIP, port);
	    }
	    catch(IOException ioe)
	    {
	    	System.out.println(ioe);
	    }
	    
		byte [] transferArray  = new byte [fileSize];
		try
		{
			InputStream is = clientSocket.getInputStream();
		    FileOutputStream fos = new FileOutputStream(saveDir + fileName);
		    BufferedOutputStream bos = new BufferedOutputStream(fos);
		    bytesRead = is.read(transferArray,0,transferArray.length);
		    current = bytesRead;
		    
		    do {
		        bytesRead =
		           is.read(transferArray, current, (transferArray.length-current));
		        if(bytesRead >= 0) current += bytesRead;
		     } while(bytesRead > -1);
		    
		    bos.write(transferArray, 0 , current);
		    bos.flush();
		    bos.close();
		    clientSocket.close();

		}
		catch(IOException ioe)
		{
			System.out.println(ioe);
		}
		
	    

	    
	    
	}
}
