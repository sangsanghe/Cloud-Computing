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

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.DeleteTopicRequest;

@Path("programs/{programId}/courses")
public class CourseResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Set<String> getCourseList(@PathParam("programId") String programId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		BasicObject program = dynamoDB.getItem("Programs", programId);
		if(program == null)
			return null;
		return ((Program)program).getCourses();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Course createCourse(Course course, @PathParam("programId") String programId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		if(dynamoDB.contains("Courses", course.id))
			return null;
		
		dynamoDB.addOrUpdateItem(course);
		BasicObject program = dynamoDB.getItem("Programs", programId);
		((Program)program).getCourses().add(course.id);
		dynamoDB.addOrUpdateItem(program);
		// Create SNS Topic
		createSNSTopic(course.id);
		return course;
	}
	
	@GET
	@Path("{courseId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Course getCourse(@PathParam("courseId") String courseId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		return (Course)dynamoDB.getItem("Courses", courseId);
	}
	
	@PUT
	@Path("{courseId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Course updateCourse(Course course, @PathParam("courseId") String courseId) {
		if(!course.id.equals(courseId))
			return null;
		DynamoDB dynamoDB = DynamoDB.getInstance();
		dynamoDB.addOrUpdateItem(course);
		return course;
	}
	
	@DELETE
	@Path("{courseId}")
	public void removeCourse(@PathParam("programId") String programId, 
			@PathParam("courseId") String courseId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		if(!dynamoDB.contains("Courses", courseId))
			return;
		dynamoDB.deleteItem("Courses", courseId);
		BasicObject program = dynamoDB.getItem("Programs", programId);
		((Program)program).getCourses().remove(courseId);
		dynamoDB.addOrUpdateItem(program);
		// Remove SNS topic
		removeSNSTopic(courseId);
	}
	
	public void createSNSTopic(String courseId) {
		AmazonSNS SNS_CLIENT = AmazonSNSClientBuilder.standard()
				.withRegion(Regions.US_WEST_2).build();
		
		CreateTopicRequest createTopicRequest = new CreateTopicRequest(courseId);
		SNS_CLIENT.createTopic(createTopicRequest);
		//return createTopicResult.getTopicArn();
	} 
	
	public void removeSNSTopic(String courseId) {
		AmazonSNS SNS_CLIENT = AmazonSNSClientBuilder.standard()
				.withRegion(Regions.US_WEST_2).build();
		
		DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest(
				"arn:aws:sns:us-west-2:806121996369:" + courseId);
		SNS_CLIENT.deleteTopic(deleteTopicRequest);
	}
}
