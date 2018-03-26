package Sangsang.courseservice;

import java.util.HashSet;
import java.util.Set;
import Sangsang.courseservice.database.DynamoDBSetCoverter;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

@DynamoDBTable(tableName = "Courses")
public class Course {
	public String courseId;
	public String courseName;
	public String programId;
	public Set<String> lectures;
	public Set<String> announcements;
	
	@DynamoDBHashKey(attributeName = "CourseId")
	public String getId() { return this.courseId; }
	public void setId(String courseId) { this.courseId =  courseId; } 
	
	@DynamoDBHashKey(attributeName = "ProgramId")
	public String getProgramId() { return this.courseId; }
	public void setProgramId(String courseId) { this.courseId =  courseId; } 
	
	@DynamoDBAttribute(attributeName = "CourseName")
	public String getCourseName() { return this.courseName; }
	public void setCourseName(String courseName) { this.courseName = courseName; }
	
	@DynamoDBAttribute(attributeName = "Announcements")
	@DynamoDBTypeConverted(converter = DynamoDBSetCoverter.class)
	public Set<String> getAnnouncements() {
		if(this.announcements == null)
			this.announcements = new HashSet<>();
		return this.announcements;
	} 
	
	public void setAnnouncements(Set<String> announcements) { this.announcements = announcements; }

	
	@DynamoDBAttribute(attributeName = "Lectures")
	@DynamoDBTypeConverted(converter = DynamoDBSetCoverter.class)
	public Set<String> getLectures() {
		if(this.lectures == null)
			this.lectures = new HashSet<>();
		return this.lectures;
	} 
	
	public void setLectures(Set<String> lectures) { this.lectures = lectures; }
}
