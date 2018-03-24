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

@Path ("programs")
public class ProgramResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Set<String> getProgramList() {
		return DynamoDB.getInstance().getAllItems("Programs");
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Program createProgram(Program program) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		if(dynamoDB.contains("Programs", program.id))
			return null;
		
		dynamoDB.addOrUpdateItem(program);
		return program;
	}
	
	@GET
	@Path("{programId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Program getProgram(@PathParam("programId") String programId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		BasicObject object = dynamoDB.getItem("Programs", programId);
		return (Program)object;
	}
	
	@PUT
	@Path("{programId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Program updateProgram(@PathParam("programId") String programId
			, Program program) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		dynamoDB.addOrUpdateItem(program);	
		return program;
	}
	
	@DELETE
	@Path("{programId}")
	public void deleteProgram(@PathParam("programId") String programId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		dynamoDB.deleteItem("Programs", programId);
	}
}
