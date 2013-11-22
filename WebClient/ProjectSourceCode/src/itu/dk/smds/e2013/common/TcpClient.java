package itu.dk.smds.e2013.common;

/**
 *
 * @author Rasmus Kreiner
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TcpClient {
  
    int serverPort = 7896; // It is the same port where server will be listening.
    String servername = "localhost";
    
    public InetAddress getIP() throws UnknownHostException
    {
    	return InetAddress.getByName(servername);	
    }
    
    public void updateTask()
    {
    	String openingMessage = "put";    	
    }
    
    public void addTask(String id, String name, String date, String stat, String req, String des, String atten)
    {
    	String openingMessage = "post",
    		   response = "";	
    	
    		try {
            
            // Open a socket for communication.
            Socket socket = new Socket(getIP(), serverPort);
            
            // Get data output stream to send a String message to server.
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            dos.writeUTF(openingMessage);
            dos.flush();
            
            // Now switch to listening mode for receiving message from server.
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            
           // Note that this is a blocking call,  
            //String 
            response = dis.readUTF();
            if (response.equals("Server is ready"))
	            {
            		System.out.println("Response from server: " + response);
	            	dos.writeUTF(id);
	            	dos.writeUTF(name);
	            	dos.writeUTF(date);
	            	dos.writeUTF(stat);
	            	dos.writeUTF(req);
	            	dos.writeUTF(des);
	            	dos.writeUTF(atten);
	            	response = dis.readUTF();
	            }
            else
	            {	
            		System.out.println("Response from server: " + response);
	            }
            
            // Finnaly close the socket.
            dos.flush();
            socket.close();
           
        } catch (IOException ex) {

            System.out.println("error message: " + ex.getMessage());
        }
    }
    
    public String getTask(String id)
    {
    	String openingMessage = "get", 
    		   response = "";

    	try {
            
            // Open a socket for communication.
            Socket socket = new Socket(getIP(), serverPort);
            
            // Get data output stream to send a String message to server.
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            dos.writeUTF(openingMessage);
            dos.flush();
            
            // Now switch to listening mode for receiving message from server.
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            
           // Note that this is a blocking call,  
            //String 
            response = dis.readUTF();
            if (response.equals("Server is ready"))
	            {
            		System.out.println("Response from server: " + response);
	            	dos.writeUTF(id);
	            	response = dis.readUTF();
	            }
            else
	            {	
            		System.out.println("Response from server: " + response);
	            }
            
            
            // Print the message.
           // System.out.println("Message from Server: " + response);
            
            
            // Finnaly close the socket.
            dos.flush();
            socket.close();
           // return (response);
           
        } catch (IOException ex) {
            Logger.getLogger(TcpClient.class.getName()).log(Level.SEVERE, null, ex);
            
            System.out.println("error message: " + ex.getMessage());
        }
    	return response;
    }

    public String deleteTask(String id)
    {
    	String openingMessage = "delete",
    			response = "";
try {
            
            // Open a socket for communication.
            Socket socket = new Socket(getIP(), serverPort);
            
            // Get data output stream to send a String message to server.
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            dos.writeUTF(openingMessage);
            dos.flush();
            
            // Now switch to listening mode for receiving message from server.
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            
           // Note that this is a blocking call,  
            //String 
            response = dis.readUTF();
            if (response.equals("Server is ready"))
	            {
            		System.out.println("Response from server: " + response);
	            	dos.writeUTF(id);
	            	response = dis.readUTF();
	            }
            else
	            {	
            		System.out.println("Response from server: " + response);
	            }
            
            
            // Print the message.
           // System.out.println("Message from Server: " + response);
            
            // Finnaly close the socket.
            dos.flush();
            socket.close();
           
        } catch (IOException ex) 
        {
            System.out.println("error message: " + ex.getMessage());
        }

    	return response;
    }
}