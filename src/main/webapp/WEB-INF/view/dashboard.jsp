<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@page isELIgnored="false" %>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h1>Read the textbox value</h1>
	<hr/>
	<p>Employee First name: ${employee.firstName}</p>
	<p>Employee Last name: ${employee.lastName}</p>
	<p>Employee country: ${employee.country}</p>
</body>
</html>