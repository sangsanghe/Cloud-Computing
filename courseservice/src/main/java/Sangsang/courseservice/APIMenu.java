package org.jim.csye6225.courseservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class APIMenu {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getAPIs() {
		return "// Programs\n" + 
				"GET /programs\n" + 
				"POST /programs\n" + 
				"GET /programs/{programId}\n" + 
				"PUT /programs/{programId}\n" + 
				"DELETE /programs/{programId}\n" + 
				"\n" + 
				"// Courses\n" + 
				"GET /programs/{programId}/courses\n" + 
				"POST /programs/{programId}/courses\n" + 
				"GET /programs/{programId}/courses/{courseId}\n" + 
				"PUT /programs/{programId}/courses/{courseId}\n" + 
				"DELETE /programs/{programId}/courses/{courseId}\n" + 
				"GET /programs/{programId}/courses/{courseId}/students\n" + 
				"\n" + 
				"// Students\n" + 
				"GET /students\n" + 
				"GET /programs/{programId}/students\n" + 
				"POST /programs/{programId}/students\n" + 
				"GET /programs/{programId}/students/{studentId}\n" + 
				"PUT /programs/{programId}/students/{studentId}\n" + 
				"DELETE /programs/{programId}/students/{studentId}\n" + 
				"\n" + 
				"// Enroll course\n" + 
				"GET /courses/{courseId}/students\n" + 
				"POST /courses/{courseId}/students/{studentId}\n" + 
				"DELETE /courses/{courseId}/students/{studentId}\n" + 
				"\n" + 
				"// Lectures\n" + 
				"GET /course/{courseId}/lectures\n" + 
				"GET /course/{courseId}/lectures/{lectureId}\n" + 
				"POST /course/{courseId}/lectures\n" + 
				"PUT /course/{courseId}/lectures/{lectureId}\n" + 
				"DELETE /course/{courseId}/lectures/{lectureId}\n" + 
				"\n" + 
				"// Notes\n" + 
				"GET /lectures/{lectureId}/notes\n" + 
				"GET /lectures/{lectureId}/notes/{noteId}\n" + 
				"POST /lectures/{lectureId}/notes\n" + 
				"PUT /lectures/{lectureId}/notes/{noteId}\n" + 
				"DELETE /lectures/{lectureId}/notes/{noteId}";
	} 
}
