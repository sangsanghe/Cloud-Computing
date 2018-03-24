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

@Path("courses/{courseId}/announcements")
public class AnnouncementResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Set<String> getAnnouncementList(@PathParam("courseId") String courseId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		Course course = (Course)dynamoDB.getItem("Courses", courseId);
		if(course == null)
			return null;
		return course.announcements;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Announcement createAnnouncement(Announcement announcement
			, @PathParam("courseId") String courseId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		if(dynamoDB.contains("Announcements", announcement.id))
			return null;
		
		announcement.setCourseId(courseId);
		dynamoDB.addOrUpdateItem(announcement);
		Course course = (Course) dynamoDB.getItem("Courses", courseId);
		course.getAnnouncements().add(announcement.id);
		dynamoDB.addOrUpdateItem(course);
		return announcement;
	}
	
	@GET
	@Path("{announcementId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Announcement getAnnouncement(@PathParam("announcementId") String announcementId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		return (Announcement)dynamoDB.getItem("Announcements", announcementId);
	} 
	
	@PUT
	@Path("{announcementId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Announcement updateAnnouncement(Announcement announcement
			, @PathParam("courseId") String courseId
			, @PathParam("announcementId") String announcementId) {
		if(!announcement.id.equals(announcementId))
			return null;
		DynamoDB dynamoDB = DynamoDB.getInstance();
		announcement.setCourseId(courseId);
		dynamoDB.addOrUpdateItem(announcement);
		return announcement;
	}
	
	@DELETE
	@Path("{announcementId}")
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteAnnouncement(@PathParam("courseId") String courseId
			, @PathParam("announcementId") String announcementId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		dynamoDB.deleteItem("Announcements", announcementId);
		Course course = (Course)dynamoDB.getItem("Courses", courseId);
		course.getAnnouncements().remove(announcementId);
		dynamoDB.addOrUpdateItem(course);
	}
}
