
<%-- 
    Document   : GetAllTasks
    Created on : Aug 31, 2012, 11:17:34 AM
    Author     : rao
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
        <title>Get All Tasks</title>
    </head>
    <body>
        <h1>Available tasks:</h1>        
        <textarea id="txtAreaTaskXml" cols="100" rows="30" >
        
        <%
     
        TcpClient tcpClient = new TcpClient();
        //This line takes the all xml file
          String id = request.getParameter("id");        
          String tasksDoc= tcpClient.getTask(id);          
          System.out.println("message from server "+tasksDoc);
          out.append(tasksDoc);
        %>
		</textarea>
		
		
    </body>
</html>
