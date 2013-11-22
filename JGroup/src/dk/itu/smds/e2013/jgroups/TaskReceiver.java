/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.smds.e2013.jgroups;

import dk.itu.smds.e2013.jgroups.common.TaskProvider;
import dk.itu.smds.e2013.serialization.common.Envelope;
import dk.itu.smds.e2013.serialization.common.Task;
import dk.itu.smds.e2013.serialization.common.TaskSerializer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

/**
 *
 * @author rao
 */
public class TaskReceiver extends ReceiverAdapter {

    final List<String> state = new LinkedList<>();
    protected TaskProvider provider;
    private JChannel channelTasks;
    private String prefix = "[receiver]: ";

    public TaskReceiver(TaskProvider provider, JChannel channel) {

        this.provider = provider;
        channelTasks = channel;


    }

    @Override
    public void getState(OutputStream output) throws Exception {
//        synchronized (provider.TaskManagerInstance) {
//
//            // write the whole task manager object to the stream.
//            Util.objectToStream(provider.TaskManagerInstance, new DataOutputStream(output));
//        }
    }

    @Override
    public void setState(InputStream input) throws Exception {
//        TaskManager taskmanager = (TaskManager) Util.objectFromStream(new DataInputStream(input));
//
//        synchronized (provider.TaskManagerInstance) {
//
//            provider.TaskManagerInstance.tasks.clear();
//
//            provider.TaskManagerInstance.users.clear();
//
//            provider.TaskManagerInstance.users.addAll(taskmanager.users);
//
//            provider.TaskManagerInstance.tasks.addAll(taskmanager.tasks);
//
//        }
//        System.out.println("received state (" + taskmanager.tasks.size() + " Tasks in chat history):");
//
//        for (Task task : taskmanager.tasks) {
//            System.out.println(task);
//        }
    }

    @Override
    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view);
    }

    @Override
    public void receive(Message msg) {
        // We know that, we will always get Xml.
        String envelopeXml = msg.getObject().toString();

        Envelope DeserializeEnvelope;

        try {
            DeserializeEnvelope = TaskSerializer.DeserializeEnvelope(envelopeXml);

            //System.out.println("Input envelope received: " + inputData);

            // We may get
        } catch (JAXBException ex) {
            System.out.println(prefix + "Failed to deserialize envelope Xml. Error message" + ex);

            return;
        }

        if (DeserializeEnvelope.command.equals("add")) 
        {
            Task taskWithId = GetTaskWithId(DeserializeEnvelope.data.get(0).id);

            if (taskWithId == null) 
            {

                provider.TaskManagerInstance.tasks.add(DeserializeEnvelope.data.get(0));

                try {
                    provider.PersistTaskManager();
                } catch (JAXBException ex) {
                    System.out.println(prefix + "Failed to persist envelope Xml. Error message" + ex);
                } catch (IOException ex) {
                    System.out.println(prefix + "Failed to persist envelope Xml. Error message" + ex);
                }

                System.out.println(prefix + "Task with Id:" + DeserializeEnvelope.data.get(0).id + " added! " + "total number of tasks: "
                        + provider.TaskManagerInstance.tasks.size());
            } 
            else 
            {
                System.out.println(prefix + "Task with Id:" + DeserializeEnvelope.data.get(0).id + " already exists and hence NOT added! " + "Total number of tasks: "
                        + provider.TaskManagerInstance.tasks.size());
            }
        } 
        else if (DeserializeEnvelope.command.equals("delete")) 
        {
            Task taskWithId = GetTaskWithId(DeserializeEnvelope.data.get(0).id);

            if (taskWithId != null) 
            {

                provider.TaskManagerInstance.tasks.remove(taskWithId);

                try {
                    provider.PersistTaskManager();
                } catch (JAXBException ex) {
                    System.out.println(prefix + "Failed to persist envelope Xml. Error message" + ex);
                } catch (IOException ex) {
                    System.out.println(prefix + "Failed to persist envelope Xml. Error message" + ex);
                }

                System.out.println(prefix + "Task with Id:" + DeserializeEnvelope.data.get(0).id + " deleted!" + "total number of tasks: "
                        + provider.TaskManagerInstance.tasks.size());


            } 
            else 
            {
                System.out.println(prefix + "Task with Id:" + DeserializeEnvelope.data.get(0).id + " can not be found and hence NOT deleted!" + " Total number of tasks: "
                        + provider.TaskManagerInstance.tasks.size());
            }




        } 
        else if (DeserializeEnvelope.command.equals("replicate")) 
        {

            // First get the array of all tasks.
            Object[] tasks = provider.TaskManagerInstance.tasks.toArray();

            //Task[] tasks = new Task[4];

            // Remove the all tasks from the task manager.
            provider.TaskManagerInstance.tasks.removeAll(provider.TaskManagerInstance.tasks);

            try {
                provider.PersistTaskManager();
            } catch (JAXBException ex) {
                System.out.println(prefix + "Failed to serialize task manager. Error message:" + ex.getMessage());
            } catch (IOException ex) {
                System.out.println(prefix + "Failed to save task manager. Error message:" + ex.getMessage());
            }

            for (int index = 0; index < tasks.length; index++) {
                SendAddTaskCommand((Task) tasks[index]);

            }

        } 
        else if (DeserializeEnvelope.command.equals("trace")) 
        {

            //System.out.println(prefix +" Failed to save task manager. Error message:");

            for (int index = 0; index < provider.TaskManagerInstance.tasks.size(); index++) 
            {
            	
            }
        }
        
        else if (DeserializeEnvelope.command.equals("execute")) 
        {
        	Task taskToWork = DeserializeEnvelope.data.get(0);
        	if (GetTaskWithId(taskToWork.id) != null)
        	{
            	for (int index = 0; index < provider.TaskManagerInstance.tasks.size(); index++) 
                {
                    if (provider.TaskManagerInstance.tasks.get(index).id.equals(taskToWork.id)) 
                    {
                    	provider.TaskManagerInstance.tasks.get(index).requiered = false;
                    	provider.TaskManagerInstance.tasks.get(index).status = "Executed";
                    }
                }
            	
            	try {
                    provider.PersistTaskManager();
                } catch (JAXBException ex) {
                    System.out.println(prefix + "Failed to persist envelope Xml. Error message" + ex);
                } catch (IOException ex) {
                    System.out.println(prefix + "Failed to persist envelope Xml. Error message" + ex);
                }	
        	}

        }
        
        else if (DeserializeEnvelope.command.equals("request")) 
        {
        	Task taskToWork = DeserializeEnvelope.data.get(0);
        	if (GetTaskWithId(taskToWork.id) != null)
        	{
        		for (int index = 0; index < provider.TaskManagerInstance.tasks.size(); index++) 
                {
                    if (provider.TaskManagerInstance.tasks.get(index).id.equals(taskToWork.id)) 
                    {
                    	provider.TaskManagerInstance.tasks.get(index).requiered = true;
                    }
                }
            	
            	try {
                    provider.PersistTaskManager();
                } catch (JAXBException ex) {
                    System.out.println(prefix + "Failed to persist envelope Xml. Error message" + ex);
                } catch (IOException ex) {
                    System.out.println(prefix + "Failed to persist envelope Xml. Error message" + ex);
                }
        	}
        	
        }
        
        else if (DeserializeEnvelope.command.equals("request")) 
        {
        	Task taskToWork = DeserializeEnvelope.data.get(0);
        	if (GetTaskWithId(taskToWork.id) != null)
        	{
        		for (int index = 0; index < provider.TaskManagerInstance.tasks.size(); index++) 
                {
                    if (provider.TaskManagerInstance.tasks.get(index).id.equals(taskToWork.id)) 
                    {
                    	provider.TaskManagerInstance.tasks.get(index).requiered = true;
                    }
                }
            	
            	try {
                    provider.PersistTaskManager();
                } catch (JAXBException ex) {
                    System.out.println(prefix + "Failed to persist envelope Xml. Error message" + ex);
                } catch (IOException ex) {
                    System.out.println(prefix + "Failed to persist envelope Xml. Error message" + ex);
                }
        	}
        	
        }
        
        else if (DeserializeEnvelope.command.equals("get")) 
        {
        	Task taskToWork = DeserializeEnvelope.data.get(0);
    		for (int index = 0; index < provider.TaskManagerInstance.tasks.size(); index++) 
            {
                if (provider.TaskManagerInstance.tasks.get(index).role.equals(taskToWork.role)) 
                {	
                	System.out.println("ID: " + provider.TaskManagerInstance.tasks.get(index).id);
                	System.out.println("Name: " + provider.TaskManagerInstance.tasks.get(index).name);
                	System.out.println("Date: " + provider.TaskManagerInstance.tasks.get(index).date);
                	System.out.println("Status: " + provider.TaskManagerInstance.tasks.get(index).status);
                	System.out.println("Description: " + provider.TaskManagerInstance.tasks.get(index).description);
                	System.out.println("Attendants: " + provider.TaskManagerInstance.tasks.get(index).attendants);
                	System.out.println("Role: " + provider.TaskManagerInstance.tasks.get(index).role);
                	System.out.println("Requiered: " + provider.TaskManagerInstance.tasks.get(index).requiered);
                	System.out.println();
                }
            }
        	
        	try {
                provider.PersistTaskManager();
            } catch (JAXBException ex) {
                System.out.println(prefix + "Failed to persist envelope Xml. Error message" + ex);
            } catch (IOException ex) {
                System.out.println(prefix + "Failed to persist envelope Xml. Error message" + ex);
            }
    	}
        	

    }

    private void SendAddTaskCommand(Task task) 
    {


        Envelope envelope = new Envelope();

        envelope.command = "add";

        envelope.data.add(task);

        WriteEnvelopeToChannel(envelope, channelTasks);

    }

    private void WriteEnvelopeToChannel(Envelope envelope, JChannel channel) {

        try 
        {
            String envelopeXml = TaskSerializer.SerializeEnvelope(envelope);

            Message msg = new Message(null, null, envelopeXml);

            channel.send(msg);
        } catch (Exception ex) {
            System.out.println("Failed to write task object to the channel. Error message:" + ex.getMessage());
        }



    }

    private Task GetTaskWithId(String taskId) {


        for (int index = 0; index < provider.TaskManagerInstance.tasks.size(); index++) 
        {
            if (provider.TaskManagerInstance.tasks.get(index).id.equals(taskId)) {
                return provider.TaskManagerInstance.tasks.get(index);
            }


        }
        return null;
        //



    }
}
