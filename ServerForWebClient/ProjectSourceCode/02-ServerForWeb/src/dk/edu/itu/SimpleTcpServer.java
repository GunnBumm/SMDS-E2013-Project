package dk.edu.itu;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ListIterator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

//import SimpleTc.ServerThread;
import dk.edu.itu.serialization.Cal;
import dk.edu.itu.serialization.Task;

public class SimpleTcpServer 
{	
	public static final int serverPort = 7896; 
	public static String path = "";
    public static void main(String args[]) throws IOException
    {    
    	System.out.println();
    	String root = System.getProperty("user.dir");
    	String filepath = "";
    	
    	if(System.getProperty("os.name").toLowerCase().equals("mac os x")) filepath = "/src/resources/task-manager-xml.xml";
    	else filepath = "\\src\\resources\\task-manager-xml.xml"; // in case of Windows: "\\path \\to\\yourfile.txt
    	
    	path = root+filepath;
    	
    	new SimpleTcpServer().runServer();
    }
    
	public void runServer() throws IOException
	{
		ServerSocket serverSocket = new ServerSocket(serverPort);
		System.out.println("Server up & ready for connections at 7896...");
		
		while(true){//we accept different connections from different clients
			Socket socket = serverSocket.accept();
			new ServerThread(socket).start();
		}
		
	}

    //This function is pretty much to show on screen what we have ask for. 
    private static void PrintTaskObject(Task task) 
    {
        try 
        {
            StringWriter writer = new StringWriter();
            // create a context object for Student Class
            JAXBContext jaxbContext = JAXBContext.newInstance(Cal.class);
            // Call marshal method to serialize student object into Xml
            jaxbContext.createMarshaller().marshal(task, writer);
            System.out.println(writer.toString());
        } catch (JAXBException ex) 
        {
        	System.out.println(ex.getMessage());
        }
    }

    //this function writes into the file, as its name suggests.
    private static void SaveFile(String xml) throws IOException {
        File file = new File(path);
        // create a bufferedwriter to write Xml
        BufferedWriter output = new BufferedWriter(new FileWriter(file));
        output.write(xml);
        output.close();
    }
    
    
   
  //-------An inner class for the thread--------
  	public class ServerThread extends Thread
  	{
  		Socket socket;
  		ServerThread(Socket socket)
  		{
  			this.socket = socket;
  		}

  		public void run()
  		{
  			 try 
  			 {  
  	            // Get the inputstream to receive data sent by client. 
  	            InputStream is = socket.getInputStream();
  	            // based on the type of data we want to read, we will open suitable input stream.  
  	            DataInputStream dis = new DataInputStream(is);

  	            // Read the String data sent by client at once using readUTF,
  	            // Note that read calls also blocking and wont return until we have some data sent by client.
  	            String message = dis.readUTF(); // blocking call
  	            
  	            switch (message)
  	            {
	  	            case "get" : getTask(dis);
	  	            break;
	  	            case "put" : updateTask(dis);
	  	            break;
	  	            case "post" : addTask(dis);
	  	            break;
	  	            case "delete" : deleteTask(dis);
	  	            break;
	  	            default : errorTask(dis);
	  	            break;  	            
  	            }
  	            } 
  			 catch (IOException ex) 
  	            {
  				 	System.out.println("error message: " + ex.getMessage());
  	            }
  		}
  		
  		public void getTask (DataInputStream dis)
  		{
  			try 
  			{
  				//Initial communication
  				DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
	            outputStream.writeUTF("Server is ready");  	         
	            //outputStream.flush();
	            
	            //Getting the id of the task it wants
	            String id = dis.readUTF();
	            
	            //For the output
	            StringWriter writer = new StringWriter();
	            
  	            try {

  	            	FileInputStream stream = new FileInputStream(path);
  	            	
  	                // create an instance context class, to serialize/deserialize.
  	                JAXBContext jaxbContext = JAXBContext.newInstance(Cal.class);

  	                // Deserialize task xml into java objects.
  	                Cal cal = (Cal) jaxbContext.createUnmarshaller().unmarshal(stream);
  	                  	                
  	                // Iterate through the collection of student object and print each student object in the form of Xml to console.
  	                ListIterator<Task> listIterator = cal.tasks.listIterator();            

  	                //Iterates thru all elements and appends the ones containing the correct id. 
  	                //If id = "" then all objects are appended.
  	                while (listIterator.hasNext()) 
  	                {
  	            	  Task t = listIterator.next(); 
	                    if (t.id.equals(id) || id.equals("")) 
	                    	{
	                    		String str = "Id: " +  t.id + 
	                    					 " \n" + "Name: " + t.name + 
	                    					 " \n" + "Date: "  + t.date + 
	                    					 " \n"  + "Status: " + t.status + 
	                    					 " \n"  + "Required: " + t.required + 
	                    					 " \n"  + "Description: " + t.description + 
	                    					 " \n"  + "Attendents: " + t.attendants + "\n\n";
	                    		System.out.println(str);
	                    		writer.append(str);
	                    	}
	                }

  	            } catch (JAXBException ex) {
  	                System.out.println(ex.getMessage());
  	            }
  	            
  	            // Now the server switches to output mode delivering some message to client.
  	            outputStream.writeUTF(writer.toString());  	         
  	            outputStream.flush();
  	            socket.close();

  	        } catch (IOException ex) 
  	        {
  	            System.out.println("error message: " + ex.getMessage());
  	        }
	            
	            
  		}
  		
  		public void updateTask(DataInputStream dis)
  		{
  			
  		}
  		
  		public void deleteTask(DataInputStream dis)
  		{
  			try 
  			{
  				StringWriter writer = new StringWriter();
  				//Initial communication
  				DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
	            outputStream.writeUTF("Server is ready");  	         
	            //outputStream.flush();
	            
	            //Getting the id of the task it want to delete
	            String id = dis.readUTF();
  	            try {

  	            	FileInputStream stream = new FileInputStream(path);
  	            	
  	                // create an instance context class, to serialize/deserialize.
  	                JAXBContext jaxbContext = JAXBContext.newInstance(Cal.class);

  	                // Deserialize task xml into java objects.
  	                Cal cal = (Cal) jaxbContext.createUnmarshaller().unmarshal(stream);
  	                  	                
  	                // Iterate through the collection of student object and print each student object in the form of Xml to console.
  	                ListIterator<Task> listIterator = cal.tasks.listIterator();
  	                
  	                int indexOfID = -1; //-1 is chosen as a safety. Because we can't have a negative index.
  	                while (listIterator.hasNext())
  	                {
  	                	Task t = listIterator.next();
  	                	if (id.equals(t.id))
  	                	{
  	                		indexOfID = listIterator.nextIndex()-1;
  	                	}
  	                }
  	                if (indexOfID != -1) //Cannot delete anything if the index is not found
  	                {
	  	                cal.tasks.remove(indexOfID);
	  	                jaxbContext.createMarshaller().marshal(cal, writer);
	  	                SaveFile(writer.toString());
  	                }
  	            }
  	            catch (JAXBException ex){
  	            	System.out.println(ex.getMessage());
  	            }
  			}
  			catch(IOException ex)
  			{
  				System.out.println(ex.getMessage());
  			}
  		}
  		
  		public void addTask(DataInputStream dis)
  		{
  			try 
  			{
  				StringWriter writer = new StringWriter();
  				//Initial communication
  				DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
	            outputStream.writeUTF("Server is ready");  	         
	            //outputStream.flush();
	            
	            //Adding a task to the end of the list
  	            try {

  	            	FileInputStream stream = new FileInputStream(path);
  	            	
  	                // create an instance context class, to serialize/deserialize.
  	                JAXBContext jaxbContext = JAXBContext.newInstance(Cal.class);

  	                // Deserialize task xml into java objects.
  	                Cal cal = (Cal) jaxbContext.createUnmarshaller().unmarshal(stream);
  	                
  	               Task t = new Task();
  	               t.id = dis.readUTF();
  	               t.name = dis.readUTF();
  	               t.date = dis.readUTF();
  	               t.status = dis.readUTF();
  	               t.required = dis.readUTF();
  	               t.description = dis.readUTF();
  	               t.attendants = dis.readUTF();
  	               
  	                cal.tasks.add(t);
  	                jaxbContext.createMarshaller().marshal(cal, writer);
  	                SaveFile(writer.toString());
  	                
  	            }
  	            catch (JAXBException ex){
  	            	System.out.println(ex.getMessage());
  	            }
  			}
  			catch(IOException ex)
  			{
  				System.out.println(ex.getMessage());
  			}
  		}
  		
  		public void errorTask(DataInputStream dis)
  		{
  			try 
  			{
  				DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
	            outputStream.writeUTF("Server does not understand the command");  	         
	            outputStream.flush();
			} catch (Exception e) {
				System.out.println(e.getMessage() + "\n");
				e.printStackTrace();
			}
  		}
  	}//end of the inner class    
    
}//End of the outer class