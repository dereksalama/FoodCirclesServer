<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
String access = (String) request.getAttribute("access");
Integer expires = (Integer) request.getAttribute("exp");
%>

<html>
<%= access + "\n" %>
<%= expires %>
</html>

