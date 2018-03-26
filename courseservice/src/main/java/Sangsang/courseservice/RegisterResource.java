package sangsang.courseservice;

import java.util.List;
import java.util.Set;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import sangsang.courseservice.database.DynamoDB;
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
		student.getCourses().add(courseId);
		dynamoDB.addOrUpdateItem(student);
		subscribeTopic(courseId, student.getEmail());
		return student.getCourses();
	} 
	
	@DELETE
	@Path("{studentId}")
	public void dropCourse(@PathParam("courseId") String courseId
			, @PathParam("studentId") String studentId) {
		DynamoDB dynamoDB = DynamoDB.getInstance();
		Course course = (Course)dynamoDB.getItem("Courses", courseId);
		Student student = (Student)dynamoDB.getItem("Students", studentId);
		if(course == null || student == null)
			return;
		student.getCourses().remove(courseId);
		dynamoDB.addOrUpdateItem(student);
		unsubscribeTopic(courseId, student.getEmail());
	} 
	
	public void subscribeTopic(String courseId, String email) {
		AmazonSNS SNS_CLIENT = AmazonSNSClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		String arn = "arn:aws:sns:us-east-1:895203570867:" + courseId;
		SubscribeRequest subscribeRequest = new SubscribeRequest(arn, "email", email);
		SNS_CLIENT.subscribe(subscribeRequest);
	} 
	
	public void unsubscribeTopic(String courseId, String email) {
		AmazonSNS SNS_CLIENT = AmazonSNSClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		String arn = "arn:aws:sns:us-east-1:895203570867:" + courseId;
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
