package org.jim.csye6225.courseservice;

import java.util.List;
import java.util.Set;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jim.csye6225.courseservice.database.DynamoDB;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.ListSubscriptionsByTopicRequest;
import com.amazonaws.services.sns.model.ListSubscriptionsByTopicResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.Subscription;
import com.amazonaws.services.sns.model.UnsubscribeRequest;

@Path("courses/{courseId}/students")
public class RegisterResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Set<String> getEnrolledStudents(@PathParam("courseId") String courseId){
		DynamoDB dynamoDB = DynamoDB.getInstance();
		Course course = (Course)dynamoDB.getItem("Courses", courseId);
		return course.getStudents();
	} 
	
	@POST
	@Path("{studentId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<String> enrollCourse(String text
			, @PathParam("courseId") String courseId
			, @PathParam("studentId") String studentId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		Course course = (Course)dynamoDB.getItem("Courses", courseId);
		Student student = (Student)dynamoDB.getItem("Students", studentId);
		if(course == null || student == null)
			return null;
		course.getStudents().add(studentId);
		dynamoDB.addOrUpdateItem(course);
		// Subscribe SNS Topic
		subscribeTopic(courseId, student.getEmail());
		return course.getStudents();
	} 
	
	@DELETE
	@Path("{studentId}")
	public void dropCourse(@PathParam("courseId") String courseId
			, @PathParam("studentId") String studentId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		Course course = (Course)dynamoDB.getItem("Courses", courseId);
		if(course == null || !dynamoDB.contains("Students", studentId))
			return;
		Student student = (Student)dynamoDB.getItem("Students", studentId);
		course.getStudents().remove(studentId);
		dynamoDB.addOrUpdateItem(course);
		// Unsubscribe SNS Topic
		unsubscribeTopic(courseId, student.getEmail());
	} 
	
	public void subscribeTopic(String courseId, String email) {
		AmazonSNS SNS_CLIENT = AmazonSNSClientBuilder.standard()
				.withRegion(Regions.US_WEST_2).build();
		String arn = "arn:aws:sns:us-west-2:806121996369:" + courseId;
		SubscribeRequest subscribeRequest = new SubscribeRequest(arn, "email", email);
		SNS_CLIENT.subscribe(subscribeRequest);
	} 
	
	public void unsubscribeTopic(String courseId, String email) {
		AmazonSNS SNS_CLIENT = AmazonSNSClientBuilder.standard()
				.withRegion(Regions.US_WEST_2).build();
		String arn = "arn:aws:sns:us-west-2:806121996369:" + courseId;
		ListSubscriptionsByTopicRequest lRequest = new ListSubscriptionsByTopicRequest(arn);
		ListSubscriptionsByTopicResult lResult = SNS_CLIENT.listSubscriptionsByTopic(lRequest);
		List<Subscription> subscriptions = lResult.getSubscriptions();
		for(Subscription s : subscriptions) {
			if(s.getEndpoint().equals(email)) {
				UnsubscribeRequest unsubscribeRequest = new UnsubscribeRequest(s.getSubscriptionArn());
				SNS_CLIENT.unsubscribe(unsubscribeRequest);
			}
		}
	} 
}
