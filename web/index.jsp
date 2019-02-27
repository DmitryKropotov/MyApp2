<%--
  Created by IntelliJ IDEA.
  User: mitya
  Date: 2/16/2019
  Time: 9:44 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <jsp:scriptlet>
      System.out.println("The request in jsp:scriptlet is " + request);
  </jsp:scriptlet>
  <body>
     Hello World!<br/>
     Today's date: <%= (new java.util.Date()).toString()%>//JSP Expression (for java expression)
     <% int i = 1; %>
     <%
        System.out.println("The request in body is " + request + " i= " + i);
     %>

     <%!int j = 0;%>//JSP declaration

     <%
         int k = 8;
         System.out.println("j=" + j + " k<j ? "+(k<j));
     %>

     <h3>MyApp</h3>
     <c:import var = "web" url="http://localhost:8080/MyApp/jsp/web.xml"/>

     <x:parse xml = "${web}" var = "output"/>
     <b>The first servlet is</b>:
     <x:out select = "output/display-name" />
     <br>

     let's have some fun: <%= com.tutorialspoint.HelloController.printHello()%>//JSP Scriplet (for java code)

     <a id="priorityCallAnalysis" class="item"> <button type ="button" onclick="getPriorityCall()">Priority </button> </a>


     <script>
         function getPriorityCall() {
             //var xmlhttp = new XMLHttpRequest(); // Create object that will make the request
             // xmlhttp.open("GET", "localhost:8080/hello/world"); // configure object (method, URL, async)
             debugger;
             //xmlhttp.send();
         }
     </script>



  </body>
</html>
