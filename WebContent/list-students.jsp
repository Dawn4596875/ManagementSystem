<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
	<title>Student Tracker App</title>
	<link type="text/css" rel="stylesheet" href="CSS/style.css">
</head>

<%-- <%
	// get students from the request object (sent by servlet)
	List<Student> theStudents = (List<Student>)request.getAttribute("STUDENT_LIST");
%> --%>

<body>
	<div id="wrapper">
		<div id="header">
			<h2>Cornell University</h2>
		</div>
	</div>
	<div id="container">
		<div id="content">
			<input type="button" value="Add Student" onclick="window.location.href='add-student-form.jsp'; return false;" 
			class="add-student-button" />
			
			<table>
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Email</th>
					<th>Action</th>
				</tr>
				<c:forEach var="tempStudent" items="${STUDENT_LIST}">
				<!-- set up a link for each student -->
				<c:url var="tempLink" value="StudentControllerServlet">
					<c:param name="command" value="LOAD" />
					<c:param name="studentId" value="${tempStudent.id}" />
				</c:url>
				<c:url var="deleteLink" value="StudentControllerServlet">
					<c:param name="command" value="DELETE" />
					<c:param name="studentId" value="${tempStudent.id}" />
				</c:url>
				<tr>
					<td>${tempStudent.firstName}</td>
					<td>${tempStudent.lastName}</td>
					<td>${tempStudent.email}</td>	
					<td>
						<a href="${tempLink}">Update</a>
						|
						<a href="${deleteLink}" onclick="if(!(confirm('Are you sure you want to delete the student?'))) return false;">
						Delete</a>
					</td>	
							
				</tr>	
				</c:forEach>
			</table>
		</div>
	</div>	
 
</body>
</html>