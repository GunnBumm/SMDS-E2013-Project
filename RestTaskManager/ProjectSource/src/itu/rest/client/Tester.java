package itu.rest.client;



import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;

public class Tester {

	public static void main(String[] args) {
	ClientConfig config = new DefaultClientConfig();
	Client client = Client.create(config);
	WebResource service = client.resource(getBaseURI());
	
	// Whatch out: The HTML. The error 406 is related to the fact that the server does not serve that kind of data in the get request
	//Fluent interfaces
	//System.out.println(service.path("rest").path("tasks").path("get").accept(MediaType.TEXT_PLAIN).get(ClientResponse.class).toString());
	//Get plain text
//	 System.out.println("XML in Text format");
	//System.out.println(service.path("rest").path("tasks").path("get").accept(MediaType.TEXT_PLAIN).get(String.class));
	// Get XML	
    
    //System.out.println("XML in HTML format");
    //System.out.println(service.path("rest").path("tasks").path("get").accept(MediaType.TEXT_HTML).get(String.class));
	 System.out.println("----------------Number of elements within the file----------------");
	 System.out.println(service.path("rest").path("tasks").path("get").path("count").accept(MediaType.TEXT_PLAIN).get(String.class));
	  
	
	 System.out.println("----------------All the data in XML in data format----------------");
	 System.out.println(service.path("rest").path("tasks").path("get").accept(MediaType.TEXT_XML).get(String.class));
	   
	 System.out.println("----------------XML in data format by id=handin-01------------------");
	 System.out.println(service.path("rest").path("tasks").path("get/handin-01").accept(MediaType.TEXT_XML).get(String.class));
	    
	 System.out.println("----------------XML in data format by id=handin-01 name=Submit assignment-01--------");
	 System.out.println(service.path("rest").path("tasks").path("get/handin-01/Submit assignment-01").accept(MediaType.TEXT_XML).get(String.class));
	 
	 System.out.println("----------------Creating a task--------");	
	 ClientResponse response;
	// create a Task using the form
	/* Form form = new Form();
	 
	 
	 form.add("id", "test");
	 form.add("name", "test");
	 form.add("date", "test");
	 form.add("status", "test");
	 form.add("required", "test");
	 form.add("description", "test");
	 form.add("attendants", "test");
	 response = service.path("rest").
			 			path("tasks").
			 			path("create").
			 			type(MediaType.APPLICATION_FORM_URLENCODED).
			 			post(ClientResponse.class, form);
	 System.out.println("Form response " + response.getEntity(String.class));
	 
	 
	 
	 System.out.println("----------------XML in data format by id=test------------------");
	 System.out.println(service.path("rest").path("tasks").path("get/test").accept(MediaType.TEXT_XML).get(String.class));
	
	 System.out.println("----------------Updating a task--------");	
	 Form form2 = new Form();
	 form2.add("id", "test");
	 form2.add("name", "test");
	 form2.add("date", "testUpdated");
	 form2.add("status", "testUpdated");
	 form2.add("required", "testUpdated");
	 form2.add("description", "testUpdated");
	 form2.add("attendants", "testUpdated");
	 response = service.path("rest").
			 			path("tasks").
			 			path("update").
			 			type(MediaType.APPLICATION_FORM_URLENCODED).
			 			post(ClientResponse.class, form2);
	 System.out.println("Form response " + response.getEntity(String.class));
	 
	 
	 
	 System.out.println("----------------XML in data format by id=testUpdated------------------");
	 System.out.println(service.path("rest").path("tasks").path("get/testUpdated").accept(MediaType.TEXT_XML).get(String.class));
	
	*/
	 System.out.println("----------------Deleting a task--------");	
	 Form form2 = new Form();
	 form2.add("id", "1");
	 form2.add("name", "1");	 
	 response = service.path("rest").
			 			path("tasks").
			 			path("delete").
			 			type(MediaType.APPLICATION_FORM_URLENCODED).
			 			post(ClientResponse.class, form2);
	 System.out.println("Form response " + response.getEntity(String.class));
	 
	 
	 
	// System.out.println("----------------XML in data format by id=test------------------");
	// System.out.println(service.path("rest").path("tasks").path("get/test").accept(MediaType.TEXT_XML).get(String.class));
	}
	
	
	
	
	 private static URI getBaseURI() {
		    return UriBuilder.fromUri("http://localhost:8080/03-RestWebServer/").build();
		  }
}
