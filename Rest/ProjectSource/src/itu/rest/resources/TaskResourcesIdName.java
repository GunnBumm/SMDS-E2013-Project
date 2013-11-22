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

public class TaskResourcesIdName {
	@Context
	  UriInfo uriInfo;
	  @Context
	  Request request;
	  String id;
	  String name;
	 
	  
	//------------------
	  public TaskResourcesIdName(UriInfo uriInfo, Request request, String id, String name){
		  this.uriInfo = uriInfo;
		    this.request = request;
		    this.id = id;
		    this.name = name;
	  }
	  
	  @GET
	  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	  public Task getSingleTaskById_Name() throws FileNotFoundException, JAXBException {
		Task task = TaskManagerDaoEnum.instance.getTaskByIdName(id, name);  
	
	    if(task==null)
	      throw new RuntimeException("Get: Todo with " + id +"name "+ name+ " not found");
	    return task;
	  }
	  
	  // for the browser
	  //This works for retreiveing one single task by id
	  
	  @GET
	  @Produces(MediaType.TEXT_XML)
	  public Task getSingleTaskById_NameHTML() throws FileNotFoundException, JAXBException {
		Task task = TaskManagerDaoEnum.instance.getTaskByIdName(id, name);  
	
	    if(task==null)
	      throw new RuntimeException("Get: Todo with " + id +"name "+ name+ " not found");
	    return task;
	  }
	  
	  //------------------
	
	
}
