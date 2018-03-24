package com.kevin.web.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	// define datasource/connection pool for resource injection
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// set up the printwriter
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		
		// get a connection to the database
		Connection myConnection = null;
		Statement myStatement = null;
		ResultSet res = null;
		
		try {
			myConnection = dataSource.getConnection();
			
			// create and execute sql statement
			String sql = "select * from student";
			myStatement = myConnection.createStatement();
			res = myStatement.executeQuery(sql);
			
			// process the result set
			while(res.next()) {
				String firstName = res.getString("first_name");
				String lastName = res.getString("last_name");
				String email = res.getString("email");
				out.println(firstName + " " + lastName + " " + email);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
