<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.foodcirclesserver.user.Facebook" %>

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Hello App Engine</title>
  </head>

  <body>
    <h1>Hello App Engine!</h1>
	
    <table>
      <tr>
        <td colspan="2" style="font-weight:bold;">Available Servlets:</td>        
      </tr>
      <tr>
        <td><a href="foodcirclesserver">FoodCirclesServer</a></td>
        <td><a href="<%= Facebook.getLoginRedirectURL()%>">FBTesting</a></td>
      </tr>
    </table>
  </body>
</html>
