package org.jim.csye6225.courseservice;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jim.csye6225.courseservice.database.DynamoDB;

@Path("programs/{programId}/students")
public class StudentResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Set<String> getStudentsInProgram(@PathParam("programId") String programId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		Program program = (Program)dynamoDB.getItem("Programs", programId);
		return program.getStudents();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Student createStudent(@PathParam("programId") String programId, Student student) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		Program program = (Program)dynamoDB.getItem("Programs", programId);
		if(dynamoDB.contains("Students", student.id) || program == null)
			return null;
		
		program.getStudents().add(student.id);
		dynamoDB.addOrUpdateItem(program);
		dynamoDB.addOrUpdateItem(student);
		return student;
	}
	
	@GET
	@Path("{studentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Student getStudent(@PathParam("studentId") String studentId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		return (Student)dynamoDB.getItem("Students", studentId);
	}
	
	@PUT
	@Path("{studentId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Student updateStudent(Student student, @PathParam("studentId") String studentId
			, @PathParam("programId") String programId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		Program program = (Program)dynamoDB.getItem("Programs", programId);
		if(program == null)
			return null;
		
		program.getStudents().add(studentId);
		dynamoDB.addOrUpdateItem(program);
		dynamoDB.addOrUpdateItem(student);
		return student;
	}
	
	@DELETE
	@Path("{studentId}")
	public void deleteStudent(@PathParam("programId") String programId
			, @PathParam("studentId") String studentId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		Program program = (Program)dynamoDB.getItem("Programs", programId);
		if(program == null)
			return;
		
		program.getStudents().remove(studentId);
		dynamoDB.addOrUpdateItem(program);
		dynamoDB.deleteItem("Students", studentId);
	}
}
