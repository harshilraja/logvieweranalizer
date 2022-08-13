<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@page isELIgnored="false" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<style>
table, th, td {
	border: 1px solid black;
	border-collapse: collapse;
}
</style>
</head>
<body>
	<h1 style="text-align: center">LogFile Centralization</h1>
	<hr/>
	<form:form action="logFiles" modelAttribute="logAnalizer">
		<div>
		Application: <form:select path="application">
			<form:option value="none">Select Application</form:option>
			<form:option value="Newgen">Newgen</form:option>
			<form:option value="Experian">Experian</form:option>
			<form:option value="LMS-T24">LMS-T24</form:option>
			<form:option value="Others">Others</form:option>
		</form:select>
		</div>
		</br>
		<div>
		Environment: <form:select path="environment" onchange="this.form.submit();">
			<form:option value="none">Select Environment</form:option>
			<form:option value="dev">Development</form:option>
			<form:option value="sit">SIT</form:option>
			<form:option value="uat">UAT</form:option>
			<form:option value="prod">Production</form:option>
		</form:select>
		</div>
		</br>
		<div>
		View Log File: <form:select path="fileName" >
			<form:option value="none">Select LogFile</form:option>
			<form:options items="${logAnalizer.logFilesList}"/>  
		</form:select>
		</div>
		</br>
		
		<div style="margin-top: 40px; margin-right: 100px;">
		<table style="width: 90%; text-align: center;">
			<thead>
				<tr style="background-color: #E0E0E1;">
					<th>ID</th>
					<th>NAME</th>
					<th>Image</th>
				</tr>
			</thead>
			<c:forEach var="filename" items="${fileNames}" varStatus="count">
				<tr>
					<td>
						${count.index}
					</td>
					<td>
						${filename}
					</td>
					<td>
						<a href="${pageContext.request.contextPath}/download?fileName=${filename}">Download</a> 
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>
		 
       </br>
		<button  type="submit">View Logs</button>
	</form:form>
</body>
</html>