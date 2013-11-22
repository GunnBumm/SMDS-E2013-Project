package dk.edu.itu.serialization;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

//Specify  XmlRootElement annotation, if you want to serailize a class into a standalone XML.

@XmlRootElement(name = "task")
public class Task {

    @XmlAttribute
    public String id;     
    @XmlAttribute
    public String name;    
    @XmlAttribute
    public String date;    
    @XmlAttribute
    public String status;    
    @XmlAttribute
    public String required;    
 // If you dont specify any annotation, it will be serialized as XmlElement.
    public String description;
    public String attendants;
    
}
