package Sangsang.courseservice;

import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
@Path("/students/{studentId}")
public class StudentResource {
	List<Student> students;
	public StudentResource() {
		students = new ArrayList<>();
		students.add(new Student("Sang", 123, "jpg", null, "IS"));
		students.add(new Student("Jin", 124, "jpg", null, "IS"));
	}
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getSIt(@PathParam("studentId") int studentId) {
		for(Student s: students) {
			if(s.StudentId == studentId) {
				return s.Name + s.StudentId + s.program + "***";
			}
		}
		return "No such studentId!";
	}
	
	@POST
	public void postStudent(@PathParam("studentId") int studentId) {
		students.add(new Student(studentId));
	}
	
	
}