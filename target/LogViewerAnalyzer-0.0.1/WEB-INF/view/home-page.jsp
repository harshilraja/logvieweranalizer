<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h1>Read the textbox value</h1>
	<hr/>
	<form:form action="processForm" modelAttribute="employee">
		First Name:<form:input path="firstName" /><br/><br/>
		Last Name: <form:input path="lastName" /><br/><br/>
		Country: <form:select path="country">
			<form:option value="India">India</form:option>
			<form:option value="Australia">Australia</form:option>
			<form:option value="Germany">Germany</form:option>
			<form:option value="USA">USA</form:option>
			<form:option value="UK">UK</form:option>
		</form:select>
		<button type="submit">Submit</button>
	</form:form>
</body>
</html>