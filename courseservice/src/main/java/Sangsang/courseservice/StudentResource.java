package sangsang.courseservice;

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

import sangsang.courseservice.database.DynamoDB;

@Path("students/{studentId}")
public class StudentResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Student getStudentsInProgram(@PathParam("studentId") String studentId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		Student student = (Program)dynamoDB.getItem("Students", studentId);
		return student;
	}
		
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Student updateStudent(Student student, @PathParam("studentId") String studentId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();		
		dynamoDB.addOrUpdateItem(student);
		return student;
	}
	
	@DELETE
	public void deleteStudent(@PathParam("studentId") String studentId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		dynamoDB.deleteItem("Students", studentId);
	}
}
