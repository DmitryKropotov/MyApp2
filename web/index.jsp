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
     Today's date: <%= (new java.util.Date()).toString()%>
     <% int i = 1; %>
     <%
        System.out.println("The request in body is " + request + " i= " + i);
     %>

     <h3>MyApp</h3>
     <c:import var = "web" url="http://localhost:8080/MyApp/jsp/web.xml"/>

     <x:parse xml = "${web}" var = "output"/>
     <b>The first servlet is</b>:
     <x:out select = "output/display-name" />
     <br>

  </body>
</html>
