<%@page import="de.rowbuddy.exceptions.NotLoggedInException"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" import="de.rowbuddy.boundary.RowBuddyFacade"
	import="de.rowbuddy.entities.Member"
	import="javax.naming.InitialContext"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="rowBuddyDesign.css" type="text/css">
<title>Login</title>
</head>
<body>
<form action="Login.jsp" method="post">
<table align="center">
<tr>
<td>
<table class="LoginMask" cellspacing="15">
<tr>
<td colspan="2"><img src="icons/logo.png" alt="Header" /></td>
</tr>
<tr>
<td colspan="2" style="color:#d12f19;">
<%
	InitialContext ic = new InitialContext();

	RowBuddyFacade rbf = (RowBuddyFacade) ic.lookup("java:global/Ear_Project/EJB_Project/RowBuddyFacade");

	String password = request.getParameter("password");
	String email = request.getParameter("email");
	
	if (!(email == null || password == null)) {
		try{
		Member member = new Member();
		member.setEmail(email);
		member.setPassword(password);
		rbf.login(member);
		session.setAttribute("rbf",rbf);
        response.sendRedirect("welcomeGWT.html?gwt.codesvr=127.0.0.1:9997");
		}catch(NotLoggedInException ex){
%>
		<%= ex.getMessage()%>
<%
		}
	}
%>
</tr>
<tr>
	<td>Email:</td>
	<td><input type="text" name="email" /></td>
</tr>

<tr>
	<td>Passwort:</td>
	<td><input type="password" name="password" /></td>
</tr>
<tr>
	<td align="center" colspan="2"><input type="submit" value="Login" class="buttonLogin buttonPositive"/></td>
</tr>
</table>
</td>
</tr>
<tr>
<td style="background:#efefef;text-align:center;font-size: 10px;">
	<a href="http://code.google.com/p/rowbuddy/">Open Source</a>
	<br/>
	Participants: Daniel Chiaradia, Georg Fleischer, Sebastian Heineke, Jan Nikolai Trzeszkowski, Lydia Wall
	
</td>
</tr>
</table>
</form>
</body>
</html>