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

@Path("courses/{courseId}/lectures")
public class LectureResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Set<String> getLectures(@PathParam("courseId") String courseId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		Course course = (Course)dynamoDB.getItem("Courses", courseId);
		if(course == null)
			return null;
		return course.lectures;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Lecture createLecture(Lecture lecture
			, @PathParam("courseId") String courseId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		Course course = (Course)dynamoDB.getItem("Courses", courseId);
		if(course == null || dynamoDB.contains("Lectures", lecture.id))
			return null;
		
		dynamoDB.addOrUpdateItem(lecture);
		course.getLectures().add(lecture.id);
		dynamoDB.addOrUpdateItem(course);
		return lecture;
	} 
	
	@GET
	@Path("{lectureId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Lecture getLecture(@PathParam("lectureId") String lectureId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		Lecture lecture = (Lecture)dynamoDB.getItem("Lectures", lectureId);
		return lecture;
	} 
	
	@PUT
	@Path("{lectureId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Lecture updateLecture(Lecture lecture
			, @PathParam("lectureId") String lectureId
			, @PathParam("courseId") String courseId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		Course course = (Course)dynamoDB.getItem("Courses", courseId);
		if(course == null)
			return null;
		dynamoDB.addOrUpdateItem(lecture);
		course.getLectures().add(lectureId);
		dynamoDB.addOrUpdateItem(course);
		return lecture;
	} 
	
	@DELETE
	@Path("{lectureId}")
	public void deleteLecture(@PathParam("courseId") String courseId
			, @PathParam("lectureId") String lectureId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		Course course = (Course)dynamoDB.getItem("Courses", courseId);
		course.getLectures().remove(lectureId);
		dynamoDB.addOrUpdateItem(course);
		dynamoDB.deleteItem("Lectures", lectureId);
	} 
}
