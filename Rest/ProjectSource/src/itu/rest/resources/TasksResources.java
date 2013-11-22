package itu.rest.resources;

import itu.rest.dao.TaskManagerDaoEnum;
import itu.rest.model.Task;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBException;

//Will map the resource to the URL todos
@Path("/tasks")
public class TasksResources {

	 // Allows to insert contextual objects into the class, 
	  // e.g. ServletContext, Request, Response, UriInfo
	  @Context
	  UriInfo uriInfo;
	  @Context
	  Request request;
	  
	
	 // Return the list of tasks to the user in the browser
	// add this to the path in the browser: rest/tasks
	  @GET
	  @Path("/get/")
	  @Produces(MediaType.TEXT_XML)	 	  	  
	  public List<Task> getTasksBrowser() throws FileNotFoundException, JAXBException {
	    List<Task> tasks = new ArrayList<Task>();
	    
	   tasks = TaskManagerDaoEnum.instance.getAllTasks();
	    return tasks; 
	  }
	
		  
	// Return the list of tasks to the user in the applicatoin
		// 
		  @GET
		  @Path("/get/")
		  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})	  
		  public List<Task> getTasks() throws FileNotFoundException, JAXBException {
		    List<Task> tasks = new ArrayList<Task>();
		    
		   tasks = TaskManagerDaoEnum.instance.getAllTasks();
		    return tasks; 
		  }
	  
		  //This function gets the number of tasks within the xml file
	 @GET
	  @Path("/get/count/")
	  @Produces(MediaType.TEXT_PLAIN)
	 // add this to the path in the browser: rest/tasks/count
	 public String getCount() throws FileNotFoundException, JAXBException{
		  int count = TaskManagerDaoEnum.instance.getAllTasks().size();
		    return String.valueOf(count);		
	  }
	 
	 
	// Defines that the next path parameter after tasks is
	  // treated as a parameter and passed to the TaskResources class
	  // Allows to type ../rest/tasks/1
	  // 1 will be treaded as parameter task and passed to TaskResources
	 
	  @Path("/get/{taskId}")
	 public TaskResourcesId getSetOfTasksById(@PathParam("taskId") String id){
		 return new TaskResourcesId(uriInfo, request, id);
		 //this info is sent to getSetOfTasksByIdHTML in TaskResouces Class
	 }
	 
	//---------------  
	  // Allows to insert contextual objects into the class, 
	  // e.g. ServletContext, Request, Response, UriInfo
	 
	 // ../rest/tasks/taskId/name
	  @Path("/get/{taskId}/{taskName}")
		 public TaskResourcesIdName getTasksByIdName(@PathParam("taskId") String id, 
				 							   @PathParam("taskName") String name){
			 return new TaskResourcesIdName(uriInfo, request, id, name);
			 //this info is sent to getSingleTaskById_NameHTML() in TaskResouces Class
		 }
	  
	  //this method is not working now
	 
	/*  @Path("/delete/{taskId}/{taskName}")
	 // @Produces(MediaType.TEXT_XML)
	  public TaskResourcesDelete deleteTask(@PathParam("taskId") String id, 
			   				@PathParam("taskName") String name){
		  return new TaskResourcesDelete(uriInfo, request, id, name);
	  }
	  */
	  //--------------------
	 @POST
	 @Path("/delete/")
	  @Produces(MediaType.TEXT_HTML)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	  public void deleteTask(@FormParam("id") String id,
			  			@FormParam("name") String name, 			  						  			  
			  			@Context HttpServletResponse servletResponse) throws IOException, JAXBException{
		  
	 TaskManagerDaoEnum.instance.deleteTask(id, name);
	 servletResponse.sendRedirect("../delete_task.html");
	 
	  }
	 
	 
	 //----
	
	 
	  
	  //--------------------
	  @POST 
	  @Path("/create/")
	  @Produces(MediaType.TEXT_HTML)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	  public void newTask(	@FormParam("id") String id,
				  			@FormParam("name") String name, 
				  			@FormParam("date") String date,
				  			@FormParam("status") String status,
				  			@FormParam("required") String required,
				  			@FormParam("description") String description,
				  			@FormParam("attendants") String attendants,			  			  
				  			@Context HttpServletResponse servletResponse) throws IOException, JAXBException{
		  
	 TaskManagerDaoEnum.instance.createTask(id, name, date, status, required, description, attendants);
	 servletResponse.sendRedirect("../create_task.html");
	  
	  }
	  
	  
	  @POST
	  @Path("/update/")
	  @Produces(MediaType.TEXT_HTML)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	  public void updateTask(	@FormParam("id") String id,
					  			@FormParam("name") String name, 
					  			@FormParam("date") String date,
					  			@FormParam("status") String status,
					  			@FormParam("required") String required,
					  			@FormParam("description") String description,
					  			@FormParam("attendants") String attendants,			  			  
					  			@Context HttpServletResponse servletResponse) throws IOException, JAXBException{
		  
	 TaskManagerDaoEnum.instance.updateTaskByIdName(id, name, date, status, required, description, attendants);
	 servletResponse.sendRedirect("../update_task.html");
	  
	  }
	  
}
