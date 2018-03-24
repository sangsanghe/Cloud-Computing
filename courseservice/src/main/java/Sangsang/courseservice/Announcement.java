package org.jim.csye6225.courseservice;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Announcements")
public class Announcement extends BasicObject{
	
	public String courseId;
	public String text;
	
	@DynamoDBHashKey(attributeName = "AnnouncementId")
	public String getId() { return this.id; }
	public void setId(String id) { this.id = id;}
	
	@DynamoDBAttribute(attributeName = "CourseId")
	public String getCourseId() { return this.courseId; }
	public void setCourseId(String courseId) { this.courseId = courseId; }
	
	@DynamoDBAttribute(attributeName = "Text")
	public String getText() { return this.text; }
	public void setText(String text) { this.text = text; }
}
