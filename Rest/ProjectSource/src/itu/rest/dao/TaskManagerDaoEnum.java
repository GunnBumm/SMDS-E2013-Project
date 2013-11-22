package itu.rest.dao;

import itu.rest.model.Cal;
import itu.rest.model.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.ws.rs.FormParam;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;



//In this class all the information is retrieved and sent to TasksResources class that serves to the browser
public enum TaskManagerDaoEnum {
instance;



public List<Task> getAllTasks() throws FileNotFoundException, JAXBException{
	//Check this line
	//FileInputStream stream = new FileInputStream("/WEB-INF/task-manager-xml.xml");
	
	FileInputStream stream = new FileInputStream("C:/Users/Efrin Gonzalez/Documents/task-manager-xml.xml");
	// create an instance context class, to serialize/deserialize.
	JAXBContext jaxbContext = JAXBContext.newInstance(Cal.class);
	// Deserialize university xml into java objects.
     Cal cal = (Cal) jaxbContext.createUnmarshaller().unmarshal(stream);

    //With the iterator we can full fill the list that will be sent in response
     ListIterator<Task> listIterator = cal.tasks.listIterator(); 
     List<Task> tasks = new ArrayList<Task>();
         while (listIterator.hasNext()) {
             //Every time there is a new task, it will be stored in the arraylist
             tasks.add(listIterator.next());
         }     
   return tasks;
}


//------------------
//This function works perfectly by passing one id. It retrieves exactly one tasks
//in the next function we try to retrieve the set of tasks with certain id
public Task getTaskByIdName(String id, String name) throws FileNotFoundException, JAXBException{
	//Check this line
		//FileInputStream stream = new FileInputStream("/WEB-INF/task-manager-xml.xml");
		
		FileInputStream stream = new FileInputStream("C:/Users/Efrin Gonzalez/Documents/task-manager-xml.xml");
		// create an instance context class, to serialize/deserialize.
		JAXBContext jaxbContext = JAXBContext.newInstance(Cal.class);
		// Deserialize university xml into java objects.
	     Cal cal = (Cal) jaxbContext.createUnmarshaller().unmarshal(stream);

	    //With the iterator we can full fill the list that will be sent in response
	     ListIterator<Task> listIterator = cal.tasks.listIterator(); 
	     
	         while (listIterator.hasNext()) {
	        	 Task task = listIterator.next();
	        	 if(task.id.equals(id)&& task.name.equals(name)){
	        		 return task;
	        	 }
	         }     
return null;	   
}

public Task updateTaskByIdName(	String id,
								String name, 
								String date,
								String status,
								String required,
								String description,
								String attendants)throws JAXBException, IOException{
	//Check this line
			//FileInputStream stream = new FileInputStream("/WEB-INF/task-manager-xml.xml");
			
			FileInputStream stream = new FileInputStream("C:/Users/Efrin Gonzalez/Documents/task-manager-xml.xml");
			String path="C:/Users/Efrin Gonzalez/Documents/task-manager-xml.xml";
			// create an instance context class, to serialize/deserialize.
			JAXBContext jaxbContext = JAXBContext.newInstance(Cal.class);
			// Deserialize university xml into java objects.
		     Cal cal = (Cal) jaxbContext.createUnmarshaller().unmarshal(stream);

		    //With the iterator we can full fill the list that will be sent in response
		     ListIterator<Task> listIterator = cal.tasks.listIterator(); 
		     int existingTask = 0;
		     Task task = null;
		         while (listIterator.hasNext()) {
		        	 task = listIterator.next();
		        	 if(task.id.equals(id)&& task.name.equals(name)){
		        		
		        		 //deleteTask(id, name);		        		
		        		
		        		
		     			cal.tasks.remove(task);
		     			
		     			//existingTask=1;
		        		 task.id = id;
			        		task.name = name;
			        		task.date = date;
			        		task.status=status;
			        		task.required=required;
			        		task.description=description;
			        		task.attendants=attendants;
		     			cal.tasks.add(task);					
		     			
		     			 // Serialize university object into xml.            
		     	        StringWriter writer = new StringWriter();

		     	        // We can use the same context object, as it knows how to 
		     	        //serialize or deserialize University class.
		     	        jaxbContext.createMarshaller().marshal(cal, writer);				       
		     	       SaveFile(writer.toString(), path);	       
		     	      System.out.println("The task id: "+ id +" name: "+name+" has been updated. inside update method ");
		        		}
		        	 }  
		         
		        /* if (existingTask == 1){
		        		//preparing the updated object to be written
		        		 //
			        		task.id = id;
			        		task.name = name;
			        		task.date = date;
			        		task.status=status;
			        		task.required=required;
			        		task.description=description;
			        		task.attendants=attendants;
		     			cal.tasks.add(task);					
		     			 // Serialize university object into xml.            
		     	        StringWriter writer = new StringWriter();

		     	        // We can use the same context object, as it knows how to 
		     	        //serialize or deserialize University class.
		     	        jaxbContext.createMarshaller().marshal(cal, writer);				       
		     	       SaveFile(writer.toString(), path);	       
		     	      System.out.println("The task id: "+ id +" name: "+name+" has been saved. inside update method ");
		     		}*/
		        	// System.out.println("The task: "+id+" name: "+name+ " has been updated. inside update method");

		         
		         
	return null;	 
	
	
		
		
}
//------------------

public List<Task> getSetOfTasksById(String id) throws FileNotFoundException, JAXBException{
	//Check this line
		//FileInputStream stream = new FileInputStream("/WEB-INF/task-manager-xml.xml");
		
		FileInputStream stream = new FileInputStream("C:/Users/Efrin Gonzalez/Documents/task-manager-xml.xml");
		// create an instance context class, to serialize/deserialize.
		JAXBContext jaxbContext = JAXBContext.newInstance(Cal.class);
		// Deserialize university xml into java objects.
	     Cal cal = (Cal) jaxbContext.createUnmarshaller().unmarshal(stream);

	    //With the iterator we can full fill the list that will be sent in response
	     ListIterator<Task> listIterator = cal.tasks.listIterator(); 
	     //list to full fill with the requested id
	     List<Task> tasksList = new ArrayList<Task>();
	         while (listIterator.hasNext()) {
	        	 Task task = listIterator.next();
	        	 if(task.id.equals(id)){
	        		 tasksList.add(task);
	        	 }
	        	
	         }    
	         return tasksList;
}





	//Ready to make a post
	public void createTask(	String id,
							String name,
							String date,
							String status,
							String required,
							String description,
							String attendants) throws JAXBException, IOException{
		
		//Probind that the data is comming correctly
		/*System.out.println( "Id: "+id+
							"Name: "+name+
							"date: "+date+
							"status: "+status+
							"required: "+required+
							"descriptino: "+description+
							"attendants: "+attendants);*/
		//trying to write into file
		FileInputStream stream = new FileInputStream("C:/Users/Efrin Gonzalez/Documents/task-manager-xml.xml");
		String path="C:/Users/Efrin Gonzalez/Documents/task-manager-xml.xml";
		
		//First Let's try to get all the info from the file and the include the new info		
		JAXBContext jaxbContext = JAXBContext.newInstance(Cal.class);
		// Deserialize university xml into java objects.
	     Cal cal = (Cal) jaxbContext.createUnmarshaller().unmarshal(stream);

		// Let's create the new task to be written in the file. 
		Task task = new Task();		
		task.id = id;
		task.name = name;
		task.date = date;
		task.status = status;
		task.required = required;
		task.description = description;
		task.attendants = attendants;
		
		//if the task does not existe, let's add it
		 //With the iterator we can full fill the list that will be sent in response
	     ListIterator<Task> listIterator = cal.tasks.listIterator(); 
	     //list to full fill with the requested id
	     //List<Task> tasksList = new ArrayList<Task>();
	     int existingTask=0;   //flag to see whether the element exist 
	     while (listIterator.hasNext()) {	   
	    	 Task taskList = listIterator.next();
	        	 if((taskList.id.equals(id))&&(taskList.name.equals(name)) ){
	        		 existingTask = 1;
	        		 System.out.println("The task id: "+ id +" name: "+name+" already exist. It is not allowed to create repeted tasks.");
	        	 }	        	
	         }    
		
		if (existingTask == 0){
			cal.tasks.add(task);					
			 // Serialize university object into xml.            
	        StringWriter writer = new StringWriter();

	        // We can use the same context object, as it knows how to 
	        //serialize or deserialize University class.
	        jaxbContext.createMarshaller().marshal(cal, writer);				       
	       SaveFile(writer.toString(), path);	       
	      System.out.println("The task id: "+ id +" name: "+name+" has been saved. ");
		}
	}
	

   public void deleteTask(String id, String name) throws JAXBException, IOException{
	   Boolean deleted=false;
	 //trying to write into file
	 		FileInputStream stream = new FileInputStream("C:/Users/Efrin Gonzalez/Documents/task-manager-xml.xml");
	 		String path="C:/Users/Efrin Gonzalez/Documents/task-manager-xml.xml";
	 		
	 		//First Let's try to get all the info from the file and the include the new info		
	 		JAXBContext jaxbContext = JAXBContext.newInstance(Cal.class);
	 		// Deserialize university xml into java objects.
	 	     Cal cal = (Cal) jaxbContext.createUnmarshaller().unmarshal(stream);

	 		//if the task does not existe, let's add it
			 //With the iterator we can full fill the list that will be sent in response
		     ListIterator<Task> listIterator = cal.tasks.listIterator(); 
		     //list to full fill with the requested id
		  //   List<Task> tasksList = new ArrayList<Task>();
		     int existingTask = 0;
		     while (listIterator.hasNext()) {	   
		    	 Task task = listIterator.next();
		        	 if((task.id.equals(id))&&(task.name.equals(name)) ){
		        		 existingTask =1;
		        		 System.out.println("The task id: "+ id +" name: "+name+"  exist. inside delete method");
		        		 deleted = true;
		        	 }
		        	 if (existingTask == 1){
		 				cal.tasks.remove(task);					
		 				 // Serialize university object into xml.            
		 		        StringWriter writer = new StringWriter();

		 		        // We can use the same context object, as it knows how to 
		 		        //serialize or deserialize University class.
		 		        jaxbContext.createMarshaller().marshal(cal, writer);				       
		 		       SaveFile(writer.toString(), path);	       
		 		      System.out.println("The task id: "+ id +" name: "+name+" has been deleted. Inside delete metho");}
		        	// return deleted;
		         }    
			//return false;
			
   }

	private static void SaveFile(String xml, String path) throws IOException {
        File file = new File(path);    	
        	 // create a bufferedwriter to write Xml
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.write(xml);
            output.close();		
    }
}
