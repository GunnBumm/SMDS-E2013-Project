package dk.itu.smds.e2013.serialization.common;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author rao - edited by group
 */
@XmlRootElement(name = "envelope")
public class Envelope {

	public String command, id, role; // We added two string options: id and role
	@XmlElementWrapper(name = "data")
	@XmlElement(name = "task")
	public List<Task> data = new ArrayList<>();

}
