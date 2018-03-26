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
import sangsang.courseservice.database.DynamoDB;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.DeleteTopicRequest;

@Path("programs/{programId}/courses")
public class CourseResource {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Course createCourse(Course course, @PathParam("programId") String programId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		if(dynamoDB.contains("Courses", course.id))
			return null;
		dynamoDB.addOrUpdateItem(course);
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
		if(!course.courseId.equals(courseId))
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
		// Remove SNS topic
		removeSNSTopic(courseId);
	}
	
	public void createSNSTopic(String courseId) {
		AmazonSNS SNS_CLIENT = AmazonSNSClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		CreateTopicRequest createTopicRequest = new CreateTopicRequest(courseId);
		SNS_CLIENT.createTopic(createTopicRequest);
	} 
	
	public void removeSNSTopic(String courseId) {
		AmazonSNS SNS_CLIENT = AmazonSNSClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest("arn:aws:sns:us-east-1:895203570867:" + courseId);
		SNS_CLIENT.deleteTopic(deleteTopicRequest);
	}
}
