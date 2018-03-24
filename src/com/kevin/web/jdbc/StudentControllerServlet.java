package com.kevin.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private StudentDBUtil studentDBUtil;
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	// called by the server(tomcat) when the servlet is initialized
	@Override
	public void init() throws ServletException {
		super.init();
		
		// create studentDBUtil instance and pass it in the connection pool/datasource
		try{
			studentDBUtil = new StudentDBUtil(dataSource); 
		}catch (Exception e) {
			throw new ServletException(e);
		}
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// read the command parameter
			String theCommand = request.getParameter("command");
			if(theCommand == null) {
				theCommand = "LIST";
			}
			switch(theCommand) {
				case "LIST":
					listStudents(request, response);
					break;
	//			case "ADD":
	//				addStudent(request, response);
	//				break;
				case "LOAD":
					loadStudents(request, response);
					break;
				case "UPDATE":
					updateStudent(request, response);
					break;
				case "DELETE":
					deleteStudent(request, response);
					break;
				default:
					listStudents(request, response);
			}
		}catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		int id = Integer.parseInt(request.getParameter("studentId"));
		studentDBUtil.deleteStudent(id);
		listStudents(request, response);
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		Student theStudent = new Student(id, firstName, lastName, email);
		studentDBUtil.updateStudent(theStudent);
		listStudents(request, response);
	}

	private void loadStudents(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String theStudentId = request.getParameter("studentId");
		Student theStudent = studentDBUtil.getStudent(theStudentId);
		request.setAttribute("THE_STUDENT", theStudent);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// get student info from form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		// create a new student object
		Student newStudent = new Student(firstName, lastName, email);
		
		// add student to database
		studentDBUtil.addStudent(newStudent);
		
		// send back to main page(the student list)
//		listStudents(request, response);
		
		// send back to main page (the student list)
        // SEND AS REDIRECT to avoid multiple-browser reload issue
        response.sendRedirect(request.getContextPath() + "/StudentControllerServlet?command=LIST");
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception{
		// get students from db util
		List<Student> students = studentDBUtil.getStudents();
		// add students to the request
		request.setAttribute("STUDENT_LIST", students);
		// send to jsp page
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
	        // read the "command" parameter
	        String theCommand = request.getParameter("command");
	                
	        // route to the appropriate method
	        switch (theCommand) {
		        case "ADD":
		            addStudent(request, response);
		            break;
		                            
		        default:
		            listStudents(request, response);
	        }
	    }catch (Exception exc) {
            throw new ServletException(exc);
        }
	}
}
