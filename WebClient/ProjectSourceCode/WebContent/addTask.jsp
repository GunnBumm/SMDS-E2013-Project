
<%-- 
    Document   : Delete Task
    Created on : Okt 08, 2013, 14:21:00
    Author     : Rasmus Kreiner
--%>

<%@page import="itu.dk.smds.e2013.common.TcpClient"%>
<%@page import="itu.dk.smds.e2013.servlets.GetAllTasksServlet"%>
<%@page import="java.util.logging.Level"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="org.jdom2.JDOMException"%>
<%@page import="org.jdom2.output.XMLOutputter"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="itu.dk.smds.e2013.common.TasksJDOMParser"%>
<%@page import="org.jdom2.Document"%>
<%@page import="java.io.InputStream"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Delete Tasks</title>
    </head>
    <body>
        <h1>Add task:</h1>        
        <textarea id="txtAreaTaskXml" cols="100" rows="30" >
        
        <%
        	TcpClient tcpClient = new TcpClient(); //Creates a new client
          	String id = request.getParameter("id"); //Gets the id of the task we want to delete
          	String name = request.getParameter("name"); //Gets the name of the task we want to delete
          	String date = request.getParameter("date"); //Gets the date of the task we want to delete
          	String stat = request.getParameter("status"); //Gets the status of the task we want to delete
          	String req = request.getParameter("required"); //Gets the required of the task we want to delete
          	String des = request.getParameter("description"); //Gets the description of the task we want to delete
          	String atten = request.getParameter("attendants"); //Gets the description of the task we want to delete
          	tcpClient.addTask(id, name, date, stat, req, des, atten);
        %>
       
		</textarea>
		
		
    </body>
</html>
