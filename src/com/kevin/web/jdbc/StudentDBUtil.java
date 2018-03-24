package com.kevin.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.sql.DataSource;

public class StudentDBUtil {

	private DataSource dataSource;
	
	public StudentDBUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}
	
	public List<Student> getStudents() throws Exception{
		List<Student> students = new ArrayList<>();
		
		Connection myConnection = null;
		Statement stmt = null;
		ResultSet res = null;
		
		try {
			// get a connection
			myConnection = dataSource.getConnection();
			
			// create sql statement
			String sql = "select * from student order by last_name";
			stmt = myConnection.createStatement();
			
			// execute query
			res = stmt.executeQuery(sql);
			
			// process result set
			while(res.next()) {
				int id = res.getInt("id");
				String fn = res.getString("first_name");
				String ln = res.getString("last_name");
				String email = res.getString("email");
				
				Student tempStudent = new Student(id, fn, ln, email);
				students.add(tempStudent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// close jdbc object
			close(myConnection, stmt, res);
		}
		return students;
	}

	public void addStudent(Student newStudent) {
		Connection myConnection = null;
		PreparedStatement stmt = null;
		try {
			myConnection = dataSource.getConnection();
			String firstName = newStudent.getFirstName();
			String sql = "insert into student " + "(first_name, last_name, email) "
						+ "values (?, ?, ?)";
			stmt = myConnection.prepareStatement(sql);
			stmt.setString(1, newStudent.getFirstName());
			stmt.setString(2, newStudent.getLastName());
			stmt.setString(3, newStudent.getEmail());
			stmt.execute();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			close(myConnection, stmt, null);
		}
	}
	
	private void close(Connection myConnection, Statement stmt, ResultSet res) {
		try {
			if(res != null) {
				res.close();
			}
			if(stmt != null) {
				stmt.close();
			}
			if(myConnection != null) {
				myConnection.close(); // put it back to the connection pool
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Student getStudent(String theStudentId) throws Exception{
		Student theStudent = null;
		Connection myConnection = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		int studentId;
		
		try {
			studentId = Integer.parseInt(theStudentId);
			myConnection = dataSource.getConnection();
			String sql = "select * from student where id = ? ";
			stmt = myConnection.prepareStatement(sql);
			stmt.setInt(1, studentId);
			res = stmt.executeQuery();
			if(res.next()) {
				String firstName = res.getString("first_name");
				String lastName = res.getString("last_name");
				String email = res.getString("email");
				theStudent = new Student(studentId, firstName, lastName, email);
			}else {
				throw new Exception("Could not find student id: " + theStudentId);
			}
		}finally {
			close(myConnection, stmt, res);
		}
		return theStudent;
	}

	public void updateStudent(Student theStudent) throws Exception {
		Connection myConnection = null;
		PreparedStatement stmt = null;
		try {
			myConnection = dataSource.getConnection();
			String sql = "update student set first_name = ?, last_name = ?, email = ? where id = ?";
			stmt = myConnection.prepareStatement(sql);
			stmt.setString(1, theStudent.getFirstName());
			stmt.setString(2, theStudent.getLastName());
			stmt.setString(3, theStudent.getEmail());
			stmt.setInt(4, theStudent.getId());
			stmt.execute();
		}finally {
			close(myConnection, stmt, null);
		}
	}

	public void deleteStudent(int id) throws Exception{
		Connection myConnection = null;
		PreparedStatement stmt = null;
		try {
			myConnection = dataSource.getConnection();
			String sql = "delete from student where id=?";
			stmt = myConnection.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.execute();
		}finally {
			close(myConnection, stmt, null);
		}
	}

}
