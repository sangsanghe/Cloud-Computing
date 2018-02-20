package Sangsang.courseservice;

import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
@Path("/students/{studentId}")
public class StudentResource {
	static List<Student> students = new ArrayList<>();
	public StudentResource() {
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
		return "No such studentId!"+students.size();
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/name")
	public String getName(@PathParam("studentId") int studentId) {
		for(Student s: students) {
			if(s.StudentId == studentId) {
				return s.Name;
			}
		}
		return "No such studentId!"+students.size();
	}
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/image")
	public String getImage(@PathParam("studentId") int studentId) {
		for(Student s: students) {
			if(s.StudentId == studentId) {
				return s.image;
			}
		}
		return "No such studentId!"+students.size();
	}
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/program")
	public String getProgram(@PathParam("studentId") int studentId) {
		for(Student s: students) {
			if(s.StudentId == studentId) {
				return s.program;
			}
		}
		return "No such studentId!"+students.size();
	}
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/courses")
	public String getCourses(@PathParam("studentId") int studentId) {
		for(Student s: students) {
			if(s.StudentId == studentId) {
				return ""+s.courses;
			}
		}
		return "No such studentId!"+students.size();
	}
	
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	public String postStudent(@PathParam("studentId") int studentId) {
		students.add(new Student(studentId));
		return ""+students.size();
	}
	
	@PUT
	@Path("/{name}/{program}")
	public void updateStudent(@PathParam("studentId") int studentId, @PathParam("name") String name, @PathParam("program") String program) {
		for(Student s: students) {
			if(s.StudentId == studentId) {
				s.Name = name;
				s.program = program;
			}
		}
	}
	
	@DELETE
	public void deleteStudent(@PathParam("studentId") int studentId) {
		for(Student s: students) {
			if(s.StudentId == studentId) {
				students.remove(s);
			}
		}
	}
	
}