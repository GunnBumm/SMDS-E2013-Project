package dk.itu.smds.e2013.jgroups;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBException;

import org.jgroups.JChannel;
import org.jgroups.Message;

import dk.itu.smds.e2013.jgroups.common.TaskProvider;
import dk.itu.smds.e2013.serialization.common.Envelope;
import dk.itu.smds.e2013.serialization.common.Task;
import dk.itu.smds.e2013.serialization.common.TaskSerializer;

/**
 * 
 * @author rao, Rasmus Kreiner
 */
public class TaskManagerJGroupApp {

	String user_name = System.getProperty("user.name", "n/a");
	private TaskProvider provider;
	private JChannel channelTasks;
	BufferedReader in;

	public void start() throws Exception {

		in = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Please enter the path to task-manager-xml!");

		System.out.println(">");

		try {

			String taskXmlPath = in.readLine();

			// First load the tasks from the task manager Xml.
			provider = new TaskProvider(taskXmlPath);

		} catch (Exception ex) {

			System.out
					.println("Failed to load Task Manager Xml! Error message:"
							+ ex.getMessage());

			return;
		}

		// Create channels for each opertaion.
		channelTasks = new JChannel();

		channelTasks.setReceiver(new TaskReceiver(provider, channelTasks));

		channelTasks.connect("Add Tasks Channel");

		channelTasks.getState(null, 10000);

		eventLoop();

		channelTasks.close();

	}

	private void eventLoop() {

		while (true) {
			try {

				System.out
						.println("Usage: type one of the commands: 'add', 'delete', 'update', 'replicate', 'execute', 'request', 'get'   ! otherwise type 'exit' to quit!  ");

				System.out.print("> ");

				System.out.flush();

				String command = in.readLine().toLowerCase();

				Envelope envelope = new Envelope();

				switch (command.toLowerCase().trim()) {

				case "add":

					System.out
							.println("type or paste task Xml you want to add (in single line)!");

					System.out.print("> ");

					// Write Task To Channel
					String taskXml = in.readLine();

					Task addTask = TaskSerializer.DeserializeTask(taskXml);

					envelope.command = command;

					envelope.data.add(addTask);

					WriteEnvelopeToChannel(envelope, channelTasks);

					break;

				case "update":

					System.out.println("type or paste task Xml ");

					System.out.print("> ");

					break;

				case "delete":

					System.out
							.println("type the id of the task(s) you want to delete:");

					System.out.print("> ");

					// Write Task To Channel
					String taskDelete = in.readLine();

					envelope.command = command;

					envelope.id = taskDelete;

					WriteEnvelopeToChannel(envelope, channelTasks);

					break;

				case "trace":

					envelope.command = command;

					WriteEnvelopeToChannel(envelope, channelTasks);

					break;

				case "replicate":

					envelope.command = command;

					WriteEnvelopeToChannel(envelope, channelTasks);

					break;

				case "execute":
					System.out
							.println("type or paste task Xml you want to want to execute (in single line)!");
					System.out.print("> ");
					String taskXml3 = in.readLine();
					Task executeTask = TaskSerializer.DeserializeTask(taskXml3);
					envelope.command = command;
					envelope.data.add(executeTask);
					WriteEnvelopeToChannel(envelope, channelTasks);
					break;

				case "request":
					System.out
							.println("type in the id you want to want to put a request on (in single line)!");
					System.out.print("> ");
					String taskRequest = in.readLine();
					envelope.command = command;
					envelope.id = taskRequest;
					WriteEnvelopeToChannel(envelope, channelTasks);
					break;

				case "get":
					System.out
							.println("type role you want to want to put a request on (in single line)!");
					System.out.print("> ");
					String taskRole = in.readLine();
					envelope.command = command;
					envelope.role = taskRole;
					WriteEnvelopeToChannel(envelope, channelTasks);
					break;

				case "exit":
					return;

				default:
					// System.out.println("Usage: type one of the commands: add, delete, update !  ");
					continue;

				}

			} catch (Exception e) {
				System.out.println("Exit from EventLoop! Error message:"
						+ e.getMessage());

			}

		}
	}

	private void WriteEnvelopeToChannel(Envelope envelope, JChannel channel)
			throws Exception {

		try {
			String envelopeXml = TaskSerializer.SerializeEnvelope(envelope);

			Message msg = new Message(null, null, envelopeXml);

			channel.send(msg);

		} catch (JAXBException ex) {
			System.out
					.println("Failed to write task object to the channel. Error message:"
							+ ex.getMessage());
		}
	}

	private void writeToChannel(JChannel channel, String message)
			throws Exception {
		message = "[" + user_name + "] " + message;

		Message msg = new Message(null, null, message);

		channel.send(msg);
	}

	public static void main(String[] args) throws Exception {

		// Start Task manager.
		new TaskManagerJGroupApp().start();

	}
}
