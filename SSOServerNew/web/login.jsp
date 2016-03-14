<%-- 
    Document   : login
    Created on : Oct 9, 2009, 10:41:32 AM
    Author     : wangyu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Please login:</h1>
        <form action="SSOServlet" method="POST">
            UserName: <input  name="username"  size="25" style="font-size: 16px;"/><br/>
            Password : <input  name="password"  type="password"  style="font-size: 16px;"/></br>
            <input  type="hidden" name="goto" value="<%=request.getParameter("goto")%>"/>
            <input  type="hidden" name="actiontype" value="login"/><br/>
            <button type="submit">Submit</button>
        </form>

    </body>
</html>
