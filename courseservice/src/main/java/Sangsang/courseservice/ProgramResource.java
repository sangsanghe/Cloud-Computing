package Sangsang.courseservice;

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
import javax.ws.rs.core.MediaType;


@Path("/program/{programId}")
public class ProgramResource {
	static List<Program> programs = new ArrayList<>();
	public ProgramResource () {
		programs.add(new Program(new Course[]{new Course(7500),new Course(6250)}, "IS"));
		programs.add(new Program(new Course[]{new Course(7550),new Course(6200)}, "CS"));
	}
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt(@PathParam("programId") String programId) {
    	for(Program s: programs) {
			if(s.programId.equals(programId)) {
				return s.programId + " has " + s.courses[0].CourseId + ", " + s.courses[1].CourseId;
			}
		}
		return "No such programId!";
    }
    
    //get a class of a program
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/classes/{classId}")
    public String getClass(@PathParam("programId") String programId, @PathParam("classId") int classId) {
    	for(Program s: programs) {
			if(s.programId.equals(programId)) {
				for(Course c: s.courses){
					if(c.CourseId == classId) return c.CourseId + c.board + c.lectures + c.students;
				}
				return s.programId + " has " + s.courses[0].CourseId + ", " + s.courses[1].CourseId;
			}
		}
		return "No such programId!";
    }
    
    
    @POST
	@Consumes(MediaType.TEXT_PLAIN)
	public void postProgram(@PathParam("programId") String programId) {
		programs.add(new Program(programId));
	}
    
	@DELETE
	public void deleteProgram(@PathParam("programId") String programId) {
		for(Program s: programs) {
			if(s.programId.equals(programId)) {
				programs.remove(s);
			}
		}
	}
	
	//delete a class of a program
	@DELETE
	@Path("/classes/{classId}")
	public void deleteClass(@PathParam("programId") String programId, @PathParam("classId") int classId) {
		for(Program s: programs) {
			if(s.programId.equals(programId)) {
				for(Course c: s.courses) {
					if(c.CourseId == classId) c = null;
				}
			}
		}
	}
    
}