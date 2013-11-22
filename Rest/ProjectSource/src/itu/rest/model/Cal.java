package itu.rest.model;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "cal")
public class Cal implements Serializable {
	  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XmlElementWrapper(name = "tasks")
	@XmlElement(name = "task")	 
	public List<Task> tasks;
	 
/*In order to get specific id´s and write more info, consider the option of creating getters and setter
* as explained in here http://www.vogella.com/articles/JAXB/article.html
* and in other website. This will be helpfull for manipulation the data in specific ways*/

}
