package Sangsang.courseservice;

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
import Sangsang.courseservice.database.DynamoDB;

@Path("professors/{professorId}")
public class AnnouncementResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Professor getProfessor(@PathParam("professorId") String professorId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		return (Professor)dynamoDB.getItem("Professors", announcementId);
	} 
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Professor updateProfessor(Professor professor
			, @PathParam("professorId") String professorId) {
		if(!professor.professorId.equals(professorId))
			return null;
		DynamoDB dynamoDB = DynamoDB.getInstance();
		dynamoDB.addOrUpdateItem(professor);
		return professor;
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteAnnouncement(@PathParam("professorId") String professorId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		dynamoDB.deleteItem("Professors", professorId);
	}
}
