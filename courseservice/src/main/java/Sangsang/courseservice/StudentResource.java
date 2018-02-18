package Sangsang.courseservice;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
@Path("/students")
public class StudentResource {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getSIt() {
	    return "Got it student after google!";
	}
}
