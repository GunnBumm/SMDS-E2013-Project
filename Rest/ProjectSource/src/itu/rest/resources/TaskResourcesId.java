package itu.rest.resources;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import itu.rest.dao.TaskManagerDaoEnum;
import itu.rest.model.Task;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBException;

public class TaskResourcesId {
	@Context
	  UriInfo uriInfo;
	  @Context
	  Request request;
	  String id;
	  String name;  
	  public TaskResourcesId(UriInfo uriInfo, Request request, String id){
		  this.uriInfo = uriInfo;
		    this.request = request;
		    this.id = id;
		    
	  }
	  
	//Application integration     
	  @GET
	  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	  public List<Task> getSetOfTasksById() throws FileNotFoundException, JAXBException {
		  List<Task> task = new ArrayList<Task>();
		   task = TaskManagerDaoEnum.instance.getSetOfTasksById(id);
	 //   Todo todo = TodoDao.instance.getModel().get(id);
	    if(task==null)
	      throw new RuntimeException("Get: Todo with " + id +  " not found");
	    return task;

	  }
	  
	// for the browser. This will serve data to tasksResources class
		  @GET
		  @Produces(MediaType.TEXT_XML)
		  public List<Task> getSetOfTasksByIdHTML() throws FileNotFoundException, JAXBException {
			  List<Task> task = new ArrayList<Task>();
			   task = TaskManagerDaoEnum.instance.getSetOfTasksById(id);
		 //   Todo todo = TodoDao.instance.getModel().get(id);
		    if(task==null)
		      throw new RuntimeException("Get: Todo with " + id +  " not found");
		    return task;

		  }
		  
		
}
