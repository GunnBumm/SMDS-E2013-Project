package itu.rest.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import itu.rest.dao.TaskManagerDaoEnum;
import itu.rest.model.Task;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBException;

public class TaskResourcesDelete {
	@Context
	  UriInfo uriInfo;
	  @Context
	  Request request;
	  String id;
	  String name;
	 
	  
	//------------------
	  public TaskResourcesDelete(UriInfo uriInfo, Request request, String id, String name){
		  this.uriInfo = uriInfo;
		    this.request = request;
		    this.id = id;
		    this.name = name;
	  }
	  
	  //@DELETE
	  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	  public void deletingTask() throws JAXBException, IOException {
		
				//Boolean taskDeleted = 		  TaskManagerDaoEnum.instance.deleteTask(id, name);  
				//if(taskDeleted==false)
				  //    throw new RuntimeException("Task: " + id + "Name: " + name + " not found");
				   // return taskDeleted;
		  TaskManagerDaoEnum.instance.deleteTask(id, name);
	  }
	  
	  // for the browser
	  //This works for retreiveing one single task by id
	  
	//  @DELETE
	  @Produces(MediaType.TEXT_XML)
	  public void deletingTaskHtml() throws JAXBException, IOException {
		  TaskManagerDaoEnum.instance.deleteTask(id, name);
		  //Boolean taskDeleted = TaskManagerDaoEnum.instance.deleteTask(id, name);  
			//if(taskDeleted==false)
			  //    throw new RuntimeException("Task: " + id + "Name: " + name + " not found");
			   // return taskDeleted;

  
}
	  
	  //------------------
	
	
}
