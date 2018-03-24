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


@Path("lectures/{lectureId}/notes")
public class NoteResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Set<String> getNotes(@PathParam("lectureId") String lectureId){
		DynamoDB dynamoDB = DynamoDB.getInstance();
		Lecture lecture = (Lecture)dynamoDB.getItem("Lectures", lectureId);
		if(lecture == null)
			return null;
		return lecture.notes;
	} 
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Note createNote(Note note, @PathParam("lectureId") String lectureId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		Lecture lecture = (Lecture)dynamoDB.getItem("Lectures", lectureId);
		if(dynamoDB.contains("Notes", note.id) || lecture == null)
			return null;
		
		dynamoDB.addOrUpdateItem(note);
		lecture.getNotes().add(note.id);
		dynamoDB.addOrUpdateItem(lecture);
		return note;
	}
	
	@GET
	@Path("{noteId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Note getNote(@PathParam("noteId") String noteId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		return (Note)dynamoDB.getItem("Notes", noteId);
	}
	
	@PUT
	@Path("{noteId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Note updateNote(Note note
			, @PathParam("lectureId") String lectureId
			, @PathParam("noteId") String noteId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		Lecture lecture = (Lecture)dynamoDB.getItem("Lectures", lectureId);
		if(lecture == null)
			return null;
		lecture.getNotes().add(note.id);
		dynamoDB.addOrUpdateItem(lecture);
		dynamoDB.addOrUpdateItem(note);
		return note;
	}
	
	@DELETE
	@Path("{noteId}")
	public void deleteNote(@PathParam("lectureId") String lectureId
			, @PathParam("noteId") String noteId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		Lecture lecture = (Lecture)dynamoDB.getItem("Lectures", lectureId);
		if(lecture == null)
			return;
		lecture.getNotes().remove(noteId);
		dynamoDB.addOrUpdateItem(lecture);
		dynamoDB.deleteItem("Notes", noteId);
	}
}
